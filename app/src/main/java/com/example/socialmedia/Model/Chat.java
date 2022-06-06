package com.example.socialmedia.Model;

public class Chat {

    String message,receiver,sender;

    public Chat(String message, String receiver, String sender) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Chat() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
