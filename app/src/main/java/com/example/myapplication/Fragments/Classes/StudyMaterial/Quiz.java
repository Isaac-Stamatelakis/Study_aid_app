package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Quiz extends StudyMaterial {
    private final String TAG = "QUIZ";
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
                // As GPT seems to love generating the quizzes with the question number in front,
                // the best solution to me is to encourage it and offset the quizparts by 1.
                HashMap<String, String> quizMap = new HashMap<String, String>();
                String[] quizParts = rawQuestionInfo.split(separator_possible_answer);
                quizMap.put("question", quizParts[1]); // Question is always first
                int i = 2;
                while (i < quizParts.length) {
                    Log.d("TEST", quizParts[i]);
                    if (quizParts[i].contains(separator_correct_answer)) {
                        String[] separatedPart = quizParts[i].split(separator_correct_answer);
                        quizMap.put("answer",separatedPart[1]);
                        quizParts[i] = separatedPart[0];
                    }
                    quizMap.put("answer".concat(String.valueOf((i-1))), quizParts[i]);
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
        studyMaterialInfo.put("attempts", Collections.emptyList());
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

    public void addAttemptToDatabase(String formattedAttempt) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("StudyMaterial").document(getdbID());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    ArrayList<String> currentAttempts = (ArrayList<String>) documentSnapshot.get("attempts");
                    currentAttempts.add(formattedAttempt);
                    documentReference.update("attempts", currentAttempts).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Attempts successfully updated");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Attempts not updated" + e);
                        }
                    });

                } else {
                    Log.e(TAG, "Could not load document snapshot to update");
                }
            }
        });
    }

    public static int getMarks(String attempt, ArrayList<String> correctAnswers) {
        int mark = 0;
        String[] answers = attempt.split("&");
        //assert answers.size() == correctAnswers.size();
        for (int i = 0; i < answers.length && i < correctAnswers.size(); i ++) {
            if (Integer.parseInt(correctAnswers.get(i))-1 == Integer.parseInt(answers[i])) {
                mark ++;
            }
        }
        return mark;
    }

    public ArrayList<String> getCorrectAnswers(String separator_question, String separator_possible_answer, String separator_correct_answer) {
        ArrayList<String> correctAnswers = new ArrayList<>();
        try {
            String[] rawQuestionInfoArray = this.getContent().split(separator_question);
            for (String rawQuestionInfo: rawQuestionInfoArray) {
                String[] quizParts = rawQuestionInfo.split(separator_possible_answer);
                Integer i = 1;
                while (i < quizParts.length) {
                    if (quizParts[i].contains(separator_correct_answer)) {
                        String[] separatedPart = quizParts[i].split(separator_correct_answer);
                        correctAnswers.add(separatedPart[1]);
                    }
                    i ++;
                }
            }

        } catch (Exception e) {
            Log.d("QUIZFRAGMENT", "Error" + e);
        }
        return correctAnswers;
    }

    public int getTotalQuestions (String separator_question) {
        return this.getContent().split(separator_question).length;
    }

    public String formatAttempt(ArrayList<String> answers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < answers.size(); i ++) {
            stringBuilder.append(answers.get(i));
            if (i < answers.size()-1) {
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }

    public String formatContent

            (
            String separator_question,
            String separator_possible_answer,
            String separator_correct_answer,
            ArrayList<HashMap<String, String>> quizMapArray
            ) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < quizMapArray.size(); i ++) {
            stringBuilder.append(i+1);
            stringBuilder.append(separator_possible_answer);
            stringBuilder.append(quizMapArray.get(i).get("question"));
            stringBuilder.append(separator_possible_answer);
            int n = 1;
            while (true) {
                if (quizMapArray.get(i).containsKey("answer" + n)) {
                    stringBuilder.append(quizMapArray.get(i).get("answer" + n));
                    if (quizMapArray.get(i).containsKey("answer" + (n+1))) {
                        stringBuilder.append(separator_possible_answer);
                    }
                    n ++;
                } else {
                    break;
                }
            }
            stringBuilder.append(separator_correct_answer);
            stringBuilder.append(quizMapArray.get(i).get("answer"));
            stringBuilder.append(separator_question);
        }
        return stringBuilder.toString();
    }

    public void updateDBContent(String newFormattedContent, int removedQuestion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("StudyMaterial").document(this.getdbID());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    ArrayList<String> attempts = (ArrayList<String>) documentSnapshot.get("attempts");
                    ArrayList<String> newAttempts = new ArrayList<>();
                    for (int i = 0; i < attempts.size(); i ++) {
                        Log.d("TEST", String.valueOf(removedQuestion));
                        String[] splitAttempt = attempts.get(i).split("&");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int n = 0; n < splitAttempt.length; n ++) {
                            if (n == removedQuestion) {
                                n ++;
                            } else {
                                stringBuilder.append(splitAttempt[n]);
                                if (n != splitAttempt.length-1 && n+1 != removedQuestion) {
                                    stringBuilder.append("&");
                                }
                            }
                        }
                        newAttempts.add(stringBuilder.toString());
                    }
                    documentReference.update("attempts", newAttempts).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "attempts updated to new content");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "attempts not updated to new content. Error: " + e);
                        }
                    });
                    documentReference.update("content", newFormattedContent).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "content updated to new content");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "content not updated to new content. Error: " + e);
                        }
                    });
                }
            }
        });
    }


}
