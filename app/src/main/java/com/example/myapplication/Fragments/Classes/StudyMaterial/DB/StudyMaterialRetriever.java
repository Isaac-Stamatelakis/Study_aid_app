package com.example.myapplication.Fragments.Classes.StudyMaterial.DB;

import android.util.Log;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudyMaterialRetriever extends CustomDocumentRetrieval {
    protected String studyMaterialID;
    protected StudyMaterial studyMaterial;
    public StudyMaterialRetriever(String TAG,String studyMaterialID) {
        super(TAG);
        this.studyMaterialID = studyMaterialID;
    }
    @Override
    protected DocumentReference getDocument() {
        return FirebaseFirestore.getInstance().collection("StudyMaterial").document(studyMaterialID);
    }

    @Override
    protected void processSnapshot(DocumentSnapshot snapshot) {
        Object val = snapshot.get("title");
        String title = "";
        if (val != null) {
            title = (String) val;
        }
        val = snapshot.get("content");
        String content = "";
        if (val != null) {
            content = (String) val;
        }
        val = snapshot.get("type");
        String type = "";
        if (val != null) {
            type = (String) val;
        }
        String dbID = snapshot.getId();
        Log.e(TAG,type);
        switch (type) {
            case "quiz":
                studyMaterial = new Quiz(title,content,dbID);
                break;
            case "note":
                studyMaterial = new Note(title,content,dbID);
                break;
            case "flashcard":
                studyMaterial = new FlashCard(title,content,dbID);
                break;
        }
    }

    @Override
    protected void retrievalComplete() {

    }
}