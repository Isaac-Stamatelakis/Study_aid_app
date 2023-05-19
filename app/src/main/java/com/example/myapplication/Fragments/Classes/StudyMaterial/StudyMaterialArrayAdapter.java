package com.example.myapplication.Fragments.Classes.StudyMaterial;

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
            view = LayoutInflater.from(context).inflate(R.layout.study_material_content, parent,false);
        }

        StudyMaterial studyMaterial = this.studyMaterials.get(position);
        TextView studyMaterialTitleText = view.findViewById(R.id.study_material_text);
        studyMaterialTitleText.setText(studyMaterial.getTitle());
        return view;
    }

    public void setStudyMaterials(ArrayList<StudyMaterial> studyMaterials) {
        this.studyMaterials = studyMaterials;
    }
}
