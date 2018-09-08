package com.example.walterzhang.instagram2.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.walterzhang.instagram2.R;

import java.util.ArrayList;

/**
 * Created by walterzhang on 8/9/18.
 */

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettingsActivity";

    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        mContext = AccountSettingsActivity.this;
        Log.d(TAG, "onCreate: Started ");

        setupSettingsList();

        //Setup the back arrow for navigating back to profile activity
        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Navigating back to Profile Page");
                finish();
            }
        });
    }

    private void setupSettingsList(){
        Log.d(TAG, "setupSettingsList: initialise Account settings List");
        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));
        options.add(getString(R.string.sign_out));
        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
        
    }
}
