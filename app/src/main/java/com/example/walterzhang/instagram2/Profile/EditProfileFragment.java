package com.example.walterzhang.instagram2.Profile;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by walterzhang on 9/9/18.
 */


public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_editprofile,container,false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        setProfileImage();

        //set back button to navigate back to profile
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to profile activity");
                getActivity().finish();
            }
        });

        return view;
    }


    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: ");
        String imgURL = "cdn.dribbble.com/users/182006/screenshots/664661/3d-instagram-04.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto,null,"https://");
        
    }
}
