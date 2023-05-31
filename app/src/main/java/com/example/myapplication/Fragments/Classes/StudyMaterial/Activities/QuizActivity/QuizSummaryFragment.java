package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class QuizSummaryFragment extends Fragment {
    FirebaseFirestore db;
    Button takeQuizButton;
    Button regenerateQuizButton;
    TakeQuizFragment takeQuizFragment;
    Button reviewQuizButton;
    ArrayList<String> userAnswers;
    ListView attemptList;
    Quiz quiz;
    ArrayList<String> attempts;
    QuizAttemptArrayAdapter quizAttemptArrayAdapter;
    String separator_correct_answer = "&a&";
    String separator_question = "&f&";
    String separator_possible_answer = "&q&";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pre_quiz_fragment, container, false);
        Bundle bundle = getArguments();
        quiz = new Quiz((String) bundle.get("title"), (String) bundle.get("content"), (String) bundle.get("dbID"));

        db = FirebaseFirestore.getInstance();
        userAnswers = new ArrayList<>();


        //Views
        takeQuizButton = view.findViewById(R.id.prompt_quiz_button_take);
        regenerateQuizButton = view.findViewById(R.id.prompt_quiz_button_generate);
        attemptList = view.findViewById(R.id.prompt_quiz_listview);
        attemptList.setAdapter(quizAttemptArrayAdapter);
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
        attemptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ReviewQuizFragment reviewQuizFragment = new ReviewQuizFragment(
                        attempts.get(position),
                        quiz.getCorrectAnswers(separator_question, separator_possible_answer, separator_correct_answer)
                );
                reviewQuizFragment.setArguments(getArguments());
                fragmentManager.beginTransaction()
                        .replace(R.id.quiz_container, reviewQuizFragment, "REVIEW_QUIZ")
                        .addToBackStack(null)
                        .commit();
            }
        });

        DocumentReference documentReference = db.collection("StudyMaterial").document(quiz.getdbID());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                quiz = new Quiz((String) value.get("title"), (String) value.get("content"), quiz.getdbID());
                attempts = (ArrayList<String>) value.get("attempts");
                quizAttemptArrayAdapter = new QuizAttemptArrayAdapter(
                        getActivity(), attempts,
                        quiz.getCorrectAnswers(separator_question, separator_possible_answer, separator_correct_answer),
                        quiz.getTotalQuestions(separator_question)
                );
                attemptList.setAdapter(quizAttemptArrayAdapter);
                quizAttemptArrayAdapter.notifyDataSetChanged();
            }
        });

        regenerateQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("WARNING: Regenerating this quiz will erase previous attempts!")
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void updateListView() {

    }
}