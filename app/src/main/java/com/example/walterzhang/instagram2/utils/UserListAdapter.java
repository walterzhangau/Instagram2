package com.example.walterzhang.instagram2.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.models.User;
import com.example.walterzhang.instagram2.models.UserAccountSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserListAdapter extends ArrayAdapter<User> {

    private LayoutInflater mInflater;
    private List<User> mUsers=null;
    private int layoutResource;
    private Context context;


    public UserListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);

        this.context=context;
        mInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutResource=resource;
        this.mUsers=objects;

    }

    private static class ViewHolder{
        TextView username, email;
        CircleImageView profileImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final ViewHolder holder;
        if(convertView==null){
            convertView=mInflater.inflate(layoutResource,parent,false);
            holder=new ViewHolder();
            holder.username=(TextView)convertView.findViewById(R.id.username);
            holder.email=(TextView)convertView.findViewById(R.id.email);
            //holder.profileImage=(TextView)convertView.findViewById(R.id.profileImage);

            convertView.setTag(holder);

        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }


        holder.username.setText(getItem(position).getUsername());
        holder.email.setText(getItem(position).getEmail());

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        Query query=reference.child(context.getString(R.string.dbname_user_account_settings))
                .child("profile_photo").orderByChild("user_id")
                .equalTo(getItem(position).getUser_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ImageLoader imageLoader=ImageLoader.getInstance();
                    imageLoader.displayImage(ds.getValue(UserAccountSettings.class).getProfile_photo(),
                            holder.profileImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return convertView;
    }
}
