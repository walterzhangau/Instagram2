package com.example.walterzhang.instagram2.Likes;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walterzhang.instagram2.Models.MyActivity;
import com.example.walterzhang.instagram2.Models.Photo;

import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.utils.SquareImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyActivityAdapter extends ArrayAdapter<MyActivity> {

    private LayoutInflater mInflater;
    private int layoutResource;
    private Context context;
    private List<MyActivity> list;


    public MyActivityAdapter(@NonNull Context context, int resource, @NonNull List<MyActivity> objects) {
        super(context, resource, objects);

        this.context = context;
        layoutResource = resource;
        mInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.list = objects;

    }

    private static class ViewHolder{
        TextView text;
        CircleImageView profileImage;
        SquareImageView squareImageView;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final ViewHolder holder;
        if(convertView==null){
            convertView=mInflater.inflate(layoutResource,parent,false);
            holder=new ViewHolder();
            holder.text=convertView.findViewById(R.id.activity_text);
            holder.profileImage=convertView.findViewById(R.id.profile_image);
            holder.squareImageView=(SquareImageView) convertView.findViewById(R.id.liked_image);

            convertView.setTag(holder);

        }
        else{
            holder=(MyActivityAdapter.ViewHolder)convertView.getTag();
        }


        Log.d("MyActivityAdapter", " Setting values to the fields");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        Query query=reference.child(context.getString(R.string.dbname_user_account_settings))
                .orderByChild(context.getString(R.string.field_user_id))
                .equalTo(getItem(position).getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ImageLoader imageLoader=ImageLoader.getInstance();
                    imageLoader.displayImage(ds.getValue(UserAccountSettings.class).getProfile_photo(),
                            holder.profileImage);
                    if(getItem(position).getPhoto()==null)
                        holder.text.setText(ds.getValue(UserAccountSettings.class).getUsername() + " followed you.");
                    else
                        holder.text.setText(ds.getValue(UserAccountSettings.class).getUsername() + " liked your photo.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(getItem(position).getPhoto()!=null) {

            Query query1 = reference.child(context.getString(R.string.dbname_photos)).orderByChild(context.getString(R.string.field_photo_id))
                    .equalTo(getItem(position).getPhoto().getPhoto_id());

            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) ds.getValue();
                        photo.setImage_path(objectMap.get(context.getString(R.string.field_image_path)).toString());
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(photo.getImage_path(), holder.squareImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        return convertView;
    }
}
