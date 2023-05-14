package com.example.myapplication.Fragments.ClassFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ClassCustomerList extends ArrayAdapter<SchoolClass> {
    private ArrayList<SchoolClass> classes;
    private Context context;

    public ClassCustomerList(Context context, ArrayList<SchoolClass> classes) {
        super(context,0,classes);
        this.classes = classes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.class_content, parent,false);
        }

        SchoolClass schoolClass = this.classes.get(position);

        TextView class_content_textview = view.findViewById(R.id.class_content_text);
        class_content_textview.setText(schoolClass.getSubject().concat(" ").concat(schoolClass.getNumber()));
        return view;
    }

}
