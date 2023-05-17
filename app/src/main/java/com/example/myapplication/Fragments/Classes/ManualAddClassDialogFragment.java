package com.example.myapplication.Fragments.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ManualAddClassDialogFragment extends DialogFragment {
    ArrayList<String> strings;
    HashMap results;
    Activity activity;
    EditText subjectEditText;
    EditText numberEditText;
    EditText sectionEditText;

    public ManualAddClassDialogFragment(ArrayList<String> strings, Activity activity) {
        this.strings = strings;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View text_input_dialog = inflater.inflate(R.layout.manual_add_class_dialog_fragment, null);
        builder.setView(text_input_dialog);

        subjectEditText = text_input_dialog.findViewById(R.id.manual_class_subject_edit_text);
        numberEditText = text_input_dialog.findViewById(R.id.manual_class_number_edit_text);
        sectionEditText = text_input_dialog.findViewById(R.id.manual_class_section_edit_text);

        builder
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString("subject", subjectEditText.getText().toString());
                        bundle.putString("number", numberEditText.getText().toString());
                        String section = sectionEditText.getText().toString();
                        if (section == "") {
                            bundle.putString("section", null);
                        } else {
                            bundle.putString("section", section);
                        }
                        Intent intent = new Intent().putExtras(bundle);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), activity.RESULT_OK, intent);
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
