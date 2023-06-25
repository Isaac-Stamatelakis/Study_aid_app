package com.example.myapplication.Fragments.Classes.ClassSelectorFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


public class StudyMaterialAddClassSelectorFragment extends AbstractClassSelectorFragment {
    StudyMaterial studyMaterial;
    public StudyMaterialAddClassSelectorFragment(StudyMaterial studyMaterial) {
        this.studyMaterial = studyMaterial;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        addClassButton.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                DocumentReference documentReference = db.collection("Classes").document(schoolClass.getDbID());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Log.d(TAG, documentSnapshot.getId());
                            new AlertDialog.Builder(getContext())
                                    .setTitle(Html.fromHtml("Do you want to copy this class?"))
                                    .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
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
                        } else {
                            Log.e(TAG, "Failed to go to class");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
