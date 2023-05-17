package com.example.myapplication.Fragments.Classes.SchoolClass;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolClass {
    private String number;
    private String section;
    private String subject;

    public SchoolClass(String number, String section, String subject) {
        this.number = number;
        this.section = section;
        this.subject = subject;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getNumber() {
        return this.number;
    }
    public String getSection() {
        return this.section;
    }
    public String getSubject() {
        return this.subject;
    }

    public void addClassToDB(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Classes");
        Map<String, Object> classInfo = new HashMap<>();
        classInfo.put("Subject", this.subject);
        classInfo.put("Number", this.number);
        classInfo.put("Section", this.section);
        classInfo.put("user_id", user_id);
        classInfo.put("study_material", Collections.emptyList());
        CollectionReference classCollection = db.collection("Classes");
        String id = classCollection.document().getId();
        classCollection.document(id).set(classInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("AddClassFragment", "Class successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AddClassFragment", "Class not added".concat(e.toString()));
                    }
                });
    }
    public String getFullClassName() {
        StringBuilder string = new StringBuilder()
                .append(this.subject)
                .append(" ")
                .append(this.number);
        if (this.section != null) {
            string.append(this.section);
        }
        return string.toString();
    }

}
