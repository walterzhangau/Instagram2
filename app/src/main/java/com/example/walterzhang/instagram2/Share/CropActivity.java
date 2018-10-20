package com.example.walterzhang.instagram2.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.walterzhang.instagram2.Filter.filter;
import com.example.walterzhang.instagram2.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by mingshunc on 20/10/18.
 */

public class CropActivity extends AppCompatActivity {

    private static final String TAG = "CropActivity";

    // Variables
    private String mAppend = "file:";
    private Intent intent;
    private String imgUrl;
    private Bitmap bitMap;
    private Uri srcUri;
    private Uri destUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Creating CropActivity");

        intent = getIntent();

        if (intent.hasExtra(getString(R.string.selected_image))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            srcUri = Uri.parse(mAppend + imgUrl);
        } else if(intent.hasExtra(getString(R.string.selected_bitmap))){
            bitMap = intent.getParcelableExtra(getString(R.string.selected_bitmap));

            String srcFile = new StringBuilder(UUID.randomUUID().toString()).append(".png").toString();
            srcUri = Uri.fromFile(new File(getCacheDir(), srcFile));

            try {
                FileOutputStream out = new FileOutputStream(
                        srcUri.toString().replaceFirst(mAppend,""));
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String destFile = new StringBuilder(UUID.randomUUID().toString()).append(".png").toString();
        destUri = Uri.fromFile(new File(getCacheDir(), destFile));

        int maxWidth = getResources().getDisplayMetrics().widthPixels;
        int maxHeight = getResources().getDisplayMetrics().heightPixels;

        UCrop.of(srcUri, destUri)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            Log.d(TAG, "onActivityResult: resultUri: " + resultUri.toString());

            Intent intent = new Intent(this, filter.class);
            intent.putExtra(getString(R.string.selected_image),
                    resultUri.toString().replaceFirst(mAppend, ""));
            startActivity(intent);
            finish();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}
