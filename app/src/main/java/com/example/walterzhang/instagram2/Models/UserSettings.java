package com.example.walterzhang.instagram2.Models;


public class UserSettings {

    private User user;
    private com.example.walterzhang.instagram2.Models.UserAccountSettings settings;

    public UserSettings(com.example.walterzhang.instagram2.Models.User user, com.example.walterzhang.instagram2.Models.UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public UserSettings() {

    }


    public com.example.walterzhang.instagram2.Models.User getUser() {
        return user;
    }

    public void setUser(com.example.walterzhang.instagram2.Models.User user) {
        this.user = user;
    }

    public com.example.walterzhang.instagram2.Models.UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(com.example.walterzhang.instagram2.Models.UserAccountSettings settings) {
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