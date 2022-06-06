package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {

    private String postText, userId, postId, imageLink, date;
    public Map<String, CommonModel> comments = new HashMap<>();
    public Map<String, Boolean> likes = new HashMap<>();

    public Post() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, CommonModel> getComments() {
        return comments;
    }

    public void setComments(Map<String, CommonModel> comments) {
        this.comments = comments;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
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

}
