package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudyMaterial {
    private String title;
    private String content;
    private String dbID;

    public StudyMaterial(String title, String content, String dbID) {
        this.title = title;
        this.content = content;
        this.dbID = dbID;
    }

    public String getTitle() {
        return this.title;
    }
    public String getContent() {return this.content;}
    public String getdbID() {return this.dbID;}


    public void addToDatabase(String classID) {

    }


}
