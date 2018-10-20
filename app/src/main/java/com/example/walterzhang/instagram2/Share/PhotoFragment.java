package com.example.walterzhang.instagram2.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.walterzhang.instagram2.Profile.AccountSettingsActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.FilePaths;
import com.example.walterzhang.instagram2.utils.Permissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mingshunc on 24/9/18.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int CAMERA_REQUEST_CODE = 99;

    String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting...");
        View view = inflater.inflate(R.layout.fragment_photo,container,false);

        // Check if app directory exists if not create it
        FilePaths filePaths = new FilePaths();
        File f = new File(filePaths.UNISOCIAL);

        if(!f.exists()) {
            f.mkdir();
        }

        Button btnOpenCamera = (Button) view.findViewById(R.id.btnOpenCamera);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening camera.");

                if (((ShareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((ShareActivity) getActivity()).checkPermission(Permissions.CAMERA_PERMISSIONS[0])) {
                        Log.d(TAG, "onClick: starting camera.");

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                                ex.getStackTrace();
                            }

                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                                        "com.example.walterzhang.instagram2.fileprovider",
                                        photoFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                            }
                        }
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

    private File createImageFile() throws IOException {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/UniSocial");
        File image = File.createTempFile(generateImageFilename() + "_",".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String generateImageFilename() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        return imageFileName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");

            try {
                Log.d(TAG, "onActivityResult: mCurrentPhotoPath: " + mCurrentPhotoPath.toString());
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));

                FilePaths filePaths = new FilePaths();
                File storedPhotoFile = new File(filePaths.UNISOCIAL + "/" +
                        generateImageFilename() + ".jpg");
                Log.d(TAG, "onActivityResult: storedPhotoFile: " + storedPhotoFile.toString());

                try {
                    FileOutputStream out = new FileOutputStream(storedPhotoFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    if (isRootTask()) {
                        try {
                            Log.d(TAG, "onActivityResults: received new bitmap from camera: ");
                            Intent intent = new Intent(getActivity(), CropActivity.class);

                            intent.putExtra(getString(R.string.selected_image), storedPhotoFile.toString());
                            startActivity(intent);
                        } catch (NullPointerException e) {
                            Log.d(TAG, "onActivityResults: NullPointerException " + e.getMessage());
                        }
                    } else {
                        try {
                            Log.d(TAG, "onActivityResults: received new bitmap from camera: ");
                            Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                            intent.putExtra(getString(R.string.selected_image), storedPhotoFile.toString());
                            intent.putExtra(getString(R.string.return_to_fragment),
                                    getString(R.string.edit_profile_fragment));
                            startActivity(intent);
                            getActivity().finish();
                        } catch (NullPointerException e) {
                            Log.d(TAG, "onActivityResults: NullPointerException " + e.getMessage());
                        }
                    }
                }
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        }
    }
}