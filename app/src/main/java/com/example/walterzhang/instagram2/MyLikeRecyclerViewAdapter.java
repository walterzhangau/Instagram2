package com.example.walterzhang.instagram2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Home.LikeListFragment;
import com.example.walterzhang.instagram2.Home.LikeListFragment.OnLikeListFragmentInteractionListener;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;
import com.example.walterzhang.instagram2.utils.UniversalImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LikeListFragment} and makes a call to the
 * specified {@link OnLikeListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLikeRecyclerViewAdapter extends RecyclerView.Adapter<MyLikeRecyclerViewAdapter.ViewHolder> {

    private final List<UserAccountSettings> mUsersSettings;
    private final LikeListFragment.OnLikeListFragmentInteractionListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicture;
        TextView username;

        private FirebaseMethods mFirebaseMethods;
        Photo photo;


        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            mFirebaseMethods = new FirebaseMethods(v.getContext());

            profilePicture = view.findViewById(R.id.profile_image_comments);
            username = view.findViewById(R.id.unameUser);
        }
    }

    public MyLikeRecyclerViewAdapter(@NonNull List<UserAccountSettings> usersAccSettings, LikeListFragment.OnLikeListFragmentInteractionListener listener) {
        mUsersSettings = usersAccSettings;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.username.setText(mUsersSettings.get(position).getUsername());
        UniversalImageLoader.setImage(mUsersSettings.get(position).getProfile_photo(), holder.profilePicture, null, "");

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.username);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsersSettings.size();
    }
}
