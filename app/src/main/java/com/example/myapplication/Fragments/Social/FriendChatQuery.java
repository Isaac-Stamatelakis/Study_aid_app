package com.example.myapplication.Fragments.Social;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendChatQuery {
    private static final String TAG = "FriendChatQuery";
    protected String userA_androidID;
    protected String userB_androidID;
    protected final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FriendChatQuery(String userA_androidID, String userB_androidID) {
        this.userA_androidID = userA_androidID;
        this.userB_androidID = userB_androidID;
    }


    public void execute() {
        generateQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<FriendChatGroup> friendChatGroups = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    friendChatGroups.add(setFriendChatGroup(queryDocumentSnapshot));
                }
                queryCompleted(friendChatGroups);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });

    }
    protected Query generateQuery() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(userA_androidID); ids.add(userB_androidID);
        return db.collection("Chats").whereEqualTo("type","friends").whereArrayContains("members",ids);
    }

    protected FriendChatGroup setFriendChatGroup(QueryDocumentSnapshot queryDocumentSnapshot) {
        Object value = queryDocumentSnapshot.get("members");
        ArrayList<String> members = new ArrayList<>();
        if (value != null) {
            members = (ArrayList<String>) value;
        } else {
            return null;
        }
        return new FriendChatGroup("FriendChatGroup",members,null,queryDocumentSnapshot.getId());
    }
    protected void queryCompleted(ArrayList<FriendChatGroup> friendChatGroups) {

    }
}
