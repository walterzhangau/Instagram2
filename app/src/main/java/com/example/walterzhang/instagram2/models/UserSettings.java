package com.example.walterzhang.instagram2.models;


public class UserSettings {

    private User user;
    private com.example.walterzhang.instagram2.models.UserAccountSettings settings;

    public UserSettings(com.example.walterzhang.instagram2.models.User user, com.example.walterzhang.instagram2.models.UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public UserSettings() {

    }


    public com.example.walterzhang.instagram2.models.User getUser() {
        return user;
    }

    public void setUser(com.example.walterzhang.instagram2.models.User user) {
        this.user = user;
    }

    public com.example.walterzhang.instagram2.models.UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(com.example.walterzhang.instagram2.models.UserAccountSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", settings=" + settings +
                '}';
    }
}