package com.example.walterzhang.instagram2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Photo implements Parcelable {

    private String photo_id;
    private String user_id;
    private String date_created;
    private String image_path;
    private String caption;
    private String tags;
    private List<Like> likes;

    public Photo() {

    }

    public Photo(String photo_id, String user_id, String date_created, String image_path, String caption, String tags, List<Like> likes) {
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.date_created = date_created;
        this.image_path = image_path;
        this.caption = caption;
        this.tags = tags;
        this.likes = likes;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    protected Photo(Parcel in) {
        photo_id = in.readString();
        user_id = in.readString();
        date_created = in.readString();
        image_path = in.readString();
        caption = in.readString();
        tags = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getPhoto_id() {
        return photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getCaption() {
        return caption;
    }

    public String getTags() {
        return tags;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", caption='" + caption + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo_id);
        dest.writeString(user_id);
        dest.writeString(date_created);
        dest.writeString(image_path);
        dest.writeString(caption);
        dest.writeString(tags);
    }
}
