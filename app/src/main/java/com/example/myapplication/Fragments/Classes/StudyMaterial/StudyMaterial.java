package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
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

public abstract class StudyMaterial {
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
    public String generateContent(Integer amount, String materialType, SchoolClass schoolClass, String unit, String school, String format, Context context) {
        StringBuilder stringBuilderPrompt = new StringBuilder()
                .append("Generate me a ");
        switch (materialType) {
            case "quiz":
                stringBuilderPrompt
                        .append(amount.toString())
                        .append(" question quiz for ");
                break;
            case "flashcard":
                stringBuilderPrompt
                        .append(amount.toString())
                        .append(" pages of flashcards for ");
                break;
            case "notes":
                stringBuilderPrompt
                        .append(amount.toString())
                        .append(" words of notes for ");
                break;
        }
        stringBuilderPrompt
                .append(schoolClass.getSubject()).append(" ")
                .append(schoolClass.getNumber()).append(" unit ")
                .append(unit).append(" at the ")
                .append(school).append(" in the following format: ")
                .append(format);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
        Python py = Python.getInstance();
        PyObject gptModule = py.getModule("gpt");
        return gptModule.callAttr("generateContent", stringBuilderPrompt.toString()).toJava(String.class);
    }

    public abstract void addToDatabase(String classID);


}
