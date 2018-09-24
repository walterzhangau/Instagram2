package com.example.walterzhang.instagram2.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.walterzhang.instagram2.CommentFragment;
import com.example.walterzhang.instagram2.Login.LoginActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.UserFragment;
import com.example.walterzhang.instagram2.dummy.DummyContent;
import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.example.walterzhang.instagram2.utils.SectionsPagerAdapter;
import com.example.walterzhang.instagram2.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity implements
        fragment_post_list.OnListFragmentInteractionListener, PostFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener, fragment_like_list.OnListFragmentInteractionListener,
        CommentFragment.OnFragmentInteractionListener,
        fragment_comment_list.onCommentListFragmentInteractionListener {
    
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    //Firebase Authentication
    private FirebaseAuth mAuth;
    private Context mContext = HomeActivity.this;
    private Class<?> cls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting");

        //Firebase Auth Object
        mAuth = FirebaseAuth.getInstance();

        initImageLoader();

        setupBottomNavigationView();
        setupViewPager();
    }

    private void initImageLoader(){

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Responsible for adding three tabs at top of home page
     */
    private void setupViewPager(){
        SectionsPagerAdapter adpater = new SectionsPagerAdapter(getSupportFragmentManager());
        adpater.addFragment(new CameraFragment());
        adpater.addFragment(new fragment_post_list());
        adpater.addFragment(new MessagesFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adpater);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }

    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    public void onListFragmentInteraction(DummyContent.DummyItem uri){
        //you can leave it empty
    }

    public void onFragmentInteraction()
    {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void onLikesCountClicked(View view)
    {
        Log.d(TAG, "onLikesCountClicked...");
        cls = LikesListActivity.class;
        onFragmentInteraction();
    }

    public void onCommentsCountClicked(View view)
    {
        Log.d(TAG, "onCommentsCountClicked...");
        cls = CommentsListActivity.class;
        onFragmentInteraction();
    }

    public void onCommentsClicked(View view)
    {
        Log.d(TAG, "onCommentsClicked...");
        cls = CommentsListActivity.class;
        onFragmentInteraction();
    }

    //Firebase Stuff DO NOT WRITE ANYTHING BELOW

    void checkUser(FirebaseUser user)
    {
        if(user==null)
        {
            Log.d("HomeActivity:","User not Signed in");
            Intent intent=new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
        else
            Log.d("HomeActivity",user.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HomeActivity","OnStart method of Firebase");
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkUser(currentUser);
    }



    public void onLikePostClicked(View view)
    {
        Log.d(TAG, "onLikePostClicked...");
    }

    public void onPostCommentClicked(View view)
    {
        Log.d(TAG, "onPostCommentClicked...");
        //TODO:get the text in add comment editText and send to db
    }
}
