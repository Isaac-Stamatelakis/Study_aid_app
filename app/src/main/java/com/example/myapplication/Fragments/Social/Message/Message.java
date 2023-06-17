package com.example.myapplication.Fragments.Social.Message;

import java.util.Date;

public class Message {
    private String owner;
    private Date date;

    public Message(String owner, Date date) {
        this.owner = owner;
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public Date getDate() {
        return date;
    }
}
