package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
    public void buildContent(String separator_question, String separator_possible_answer, String separator_correct_answer,
                             ArrayList<HashMap<String, String>> quizMapArray, Activity activity) {
        try {
            String[] rawQuestionInfoArray = this.getContent().split(separator_question);
            for (String rawQuestionInfo: rawQuestionInfoArray) {
                HashMap<String, String> quizMap = new HashMap<String, String>();
                String[] quizParts = rawQuestionInfo.split(separator_possible_answer);
                quizMap.put("question", quizParts[0]); // Question is always first
                Integer i = 1;
                while (i < quizParts.length) {
                    if (quizParts[i].contains(separator_correct_answer)) {
                        String[] separatedPart = quizParts[i].split(separator_correct_answer);
                        quizMap.put("answer",separatedPart[1]);
                        quizParts[i] = separatedPart[0];
                    }
                    quizMap.put("answer".concat(i.toString()), quizParts[i]);
                    i ++;
                }
                quizMapArray.add(quizMap);
            }

        } catch (Exception e) {
            Log.d("QUIZFRAGMENT", "Error" + e);
            activity.finish();
        }
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
