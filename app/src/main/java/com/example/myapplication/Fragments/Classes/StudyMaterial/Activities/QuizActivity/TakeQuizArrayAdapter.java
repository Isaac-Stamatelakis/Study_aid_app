package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class TakeQuizArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> currentAnswers;
    private ArrayList<String> answers;

    private Context context;
    private TextView answerText;
    Integer currentQuiz;

    RadioButton radioButton;

    public TakeQuizArrayAdapter(Context context, ArrayList<String> currentAnswers, ArrayList<String> answers, Integer currentQuiz) {
        super(context,0,currentAnswers);
        this.currentAnswers = currentAnswers;
        this.context = context;
        this.answers=answers;
        this.currentQuiz=currentQuiz;
    }
    int selectedPosition = -1;


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.multiple_choice_quiz_content, parent, false);
            radioButton = view.findViewById(R.id.multiple_choice_quiz_selector);
        }
        String answer = this.currentAnswers.get(position);
        //Views
        radioButton.setTag(position);
        answerText = view.findViewById(R.id.multiple_choice_quiz_content_answer);
        radioButton = view.findViewById(R.id.multiple_choice_quiz_selector);
        radioButton.setChecked(selectedPosition==position);
        answerText.setText(answer);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                answers.set(currentQuiz-1, String.valueOf(position));
                Log.d("Set question ".concat(currentQuiz.toString().concat(" answer to:")), String.valueOf(position));
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public void changeCurrentQuiz(int amount) {
        currentQuiz += amount;
        selectedPosition = Integer.parseInt(answers.get(currentQuiz-1));
        notifyDataSetChanged();
    }
    public void resetCurrentAnswer() {
        selectedPosition = -1;
        answers.set(currentQuiz-1, String.valueOf(-1));
    }
}
