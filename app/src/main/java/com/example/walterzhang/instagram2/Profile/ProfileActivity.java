package com.example.walterzhang.instagram2.Profile;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.example.walterzhang.instagram2.utils.GridImageAdapter;
import com.example.walterzhang.instagram2.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by walterzhang on 7/9/18.
 */

public class ProfileActivity extends AppCompatActivity{
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int  NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started ");
        //mProgressBar =(ProgressBar) findViewById(R.id.profileProgressBar);
        //mProgressBar.setVisibility(View.GONE);
        init();

//        setupBottomNavigationView();
//        setupToolbar();
//        setupActivityWidgets();
//        setProfileImage();
//
//        tempGridSetup();
    }

    private void init(){
        Log.d(TAG, "init: inflating  "+getString(R.string.profile_fragment));
        ProfileFragment fragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();

    }

//
//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://preview.redd.it/c0kzz1fibhp11.png?width=640&crop=smart&s=a647ba9a8fbf67a2432027610c4e80951e08b243");
//        imgURLs.add("https://preview.redd.it/e2w2qlamxhp11.jpg?width=640&crop=smart&s=b25ca8cb9ba98c9014d836725047f42739a66415");
//        imgURLs.add("https://preview.redd.it/6myh1cjlxgp11.jpg?width=640&crop=smart&s=773075a9fa488bda991fea3dbe2f92c6faa4e4ce");
//        imgURLs.add("https://preview.redd.it/ecpaovqvehp11.jpg?width=640&crop=smart&s=f65b35917cd9622a7aaee22a6c86fe9efacc1cae");
//        imgURLs.add("https://preview.redd.it/q2n0km0a9gp11.jpg?width=640&crop=smart&s=bc18f24beec4fefb34c4b1ea21bbbdf6ce3a415a");
//        imgURLs.add("https://preview.redd.it/30rbdodw2gp11.jpg?width=640&crop=smart&s=5f35fa9918834fb00ca9e47df27de6cc2cc1ff38");
//        imgURLs.add("https://preview.redd.it/zdhtxdbplgp11.jpg?width=640&crop=smart&s=3e9ed45a696e749b0eed1287e04749b30302e848");
//        imgURLs.add("https://preview.redd.it/oeqfrua3jfp11.jpg?width=640&crop=smart&s=3f6ba95e7920294d0f3bbc1f8d8fb24a7dd5d9ba");
//        setupImageGrid(imgURLs);
//    }
//
//    //For now takes a list of urls but will take dynamic inut in future
//    private void setupImageGrid(ArrayList<String> imgUrls){
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview,"" ,imgUrls);
//        gridView.setAdapter(adapter);
//
//    }
//
//    //will have variable as inut when photo is dynamic
//    private void setProfileImage(){
//        Log.d(TAG, "setProfileImage: Setting profile image");
//        String imgURL = "cdn.dribbble.com/users/182006/screenshots/664661/3d-instagram-04.jpg";
//        UniversalImageLoader.setImage(imgURL,profilePhoto,mProgressBar,"https://");
//
//
//    }
//
//    private void setupActivityWidgets(){
//        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
//        mProgressBar.setVisibility(View.GONE);
//        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
//
//    }
//
//
//
//    private void setupToolbar(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
//        setSupportActionBar(toolbar);
//        ImageView profileMenu = (ImageView) findViewById(R.id.profileMenu);
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: navigation to account settings clicked ");
//                Intent intent =new Intent(mContext,AccountSettingsActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    /**
//     * BottomNavigationView Setup
//     */
//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }


}
