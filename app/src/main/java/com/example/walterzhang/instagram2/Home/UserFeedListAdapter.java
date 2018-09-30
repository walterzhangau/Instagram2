package com.example.walterzhang.instagram2.Home;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent.DummyItem} and makes a call to the
 * specified {@link fragment_post_list.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserFeedListAdapter extends RecyclerView.Adapter<UserFeedListAdapter.MyViewHolder> {

    static class MyViewHolder extends RecyclerView.ViewHolder {
        com.example.walterzhang.instagram2.models.User user = new com.example.walterzhang.instagram2.models.User();
//        StringBuilder users;
//        CircleImageView profileImage;
//        TextView username;
        ImageView image;

//        boolean likedByCurrentUser;
//        Photo photo;
//        GestureDetector detector;

        View view;

        public MyViewHolder(View v) {
            super(v);
            view = v;

            image = v.findViewById(R.id.imageView_photo);
        }
    }

    private static final String TAG = "UserFeedListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;
    private String username;

    private List<Photo> mDataset;

    public UserFeedListAdapter(@NonNull Context context, int resource, @NonNull List<Photo> photos) {

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
        mReference = FirebaseDatabase.getInstance().getReference();
        mDataset = photos;
    }

    @Override
    public UserFeedListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(this.mContext).load(mDataset.get(position).getImage_path()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
