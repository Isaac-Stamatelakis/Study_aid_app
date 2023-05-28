package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A quiz fragment with pre determined answers
 */
public class ReviewQuizFragment extends QuizFragment {
    ReviewQuizArrayAdapter reviewQuizArrayAdapter;
    ArrayList<String> correctAnswers;
    public ReviewQuizFragment(ArrayList<String> answers, ArrayList<String> correctAnswers) {
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        reviewQuizArrayAdapter  = new ReviewQuizArrayAdapter(getContext(), currentAnswers, answers, currentQuiz, correctAnswers);
        setArrayAdapter();
        reviewQuizArrayAdapter.changeCurrentQuiz(0);
        answerList.setAdapter(reviewQuizArrayAdapter);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuiz  == quizMapArray.size()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to exit this review?")
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
                    reviewQuizArrayAdapter.changeCurrentQuiz(1);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuiz == 1) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Are you sure you want to exit this review?")
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
                    currentQuiz --;
                    setArrayAdapter();
                    reviewQuizArrayAdapter.changeCurrentQuiz(-1);

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
                    reviewQuizArrayAdapter.changeCurrentQuiz(1);
                }
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
        StringBuilder stringBuilder = new StringBuilder();
        if (Integer.parseInt(correctAnswers.get(currentQuiz-1))-1 == Integer.parseInt(answers.get(currentQuiz-1))) {
            stringBuilder.append("Your answer is correct.\n");
        } else {
            stringBuilder.append("Your answer is incorrect.\n");
        }
        stringBuilder.append("The correct answer is: ");
        stringBuilder.append(quizMapArray.get(currentQuiz-1).get("answer".concat(correctAnswers.get(currentQuiz-1))));
        extraText.setText(stringBuilder.toString());
        if (currentQuiz == quizMapArray.size()) {
            nextButton.setText("Finish Review");
        } else {
            nextButton.setText("Next");
        }
        if (currentQuiz == 1) {
            previousButton.setText("Exit");
        } else {
            previousButton.setText("Previous");

        }
        reviewQuizArrayAdapter.notifyDataSetChanged();
        super.setArrayAdapter();


    }
}