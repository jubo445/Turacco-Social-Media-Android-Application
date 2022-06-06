package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Story implements Serializable {

    private String
            userImage, userName, userId, postId, imageLink, date;


    public Story() {
    }

    public Story(String userImage, String userName, String userId, String postId, String imageLink, String date) {
        this.userImage = userImage;
        this.userName = userName;
        this.userId = userId;
        this.postId = postId;
        this.imageLink = imageLink;
        this.date = date;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


