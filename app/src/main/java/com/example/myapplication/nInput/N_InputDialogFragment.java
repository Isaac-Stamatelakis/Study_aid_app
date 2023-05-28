package com.example.myapplication.nInput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.ArrayList;

public class N_InputDialogFragment extends DialogFragment {
    Activity activity;
    EditText editText;
    ListView inputList;
    N_InputArrayAdapter arrayAdapter;
    ArrayList<String> hints;

    public N_InputDialogFragment(ArrayList<String> hints, Activity activity) {
        this.activity = activity;
        this.hints = hints;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.n_input_dialog_fragment, null);
        inputList = view.findViewById(R.id.n_input_list);
        arrayAdapter = new N_InputArrayAdapter(activity, hints);
        inputList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        builder.setView(view);


        //subjectEditText = text_input_dialog.findViewById(R.id.manual_class_subject_edit_text);

        builder
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        inputList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return builder.create();

    }
}
