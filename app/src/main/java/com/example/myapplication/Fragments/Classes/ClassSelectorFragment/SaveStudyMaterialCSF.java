package com.example.myapplication.Fragments.Classes.ClassSelectorFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class SaveStudyMaterialCSF extends ChatClassSelectorFragment{
    protected StudyMaterial studyMaterial;
    public SaveStudyMaterialCSF(StudyMaterial studyMaterial) {
        this.studyMaterial = studyMaterial;
    }

    @Override
    protected void documentLoaded(DocumentSnapshot documentSnapshot) {
        new AlertDialog.Builder(getContext())
                .setTitle(Html.fromHtml("Do you want to put " + studyMaterial.getTitle() + " into this class?"))
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        studyMaterial.addToDatabase(documentSnapshot.getId());
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
