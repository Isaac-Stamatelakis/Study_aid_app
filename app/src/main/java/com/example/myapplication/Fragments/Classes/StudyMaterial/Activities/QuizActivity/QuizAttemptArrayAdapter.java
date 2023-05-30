package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizAttemptArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> userAttempts;
    private Context context;
    private ArrayList<String> correctAnswers;
    private int outOf;

    public QuizAttemptArrayAdapter(Context context,ArrayList<String> userAttempts, ArrayList<String> correctAnswers, int outOf) {
        super(context,0,userAttempts);
        this.userAttempts = userAttempts;
        this.correctAnswers = correctAnswers;
        this.context = context;
        this.outOf = outOf;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.quiz_attempt_content, parent,false);
        }

        TextView attemptNumber = view.findViewById(R.id.attempt_content_text);
        attemptNumber.setText("Attempt " + (Integer.toString(position+1)));
        TextView score = view.findViewById(R.id.attempt_content_score);
        StringBuilder stringBuilder = new StringBuilder()
                .append(Quiz.getMarks(userAttempts.get(position), this.correctAnswers))
                .append("/")
                .append(outOf);
        score.setText(stringBuilder.toString());
        return view;
    }
}
