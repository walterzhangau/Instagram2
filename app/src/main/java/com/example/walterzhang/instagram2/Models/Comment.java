package com.example.walterzhang.instagram2.Models;

public class Comment {
    private String user_id;
    private String comment;
    private String date_created;

    public Comment() {

    }

    public Comment(String user_id, String comment, String date_created) {
        this.user_id = user_id;
        this.comment = comment;
        this.date_created = date_created;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "user_id='" + user_id + '\'' +
                ", comment='" + comment + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
