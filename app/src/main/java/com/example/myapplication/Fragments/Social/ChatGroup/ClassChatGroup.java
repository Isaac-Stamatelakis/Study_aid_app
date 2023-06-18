package com.example.myapplication.Fragments.Social.ChatGroup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Social.Message.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ClassChatGroup extends ChatGroup {
    private ArrayList<String> teachers;
    private ArrayList<String> assistants;

    public String getClassID() {
        return classID;
    }

    private String classID;
    private final String TAG = "ClassChatGroup";

    public ClassChatGroup(String name, ArrayList<String> members, ArrayList<Message> messages, String dbID) {
        super(name, members, messages, dbID);
    }


    @Override
    public void getFromDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setMemberNames();
        db.collection("Chats").document(getDbID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot =  task.getResult();
                    setName((String) documentSnapshot.get("name"));
                    setMemberIDS((ArrayList<String>) documentSnapshot.get("members"));
                    ArrayList<String> messageIDS = (ArrayList<String>) documentSnapshot.get("messages");
                    setMemberIDS(messageIDS);
                    classID = (String) documentSnapshot.get("classID");
                    ArrayList<Message> messages = new ArrayList<>();
                    for (String messageID : messageIDS) {
                        Message message = new Message(null, null, null);
                        message.setMessageToDB(messageID);
                        messages.add(message);
                    }
                    setMessages(messages);
                } else {
                    Log.e(TAG, "getFromDatabase could not get chatgroup reference");
                }
            }
        });
    }
}
