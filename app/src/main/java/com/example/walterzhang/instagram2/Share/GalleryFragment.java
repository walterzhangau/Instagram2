package com.example.walterzhang.instagram2.Share;

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

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.FilePaths;
import com.example.walterzhang.instagram2.utils.FileSearch;
import com.example.walterzhang.instagram2.utils.GridImageAdapter;

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

        TextView nextScreen = (TextView) view.findViewById(R.id.txtViewScreenNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
            }
        });

        setupDirectories();

        return view;
    }

    private void setupDirectories() {
        FilePaths filePaths = new FilePaths();

        // Check for all directories inside "Pictures" and sort them
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
            Collections.sort(directories);
        }
        directories.add(filePaths.CAMERA);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directories);
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
    }

}
