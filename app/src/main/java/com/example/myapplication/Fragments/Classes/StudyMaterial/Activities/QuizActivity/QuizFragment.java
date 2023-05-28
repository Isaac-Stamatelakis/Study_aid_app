package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizFragment extends Fragment {
    FirebaseFirestore db;
    Quiz quiz;
    TextView questionText;
    String separator_correct_answer = "&a&";
    String separator_question = "&f&";
    ListView answerList;
    String separator_possible_answer = "&q&";
    ArrayList<String> currentAnswers;
    Integer currentQuiz;
    ArrayList<HashMap<String, String>> quizMapArray;
    //ArrayList<String> answers;
    Button nextButton;
    Button previousButton;
    TextView extraText;
    ImageView flagQuestion;
    ArrayList<String> answers;
    int quizLayout;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_choice_quiz, container, false);
        //question1&q&answer1&q&answer2&q&answer3&q&answer4&a&1&f&question2&q&answer1&q&answer2&q&answer3&q&answer4&a&4&f&question3&q&answer1&q&answer2&q&answer3&q&answer4&a&3&f&question4&q&answer1&q&answer2&q&answer3&q&answer4&a&2. Make sure to use &q& and &f& as specified. After &a& include the correct answer. Do not include question numbers."
        // General
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        quiz = new Quiz((String) bundle.get("title"), (String) bundle.get("content"), (String) bundle.get("dbID"));
        quizMapArray = new ArrayList<>();
        currentQuiz = 1;
        currentAnswers = new ArrayList<>();
        //Views
        questionText = view.findViewById(R.id.multiple_choice_quiz_question);
        nextButton = view.findViewById(R.id.multiple_choice_next_button);
        previousButton = view.findViewById(R.id.multiple_choice_previous_button);
        extraText = view.findViewById(R.id.multiple_choice_extra_text);
        flagQuestion = view.findViewById(R.id.multiple_choice_flag);
        answerList = view.findViewById(R.id.multiple_choice_answer_list);

        quiz.buildContent(separator_question, separator_possible_answer, separator_correct_answer, quizMapArray, getActivity());
        return view;


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setArrayAdapter() {
        HashMap<String, String> hashMap = quizMapArray.get(currentQuiz-1);
        currentAnswers.clear();
        questionText.setText(hashMap.get("question"));
        Integer i = 1;
        while (hashMap.keySet().contains("answer".concat(i.toString()))) {
            currentAnswers.add(hashMap.get("answer".concat(i.toString())));
            i ++;
        }

    }
}