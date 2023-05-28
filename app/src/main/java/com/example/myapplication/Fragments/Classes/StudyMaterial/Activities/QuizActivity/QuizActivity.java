package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class QuizActivity extends FragmentActivity {
    String user_id;
    FirebaseFirestore db;
    Quiz quiz;
    Button takeQuizButton;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);
        Bundle bundle = getIntent().getExtras();
        quiz = new Quiz(bundle.get("title").toString(), bundle.get("content").toString(),bundle.get("dbID").toString());
        user_id = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        QuizSummaryFragment preQuizFragment = new QuizSummaryFragment();
        preQuizFragment.setArguments(bundle);
        FragmentManager fragmentManager = QuizActivity.this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.quiz_container, preQuizFragment)
                .commit();

    }

}
