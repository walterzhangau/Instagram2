package com.example.walterzhang.instagram2.Home;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Models.Like;
import com.example.walterzhang.instagram2.MyLikeRecyclerViewAdapter;
import com.example.walterzhang.instagram2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLikeListFragmentInteractionListener}
 * interface.
 */
public class fragment_like_list extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private fragment_like_list.OnLikeListFragmentInteractionListener mListener;
    private MyLikeRecyclerViewAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private DatabaseReference myRef;
    private static final String TAG = "fragment_like_list";
    Context context;
    View likesListView;

    List<com.example.walterzhang.instagram2.models.UserAccountSettings> usersSettingsLiked = new ArrayList<>();
    com.example.walterzhang.instagram2.models.UserAccountSettings userSettings;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_like_list() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_like_list newInstance(int columnCount) {
        fragment_like_list fragment = new fragment_like_list();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);
        mListRecyclerView = (RecyclerView) view.findViewById(R.id.likes_list);
        context = view.getContext();
        String photo_id = getArguments().getString("photo_message");

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            likesListView = view;
            getUsersLikedPhoto(photo_id);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof fragment_like_list.OnLikeListFragmentInteractionListener) {
            mListener = (fragment_like_list.OnLikeListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLikeListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(TextView item);
    }

    public void getUsersLikedPhoto(final String photoId) {
        FirebaseDatabase mFirebaseDatabase;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Query queryPhotos = myRef.child(context.getString(R.string.dbname_photos))
                .child(photoId)
                .child(context.getString(R.string.field_likes));
        queryPhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "searching likes...");

                    //get the user account settings based on the user id:
                    displayUsersLikedPhotoByUserId(singleSnapshot.getValue(Like.class).getUser_id());
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "198");
            }
        });
    }

    private void displayUsersLike() {
        if (usersSettingsLiked != null && usersSettingsLiked.size() > 0) {
            Log.d(TAG, "displayUsersLike: usersSettingsLiked found...");

            mAdapter = new MyLikeRecyclerViewAdapter(usersSettingsLiked, mListener);
            mListRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d(TAG, "displayUsersLike: null usersSettingsLiked...");
        }
    }

    public void displayUsersLikedPhotoByUserId(final String userId) {

        FirebaseDatabase mFirebaseDatabase;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        context = likesListView.getContext();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "searching user accounts...");
                userSettings = dataSnapshot.child(context.getString(R.string.dbname_user_account_settings)).child(userId).getValue(com.example.walterzhang.instagram2.models.UserAccountSettings.class);
                usersSettingsLiked.add(userSettings);

                displayUsersLike();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
