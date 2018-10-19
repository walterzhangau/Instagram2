package com.example.walterzhang.instagram2.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.walterzhang.instagram2.models.User;
import com.example.walterzhang.instagram2.Profile.ProfileActivity;
import com.example.walterzhang.instagram2.R;

import com.example.walterzhang.instagram2.utils.BottomNavigationViewHelper;
import com.example.walterzhang.instagram2.utils.UserListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by walterzhang on 7/9/18.
 */

public class SearchActivity extends AppCompatActivity{
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = SearchActivity.this;
    private EditText edtSearch;
    private UserListAdapter adapter;
    private DatabaseReference mUserDatabase;
    private List<User> mUserList;
    private ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupBottomNavigationView();
        Log.d(TAG, "onCreate: started ");

        edtSearch=(EditText)findViewById(R.id.edtSearch);
        mListView=(ListView)findViewById(R.id.listView);
        mUserDatabase=FirebaseDatabase.getInstance().getReference();
        initTextListener();
    }

    public void initTextListener(){
        Log.d(TAG, "initTextlistener: initialising");

        mUserList=new ArrayList<>();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text=edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                firebaseUserSearch(text);

            }
        });
    }

    private void firebaseUserSearch(String text) {
        mUserList.clear();
        if(text.length()==0)
        {}
        else {
            Toast.makeText(this, "Search Started", Toast.LENGTH_SHORT).show();

            Query personsQuery = mUserDatabase.child("users").orderByChild("username").startAt(text).endAt("\uf8ff");


            personsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d("SeachActivity", "OnDataChange: found user" + singleSnapshot.getValue(User.class).toString());
                        mUserList.add(singleSnapshot.getValue(User.class));
                        updateUsersList();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }


    private void updateUsersList(){
        adapter = new UserListAdapter(SearchActivity.this,R.layout.search_list, mUserList);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"onItemClick: selected user;" + mUserList.get(i).toString());
                Intent intent=new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.search_activity));
                intent.putExtra(getString(R.string.intent_user), mUserList.get(i));
                startActivity(intent);
            }
        });

    }


    /**
     * BottomNavigationView Setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up bottom nav view");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }



}




