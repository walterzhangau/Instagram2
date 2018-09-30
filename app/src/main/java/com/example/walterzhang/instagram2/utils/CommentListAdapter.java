package com.example.walterzhang.instagram2.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Models.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static String TAG = "CommentListAdapter";

    private LayoutInflater mInflater;
    private int layoutResource;
    private Context mContext;

    public CommentListAdapter(@NonNull Context context, int resource, @NonNull Comment[] objects) {
        super(context, resource, objects);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResource = resource;
    }

    private static class ViewHolder {
        TextView comment, username, timestamp;
        CircleImageView profileImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //TODO: FIX LAYOUT BEFORE UNCOMMENTING THESE LINES
//            holder.username = (TextView) convertView.findViewById(R.id.comment_username);
//            holder.comment = (TextView) convertView.findViewById(R.id.comment);
//            holder.timestamp = (TextView) convertView.findViewById(R.id.comment_time_posted);
//            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.comment_profile_image);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.comment.setText(getItem(position).getComment());

        //TODO: TIMESTAMP DIFFERENCE

        return convertView;
    }
}
