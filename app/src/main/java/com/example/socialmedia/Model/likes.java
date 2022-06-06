package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class likes implements Serializable {
    public List<CommonModel> likes = new ArrayList<>();

    public likes() {
    }

    public likes(List<CommonModel> comments) {
        likes = comments;
    }

    public List<CommonModel> getlikes() {
        return likes;
    }

    public void setlikes(List<CommonModel> likes) {
        this.likes = likes;
    }
}
