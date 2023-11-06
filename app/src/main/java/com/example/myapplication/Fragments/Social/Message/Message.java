package com.example.myapplication.Fragments.Social.Message;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Profile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Message {
    protected User owner;
    protected static String TAG = "Message";
    protected LocalDateTime date;

    public Message(User owner, LocalDateTime date) {
        this.owner = owner;
        this.date = date;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setOwner(User user) {
        this.owner = user;
    }
    public abstract String getContent();

}
