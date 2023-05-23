package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.HashMap;

public class AddFlashCardDialogFragment extends DialogFragment {
    Activity activity;
    EditText questionText;
    EditText answerText;
    public OnInputListener onInputListener;
    public interface OnInputListener {
        void sendInput(HashMap<String, String> input);
    }
    public AddFlashCardDialogFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("AddFlashCard", "OnAttach : ClassCastException: " + e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View text_input_dialog = inflater.inflate(R.layout.add_flashcard_dialog_fragment, null);
        builder.setView(text_input_dialog);

        questionText = text_input_dialog.findViewById(R.id.add_flash_card_question_text);
        answerText = text_input_dialog.findViewById(R.id.add_flash_card_answer_text);

        builder
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString("question", questionText.getText().toString());
                        bundle.putString("answer", answerText.getText().toString());
                        HashMap<String, String> tempHashMap = new HashMap<String, String>();
                        tempHashMap.put("question", questionText.getText().toString());
                        tempHashMap.put("answer", answerText.getText().toString());
                        onInputListener.sendInput(tempHashMap);
                        dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();

    }
}
