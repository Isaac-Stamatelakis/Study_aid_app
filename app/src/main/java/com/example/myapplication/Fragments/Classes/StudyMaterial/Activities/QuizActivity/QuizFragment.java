package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class QuizFragment extends Fragment {
    FirebaseFirestore db;
    Quiz quiz;
    TextView questionText;
    String separator_correct_answer = "&a&";
    String separator_question = "&f&";
    ListView answerList;
    String separator_possible_answer = "&q&";
    MultipleChoiceArrayAdapter multipleChoiceArrayAdapter;
    ArrayList<String> currentAnswers;
    RadioGroup radioGroup;
    Integer currentQuiz;
    ArrayList<HashMap<String, String>> quizMapArray;
    ArrayList<String> answers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_choice_quiz, container, false);
        //question1&q&answer1&q&answer2&q&answer3&q&answer4&a&1&f&question2&q&answer1&q&answer2&q&answer3&q&answer4&a&4&f&question3&q&answer1&q&answer2&q&answer3&q&answer4&a&3&f&question4&q&answer1&q&answer2&q&answer3&q&answer4&a&2. Make sure to use &q& and &f& as specified. After &a& include the correct answer. Do not include question numbers."
        // General
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        quiz = new Quiz((String) bundle.get("title"), (String) bundle.get("content"), (String) bundle.get("dbID"));
        quizMapArray = new ArrayList<>();
        answers = new ArrayList<>();
        currentQuiz = 1;
        currentAnswers = new ArrayList<>();
        //Views
        questionText = view.findViewById(R.id.multiple_choice_quiz_question);

        // Build Content
        try {
            String[] rawQuestionInfoArray = quiz.getContent().split(separator_question);
            for (String rawQuestionInfo: rawQuestionInfoArray) {
                HashMap<String, String> quizMap = new HashMap<String, String>();
                String[] quizParts = rawQuestionInfo.split(separator_possible_answer);
                quizMap.put("question", quizParts[0]); // Question is always first
                Integer i = 1;
                while (i < quizParts.length) {
                    if (quizParts[i].contains(separator_correct_answer)) {
                        String[] separatedPart = quizParts[i].split(separator_correct_answer);
                        quizMap.put("answer",separatedPart[1]);
                        quizParts[i] = separatedPart[0];
                    }
                    quizMap.put("answer".concat(i.toString()), quizParts[i]);
                    i ++;
                }
                quizMapArray.add(quizMap);
                answers.add(null);
            }

        } catch (Exception e) {
            Log.d("QUIZFRAGMENT", "Error" + e);
            getActivity().finish();
        }
        answerList = view.findViewById(R.id.multiple_choice_answer_list);
        multipleChoiceArrayAdapter = new MultipleChoiceArrayAdapter(getContext(), currentAnswers, answers, currentQuiz);
        setArrayAdapter();
        answerList.setAdapter(multipleChoiceArrayAdapter);

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
        multipleChoiceArrayAdapter.notifyDataSetChanged();


    }
}