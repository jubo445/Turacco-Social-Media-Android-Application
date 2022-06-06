package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class comments implements Serializable {
    public List<CommonModel> comments = new ArrayList<>();

    public comments() {
    }

    public comments(List<CommonModel> comments) {
        this.comments = comments;
    }

    public List<CommonModel> getComments() {
        return comments;
    }

    public void setComments(List<CommonModel> comments) {
        this.comments = comments;
    }

    public void set1Comments(List<CommonModel> comments) {
        this.comments = comments;
    }
}
