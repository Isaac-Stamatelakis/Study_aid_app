package com.example.myapplication.Fragments.Social.ChatGroup;

import com.example.myapplication.Fragments.Social.Message.Message;

import java.util.ArrayList;

public class FriendChatGroup extends ChatGroup {
    public FriendChatGroup(String name, ArrayList<String> members, ArrayList<Message> messages, String dbID) {
        super(name, members, messages, dbID);
    }

}
