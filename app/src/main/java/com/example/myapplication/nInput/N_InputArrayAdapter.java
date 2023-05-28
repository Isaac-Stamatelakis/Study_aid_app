package com.example.myapplication.nInput;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class N_InputArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> hints;
    private Context context;

    public N_InputArrayAdapter(Context context, ArrayList<String> hints) {
        super(context,0,hints);
        this.hints = hints;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.n_input_content, parent,false);
        }

        String hint = this.hints.get(position);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editText = view.findViewById(R.id.n_input_text);
        editText.setFocusable(true);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                editText.requestFocusFromTouch();
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        editText.setHint(hint);
        return view;
    }


}
