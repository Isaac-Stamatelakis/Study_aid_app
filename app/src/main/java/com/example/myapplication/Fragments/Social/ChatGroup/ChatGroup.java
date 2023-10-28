package com.example.myapplication.Fragments.Social.ChatGroup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Social.ChatGroup.OldChatGroupFragment.OldChatGroupFragment;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.example.myapplication.Fragments.Social.Message.StudyMaterialMessage;
import com.example.myapplication.Fragments.Social.Message.TextMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatGroup {
    private String name;
    private String TAG = "ChatGroup";
    private ArrayList<String> memberIDS;


    private HashMap<String, String> memberNames;
    private ArrayList<Message> messages;
    private ArrayList<String> messageIDS;

    public String getDbID() {
        return dbID;
    }

    private String dbID;


    public void setName(String name) {
        this.name = name;
    }

    public void setMemberIDS(ArrayList<String> memberIDS) {
        this.memberIDS = memberIDS;
    }

    public void setMemberNames(HashMap<String, String> memberNames) {
        this.memberNames = memberNames;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void setMessageIDS(ArrayList<String> messageIDS) {
        this.messageIDS = messageIDS;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public ChatGroup(String name, ArrayList<String> members, ArrayList<Message> messages, String dbID) {
        this.name = name;
        this.memberIDS = members;
        this.messages = messages;
        this.dbID = dbID;
        this.memberNames = new HashMap<>();
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }
    public String getName() {
        return this.name;
    }
    public ArrayList<String> getMemberIDS() {
        return this.memberIDS;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
    public HashMap<String, String> getMemberNames() {
        return memberNames;
    }

    public void addMessageToDB(Message message, String user_id, String type) {
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("content", message.getContent());
        messageInfo.put("date", message.getDate().toString());
        messageInfo.put("type", type);
        messageInfo.put("owner", user_id);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Messages").add(messageInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference messageReference = task.getResult();
                    db.collection("Chats").document(dbID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot chatSnapshot = task.getResult();
                                ArrayList<String> currentMessages = (ArrayList<String>) chatSnapshot.get("messages");
                                currentMessages.add(messageReference.getId());
                                db.collection("Chats").document(dbID).update("messages", currentMessages).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Added message" + message.getContent());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Failed to add message" + e);
                                    }
                                });
                            } else {
                                Log.e(TAG, "Could not get chat document reference");
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Could not get message document reference");
                }
            }
        });
    }

    public void getFromDatabase(OldChatGroupFragment chatGroupFragment) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setMemberNames();
        db.collection("Chats").document(dbID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot =  task.getResult();
                    name = (String) documentSnapshot.get("name");
                    memberIDS = (ArrayList<String>) documentSnapshot.get("members");
                    messageIDS = (ArrayList<String>) documentSnapshot.get("messages");
                    for (String messageID : messageIDS) {
                        db.collection("Messages").document(messageID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String owner = (String) documentSnapshot.get("owner");
                                    LocalDateTime date = LocalDateTime.parse((String) documentSnapshot.get("date"));
                                    String content = (String) documentSnapshot.get("content");
                                    switch ((String) documentSnapshot.get("type")) {
                                        case "text":
                                            messages.add(new TextMessage(owner, date, content));
                                            break;
                                        case "studymaterial":
                                            messages.add(new StudyMaterialMessage(owner, date, content));
                                            break;
                                    }
                                    chatGroupFragment.updateAdapter();
                                } else {
                                    Log.e(TAG, "getFromDatabase could not get message reference");
                                }
                            }
                        });
                    }
                } else {
                    Log.e(TAG, "getFromDatabase could not get chatgroup reference");
                }
            }
        });
    }


    public void setMemberNames() {
        for (String memberID : memberIDS) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("Users").whereEqualTo("user_id", memberID);
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                        memberNames.put(memberID, (String) queryDocumentSnapshot.get("Name"));
                        Log.d("TEST", (String) queryDocumentSnapshot.get("Name"));
                    }
                }
            });
        }
    }

    public ArrayList<String> getMessageIDS() {
        return messageIDS;
    }
}
