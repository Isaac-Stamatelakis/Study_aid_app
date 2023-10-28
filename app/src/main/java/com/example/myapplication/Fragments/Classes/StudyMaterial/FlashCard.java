package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FlashCard extends StudyMaterial {
    public FlashCard(String title, String content, String dbID) {
        super(title, content, dbID);
    }
    /*
    Format:
    1&What is 1+7?&8$2&What is 7*8?&56$3What is the definition of the laplace transform?&integral from 0 to infinity of e^(-s*t) * f(t)dt$
     */
    public void buildContent() {

    }

    @Override
    public void addToDatabase(String classID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference studyMaterialReference = db.collection("StudyMaterial");
        Map<String, Object> studyMaterialInfo = new HashMap<>();
        studyMaterialInfo.put("title", getTitle());
        if (getContent() == null) {
            studyMaterialInfo.put("content", "Undefined Question&q&UndefinedAnswer&f&");
        } else {
            studyMaterialInfo.put("content", getContent());
        }
        studyMaterialInfo.put("type", "flashcard");
        studyMaterialInfo.put("class", classID);
        String id = studyMaterialReference.document().getId();
        studyMaterialReference.document(id).set(studyMaterialInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Flashcard", "Flashcard successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Flashcard", "Flashcard not added" + e.toString());
                    }
                });
    }
}
