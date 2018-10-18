package com.example.walterzhang.instagram2.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Home.HomeActivity;
import com.example.walterzhang.instagram2.Models.UserSettings;
import com.example.walterzhang.instagram2.Profile.AccountSettingsActivity;
import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.Models.Comment;
import com.example.walterzhang.instagram2.Models.Like;
import com.example.walterzhang.instagram2.Models.Photo;
import com.example.walterzhang.instagram2.Models.User;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;

    // Variables
    private Context mContext;
    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Upload a new photo
     * @param photoType
     * @param caption
     * @param count
     * @param imgUrl
     */
    public void uploadNewPhoto(String photoType, final String caption, final int count,
                               final String imgUrl, Bitmap bm) {
        Log.d(TAG, "uploadNewPhoto: attempting to upload new photo.");

        FilePaths filePaths = new FilePaths();

        // New photo
        if (photoType.equals(mContext.getString(R.string.new_photo))) {
            Log.d(TAG, "uploadNewPhoto: uploading new photo.");

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + uid + "/photo" + (count + 1));

            // Convert imageUrl to bitmap
            if (bm == null) {
                bm = ImageManager.getBitmap(imgUrl);
            }

            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: photo upload success.");
                    Toast.makeText(mContext, "Photo upload success.",
                            Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    // Add photo to "photos" node and "user_photos" node
                                    addPhotoToDatabase(caption, task.getResult().toString());

                                    // Navigate to the main feed so the user can see their photo
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) /
                            taskSnapshot.getTotalByteCount();
                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");

                    if (progress - 15 > mPhotoUploadProgress) {
                        Toast.makeText(mContext, "Photo upload progress: " +
                                String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                }
            });
        }
        // Profile photo
        else if (photoType.equals(mContext.getString(R.string.profile_photo))) {
            Log.d(TAG, "uploadNewPhoto: uploading profile photo.");

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + uid + "/profile_photo");

            // Convert imageUrl to bitmap
            if (bm == null) {
                bm = ImageManager.getBitmap(imgUrl);
            }

            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "onSuccess: photo upload success.");
                    Toast.makeText(mContext, "Photo upload success.",
                            Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    // Insert into the "user_account_settings" node
                                    setProfilePhoto(task.getResult().toString());

                                    ((AccountSettingsActivity)mContext).setViewPager(
                                            ((AccountSettingsActivity)mContext).pagerAdapter.getFragmentNumber(
                                                    mContext.getString(R.string.edit_profile_fragment))
                                    );
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) /
                            taskSnapshot.getTotalByteCount();
                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");

                    if (progress - 15 > mPhotoUploadProgress) {
                        Toast.makeText(mContext, "Photo upload progress: " +
                                String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }
                }
            });
        }
    }

    /**
     * Add a profile photo to the database
     * @param url
     */
    private void setProfilePhoto(String url) {
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(uid).child(mContext.getString(R.string.profile_photo)).setValue(url);
    }

    /**
     * Add a photo to the database
     * @param caption
     * @param url
     */
    private void addPhotoToDatabase(String caption, String url) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database");

        String photoKey = myRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String tags = StringManipulation.getTags(caption);

        Photo photo = new Photo();
        photo.setPhoto_id(photoKey);
        photo.setUser_id(uid);
        photo.setDate_created(getTimeStamp());
        photo.setImage_path(url);
        photo.setCaption(caption);
        photo.setTags(tags);

        // Insert into database
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(uid).child(photoKey).setValue(photo);
        myRef.child(mContext.getString(R.string.dbname_photos))
                .child(photoKey).setValue(photo);
    }

    /**
     * Get the total number of images posted by a user
     * @param dataSnapshot
     * @return
     */
    public int getImageCount(DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot ds : dataSnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()) {
            count++;
        }
        return count;
    }

    /**
     * Get the current timestamp
     * @return
     */
    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Australia/ACT"));
        return sdf.format(new Date());
    }

    /**
     * Save new like photo to the database
     * @return
     */
    public void addNewLike(String photoId) {
        Log.d(TAG, "addNewLike: starting...");

        String newLikeId = myRef.push().getKey();
        Like like = new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.child(mContext.getString(R.string.dbname_photos))
                .child(photoId)
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeId)
                .setValue(like);

        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(photoId)
                .child(mContext.getString(R.string.field_likes))
                .child(newLikeId)
                .setValue(like);
    }

    /**
     * remove the like from the database
     * @return
     */
    public void removeLike(final String photoId) {
        Log.d(TAG, "removeLike: starting...");

        Query queryPhotos = myRef.child(mContext.getString(R.string.dbname_photos))
                .child(photoId)
                .child(mContext.getString(R.string.field_likes));
        queryPhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String keyId = singleSnapshot.getKey();
                    Log.d(TAG, "searching to delete from dbname_user_photos...");
                    if (singleSnapshot.getValue(Like.class).getUser_id()
                            .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        myRef.child(mContext.getString(R.string.dbname_photos))
                                .child(photoId)
                                .child(mContext.getString(R.string.field_likes))
                                .child(keyId)
                                .removeValue();
                        Log.d(TAG, "like deleted from dbname_photos...");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query queryUserPhotos = myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(photoId)
                .child(mContext.getString(R.string.field_likes));
        queryUserPhotos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String keyId = singleSnapshot.getKey();
                    Log.d(TAG, "searching to delete from dbname_user_photos...");
                    if (singleSnapshot.getValue(Like.class).getUser_id()
                            .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        myRef.child(mContext.getString(R.string.dbname_user_photos))
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(photoId)
                                .child(mContext.getString(R.string.field_likes))
                                .child(keyId)
                                .removeValue();
                        Log.d(TAG, "like deleted from dbname_user_photos...");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Save a comment to the database
     * @return
     */
    public void postComment(String photoId, String text) {
        String newCommentId = myRef.push().getKey();
        Comment comment = new Comment();
        comment.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        comment.setComment(text);
        comment.setDate_created(getTimeStamp());

        myRef.child(mContext.getString(R.string.dbname_photos))
                .child(photoId)
                .child(mContext.getString(R.string.field_comments))
                .child(newCommentId)
                .setValue(comment);

        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(photoId)
                .child(mContext.getString(R.string.field_comments))
                .child(newCommentId)
                .setValue(comment);
    }

    public UserSettings getUserAccountSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.getChildren()){
            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);
                try {

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }

            }
                // users node
                if(ds.getKey().equals(mContext.getString(R.string.dbname_user))) {
                    Log.d(TAG, "getUserAccountSettings: datasnapshot: " + ds);

                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );
                    user.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );
                    user.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );
                    user.setUser_id(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUser_id()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
                }
            }

        return new UserSettings(user, settings);

    }

    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: upadting username to: " + username);

        myRef.child(mContext.getString(R.string.dbname_user))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }


}
