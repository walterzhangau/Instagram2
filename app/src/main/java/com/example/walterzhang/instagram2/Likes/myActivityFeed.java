package com.example.walterzhang.instagram2.Likes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.walterzhang.instagram2.Home.UserFeedListAdapter;
import com.example.walterzhang.instagram2.Models.Comment;
import com.example.walterzhang.instagram2.Models.Like;
import com.example.walterzhang.instagram2.Models.MyActivity;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.Models.User;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.Models.UserSettings;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class myActivityFeed extends Fragment {

    private static final String TAG = "MyActivityFeed";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private ArrayList<Photo> userPhotos;
    private ListView listView;
    private MyActivityAdapter myActivityAdapter;
    private List<MyActivity> myActivityList;
    public myActivityFeed() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_my_activity_feed,container,false);
        setupFirebaseAuth();
        myActivityList = new ArrayList<>();
        myActivityAdapter = new MyActivityAdapter(getActivity(), R.layout.layout_my_activity_row, myActivityList );
        listView = view.findViewById(R.id.listview_activity);
        listView.setAdapter(myActivityAdapter);

        getAllUserPhotos();
        getFollowersList();
        return view;



    }

    //This method is used to fetch all the user photos from the database

    private void getAllUserPhotos() {

        Log.d(TAG, "getAllUserPhotos: Getting all user photos");
        final ArrayList<Photo> photos = new ArrayList<>();
        Query query = myRef.child(getString(R.string.dbname_user_photos)).child(mAuth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                    photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                    photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
                    photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                    photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                    photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());


                    ArrayList<Comment> comments = new ArrayList<Comment>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getString(R.string.field_comments)).getChildren()){
                        Comment comment = new Comment();
                        comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                        comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                        comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                        comments.add(comment);
                    }

                    photo.setComments(comments);

                    List<Like> likesList = new ArrayList<Like>();
                    for (DataSnapshot dSnapshot : singleSnapshot.child(getString(R.string.field_likes)).getChildren()){
                        Like like = new Like();
                        like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
                        likesList.add(like);
                    }
                    photo.setLikes(likesList);
                    photos.add(photo);
                }
                final ArrayList<Photo> photos_reversed = new ArrayList<>();
                Collections.reverse(photos);

                Log.d(TAG,"Photos added: " + photos.size());
                if(photos.isEmpty()){}
                else
                {
                    getUserLike(photos);
                }


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserLike(ArrayList<Photo> photos) {


        Log.d(TAG, "Fetching users who liked the photos");

        for(final Photo photo:photos){
        Log.d(TAG, "Getting likes for photo:" + photo);

        Query query = myRef.child(getString(R.string.dbname_photos)).child(photo.getPhoto_id()).child(getString(R.string.field_likes));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot: dataSnapshot.getChildren()){

                    Log.d(TAG, "Finding Likes for photo:"+ singleSnapShot.getValue(Like.class));
                    final String user_id = singleSnapShot.getValue(Like.class).getUser_id();
                    myActivityList.add(new MyActivity(photo,user_id));
                    Log.d(TAG, "myActivityList: " + myActivityList.size());
                    Collections.reverse(myActivityList);
                    myActivityAdapter.notifyDataSetChanged();

                  /*  myRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "setting photo author name...");
                            String authorName = dataSnapshot.child(getString(R.string.dbname_user_account_settings)).child(user_id).getValue(UserAccountSettings.class).getUsername();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                    /*Query query1 = myRef.child(getString(R.string.dbname_user_account_settings))
                            .child(singleSnapShot.getValue(Like.class).getUser_id());
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    }

    private void getFollowersList(){
            Log.d(TAG, "Getting Follower list");

            Query query = myRef.child(getString(R.string.dbname_followers)).child(mAuth.getCurrentUser().getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapShot: dataSnapshot.getChildren()){
                        Log.d(TAG, "Found Follower:"+ singleSnapShot.getValue(User.class).toString());
                        myActivityList.add(new MyActivity(null, singleSnapShot.getValue(User.class).getUser_id()));
                        Log.d(TAG, "myActivityList: " + myActivityList.size());
                        Collections.reverse(myActivityList);
                        myActivityAdapter.notifyDataSetChanged();

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


}
