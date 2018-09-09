package com.example.walterzhang.instagram2.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by walterzhang on 8/9/18.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    //Stores fragments as keys and can get number
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>();
    //If you know fragment name you can get number
    private final HashMap<String,Integer> mFragmentNumbers = new HashMap<>();
    //If you know number of fragment can get name
    private final HashMap<Integer,String> mFragmentNames = new HashMap<>();


    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    private void addFragment(Fragment fragment,String fragmentName){
        mFragmentList.add(fragment);
        mFragments.put(fragment,mFragmentList.size()-1);
        mFragmentNames.put(mFragmentList.size()-1, fragmentName);
        mFragmentNumbers.put(fragmentName,mFragmentList.size()-1);
    }

    /**
     * returns the fragment with the @param
     * @param fragmentName
     * @return
     */
    public Integer getFragmentNumber(String fragmentName){
        if (mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }
        else{
            return null;
        }
    }

    /**
     * returns the fragment with the @param
     * @param fragment
     * @return
     */
    public Integer getFragmentNumber(Fragment fragment){
        if (mFragmentNumbers.containsKey(fragment)){
            return mFragmentNumbers.get(fragment);
        }
        else{
            return null;
        }
    }
}
