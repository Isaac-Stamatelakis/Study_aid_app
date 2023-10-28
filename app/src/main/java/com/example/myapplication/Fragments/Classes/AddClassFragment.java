package com.example.myapplication.Fragments.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class AddClassFragment extends Fragment {
    ListView classList;
    ArrayList<SchoolClass> classes;
    ArrayList<SchoolClass> displayedClasses;
    FirebaseFirestore db;
    SchoolClassArrayAdapter classAdapter;
    EditText searchBar;
    Button manualAddButton;
    Fragment thisFragment;
    String user_id;
    private final int requiredCountToShow = 2;
    private static String TAG = "AddClassFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_class_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        classList = view.findViewById(R.id.add_class_fragment_class_list);
        searchBar = view.findViewById(R.id.add_class_fragment_class_search);
        manualAddButton = view.findViewById(R.id.add_class_fragment_manual_button);
        thisFragment = this;
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));

        getClasses();

        //classes.add(new SchoolClass("301",null,"CMPUT"));
        //classes.add(new SchoolClass("466",null,"CMPUT"));
        classAdapter = new SchoolClassArrayAdapter(this.getActivity(),displayedClasses);
        classList.setAdapter(classAdapter);


        return view;


    }
    private void getClasses() {
        classes = new ArrayList<>();
        displayedClasses = new ArrayList<>();
        db.collection("Classes").whereEqualTo("user_id",user_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<SchoolClass> ownedClasses = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    ownedClasses.add(new SchoolClass(
                            queryDocumentSnapshot.getString("Number"),
                            queryDocumentSnapshot.getString("Section"),
                            queryDocumentSnapshot.getString("Subject")
                    ));
                }
                db.collection("Users").whereEqualTo("user_id",user_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<ClassCounter> institutionSchoolClasses = new ArrayList<>();
                        ArrayList<String> institutions = (ArrayList<String>) queryDocumentSnapshots.getDocuments().get(0).get("School");
                        if (institutions == null) {
                            return;
                        }
                        for(String institution : institutions) {
                            db.collection("Classes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                                        if (!institution.equals(querySnapshot.getString("Institution"))) {
                                            continue;
                                        }
                                        SchoolClass schoolClass = (new SchoolClass(
                                                (String) querySnapshot.get("Number"),
                                                (String) querySnapshot.get("Section"),
                                                (String) querySnapshot.get("Subject")
                                        ));
                                        boolean owned = false;
                                        for (SchoolClass ownedClass : ownedClasses) {
                                            if (schoolClass.equal(ownedClass)) {
                                                owned = true;
                                                break;
                                            }
                                        }
                                        if (owned) {
                                            continue;
                                        }

                                        boolean found = false;
                                        for (ClassCounter classCounter : institutionSchoolClasses) {
                                            SchoolClass counterClass = classCounter.schoolClass;
                                            if (schoolClass.equal(counterClass)){
                                                classCounter.count ++;
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            institutionSchoolClasses.add(new ClassCounter(1,schoolClass));
                                        }
                                    }
                                    ArrayList<SchoolClass> classesToShow = new ArrayList<>();
                                    for (ClassCounter classCounter : institutionSchoolClasses) {
                                        if (classCounter.count >= requiredCountToShow) {
                                            classesToShow.add(classCounter.schoolClass);
                                        }
                                    }
                                    institutionQueryCompleted(classesToShow);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG,e.toString());
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }

    private void institutionQueryCompleted(ArrayList<SchoolClass> classesToAdd) {
        classes.addAll(classesToAdd);
        displayedClasses.addAll(classesToAdd);
        classAdapter.notifyDataSetChanged();
    }
    private class ClassCounter {
        public int count;
        public SchoolClass schoolClass;
        public ClassCounter(int count, SchoolClass schoolClass) {
            this.count = count;
            this.schoolClass = schoolClass;
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolClass schoolClass = displayedClasses.get(position);


                StringBuilder dialogTitle = new StringBuilder().append("<font color='#758BFD'>Do you want to add ")
                        .append(schoolClass.getSubject())
                        .append(" ")
                        .append(schoolClass.getNumber());
                if (schoolClass.getSection() != null) {
                    dialogTitle.append(schoolClass.getSection());
                }
                dialogTitle.append(" to your classes?</font>");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml(dialogTitle.toString()))
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addUserClass(schoolClass);
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        manualAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> inputs = new ArrayList<>();
                inputs.add("Section"); inputs.add("Class Number"); inputs.add("Subject");
                ManualAddClassDialogFragment classInputDialog = new ManualAddClassDialogFragment(inputs, getActivity());
                classInputDialog.setTargetFragment(thisFragment, 963);
                classInputDialog.show(getParentFragmentManager(), "Showing classInputDialog");

            }
        });
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sortClasses(searchBar.getText().toString());
            }
        });
    }
    private void sortClasses(String text) {

        displayedClasses.clear();
        if (text.isEmpty()) {
            displayedClasses.addAll(classes);
        } else {
            for (SchoolClass schoolClass : classes) {
                if (schoolClass.getSubjectAndNumber().toLowerCase().contains(text)) {
                    displayedClasses.add(schoolClass);
                }
            }
        }
        classAdapter.notifyDataSetChanged();
    }
    /*
    Recieves class information from ManualAddClassDialogFragment and attempts to add class to database
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 963) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                SchoolClass schoolClass = new SchoolClass(
                        (String) bundle.get("number"),
                        (String) bundle.get("section"),
                        (String) bundle.get("subject")
                );
                this.addUserClass(schoolClass);
                StringBuilder string = new StringBuilder().append("<font color='#758BFD'>")
                        .append(bundle.get("subject"))
                        .append(" ")
                        .append(bundle.get("number"));
                if (bundle.get("section") != null) {
                    string.append(bundle.get("section"));
                }
                string.append(" has been added to your classes!");
                new AlertDialog.Builder(getActivity())
                        .setTitle(Html.fromHtml(string.toString()))
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*
    Searchs for all classes which match the searchText and are at the
    user_id's university/school
     */
    public void getUserClasses(String searchText){

    }

    public void addUserClass(SchoolClass schoolClass) {
        // Check database for name and subject duplicates
        Query userClassesQuery = db.collection("Classes")
                .whereEqualTo("Subject", schoolClass.getSubject())
                .whereEqualTo("Number", schoolClass.getNumber());
        AggregateQuery userClassesQueryCount = userClassesQuery.count();
        userClassesQueryCount.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    if (snapshot.getCount() > 0) {
                        AggregateQuery userSectionQuery = userClassesQuery.whereEqualTo("Section", schoolClass.getSection()).count();
                        userSectionQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task2) {
                                if (task2.isSuccessful()) {
                                    AggregateQuerySnapshot snapshot2 = task2.getResult();
                                    // User has a class with the same subject, number and section in the database. Do not add
                                    if (snapshot2.getCount() > 0) {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("You already have this class")
                                                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {}
                                                })
                                                .show();
                                    }
                                    // User has a class with the same subject and number already in the database.
                                    // Ask if they want to swap?
                                    else {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Do you want to swap sections?")
                                                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Confirm</font>"), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

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
                    }
                    // User doesn't already have class
                    else {
                        schoolClass.addClassToDB(user_id);
                    }
                }
            }
        });
    }
    /*
    Swaps the section of a class.
    This function should only be called when the user already has the class in the database
     */
    public void swapSection(SchoolClass schoolClass) {
        Query userClassesQuery = db.collection("Classes")
                .whereEqualTo("Subject", schoolClass.getSubject())
                .whereEqualTo("Number", schoolClass.getNumber());
        userClassesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        db.collection("Users")
                                .document(documentSnapshot.getId())
                                .update("Section", schoolClass.getSection());
                    }
                }
            }
        });

    }
}
