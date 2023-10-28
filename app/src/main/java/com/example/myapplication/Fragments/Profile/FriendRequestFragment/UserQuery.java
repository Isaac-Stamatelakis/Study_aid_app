package com.example.myapplication.Fragments.Profile.FriendRequestFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Profile.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserQuery {
    protected final static String TAG = "UserQuery";
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected void execute() {
        Query query = generateQuery();
        if (query == null) {
            return;
        }
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    users.add(setUser(queryDocumentSnapshot));
                }
                queryCompleted(users);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    protected Query generateQuery() {
        return db.collection("Users");
    }
    protected User setUser(QueryDocumentSnapshot documentSnapshot) {
        User user = new User(null);
        Object value = documentSnapshot.get("Name");
        if (value != null) {
            user.setName((String) documentSnapshot.get("Name"));
        }
        value = documentSnapshot.get("Education_level");
        if (value != null) {
            user.setEducationLevel((String) documentSnapshot.get("Education_level"));
        }
        value = documentSnapshot.get("Faculty");
        if (value != null) {
            user.setFaculty((String) documentSnapshot.get("Faculty"));
        }
        value = documentSnapshot.get("Major");
        if (value != null) {
            user.setMajors((ArrayList<String>) documentSnapshot.get("Major"));
        }
        value = documentSnapshot.get("Minor");
        if (value != null) {
            user.setMinors((ArrayList<String>) documentSnapshot.get("Minor"));
        }
        value = documentSnapshot.get("School");
        if (value != null) {
            user.setSchools((ArrayList<String>) documentSnapshot.get("School"));
        }
        value = documentSnapshot.get("Year");
        if (value != null) {
            user.setYear(String.valueOf(documentSnapshot.get("Year")));
        }
        value = documentSnapshot.get("FriendRequests");
        if (value != null) {
            user.setFriendRequestAndroidIDs((ArrayList<String>) documentSnapshot.get("FriendRequests"));
        }
        value = documentSnapshot.get("Friends");
        if (value != null) {
            user.setFriends((HashMap<String, String>) documentSnapshot.get("Friends"));
        }
        user.setDocID(documentSnapshot.getId());
        return user;
    }
    protected void queryCompleted(ArrayList<User> users) {

    }
}
