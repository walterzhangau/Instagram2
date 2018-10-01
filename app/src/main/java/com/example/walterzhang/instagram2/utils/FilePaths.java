package com.example.walterzhang.instagram2.utils;

import android.os.Environment;

/**
 * Created by mingshunc on 24/9/18.
 */

public class FilePaths {

    // "storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    // firebase image storage path
    public String FIREBASE_IMAGE_STORAGE = "photos/users/";

}
