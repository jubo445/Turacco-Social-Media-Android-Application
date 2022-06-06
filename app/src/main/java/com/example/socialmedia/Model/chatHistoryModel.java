package com.example.socialmedia.Model;

public class chatHistoryModel {
    String user1 , user2  , lsatMsgSender , lastMsg , msg;
    long timestamp  ;

    public chatHistoryModel() {
    }

    public chatHistoryModel(String user1, String user2, String lsatMsgSender, String lastMsg, String msg, long timestamp) {
        this.user1 = user1;
        this.user2 = user2;
        this.lsatMsgSender = lsatMsgSender;
        this.lastMsg = lastMsg;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getLsatMsgSender() {
        return lsatMsgSender;
    }

    public void setLsatMsgSender(String lsatMsgSender) {
        this.lsatMsgSender = lsatMsgSender;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
