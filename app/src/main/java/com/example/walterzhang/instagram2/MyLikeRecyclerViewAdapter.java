package com.example.walterzhang.instagram2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Home.fragment_like_list;
import com.example.walterzhang.instagram2.Home.fragment_like_list.OnLikeListFragmentInteractionListener;
import com.example.walterzhang.instagram2.models.Photo;
import com.example.walterzhang.instagram2.models.UserAccountSettings;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link fragment_like_list} and makes a call to the
 * specified {@link OnLikeListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLikeRecyclerViewAdapter extends RecyclerView.Adapter<MyLikeRecyclerViewAdapter.ViewHolder> {

    private final List<UserAccountSettings> mUsersSettings;
    private final fragment_like_list.OnLikeListFragmentInteractionListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;

        private FirebaseMethods mFirebaseMethods;
        Photo photo;

        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            mFirebaseMethods = new FirebaseMethods(v.getContext());

            username = view.findViewById(R.id.uname);
        }
    }

    public MyLikeRecyclerViewAdapter(@NonNull List<UserAccountSettings> usersAccSettings, fragment_like_list.OnLikeListFragmentInteractionListener listener) {
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
