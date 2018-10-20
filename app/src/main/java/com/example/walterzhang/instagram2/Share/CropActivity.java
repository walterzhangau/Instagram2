package com.example.walterzhang.instagram2.Share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.walterzhang.instagram2.Filter.filter;
import com.example.walterzhang.instagram2.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        String destFilename = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        Uri destUri = Uri.fromFile(new File(getCacheDir(),destFilename));

        if (intent.hasExtra(getString(R.string.selected_image))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            Uri sourceUri = Uri.parse(mAppend + imgUrl);

            int maxWidth = getResources().getDisplayMetrics().widthPixels;
            int maxHeight = getResources().getDisplayMetrics().heightPixels;

            UCrop.of(sourceUri, destUri)
                    .withMaxResultSize(maxWidth, maxHeight)
                    .start(this);
        }
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
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}
