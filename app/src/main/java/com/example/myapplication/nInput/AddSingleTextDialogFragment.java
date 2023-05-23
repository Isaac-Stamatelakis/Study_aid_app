package com.example.myapplication.nInput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AddSingleTextDialogFragment extends DialogFragment {
    /**
     * Due to a bug in with edittext arrayadpters and imm in listviews,
     * we require seperate dialogs for each input amount :)
     */
    Activity activity;
    String hint1;
    EditText hint1Text;
    public AddSingleTextDialogFragment(Activity activity, String hint1) {
        this.hint1 = hint1;
        this.activity = activity;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View text_input_dialog = inflater.inflate(R.layout.add_single_text_dialog_fragment, null);
        builder.setView(text_input_dialog);

        hint1Text = text_input_dialog.findViewById(R.id.add_single_dialog_hint1);

        builder
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent().putExtra(hint1, hint1Text.getText().toString());
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
