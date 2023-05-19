package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class QuizActivity extends Activity {
    String user_id;
    FirebaseFirestore db;
    Quiz quiz;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        Bundle bundle = getIntent().getExtras();
        quiz = new Quiz(bundle.get("title").toString(), bundle.get("content").toString(),bundle.get("dbID").toString());
        user_id = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();

    }

}
