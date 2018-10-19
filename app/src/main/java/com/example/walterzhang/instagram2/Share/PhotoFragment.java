package com.example.walterzhang.instagram2.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.walterzhang.instagram2.Filter.filter;
import com.example.walterzhang.instagram2.Profile.AccountSettingsActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.Permissions;

/**
 * Created by mingshunc on 24/9/18.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int CAMERA_REQUEST_CODE = 99;

    private ContentValues values;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting...");
        View view = inflater.inflate(R.layout.fragment_photo,container,false);

        Button btnOpenCamera = (Button) view.findViewById(R.id.btnOpenCamera);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening camera.");

                if (((ShareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((ShareActivity) getActivity()).checkPermission(Permissions.CAMERA_PERMISSIONS[0])) {
                        Log.d(TAG, "onClick: starting camera.");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    private boolean isRootTask() {
        if (((ShareActivity)getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");
            if(data!=null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (isRootTask()) {
                    try {
                        Log.d(TAG, "onActivityResults: received new bitmap from camera: " + bitmap);
                        Intent intent = new Intent(getActivity(), filter.class);
                        intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        Log.d(TAG, "onActivityResults: NullPointerException " + e.getMessage());
                    }
                } else {
                    try {
                        Log.d(TAG, "onActivityResults: received new bitmap from camera: " + bitmap);
                        Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                        intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                        intent.putExtra(getString(R.string.return_to_fragment),
                                getString(R.string.edit_profile_fragment));
                        startActivity(intent);
                        getActivity().finish();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "onActivityResults: NullPointerException " + e.getMessage());
                    }
                }
            } else{
                Intent i=getActivity().getIntent();
                startActivity(i);
                getActivity().finish();


            }
        }
    }
}
