package com.example.walterzhang.instagram2.Home;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.dummy.DummyContent;
import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class LikesListActivity extends AppCompatActivity implements fragment_like_list.OnListFragmentInteractionListener {
    private static final String TAG = "LikesListActivity";
    private static final int ACTIVITY_NUM = 0;

    private Class<?> cls;
    private Context mContext = LikesListActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        Log.d(TAG, "onCreate: started ");

        setupBottomNavigationView();

        android.app.Fragment userFragment = new fragment_like_list();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.relLayout_activity_likes, userFragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

        TextView snippet_top_title = (TextView)findViewById(R.id.snippet_top_return);
        snippet_top_title.setText("Likes");
    }

    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void onListFragmentInteraction(DummyContent.DummyItem uri) {
        //you can leave it empty
    }

    public void onFragmentInteraction()
    {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void onBackClicked(View view)
    {
        Log.d(TAG, "onBackClicked...");
        onBackPressed();
    }
}