package com.example.walterzhang.instagram2.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.example.walterzhang.instagram2.utils.Permissions;
import com.example.walterzhang.instagram2.utils.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by walterzhang on 7/9/18.
 * Edited by mingshunc on 24/9/18.
 */

public class ShareActivity extends AppCompatActivity{
    private static final String TAG = "ShareActivity";
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext = ShareActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started ");

        if (checkPermissionArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    /**
     * Setup viewpager for managing the tabs
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(R.string.gallery);
        tabLayout.getTabAt(1).setText(R.string.photo);
    }

    /**
     * Return the current tab number
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    /**
     * Return the flags of the current intent
     * @return
     */
    public int getTask() {
        Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());
        return getIntent().getFlags();
    }

    /**
     * Get user to verify permissions
     * @param permissions
     */
    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions.");
        
        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST);
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermission(check)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if a single permission has been granted
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        Log.d(TAG, "checkPermission: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                ShareActivity.this,
                permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermission: permission was granted for: " + permission);
            return true;
        }
    }

    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
