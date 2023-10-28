package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Debug;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;


public class AITaskManager {
    private static boolean running = false;

    private static AITaskManager aiTaskManager;
    private static Activity activity = null;
    private static TextView textView;
    private static ProgressBar progressBar;
    private static String TAG = "AITaskManager";
    private AITaskManager() {

    }
    public static AITaskManager getInstance() {
        return aiTaskManager;
    }
    public static boolean initalize(Activity activity) {
        if (AITaskManager.activity != null) {
            return false;
        }
        AITaskManager.activity=activity;
        aiTaskManager = new AITaskManager();
        progressBar = activity.findViewById(R.id.progress_bar); progressBar.setVisibility(View.GONE);
        textView = activity.findViewById(R.id.progress_bar_text); textView.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);


        return true;
    }

    public boolean callGPT(String prompt, String schoolClassID, String mode, String title) {
        if (running) {
            Log.d(TAG, "Could not enter request as GPT is already running");
            return false;
        }
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress();
                        }
                    });
                    running = true;
                    PyObject module = Python.getInstance().getModule("gpt");
                    String content = module.callAttr("gpt",prompt).toString();
                    String completionMessage = null;
                    if (content.contains("FATALERROR:")) {
                        String errorMessage = content.split("FATALERROR:")[1];
                        completionMessage = errorMessage;
                    } else {
                        switch (mode) {
                            case "flashcards":
                                FlashCard flashCard = new FlashCard(title,content,schoolClassID);
                                flashCard.addToDatabase(schoolClassID);
                                break;
                            case "quizzes":
                                Quiz quiz = new Quiz(title,content,schoolClassID);
                                quiz.addToDatabase(schoolClassID);
                                break;
                            case "notes":
                                Note note = new Note(title,content,schoolClassID);
                                note.addToDatabase(schoolClassID);
                        }
                        completionMessage = "GPT has finished generating!";
                    }
                    String finalCompletionMessage = completionMessage;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                            alertCompletion(finalCompletionMessage);
                        }
                    });
                    running = false;

                }
            }
        };
        thread.start();
        return true;
    }


    private void alertCompletion(String completionMessage) {
        new AlertDialog.Builder(activity)
                .setTitle(completionMessage)
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public static boolean isRunning() {
        return running;
    }

    private void showProgress() {
        textView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgress() {
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }



}

