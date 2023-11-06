package com.example.myapplication.Fragments.Social.Message;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.Message.Message;

import java.time.LocalDateTime;
import java.util.Date;

public class TextMessage extends Message {
    protected String content;
    public TextMessage(User owner, LocalDateTime date, String content) {
        super(owner, date);
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
