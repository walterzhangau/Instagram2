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

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.dummy.DummyContent.DummyItem;
import com.example.walterzhang.instagram2.Models.Photo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class fragment_post_list extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static final String TAG = "fragment_post_list";

    private ArrayList<Photo> mPhotos;
    private RecyclerView mListRecyclerView;
    private UserFeedListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public fragment_post_list() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static fragment_post_list newInstance(int columnCount) {
        fragment_post_list fragment = new fragment_post_list();
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
        mListRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            getPhotos();
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
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private void getPhotos() {
        Log.d(TAG, "getPhotos");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        for (int i = 0; i < 1/*mFollowing.size()*/; i++) {
            final int count = i;
            //Query query = reference.child(getString(R.string.dbname_user_photos)).child(mFollowing.get(i).orderByChild(getString(R.string.field_user_id)).equalTo(mFollowing.get(i)));
            Query query = reference.child("photos/");//.child(""); //TEST ONLY / ALSO REMOVE STRING HARD CODING!
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange...");
                    mPhotos = new ArrayList<>();

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Photo photo  = singleSnapshot.getValue(Photo.class);

                        //todo: see how to set these values: (and for the other properties of photo too
//                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                        photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
//                        photo.setDate_taken(objectMap.get(getString(R.string.field_date_created)).toString());
//                        photo.setImage_path(objectMap.get(getString(R.string.field_photo_path)).toString());
                        mPhotos.add(photo);
                        Collections.reverse(mPhotos);
                    }

//                    if (count >= 1) { //mFollowing.size() - 1) {
                        displayPhotos();
//                    } else {
//                        Log.d(TAG, "Not following anyone!");
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "getPhotos:onCancelled", databaseError.toException());
                }
            });
        }
    }

    private void displayPhotos() {
        if (mPhotos != null) {
//            Collections.sort(mPhotos, new Comparator<Photo>() {
//                @Override
//                public int compare(Photo o1, Photo o2) {
//                    return o2.getDate_Created().compareTo(o1.getDate_Created());
//                }
//            });
            mAdapter = new UserFeedListAdapter(getActivity(), R.layout.fragment_post_list, mPhotos);
            mListRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d(TAG, "displayPhotos: null mPhotos...");
        }
    }
}
