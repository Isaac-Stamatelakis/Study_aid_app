package com.example.myapplication.Fragments.Classes.StudyMaterial.Selector;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterialArrayAdapter;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class StudyMaterialSelectorFragment extends Fragment{
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

    public StudyMaterialSelectorFragment(String schoolClassID) {
        this.schoolClassID = schoolClassID;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateView(inflater,container);
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

        return view;


    }

    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.class_fragment, container, false);
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
     * Adds a snapshotlistener which updates the studymaterials belonging to the class everytime it changes.
     * snapshots should be reupdated everytime a studymaterial is added or removed from this class.
     */
    public void updateStudyMaterialSnapshots() {
        notes.clear();
        flashCards.clear();
        quizzes.clear();
        Query query = db.collection("StudyMaterial").whereEqualTo("class", schoolClassID);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : value) {
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
                setArrayAdapter();
            }
        });
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
