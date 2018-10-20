package com.example.walterzhang.instagram2.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.Models.Like;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link RecyclerView.Adapter} that can display a {@link //UserFeedListAdapter.MyViewHolder} and makes a call to the
 * specified {@link PostListFragment.OnListFragmentInteractionListener}.
 */
public class UserFeedListAdapter extends RecyclerView.Adapter<UserFeedListAdapter.MyViewHolder> {

    private static final String TAG = "UserFeedListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;

    private List<Photo> mDataset;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        DatabaseReference myRef;
        FirebaseDatabase mFirebaseDatabase;

        ImageView image, mButton_comments;
        ImageView mHeartWhite, mHeartRed;
        TextView likesText, commentsCountTextView, authorNameTextView, postTextView, mTimestamp;
        EditText editTextAddComment;

        private FirebaseMethods mFirebaseMethods;
        Photo photo;
        View view;

        private void broadcastPhotoIdAndStartActivity(Class mClass) {
            Context context = view.getContext();
            String photoId = photo.getPhoto_id();
            Intent intent = new Intent("photo_info");
            intent.putExtra("photoId",photoId);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            intent = new Intent(context, mClass);
            intent.putExtra("photo_message", photoId);
            context.startActivity(intent);
        }

        public MyViewHolder(View v) {
            super(v);
            view = v;

            mFirebaseMethods = new FirebaseMethods(v.getContext());
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();

            image = v.findViewById(R.id.imageView_photo);
            mButton_comments = view.findViewById(R.id.button_comments);
            authorNameTextView = view.findViewById(R.id.text_author_name);

            mHeartWhite =  view.findViewById(R.id.button_notLiked);
            mHeartRed =  view.findViewById(R.id.button_liked);

            likesText =  view.findViewById(R.id.text_likes_count);
            commentsCountTextView =  view.findViewById(R.id.text_view_all_comments);
            editTextAddComment = view.findViewById(R.id.editTextAddComment);
            postTextView = view.findViewById(R.id.text_post_comment);
            mTimestamp = view.findViewById(R.id.text_date_posted);

            mHeartRed.setVisibility(View.GONE);
            mHeartWhite.setVisibility(View.VISIBLE);

            mHeartWhite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: toggling like...");
                    onLikePostClicked();
                }
            });

            mHeartRed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: toggling like...");
                    onLikePostClicked();
                }
            });

            likesText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick likesText: getting photoId...");
                    broadcastPhotoIdAndStartActivity(LikesListActivity.class);
                }
            });

            mButton_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick buttonComment: getting photoId...");
                    broadcastPhotoIdAndStartActivity(CommentsListActivity.class);
                }
            });

            commentsCountTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick commentsCountTextView: getting photoId...");
                    broadcastPhotoIdAndStartActivity(CommentsListActivity.class);
                }
            });

            postTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: posting comment...");
                    onPostCommentClicked();
                }
            });
        }

        /* Toggle between photo liked and not liked */
        public void onLikePostClicked()
        {
            Log.d(TAG, "onLikePostClicked...");
            if (mHeartWhite.getVisibility() == View.VISIBLE) {
                mHeartWhite.setVisibility(View.GONE);
                mHeartRed.setVisibility(View.VISIBLE);
                //save the like information to the db:
                mFirebaseMethods.addNewLike(photo.getPhoto_id());
            }
            else {
                mHeartRed.setVisibility(View.GONE);
                mHeartWhite.setVisibility(View.VISIBLE);
                mFirebaseMethods.removeLike(photo.getPhoto_id());
            }
        }

        /* Save comment to the db */
        public void onPostCommentClicked() {
            Log.d(TAG, "onPostCommentClicked...");

            String text = editTextAddComment.getText().toString();

            mFirebaseMethods.postComment(photo.getPhoto_id(), text);
            editTextAddComment.setText("");
        }

        /* Check if the photo has been liked by the user and if yes, set heart to red */
        public void setHeartColor(final String photoId) {

            Query query = myRef.child(view.getContext().getString(R.string.dbname_photos))
                    .child(photoId)
                    .child(view.getContext().getString(R.string.field_likes));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        if (singleSnapshot.getValue(Like.class).getUser_id()
                                .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            mHeartRed.setVisibility(View.VISIBLE);
                            mHeartWhite.setVisibility(View.GONE);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        /* show the number of users who liked the photo if at least one user liked
          the photo: */
        private void setLikesCount(String photoId) {

            Query query = myRef.child(view.getContext().getString(R.string.dbname_photos))
                    .child(photoId)
                    .child(view.getContext().getString(R.string.field_likes));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = 0;
                    count = dataSnapshot.getChildrenCount();

                    String text = "";
                    if (count == 1) {
                        text = count + " like";
                        likesText.setText(text);
                    }
                    else if (count > 1) {
                        text = count + " likes";
                        likesText.setText(text);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        /* If at least on person commented on the photo, show the number of users who commented on
          the photo and provide a link to view comments: */
        private void setCommentsCount(String photoId) {

            Query query = myRef.child(view.getContext().getString(R.string.dbname_photos))
                    .child(photoId)
                    .child(view.getContext().getString(R.string.field_comments));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = 0;
                    count = dataSnapshot.getChildrenCount();

                    String text = "";
                    if (count == 1) {
                        text = "View " + count + " comment";
                        commentsCountTextView.setText(text);
                    }
                    else if (count > 1) {
                        text = "View " + count + " comments";
                        commentsCountTextView.setText(text);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void getUserAccountSettingsByUserId(final String userId) {

            myRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "setting photo author name...");
                    String authorName = dataSnapshot.child(view.getContext().getString(R.string.dbname_user_account_settings)).child(userId).getValue(UserAccountSettings.class).getUsername();
                    authorNameTextView.setText(authorName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private String getTimestampDifference() {
            Log.d(TAG, "getTimestampDifference: getting timestamp difference...");

            String difference ;
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("Australia/Melbourne"));
            sdf.format(today);
            Date timestamp;
            final String photoTimeStamp = photo.getDate_created();

            try {
                timestamp = sdf.parse(photoTimeStamp);
                difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
            } catch (ParseException e) {
                Log.d(TAG, "getTimestampDifference: Parse exception:" + e.getMessage());
                difference = "0";
            }
            return  difference;
        }

        private void showTimeDifference() {
            String timestampDiff = getTimestampDifference();
            if (!timestampDiff.equals("0")) {
                mTimestamp.setText(timestampDiff + " Days Ago");
            }
            else {
                mTimestamp.setText("Today");
            }
        }
    }

    public UserFeedListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Photo> photos) {

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
        holder.photo = mDataset.get(position);
        String photoId = holder.photo.getPhoto_id();
        String photoUserId = holder.photo.getUser_id();

        //set the name of the photo's author in the top bar of the post in the user feed:
        holder.getUserAccountSettingsByUserId(photoUserId);

        //if the photo has been liked by the user then set the heart to red:
        holder.setHeartColor(photoId);

        holder.setLikesCount(photoId);
        holder.setCommentsCount(photoId);

        holder.showTimeDifference();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
