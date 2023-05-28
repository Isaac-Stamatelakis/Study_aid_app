package com.example.myapplication.Fragments.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
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

import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.FlashcardActivity.FlashCardActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.NoteActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity.QuizActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterialArrayAdapter;
import com.example.myapplication.R;
import com.example.myapplication.nInput.AddSingleTextDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ClassFragment extends Fragment{
    ListView studyMaterialList;
    String user_id;
    String mode;
    FirebaseFirestore db;
    String schoolClassID;
    HashMap<String, StudyMaterial> flashCards;
    HashMap<String, StudyMaterial> notes;
    HashMap<String, StudyMaterial> quizzes;
    StudyMaterialArrayAdapter studyMaterialArrayAdapter;
    HashMap<String, ImageView> modeSelectors;
    ArrayList<StudyMaterial> arrayAdapterList;
    FloatingActionButton addStudyMaterialButton;
    ArrayList<ListenerRegistration> studyMaterialSnapshots;
    Fragment thisFragment;
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
        studyMaterialSnapshots = new ArrayList<>();
        thisFragment = this;

        // Array Adapter
        flashCards = new HashMap<String, StudyMaterial>();
        notes = new HashMap<String, StudyMaterial>();
        quizzes = new HashMap<String, StudyMaterial>();
        arrayAdapterList = new ArrayList<>();
        studyMaterialArrayAdapter = new StudyMaterialArrayAdapter(this.getActivity(), arrayAdapterList);
        mode = "notes";
        studyMaterialList.setAdapter(studyMaterialArrayAdapter);

        updateStudyMaterialSnapshots();
        modeSelectorHandler();


        /**
         * Goes to the study material activity of the clicked studymaterial
         */
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
        /**
         * Deletes a study material from the class
         */
        studyMaterialList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Do you want to delete ".concat(arrayAdapterList.get(position).getTitle()).concat("?"))
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Delete</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMaterialFromClass(arrayAdapterList.get(position).getdbID());
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });

        /**
         * When clicked starts a dialog which requests a title for a new studymaterial
         */
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
                                AddSingleTextDialogFragment addSingleTextDialogFragment =  new AddSingleTextDialogFragment(getActivity(), "title");
                                addSingleTextDialogFragment.setTargetFragment(thisFragment, 964);
                                addSingleTextDialogFragment.show(getActivity().getSupportFragmentManager(), "Showing addsingle");
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

    /**
     * Is called every time an add studymaterial dialog concludes
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 964) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                switch (mode) {
                    case "notes":
                        Note note = new Note((String) bundle.get("title"), null, null);
                        note.addToDatabase(schoolClassID);
                        break;
                    case "flashcards":
                        FlashCard flashcard = new FlashCard((String) bundle.get("title"), null, null);
                        flashcard.addToDatabase(schoolClassID);
                        break;
                    case "quizzes":
                        Quiz quiz = new Quiz((String) bundle.get("title"), null, null);
                        quiz.addToDatabase(schoolClassID);
                        break;
                }
            }
        }
    }

    /**
     * Switches fragments
     * @param fragment: fragment to be switched to
     * @param bundle: bundle to be sent to new fragment
     */
    public void switchFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Sets the arrayAdapterList (the list which is displayed by the arrayadapter)
     * to whichever studymaterial is the current mode
     */
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

    /**
     * Deletes a studymaterial from the class
     * @param dbID: ID of studymaterial to be deleted
     */
    public void deleteMaterialFromClass(String dbID) {
            CollectionReference studyMaterialRef = db.collection("StudyMaterial");
            studyMaterialRef.document(dbID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("StudyMaterial", "Deleted " + dbID);
                    updateStudyMaterialSnapshots();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("StudyMaterial", "Didn't delete " + dbID + " " + e.toString());
                }
            });

    }
    /**
     * Adds a snapshotlistener which updates the studymaterials belonging to the class everytime it changes.
     * snapshots should be reupdated everytime a studymaterial is added or removed from this class.
     */
    public void updateStudyMaterialSnapshots() {
        // Clear out studyMaterialSnapshots
        for (ListenerRegistration listener: studyMaterialSnapshots) {
            listener.remove();
        }
        studyMaterialSnapshots.clear();
        notes.clear(); quizzes.clear(); flashCards.clear();

        Query classStudyMaterial = db.collection("StudyMaterial").whereEqualTo("class", schoolClassID);
        studyMaterialSnapshots.add(classStudyMaterial.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: value) {
                        switch ((String) Objects.requireNonNull(doc.get("type"))) {
                            case "note":
                                notes.put(doc.getId(), new Note(
                                        (String) doc.get("title"),
                                        (String) doc.get("content"),
                                        doc.getId()
                                ));
                                break;
                            case "flashcard":
                                flashCards.put(doc.getId(), new Note(
                                        (String) doc.get("title"),
                                        (String) doc.get("content"),
                                        doc.getId()
                                ));
                                break;
                            case "quiz":
                                quizzes.put(doc.getId(), new Note(
                                        (String) doc.get("title"),
                                        (String) doc.get("content"),
                                        doc.getId()
                                ));
                                break;
                        }
                    }
                arrayAdapterList.clear();
                setArrayAdapter();
            }
        }));
    }

    /**
     * Sets the colors of the mode selectors.
     * The selected one is orange, the rest are light blue.
     */
    public void modeSelectorHandler() {
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
    }
}
