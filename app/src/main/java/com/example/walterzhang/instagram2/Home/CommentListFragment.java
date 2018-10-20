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

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.Models.Comment;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
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
 * Activities containing this fragment MUST implement the {@link onCommentListFragmentInteractionListener}
 * interface.
 */
public class CommentListFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;

    private CommentListFragment.onCommentListFragmentInteractionListener mListener;
    private CommentRecyclerViewAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private DatabaseReference myRef;
    private static final String TAG = "CommentListFragment";
    Context context;
    View commentsListView;

    List<Comment> commentsList = new ArrayList<>();
    Comment commentDetails;
    List<UserAccountSettings> usersSettingsCommentedList = new ArrayList<>();
    UserAccountSettings userSettingsCommented;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CommentListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        mListRecyclerView = view.findViewById(R.id.comments_list);
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
            getCommentAndUsersDetails(photo_id);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onCommentListFragmentInteractionListener) {
            mListener = (onCommentListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onCommentListFragmentInteractionListener");
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
    public interface onCommentListFragmentInteractionListener {
        void onListFragmentInteraction(TextView item);
    }

    public void getCommentAndUsersDetails(String photoId) {
        FirebaseDatabase mFirebaseDatabase;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Query queryPhotos = myRef.child(context.getString(R.string.dbname_photos))
                .child(photoId)
                .child(context.getString(R.string.field_comments));
        queryPhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "searching comments...");

                    commentDetails = singleSnapshot.getValue(Comment.class);
                    commentsList.add(commentDetails);
                    final String userId = commentDetails.getUser_id();

                    populateUsersSettingsCommentedList(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void populateUsersSettingsCommentedList(final String userId) {

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "getting details of user who made a comment...");
                userSettingsCommented = dataSnapshot.child(context.getString(R.string.dbname_user_account_settings)).child(userId).getValue(UserAccountSettings.class);
                usersSettingsCommentedList.add(userSettingsCommented);

                if (commentsList != null && commentsList.size() > 0) {
                    Log.d(TAG, "displayUsersLike: usersSettingsLiked found...");

                    mAdapter = new CommentRecyclerViewAdapter(usersSettingsCommentedList, commentsList, mListener);
                    mListRecyclerView.setAdapter(mAdapter);
                }
                else {
                    Log.d(TAG, "displayUsersLike: null usersSettingsLiked...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
