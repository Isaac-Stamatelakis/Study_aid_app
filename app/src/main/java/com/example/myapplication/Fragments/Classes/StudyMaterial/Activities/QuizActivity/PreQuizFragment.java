package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PreQuizFragment extends Fragment {
    FirebaseFirestore db;
    Button takeQuizButton;
    Button regenerateQuizButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pre_quiz_fragment, container, false);

        db = FirebaseFirestore.getInstance();

        //Views
        takeQuizButton = view.findViewById(R.id.prompt_quiz_button_take);
        regenerateQuizButton = view.findViewById(R.id.prompt_quiz_button_generate);

        //Listeners
        takeQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goes to quiz fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                QuizFragment quizFragment = new QuizFragment();
                quizFragment.setArguments(getArguments());
                fragmentManager.beginTransaction()
                        .replace(R.id.quiz_container, quizFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        regenerateQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goes to generate quiz content fragment
            }
        });

        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}