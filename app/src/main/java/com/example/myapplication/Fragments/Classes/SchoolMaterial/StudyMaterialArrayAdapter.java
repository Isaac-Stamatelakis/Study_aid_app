package com.example.myapplication.Fragments.Classes.SchoolMaterial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;

public class StudyMaterialArrayAdapter extends ArrayAdapter<StudyMaterial> {
    private ArrayList<StudyMaterial> studyMaterials;
    private Context context;

    public StudyMaterialArrayAdapter(Context context, ArrayList<StudyMaterial> studyMaterials) {
        super(context,0,studyMaterials);
        this.studyMaterials = studyMaterials;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.class_content, parent,false);
        }

        StudyMaterial studyMaterial = this.studyMaterials.get(position);

        return view;
    }

}
