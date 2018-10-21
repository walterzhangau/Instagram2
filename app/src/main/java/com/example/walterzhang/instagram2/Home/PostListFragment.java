package com.example.walterzhang.instagram2.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walterzhang.instagram2.Models.Like;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PostListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
     private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static final String TAG = "PostListFragment";

    private ArrayList<Photo> mPhotos;
    private ArrayList<String> mFollowing;
    private RecyclerView mListRecyclerView;
    private UserFeedListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostListFragment newInstance(int columnCount) {
        PostListFragment fragment = new PostListFragment();
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
        Log.d(TAG, "onCreateView: starting...");
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        mListRecyclerView = view.findViewById(R.id.list);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            getPhotosFromFollowedUsers();
        }
        else {
            Log.d(TAG, "onCreateView: view not instance of RecyclerView...");
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
     */
    public interface OnListFragmentInteractionListener {

    }

    private  void getPhotosFromFollowedUsers() {
        final ArrayList<String> userIds = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query queryFollowing = reference.child("following/")
                .child(FirebaseAuth.getInstance().getUid());
        queryFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "getting followers: onDataChange...");
                //mFollowing = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    userIds.add(singleSnapshot.getKey());
                }
                mFollowing = userIds;
                getPhotos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "Users following count: " + userIds.size());
    }

    private void getPhotos() {
        Log.d(TAG, "getPhotos");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("photos/");  //todo: REMOVE STRING HARD CODING!
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange...");
                mPhotos = new ArrayList<>();

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {



//                    final Photo photo  = singleSnapshot.getValue(Photo.class);
//
//                    if (mFollowing.contains(photo.getUser_id())) {
//                        mPhotos.add(photo);
//                    }
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                    photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                    photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                    photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                    photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                    photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());

                    List<Like> likesList = new ArrayList<Like>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getString(R.string.field_likes)).getChildren()){
                        Like like = new Like();
                        like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
                        likesList.add(like);
                    }
                    photo.setLikes(likesList);
                    if(mFollowing.contains(photo.getUser_id())) {
                        mPhotos.add(photo);
                    }

                }

                Collections.sort(mPhotos, new Comparator<Photo>() {
                    @Override
                    public int compare(Photo u1, Photo u2) {
                        return u1.getDate_created().compareTo(u2.getDate_created());
                    }
                });
                Collections.reverse(mPhotos);

                if (mPhotos.size() > 0) {
                    displayPhotos();
                } else {
                    Log.d(TAG, "Not following anyone!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getPhotos:onCancelled", databaseError.toException());
            }
        });
    }

    private void displayPhotos() {
        if (mPhotos != null) {
            mAdapter = new UserFeedListAdapter(getActivity(), R.layout.fragment_post_list, mPhotos);
            mListRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d(TAG, "displayPhotos: null mPhotos...");
        }
    }
}
