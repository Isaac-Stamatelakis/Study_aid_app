package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Note extends StudyMaterial {
    public Note(String title, String content, String dbID) {
        super(title, content, dbID);
    }

    /*
    Format: Hello these are some notes. I am writing notes now for fun. This is very fun.
     */
    public void buildContent() {

    }

    @Override
    public void addToDatabase(String classID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference studyMaterialReference = db.collection("StudyMaterial");
        Map<String, Object> studyMaterialInfo = new HashMap<>();
        studyMaterialInfo.put("title", getTitle());
        studyMaterialInfo.put("content", "");
        studyMaterialInfo.put("type", "note");
        studyMaterialInfo.put("class", classID);
        String id = studyMaterialReference.document().getId();
        studyMaterialReference.document(id).set(studyMaterialInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Note", "Note successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Note", "Note not added" + e.toString());
                    }
                });
    }
}
