package com.example.myapplication.Fragments.Profile.Query;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Profile.Query.Filter.QueryFilter;
import com.example.myapplication.Fragments.Profile.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserSearchQuery {

    public UserSearchQuery(String TAG, QueryDocumentSnapshot previousDocument,boolean clearOnComplete, QueryFilter queryFilter) {
        this.savedSnapshot = previousDocument;
        this.TAG = TAG;
        this.clearOnComplete = clearOnComplete;
        this.queryFilter = queryFilter;
    }
    private final int MAXREQUERIES = 16;
    private int timesReQueried = 0;
    private ArrayList<User> users = new ArrayList<>();
    private QueryDocumentSnapshot savedSnapshot;
    private String TAG;
    protected boolean clearOnComplete;
    public QueryFilter queryFilter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void execute() {
        Query query = db.collection("Users").orderBy("Name");
        if (savedSnapshot != null) {
            query = query.startAfter(savedSnapshot);
        }
        query = query.limit(10);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    if(filter(queryDocumentSnapshot)) {
                        addUser(queryDocumentSnapshot);
                    }
                    savedSnapshot = queryDocumentSnapshot;
                }
                sectionCompleted(queryDocumentSnapshots);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }
    protected void executeCompleted() {
        Log.d(TAG,"Query Completed");
    }
    protected boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        return queryFilter.filter(queryDocumentSnapshot);
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    protected boolean addUser(QueryDocumentSnapshot querySnapshot) {
        if (users.size() >= 10) {
            return false;
        }
        User user = new User(querySnapshot.getId());
        user.setName((String) querySnapshot.get("Name"));
        user.setEducationLevel((String) querySnapshot.get("Education_level"));
        user.setFaculty((String) querySnapshot.get("Faculty"));
        user.setMajors((ArrayList<String>) querySnapshot.get("Major"));
        user.setMinors((ArrayList<String>) querySnapshot.get("Minor"));
        user.setName((String) querySnapshot.get("Name"));
        user.setSchools((ArrayList<String>) querySnapshot.get("School"));
        user.setYear(String.valueOf(querySnapshot.get("Year")));
        user.setDocID(querySnapshot.getId());
        user.setUser_id(String.valueOf(querySnapshot.get("user_id")));
        users.add(user);
        return true;
    }
    protected void sectionCompleted(QuerySnapshot queryDocumentSnapshots) {
        timesReQueried ++;
        if (!queryDocumentSnapshots.isEmpty() && users.size() < 10 && timesReQueried < MAXREQUERIES) {
            Log.d(TAG,"Re-executing Query");
            execute();
        } else {
            executeCompleted();
        }
    }

    public QueryDocumentSnapshot getSavedSnapshot() {
        return savedSnapshot;
    }

    public String getTAG() {
        return TAG;
    }
    public boolean getClearOnComplete() {
        return clearOnComplete;
    }
}
