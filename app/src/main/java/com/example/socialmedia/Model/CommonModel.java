package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonModel implements Serializable
{
    String id , uid , text ;

    public CommonModel() {
    }

    public CommonModel(String id, String uid, String text) {
        this.id = id;
        this.uid = uid;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}


