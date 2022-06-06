package com.example.socialmedia.Model;

public class NotificationModel {
    String id, post_id, user_id, other_user_id, msg , type;
    Long timestamp;

    public NotificationModel(String id, String post_id, String user_id, String other_user_id, String msg, String type, Long timestamp) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.other_user_id = other_user_id;
        this.msg = msg;
        this.type = type;
        this.timestamp = timestamp;
    }

    public NotificationModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOther_user_id() {
        return other_user_id;
    }

    public void setOther_user_id(String other_user_id) {
        this.other_user_id = other_user_id;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
