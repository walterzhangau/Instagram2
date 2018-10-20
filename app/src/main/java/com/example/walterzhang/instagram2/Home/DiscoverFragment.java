package com.example.walterzhang.instagram2.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.walterzhang.instagram2.Profile.ProfileActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.Search.SearchActivity;
import com.example.walterzhang.instagram2.models.User;
import com.example.walterzhang.instagram2.models.UserAccountSettings;
import com.example.walterzhang.instagram2.utils.UserListAdapter;
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
import java.util.List;


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
    private UserListAdapter mAdapter;
    private List<User> mUserList;
    private ListView listView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_messages,container,false);
        setupFirebaseAuth();
        mUser = mAuth.getCurrentUser();
        listView = view.findViewById(R.id.listView);
        generateFollowingList();

        return view;
    }

    private void generateSuggestionList() {


        Log.d(TAG, "Finding Logged User suggestion list");

        suggestionList = new ArrayList<>();
        suggestionList.clear();
        myRef = FirebaseDatabase.getInstance().getReference();
        final Object obj = new Object();
        for (String user_id: followingList){
            Log.d(TAG, "Finding Following of:"+user_id);
            Query query = myRef.child(getString(R.string.dbname_following)).child(user_id);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "Found Following User: "+ singleSnapshot.getValue());
                        HashMap<String,String> hmap = ((HashMap)singleSnapshot.getValue(obj.getClass()));
                        if(!hmap.get("user_id").equals(mUser.getUid())){
                            suggestionList.add(hmap.get("user_id"));
                        }
                    }
                    if(suggestionList.isEmpty())
                        Log.d(TAG, "Its Empty");
                    else{
                        for (int i=0; i < suggestionList.size(); i++)
                            Log.d(TAG, "Suggesting User:" + suggestionList.get(i));
                        suggestionList.removeAll(followingList);
                        for (int i=0; i < suggestionList.size(); i++)
                            Log.d(TAG, "Suggesting User after removing Followers:" + suggestionList.get(i));

                        populateUserList();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }

    private void populateUserList(){

        mUserList = new ArrayList<>();
        mUserList.clear();

        myRef = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, "Populating User List");

        for (String user: suggestionList){
            Query query = myRef.child(getString(R.string.dbname_user)).orderByChild(getString(R.string.field_user_id)).equalTo(user);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "OnDataChange: found user details" + singleSnapshot.getValue(User.class).toString());
                        mUserList.add(singleSnapshot.getValue(User.class));
                        displaySuggestions();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        displaySuggestions();

    }

    private void displaySuggestions() {

        mAdapter = new UserListAdapter(getActivity(), R.layout.discover_list, mUserList);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"onItemClick: selected user;" + mUserList.get(i).toString());
                Intent intent=new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.search_activity));
                intent.putExtra(getString(R.string.intent_user), mUserList.get(i));
                startActivity(intent);
            }
        });


    }

    /** generateFollowerList method will generate a list of all the users whom current user follows **/

    private void generateFollowingList() {

        Log.d(TAG, "Finding Logged User following list");

        followingList = new ArrayList<>();

        followingList.clear();

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
