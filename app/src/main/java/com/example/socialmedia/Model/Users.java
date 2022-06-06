package com.example.socialmedia.Model;

import java.util.HashMap;
import java.util.Map;

public class Users {
    String email,mobile,name,password,user_id , user_image = "" , user_cover_image = "";
    String n_token = "" ;
    public Map<String, Boolean> followers = new HashMap<>();
    public Map<String, Boolean>  following  = new HashMap<>();

    public Users() {
    }

    public Users(String email, String mobile, String name, String password, String user_id, String user_image, String user_cover_image, String n_token, Map<String, Boolean> followers, Map<String, Boolean> following) {
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.password = password;
        this.user_id = user_id;
        this.user_image = user_image;
        this.user_cover_image = user_cover_image;
        this.n_token = n_token;
        this.followers = followers;
        this.following = following;
    }

    public String getN_token() {
        return n_token;
    }

    public void setN_token(String n_token) {
        this.n_token = n_token;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_cover_image() {
        return user_cover_image;
    }

    public void setUser_cover_image(String user_cover_image) {
        this.user_cover_image = user_cover_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
