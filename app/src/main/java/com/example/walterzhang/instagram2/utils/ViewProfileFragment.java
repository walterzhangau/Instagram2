package com.example.walterzhang.instagram2.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.Models.User;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.Models.UserSettings;
import com.example.walterzhang.instagram2.Home.DiscoverFragment;
import com.example.walterzhang.instagram2.Profile.AccountSettingsActivity;
import com.example.walterzhang.instagram2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileFragment extends Fragment{

    private static final String TAG = "ViewProfileFragment";

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription,
    mFollow, mUnfollow, editProfile;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu, mBackArrow;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    private DiscoverFragment discoverFragment;


    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    //variables
    private User mUser;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        mDisplayName = (TextView) view .findViewById(R.id.display_name);
        mUsername = (TextView) view .findViewById(R.id.username);
        mWebsite = (TextView) view .findViewById(R.id.website);
        mDescription = (TextView) view .findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view .findViewById(R.id.profile_photo);
        mFollow = view.findViewById(R.id.follow);
        mUnfollow = view.findViewById(R.id.unfollow);
        editProfile = view.findViewById(R.id.textEditProfile);
        mPosts = (TextView) view .findViewById(R.id.tvPosts);
        mFollowers = (TextView) view .findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view .findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view .findViewById(R.id.profileProgressBar);
        gridView = (GridView) view .findViewById(R.id.gridView);
        toolbar = (Toolbar) view .findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view .findViewById(R.id.profileMenu);
        mBackArrow = view.findViewById(R.id.backArrow);
        bottomNavigationView = (BottomNavigationViewEx) view .findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();
        discoverFragment = new DiscoverFragment();
        Log.d(TAG, "onCreateView: started. ");
        setupFirebaseAuth();
        setupBottomNavigationView();


        try{
            mUser = getUserFromBundle();
            init();
        }
        catch(NullPointerException e){
            Log.e(TAG, "NullPointerException" + e.getMessage());
            Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Now following "+ mUser.getUsername());
                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.dbname_following))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(mUser.getUser_id())
                        .child(getString(R.string.field_user_id))
                        .setValue(mUser.getUser_id());

                FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbname_followers))
                        .child(mUser.getUser_id())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.field_user_id))
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                setFollowing();
                getFollowersCount();


            }
        });

        mUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Unfollowing: "+ mUser.getUsername());
                FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.dbname_following))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(mUser.getUser_id())
                        .removeValue();

                FirebaseDatabase.getInstance().getReference().child(getString(R.string.dbname_followers))
                        .child(mUser.getUser_id())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();
                setUnfollowing();
                getFollowersCount();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);

            }
        });

        isFollowing();
        getFollowersCount();
        getFollowingCount();
        getPostsCount();

        return view;
    }

    private void setProfileWidgets(UserSettings userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mDescription.setText(settings.getDescription());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mProgressBar.setVisibility(View.GONE);
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
    }

    private void init(){

        //set the profile details
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        Query personsQuery = mUserDatabase.child(getString(R.string.dbname_user_account_settings))
                .orderByChild(getString(R.string.field_username)).equalTo(mUser.getUsername());


        personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found user in init()" + singleSnapshot.getValue(UserAccountSettings.class).toString());
                    UserSettings settings = new UserSettings();
                    settings.setSettings(singleSnapshot.getValue(UserAccountSettings.class));
                    settings.setUser(mUser);
                    setProfileWidgets(settings);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Setting the profile photos

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_user_photos))
                .child(mUser.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Photo> photos = new ArrayList<Photo>();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

                // Setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth / NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i < photos.size(); i++) {
                    imgUrls.add(photos.get(i).getImage_path());
                }

                GridImageAdapter adapter = new GridImageAdapter(mContext,
                        R.layout.layout_grid_imageview, "", imgUrls);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

    }

    private void getFollowersCount(){
        mFollowersCount = 0;
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        Query personsQuery = mUserDatabase.child(getString(R.string.dbname_followers))
                .child(mUser.getUser_id());


        personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found followers" + singleSnapshot.getValue());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowingCount(){
        mFollowingCount = 0;
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        Query personsQuery = mUserDatabase.child(getString(R.string.dbname_following))
                .child(mUser.getUser_id());


        personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found following user" + singleSnapshot.getValue());
                    mFollowingCount++;
                }
                mFollowing.setText(String.valueOf(mFollowingCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPostsCount(){
        mPostsCount = 0;
        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        Query personsQuery = mUserDatabase.child(getString(R.string.dbname_user_photos))
                .child(mUser.getUser_id());


        personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found post" + singleSnapshot.getValue());
                    mPostsCount++;
                }
                mPosts.setText(String.valueOf(mPostsCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isFollowing(){
        setUnfollowing();

        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference();
        Query personsQuery = mUserDatabase.child(getString(R.string.dbname_following))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild(getString(R.string.field_user_id)).equalTo(mUser.getUser_id());


        personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found user in isFollowing()" + singleSnapshot.getValue());
                    setFollowing();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFollowing(){
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);
    }
    private void setUnfollowing(){
        mFollow.setVisibility(View.VISIBLE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE);
    }

    private void setCurrentUserProfile(){
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.VISIBLE);
    }

    private User getUserFromBundle(){
        Log.d(TAG, "Getting user information from the bundle"+ getArguments());
        Bundle bundle=this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.intent_user));
        }else {
            return null;
        }
    }



    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(),bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * ---------------------------------------- Firebase ----------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
