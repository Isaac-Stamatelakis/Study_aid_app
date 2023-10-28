package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public abstract class CustomQuery {
    protected String TAG;
    public CustomQuery(String TAG) {
        this.TAG = TAG;
    }
    public void execute() {
        generateQuery().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    processSnapshot(queryDocumentSnapshot);
                }
                queryComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                queryFailure(e);
            }
        });
    }
    protected Query generateQuery() {
        return null;
    }
    protected void processSnapshot(QueryDocumentSnapshot snapshot) {

    }
    protected void queryComplete() {

    }
    protected void queryFailure(Exception e) {
        Log.e(TAG,e.toString());
    }
}
