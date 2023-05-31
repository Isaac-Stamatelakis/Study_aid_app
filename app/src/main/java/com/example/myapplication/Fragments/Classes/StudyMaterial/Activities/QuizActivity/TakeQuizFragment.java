package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class TakeQuizFragment extends QuizFragment {
    TakeQuizArrayAdapter multipleChoiceArrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        answers = new ArrayList<>();
        for (int i = 0; i < quizMapArray.size(); i ++) {
            answers.add("-1");
        }
        extraText.setText("Clear my choice");
        multipleChoiceArrayAdapter = new TakeQuizArrayAdapter(getContext(), currentAnswers, answers, currentQuiz);
        setArrayAdapter();
        answerList.setAdapter(multipleChoiceArrayAdapter);

        DocumentReference documentReference = db.collection("StudyMaterial").document(quiz.getdbID());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                quiz = new Quiz((String) value.get("title"), (String) value.get("content"), quiz.getdbID());
                setArrayAdapter();
                multipleChoiceArrayAdapter.changeCurrentQuiz(0);
                multipleChoiceArrayAdapter.notifyDataSetChanged();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuiz  == quizMapArray.size()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to submit?")
                            .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    currentQuiz ++;
                    setArrayAdapter();
                    multipleChoiceArrayAdapter.changeCurrentQuiz(1);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuiz == 1) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to exit this quiz?")
                            .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    returnAnswerResults();
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    currentQuiz --;
                    setArrayAdapter();
                    multipleChoiceArrayAdapter.changeCurrentQuiz(-1);

                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuiz  == quizMapArray.size()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to submit?")
                            .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    returnAnswerResults();
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    currentQuiz ++;
                    setArrayAdapter();
                    multipleChoiceArrayAdapter.changeCurrentQuiz(1);
                }
            }
        });
        extraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleChoiceArrayAdapter.resetCurrentAnswer();
                multipleChoiceArrayAdapter.notifyDataSetChanged();
            }
        });

        flagQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Do you want to remove this question?")
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quizMapArray.remove(currentQuiz-1);
                                quiz.updateDBContent(quiz.formatContent(separator_question, separator_possible_answer, separator_correct_answer, quizMapArray), currentQuiz-1);
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
        return view;


    }
    @Override
    public void setArrayAdapter() {
        if (currentQuiz == quizMapArray.size()) {
            nextButton.setText("Submit");
        } else {
            nextButton.setText("Next");
        }
        if (currentQuiz == 1) {
            previousButton.setText("Exit");
        } else {
            previousButton.setText("Previous");

        }
        multipleChoiceArrayAdapter.notifyDataSetChanged();
        super.setArrayAdapter();
    }

    public void returnAnswerResults() {
        quiz.addAttemptToDatabase(quiz.formatAttempt(answers));
        getActivity().getSupportFragmentManager().popBackStack();
    }
}