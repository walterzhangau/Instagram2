package com.example.walterzhang.instagram2.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageManager {

    private static final String TAG = "ImageManager";

    /**
     * Get the bitmap of an imgurl
     * @param imgUrl
     * @return
     */
    public static Bitmap getBitmap(String imgUrl) {
        Log.d("ImageManager", "The path is:"+imgUrl);
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bm = null;

        try {
            Log.d(TAG, "Attempting to get FileInputStream");
            fis = new FileInputStream(imageFile);
            bm = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage());
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                Log.e(TAG, "getBitmap: IOException: " + e.getMessage());
            }
        }
        return bm;
    }

    /**
     * Return byte array from a bitmap
     * Quality is between 0 to 100
     * @param bm
     * @param quality
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bm, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

}
