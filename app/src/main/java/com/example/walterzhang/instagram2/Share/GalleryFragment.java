package com.example.walterzhang.instagram2.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Filter.filter;
import com.example.walterzhang.instagram2.Profile.AccountSettingsActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.FilePaths;
import com.example.walterzhang.instagram2.utils.FileSearch;
import com.example.walterzhang.instagram2.utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mingshunc on 24/9/18.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    // Constants
    final static int NUM_GRID_COLUMNS = 4;

    // Widgets
    private ImageView galleryImage;
    private GridView galleryGrid;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    // Variables
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starting...");
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);

        galleryImage = (ImageView) view.findViewById(R.id.imgViewGallery);
        galleryGrid = (GridView) view.findViewById(R.id.gridViewGallery);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        directories = new ArrayList<String>();

        ImageView shareClose = (ImageView) view.findViewById(R.id.imgViewCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });

        TextView nextActivity = (TextView) view.findViewById(R.id.txtViewNext);
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");

                if (isRootTask()) {
                    Intent intent = new Intent(getActivity(), CropActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment),
                            getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        setupDirectories();

        return view;
    }

    private boolean isRootTask() {
        if (((ShareActivity)getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void setupDirectories() {
        FilePaths filePaths = new FilePaths();

        // Check for all directories inside "Pictures" and sort them
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
            Collections.sort(directories);
        }

        File file = new File(filePaths.CAMERA);
        if (file.exists()) {
            directories.add(filePaths.CAMERA);
        }

        ArrayList<String> directoryNames = new ArrayList<String>();
        for (int i = 0; i < directories.size(); i++) {
            int index = directories.get(i).lastIndexOf("/");
            directoryNames.add(directories.get(i).substring(index));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected: " + directories.get(position));
                // setup our image grid for chosen directory
                setupGalleryGrid(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupGalleryGrid(String selectedDirectory) {
        Log.d(TAG, "setupGalleryGrid: directory chosen: " + selectedDirectory);
        final ArrayList<String> imgUrls = FileSearch.getFilePaths(selectedDirectory);

        // Set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        galleryGrid.setColumnWidth(imageWidth);

        // Use the grid adapter to adapt images to gridview
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgUrls);
        galleryGrid.setAdapter(adapter);

        // Set the first image to be displayed when the activity fragment view is inflated
        if (!imgUrls.isEmpty()) {
            setImage(imgUrls.get(0), galleryImage, mAppend);
            mSelectedImage = imgUrls.get(0);
        } else {
            galleryImage.setImageResource(R.drawable.no_image_available);
        }

        galleryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgUrls.get(position));

                setImage(imgUrls.get(position), galleryImage, mAppend);
                mSelectedImage = imgUrls.get(position);
            }
        });
    }

    private void setImage(String imgUrl, ImageView image, String append) {
        Log.d(TAG, "setImage: setting image.");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgUrl, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
