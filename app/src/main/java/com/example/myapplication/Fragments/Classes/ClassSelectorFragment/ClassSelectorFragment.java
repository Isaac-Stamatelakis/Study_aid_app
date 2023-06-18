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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.AddClassFragment;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ClassStudyMaterialSelectorFragment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ClassSelectorFragment extends AbstractClassSelectorFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        deleteClassInit();
        selectClassInit();
        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addClassButton.setOnClickListener(new View.OnClickListener() {
          @Override
        public void onClick(View view) {
              AddClassFragment addClassFragment = new AddClassFragment();
              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.container, addClassFragment)
                      .addToBackStack(null)
                      .commit();
        }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void deleteClassInit() {
        classList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = classes.get(position);
                Query userClassesQuery = db.collection("Classes")
                        .whereEqualTo("Subject", schoolClass.getSubject())
                        .whereEqualTo("Number", schoolClass.getNumber())
                        .whereEqualTo("Section", schoolClass.getSection());
                userClassesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Do you want to delete ".concat(schoolClass.getFullClassName()).concat(" ?"))
                                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                db.collection("Classes").document(documentSnapshot.getId()).delete();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })

                                        .show();
                            }
                        }
                    }
                });
                return true;
            }
        });
    }

    private void selectClassInit() {
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
                            ClassStudyMaterialSelectorFragment classFragment = new ClassStudyMaterialSelectorFragment(documentSnapshot.getId());
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, classFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Log.e(TAG, "Failed to go to class");
                        }
                    }
                });
            }
        });
    }

}
