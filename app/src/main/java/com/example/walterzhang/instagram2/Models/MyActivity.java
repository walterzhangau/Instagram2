package com.example.walterzhang.instagram2.Models;

public class MyActivity  {



    private Photo photo;
    private String user_id;

    public MyActivity(Photo photo, String user_id) {
        this.photo = photo;
        this.user_id = user_id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
