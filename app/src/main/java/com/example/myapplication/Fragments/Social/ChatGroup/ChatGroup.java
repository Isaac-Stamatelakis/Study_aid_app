package com.example.myapplication.Fragments.Social.ChatGroup;

import com.example.myapplication.Fragments.Social.Message.Message;

import java.util.ArrayList;

public class ChatGroup {
    private String name;
    private ArrayList<String> members;
    private ArrayList<Message> messages;

    public ChatGroup(String name, ArrayList<String> members) {
        this.name = name;
        this.members = members;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
    public String getName() {
        return this.name;
    }
    public ArrayList<String> getMembers() {
        return this.members;
    }

    public void addMessage(Message message) {

    }


}
