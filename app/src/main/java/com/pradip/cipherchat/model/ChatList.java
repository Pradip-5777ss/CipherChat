package com.pradip.cipherchat.model;

public class ChatList {
    public String uid;
    public String fullName;
    public String description;
    public Long date;
    public String profileImage;

    public ChatList() {
    }

    public ChatList(String uid, String fullName, String description, Long date, String profileImage) {
        this.uid = uid;
        this.fullName = fullName;
        this.description = description;
        this.date = date;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}