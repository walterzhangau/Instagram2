package com.example.walterzhang.instagram2.Models;

public class Photo {
    private String photo_id;
    private String date_taken;
    private String image_path;
    private String user_id;

    public Photo() {

    }

    public Photo(String photo_id, String date_taken, String image_path, String user_id) {
        this.photo_id = photo_id;
        this.date_taken = date_taken;
        this.image_path = image_path;
        this.user_id = user_id;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photo_id='" + photo_id + '\'' +
                ", date_taken='" + date_taken + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
