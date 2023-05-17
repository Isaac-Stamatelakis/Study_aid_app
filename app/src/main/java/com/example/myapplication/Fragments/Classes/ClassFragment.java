package com.example.myapplication.Fragments.Classes;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.SchoolMaterial.Note;
import com.example.myapplication.Fragments.Classes.SchoolMaterial.Quiz;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ClassFragment extends Fragment {
    ListView classList;
    String user_id;
    FirebaseFirestore db;
    SchoolClass schoolClass;
    String schoolClassID;
    ListView studyMaterialListView;
    ArrayList<FlashCard> flashCards;
    ArrayList<Note> notes;
    ArrayList<Quiz> quizzes;
    ArrayList<String> studyMaterialIDs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_selector_fragment, container, false);
        classList = view.findViewById(R.id.class_fragment_class_list);
        schoolClassID = getArguments().get("ID").toString();
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        flashCards = new ArrayList<FlashCard>();
        notes = new ArrayList<Note>();
        quizzes = new ArrayList<Quiz>();

        DocumentReference schoolClassReference = db.collection("Classes").document(schoolClassID);
        schoolClassReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    schoolClass = new SchoolClass(
                            (String) documentSnapshot.get("Section"),
                            (String) documentSnapshot.get("Number"),
                            (String) documentSnapshot.get("Subject")
                            );

                    studyMaterialIDs = (ArrayList<String>) documentSnapshot.getData().get("study_material");
                    CollectionReference studyMaterialReference = db.collection("StudyMaterial");
                    studyMaterialReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    if (studyMaterialIDs.contains(documentSnapshot.getId())) {
                                        switch (documentSnapshot.get("type").toString()) {
                                            case "note":
                                                notes.add(new Note(
                                                        (String) documentSnapshot.get("title"),
                                                        (String)documentSnapshot.get("content")
                                                ));
                                            case "flashcard":
                                                flashCards.add(new FlashCard(
                                                        (String) documentSnapshot.get("title"),
                                                        (String)documentSnapshot.get("content")
                                                ));
                                            case "quiz":
                                                quizzes.add(new Quiz(
                                                        (String) documentSnapshot.get("title"),
                                                        (String) documentSnapshot.get("content")
                                                ));
                                        }
                                    }
                                }
                            }
                        }
                    });

                }
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

}
