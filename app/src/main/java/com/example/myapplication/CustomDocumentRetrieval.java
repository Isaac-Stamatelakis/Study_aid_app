package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public abstract class CustomDocumentRetrieval {
    protected String TAG;
    public CustomDocumentRetrieval(String TAG) {
        this.TAG = TAG;
    }
    public void execute() {
        getDocument().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                processSnapshot(documentSnapshot);
                retrievalComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                retrievalFailure(e);
            }
        });
    }
    protected DocumentReference getDocument() {
        return null;
    }
    protected void processSnapshot(DocumentSnapshot snapshot) {

    }
    protected void retrievalComplete() {

    }
    protected void retrievalFailure(Exception e) {
        Log.e(TAG,e.toString());
    }
}
