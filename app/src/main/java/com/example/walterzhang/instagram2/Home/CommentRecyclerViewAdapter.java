package com.example.walterzhang.instagram2.Home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Models.Comment;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TextView} and makes a call to the
 * specified {@link fragment_comment_list.onCommentListFragmentInteractionListener}.
 */
public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private final List<Comment> mComments;
    private final List<UserAccountSettings> mUsersSettings;
    private final fragment_comment_list.onCommentListFragmentInteractionListener mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        EditText commentText;
        View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            username = (TextView) view.findViewById(R.id.unameComment);
            commentText = (EditText) view.findViewById(R.id.commentText);
        }
    }

    public CommentRecyclerViewAdapter(@NonNull List<UserAccountSettings> usersAccSettings, List<Comment> comments, fragment_comment_list.onCommentListFragmentInteractionListener listener) {
        mUsersSettings = usersAccSettings;
        mComments = comments;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.username.setText(mUsersSettings.get(position).getUsername());
        holder.commentText.setText(mComments.get(position).getComment());

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
        return mComments.size();
    }
}
