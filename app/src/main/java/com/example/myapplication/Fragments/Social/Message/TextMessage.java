package com.example.myapplication.Fragments.Social.Message;

import com.example.myapplication.Fragments.Social.Message.Message;

import java.util.Date;

public class TextMessage extends Message {
    public String message;
    public TextMessage(String owner, Date date, String message) {
        super(owner, date);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
