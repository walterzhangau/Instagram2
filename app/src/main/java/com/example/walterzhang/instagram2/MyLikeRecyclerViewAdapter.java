package com.example.walterzhang.instagram2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Home.fragment_like_list;
import com.example.walterzhang.instagram2.Home.fragment_like_list.OnLikeListFragmentInteractionListener;
import com.example.walterzhang.instagram2.dummy.DummyContent.DummyItem;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnLikeListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLikeRecyclerViewAdapter extends RecyclerView.Adapter<MyLikeRecyclerViewAdapter.ViewHolder> {

    private final List<com.example.walterzhang.instagram2.models.UserAccountSettings> mUsersSettings;
    private Context mContext;
    private final fragment_like_list.OnLikeListFragmentInteractionListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;

        private FirebaseMethods mFirebaseMethods;
        com.example.walterzhang.instagram2.Models.Photo photo;

        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            mFirebaseMethods = new FirebaseMethods(v.getContext());

            username = (TextView) view.findViewById(R.id.uname);
        }
    }

    public MyLikeRecyclerViewAdapter(@NonNull List<com.example.walterzhang.instagram2.models.UserAccountSettings> usersAccSettings, fragment_like_list.OnLikeListFragmentInteractionListener listener) {
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
