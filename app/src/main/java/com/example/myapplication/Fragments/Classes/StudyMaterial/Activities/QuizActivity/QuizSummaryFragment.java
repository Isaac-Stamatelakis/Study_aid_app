package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuizSummaryFragment extends Fragment {
    FirebaseFirestore db;
    Button takeQuizButton;
    Button regenerateQuizButton;
    TakeQuizFragment takeQuizFragment;
    Button reviewQuizButton;
    ArrayList<String> userAnswers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pre_quiz_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        userAnswers = new ArrayList<>();
        //Views
        takeQuizButton = view.findViewById(R.id.prompt_quiz_button_take);
        regenerateQuizButton = view.findViewById(R.id.prompt_quiz_button_generate);
        reviewQuizButton = view.findViewById(R.id.prompt_quiz_button_review);
        //Listeners
        takeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goes to quiz fragment
                new AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to take this quiz?")
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                takeQuizFragment = new TakeQuizFragment();
                                takeQuizFragment.setArguments(getArguments());
                                fragmentManager.beginTransaction()
                                        .replace(R.id.quiz_container, takeQuizFragment, "TAKE_QUIZ")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        reviewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takeQuizFragment == null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("You cannot review a quiz you haven't taken")
                            .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    ReviewQuizFragment reviewQuizFragment = new ReviewQuizFragment(takeQuizFragment.getAnswers(), takeQuizFragment.getCorrectAnswers());
                    reviewQuizFragment.setArguments(getArguments());
                    fragmentManager.beginTransaction()
                            .replace(R.id.quiz_container, reviewQuizFragment, "TAKE_QUIZ")
                            .addToBackStack(null)
                            .commit();
                }

                // Goes to generate quiz content fragment
            }
        });

        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}