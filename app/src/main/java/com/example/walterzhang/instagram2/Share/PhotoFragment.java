package com.example.walterzhang.instagram2.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walterzhang.instagram2.R;

/**
 * Created by mingshunc on 24/9/18.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting...");
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        return view;
    }
}
