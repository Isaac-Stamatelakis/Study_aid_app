package com.example.myapplication.Fragments.Classes.StudyMaterial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.myapplication.Fragments.Classes.ManualAddClassDialogFragment;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity.QuizActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.Random;

public class AIGenerateFragment extends Fragment {
    FirebaseFirestore db;

    TextView mainHeader;

    EditText inputText;
    Button button;
    EditText titleText;
    EditText numberText;
    private String schoolClassID;
    private String mode;
    private static String TAG = "AIGenerateFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            schoolClassID = bundle.get("id").toString();
            mode = bundle.get("mode").toString();
        }
        View view = inflater.inflate(R.layout.ai_generate, container, false);
        mainHeader = view.findViewById(R.id.ai_generate_top_text);

        inputText = view.findViewById(R.id.ai_generate_user_input);
        button = view.findViewById(R.id.ai_generate_button);
        numberText = view.findViewById(R.id.ai_number_text);
        titleText = view.findViewById(R.id.ai_generate_title_edit);
        TextView numberText = view.findViewById(R.id.ai_number_text);
        String text = mainHeader.getText().toString();

        switch (mode) {
            case "quizzes":
                numberText.setHint("Number of questions");
                text = text.replace("[STUDYMATERIAL]","quiz");
                break;
            case "flashcards":
                numberText.setHint("Number of flashcards");
                text = text.replace("[STUDYMATERIAL]","flashcard");
                break;
            case "notes":
                numberText.setHint("Amount of words");
                text = text.replace("[STUDYMATERIAL]","note");
                break;
        }
        mainHeader.setText(text);

        db = FirebaseFirestore.getInstance();
        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputText.getText().toString();
                String numberStr = numberText.getText().toString();
                if (numberStr.isEmpty()) {
                    errorMessage("Amount required");
                    return;
                }
                int amount = Integer.parseInt(numberStr);
                if (amount < 10) {
                    errorMessage("Amount cannot be less than 10");
                    return;
                }
                if (input.isEmpty()) {
                    errorMessage("Input cannot be empty");
                    return;
                }
                String title = titleText.getText().toString();
                if (title.isEmpty()) {
                    title = "StudyMaterial";
                }
                AITaskManager.getInstance().callGPT(generatePrompt(input,amount), schoolClassID,mode,title);
            }
        });
    }

    private void errorMessage(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(message)
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    private String generatePrompt(String input, Integer n) {
        String string = null;
        switch (mode) {
            case "quizzes":
                string = "Generate me a " +
                        n.toString() +
                        " question quiz on " +
                        input +
                        " in the following format: ";
                for (Integer i = 1; i <= n; i ++) {
                    string = string + "question" + i.toString() + "&q&" +
                            "question&q&answer1&q&answer2&q&answer3&q&answer4&a& +" +
                            (new Random().nextInt(4 - 1 + 1) + 1) +
                            "&f&";
                }
                string = string + ". Please pay careful attention to the format. This will be read by code and must be precisely as requested." +
                        "Place the number of the correct answer between &a& and &f&.";
                break;
            case "flashcards":
                /*
                Generate me 10 flashcards on grade 6 level math in canada in the following format:
                .1.question&q&answer&f&2.question&q&answer&f&3.question&q&answer&f&4.question&q&answer&f&
                Please pay careful attention to the format. This will be read by code and must be precisely as requested. Do not include "The answer is" in the answer, just give the answer.
                In words, give a question then separate it with &q& give the answer then separate it with &f&. Repeat.
                */
                string = "Generate me " + n.toString() + " flashcards on " +
                        input + " in the following format: ";
                        for (Integer i = 1; i <= n; i ++) {
                            String  tempStr = i.toString();
                            string = string + tempStr + "." +
                                    "[question" + tempStr + "]" +
                                    "&q&[answer" + tempStr + "]&f&";
                        }
                        string = string + ". Please pay careful attention to the format. " +
                                "replace [question] with the question, replace [answer] with the answer. " +
                                "This will be read by code and must be precisely as requested.";
                        break;
            case "notes":
                /*
                Generate me 1000 words of notes on grade 6 level math in canada that would be useful to someone studying
                */
                string ="Generate me " + n.toString() + " paragraphs of notes on " + input +
                        " that would be useful to someone studying.";
        }
        /*
        if (string != null) {
            string = string + "If this is a request you cannot fulfill for any reason. Respond in this format:" +
                    "FATALERROR:[ERROR_MESSAGE]" +
                    "replace [ERROR_MESSAGE] with the reason summarized in less than 15 words";
        }
        */

        return string;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
