package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        getActivity().getSupportFragmentManager().popBackStack();
    }
    public ArrayList<String> getAnswers() {
        return this.answers;
    }
    public ArrayList<String> getCorrectAnswers() {
        ArrayList<String> correctAnswers = new ArrayList<>();
        for (int i = 0; i < quizMapArray.size(); i ++) {
            correctAnswers.add(quizMapArray.get(i).get("answer"));
        }
        return correctAnswers;
    }
}