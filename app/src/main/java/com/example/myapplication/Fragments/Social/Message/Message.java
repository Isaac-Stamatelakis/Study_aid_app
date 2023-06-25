package com.example.myapplication.Fragments.Social.Message;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String owner;
    private String content;
    private static String TAG = "Message";
    private LocalDateTime date;

    public Message(String owner, LocalDateTime date, String content) {
        this.owner = owner;
        this.date = date;
        this.content = content;
    }

    public String getOwner() {
        return owner;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getContent() {
        return this.content;
    }

    public void setMessageToDB(String dbID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Messages").document(dbID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    owner = (String) documentSnapshot.get("owner");
                    date = LocalDateTime.parse((String) documentSnapshot.get("date"));
                    content = (String) documentSnapshot.get("content");

                } else {
                    Log.e(TAG, "getFromDatabase could not get message reference");
                }
            }
        });
    }


}
