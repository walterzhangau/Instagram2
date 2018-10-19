package com.example.walterzhang.instagram2.Home;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class CommentsListActivity extends AppCompatActivity implements fragment_comment_list.onCommentListFragmentInteractionListener {
    private static final String TAG = "CommentsListActivity";
    private static final int ACTIVITY_NUM = 0;

    private Class<?> cls;
    private Context mContext = CommentsListActivity.this;
    private String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Log.d(TAG, "onCreate: started ");

        setupBottomNavigationView();

        android.app.Fragment commentFragment = new fragment_comment_list();

        photoId = getIntent().getStringExtra("photo_message"); // Use this to receive from home activity

        Bundle bundle = new Bundle();
        bundle.putString("photo_message", photoId);
        //set Fragmentclass Arguments
        commentFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.relLayout_activity_comments, commentFragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

        TextView snippet_top_title = (TextView)findViewById(R.id.snippet_top_return);
        snippet_top_title.setText("Comments");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("photo_info"));
    }

    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public void onListFragmentInteraction(TextView uri) {
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            photoId = intent.getStringExtra("photoId");
            Log.d(TAG, "PhotoID: " + photoId);
        }
    };
}
