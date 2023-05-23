package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Quiz extends StudyMaterial {
    private final String format = "1&What is the meaning of life?&who knows&42&find it&yes&all of the above$2&What is 2 + 3?&1&4&8&5";
    public Quiz(String title, String content, String dbID) {
        super(title, content, dbID);
    }

    /*
    Format:
    1&What is the meaning of life?&who knows&42&find it&yes&all of the above$
    2&What is 2 + 3?&1&4&8&5$
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
        studyMaterialInfo.put("type", "quiz");
        studyMaterialInfo.put("class", classID);
        String id = studyMaterialReference.document().getId();
        studyMaterialReference.document(id).set(studyMaterialInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Quiz", "Quiz successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Quiz", "Quiz not added" + e.toString());
                    }
                });
    }


}
