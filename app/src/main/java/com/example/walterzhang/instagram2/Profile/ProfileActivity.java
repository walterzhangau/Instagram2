package com.example.walterzhang.instagram2.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;
import com.example.walterzhang.instagram2.utils.ViewCommentsFragment;
import com.example.walterzhang.instagram2.utils.ViewPostFragment;
import com.example.walterzhang.instagram2.utils.ViewProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnGridImageSelectedListener ,
        ViewPostFragment.OnCommentThreadSelectedListener ,
        ViewProfileFragment.OnGridImageSelectedListener {

    private static final String TAG = "ProfileActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG, "onGridImageSelected: selected image on gridview: " + photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), activityNumber);
        fragment.setArguments(args);

        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }

    @Override
    public void onCommentThreadSelectedListener(Photo photo) {
        Log.d(TAG, "onCommentThreadSelectedListener:  selected a comment thread");

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();
    }



    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");
        mFirebaseMethods = new FirebaseMethods(this);

        init();


    }

    private void init(){
        Log.d(TAG, "init: inflating " + getString(R.string.profile_fragment));

        /*ProfileFragment fragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();*/

        Log.d(TAG, "init: inflating  " + getString(R.string.profile_fragment));
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.calling_activity))) {
            Log.d(TAG, "init: searching for user object from intent");

            if (intent.hasExtra(getString(R.string.intent_user))) {
                ViewProfileFragment fragment = new ViewProfileFragment();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.intent_user),
                        intent.getParcelableExtra(getString(R.string.intent_user)));
                fragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(getString(R.string.view_profile_fragment));
                transaction.commit();
            } else {
                Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        } else {
            ProfileFragment fragment = new ProfileFragment();
            android.support.v4.app.FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(getString(R.string.profile_fragment));
            transaction.commit();

        }


    }


//
//
//    private static final int ACTIVITY_NUM = 4;
//    private static final int NUM_GRID_COLUMNS = 3;
//
//    private Context mContext = ProfileActivity.this;
//
//    // Firebase
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference myRef;
//    private FirebaseMethods mFirebaseMethods;
//
//    // Widgets
//    private ProgressBar mProgressBar;
//    private ImageView profilePhoto;
//    private GridView gridView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        Log.d(TAG, "onCreate: started ");
//
//        mFirebaseMethods = new FirebaseMethods(this);
//
//
//        init();
//
//    }
//
//
//    private void init() {
//
//        Log.d(TAG, "init: inflating  " + getString(R.string.profile_fragment));
//
//        Intent intent = getIntent();
//        if (intent.hasExtra(getString(R.string.calling_activity))) {
//            Log.d(TAG, "init: searching for user object from intent");
//
//            if (intent.hasExtra(getString(R.string.intent_user))) {
//                ViewProfileFragment fragment = new ViewProfileFragment();
//                Bundle args = new Bundle();
//                args.putParcelable(getString(R.string.intent_user),
//                        intent.getParcelableExtra(getString(R.string.intent_user)));
//                fragment.setArguments(args);
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, fragment);
//                transaction.addToBackStack(getString(R.string.view_profile_fragment));
//                transaction.commit();
//            } else {
//                Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
//            }
//        } else {
//            ProfileFragment fragment = new ProfileFragment();
//            android.support.v4.app.FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.container, fragment);
//            transaction.addToBackStack(getString(R.string.profile_fragment));
//            transaction.commit();
//
//        }
//
//    }

}

