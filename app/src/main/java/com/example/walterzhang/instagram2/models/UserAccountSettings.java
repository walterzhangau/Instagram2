package com.example.walterzhang.instagram2.models;

public class UserAccountSettings {

    private String description;
    private long followers;
    private long following;
    private long posts;
    private String display_name;
    private String profile_photo;
    private String username;


    public UserAccountSettings(String description, long followers, long following, long posts, String display_name, String profile_photo, String username) {
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.display_name = display_name;
        this.profile_photo = profile_photo;
        this.username = username;
    }

    public UserAccountSettings() {
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
