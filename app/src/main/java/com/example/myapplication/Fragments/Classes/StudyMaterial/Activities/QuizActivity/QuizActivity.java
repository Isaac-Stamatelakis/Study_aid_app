package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


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
                .replace(R.id.quiz_container, preQuizFragment, "SUMMARY")
                .commit();

    }

    @Override
    public void onBackPressed() {
        ArrayList<String> tags = new ArrayList<>(); tags.add("SUMMARY"); tags.add("TAKE_QUIZ"); tags.add("REVIEW_QUIZ");
        FragmentManager fragmentManager = QuizActivity.this.getSupportFragmentManager();
        for (String tag : tags) {
            final Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null && fragment.isVisible()) {
                switch (tag) {
                    case "SUMMARY":
                        super.onBackPressed();
                        return;
                    case "TAKE_QUIZ":
                        takeQuizBack();
                        return;
                    case "REVIEW_QUIZ":
                        reviewQuizBack();
                        return;
                }
            }
        }
        super.onBackPressed();
    }

    public void takeQuizBack() {
        new AlertDialog.Builder(QuizActivity.this)
                .setTitle("Exiting will not save an attempt for this quiz.")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QuizActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public void reviewQuizBack() {
        new AlertDialog.Builder(QuizActivity.this)
                .setTitle("Are you sure you want to exit this review?")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QuizActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
