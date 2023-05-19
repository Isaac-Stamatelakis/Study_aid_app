package com.example.myapplication.Fragments.Classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.FlashCardActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.NoteActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterialArrayAdapter;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ClassFragment extends Fragment {
    ListView studyMaterialList;
    String user_id;
    String mode;
    FirebaseFirestore db;
    SchoolClass schoolClass;
    String schoolClassID;
    HashMap<String, StudyMaterial> flashCards;
    HashMap<String, StudyMaterial> notes;
    HashMap<String, StudyMaterial> quizzes;
    ArrayList<String> studyMaterialIDs;
    StudyMaterialArrayAdapter studyMaterialArrayAdapter;
    HashMap<String, ImageView> modeSelectors;
    ArrayList<StudyMaterial> arrayAdapterList;
    FloatingActionButton addStudyMaterialButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_fragment, container, false);
        // Views
        studyMaterialList = view.findViewById(R.id.class_fragment_study_material_list);
        modeSelectors = new HashMap<>();
        modeSelectors.put("notes", view.findViewById(R.id.class_fragment_note_selector));
        modeSelectors.put("flashcards", view.findViewById(R.id.class_fragment_flashcard_selector));
        modeSelectors.put("quizzes", view.findViewById(R.id.class_fragment_quiz_selector));
        addStudyMaterialButton = view.findViewById(R.id.class_fragment_add_studymaterial_button);

        // General
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        schoolClassID = getArguments().get("ID").toString();

        // Array Adapter
        flashCards = new HashMap<String, StudyMaterial>();
        notes = new HashMap<String, StudyMaterial>();
        quizzes = new HashMap<String, StudyMaterial>();
        arrayAdapterList = new ArrayList<>();
        studyMaterialArrayAdapter = new StudyMaterialArrayAdapter(this.getActivity(), arrayAdapterList);
        mode = "notes";
        studyMaterialList.setAdapter(studyMaterialArrayAdapter);


        DocumentReference schoolClassReference = db.collection("Classes").document(schoolClassID);
        schoolClassReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                schoolClass = new SchoolClass(
                        (String) value.get("Section"),
                        (String) value.get("Number"),
                        (String) value.get("Subject")
                );
                studyMaterialIDs = (ArrayList<String>) value.getData().get("study_material");
                for (String studyMaterialID : studyMaterialIDs) {
                    db.collection("StudyMaterial").document(studyMaterialID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            switch ((String) Objects.requireNonNull(value.getData().get("type"))) {
                                case "note":
                                    notes.put(value.getId(), new Note(
                                            (String) value.getData().get("title"),
                                            (String) value.getData().get("content"),
                                            studyMaterialID
                                    ));
                                    break;
                                case "flashcard":
                                    flashCards.put(value.getId(), new Note(
                                            (String) value.getData().get("title"),
                                            (String) value.getData().get("content"),
                                            studyMaterialID
                                    ));
                                    break;
                                case "quiz":
                                    quizzes.put(value.getId(), new Note(
                                            (String) value.getData().get("title"),
                                            (String) value.getData().get("content"),
                                            studyMaterialID
                                    ));
                                    break;
                            }
                            arrayAdapterList.clear();
                            setArrayAdapter();

                        }
                    });
                }
            }
        });

        studyMaterialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = null;
                switch (mode) {
                    case "notes":
                        myIntent = new Intent(getActivity(), NoteActivity.class);
                        break;
                    case "flashcards":
                        myIntent = new Intent(getActivity(), FlashCardActivity.class);
                        break;
                    case "quizzes":
                        myIntent = new Intent(getActivity(), QuizActivity.class);
                        break;
                }
                if (myIntent == null) {
                    return;
                }
                myIntent.putExtra("title", arrayAdapterList.get(position).getTitle());
                myIntent.putExtra("content", arrayAdapterList.get(position).getContent());
                myIntent.putExtra("dbID", arrayAdapterList.get(position).getdbID());
                startActivity(myIntent);
            }
        });

        for (String modeKey : modeSelectors.keySet()) {
            modeSelectors.get(modeKey).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == modeKey) {
                        return;
                    }
                    mode = modeKey;
                    for (String anotherKey: modeSelectors.keySet()) {
                        if (modeKey == anotherKey) {
                            modeSelectors.get(anotherKey).setImageResource(R.drawable.small_orange_rect);
                        } else {
                            modeSelectors.get(anotherKey).setImageResource(R.drawable.small_blue_rect);
                        }
                    }
                    setArrayAdapter();
                }
            });
        }
        addStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder().append("Add a new ");
                switch (mode) {
                    case "notes":
                        stringBuilder.append("note");
                        break;
                    case "flashcards":
                        stringBuilder.append("flashcard");
                        break;
                    case "quizzes":
                        stringBuilder.append("quiz");
                        break;
                }
                new AlertDialog.Builder(getActivity())
                        .setTitle(stringBuilder.toString())
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Manually Create</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton(Html.fromHtml("<font color = '#AEB8FE'>AI Generate</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void switchFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void setArrayAdapter() {
        arrayAdapterList.clear();
        switch (mode) {
            case "notes":
                for (String key: notes.keySet()) {
                    arrayAdapterList.add(notes.get(key));
                }
                break;
            case "flashcards":
                for (String key: flashCards.keySet()) {
                    arrayAdapterList.add(flashCards.get(key));
                }
                break;
            case "quizzes":
                for (String key: quizzes.keySet()) {
                    arrayAdapterList.add(quizzes.get(key));
                }
                break;
        }
        studyMaterialArrayAdapter.notifyDataSetChanged();
    }

}
