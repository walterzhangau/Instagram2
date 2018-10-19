package com.example.walterzhang.instagram2.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by walterzhang on 7/9/18.
 */

public class DiscoverFragment extends Fragment {

    private static final String TAG = "DiscoverFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUser mUser;
    private ArrayList<String> followingList;
    private ArrayList<String> suggestionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_messages,container,false);
        setupFirebaseAuth();
        mUser = mAuth.getCurrentUser();

        generateFollowingList();

        return view;
    }

    private void generateSuggestionList() {

        Log.d(TAG, "Finding Logged User suggestion list");

        suggestionList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference();
        final Object obj = new Object();
        for (String user_id: followingList){
            Log.d(TAG, "Finding Follower for:"+user_id);
            Query query = myRef.child(getString(R.string.dbname_followers)).child(user_id);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "Found Follower: "+ singleSnapshot.getValue());
                        HashMap<String,String> hmap = ((HashMap)singleSnapshot.getValue(obj.getClass()));
                        if(!hmap.get("user_id").equals(mUser.getUid())){
                            suggestionList.add(hmap.get("user_id"));
                        }
                    }
                    if(suggestionList.isEmpty())
                        Log.d(TAG, "Its Empty");
                    else{
                        for (int i=0; i < suggestionList.size(); i++)
                            Log.d(TAG, "Suggesting User:" + suggestionList.get(i));}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

    /** generateFollowerList method will generate a list of all the users whom current user follows **/

    private void generateFollowingList() {

        Log.d(TAG, "Finding Logged User following list");

        followingList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(getString(R.string.dbname_following))
                .child(mUser.getUid());
        final Object obj = new Object();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "OnDataChange: found following user" + singleSnapshot.getValue());
                        HashMap<String,String> hmap = ((HashMap)singleSnapshot.getValue(obj.getClass()));
                        followingList.add(hmap.get("user_id"));
                }
                if(followingList.isEmpty())
                    Log.d(TAG, "Its Empty");
                else{
                    for (int i=0; i < followingList.size(); i++)
                        Log.d(TAG, "Following User:" + followingList.get(i));
                    generateSuggestionList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * ---------------------------------------- Firebase ----------------------------------------
     */

    /**
     * Setup the firebase auth object
     */

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
