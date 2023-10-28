package com.example.myapplication.Fragments.Classes.StudyMaterial.Selector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.AIGenerateFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.FlashcardActivity.FlashCardActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.NoteActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Activities.QuizActivity.QuizActivity;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.StaticHelper;
import com.example.myapplication.nInput.AddSingleTextDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;


public class ClassStudyMaterialSelectorFragment extends StudyMaterialSelectorFragment {
    public ClassStudyMaterialSelectorFragment(String schoolClassID) {
        super(schoolClassID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        /**
         * Goes to the study material activity of the clicked studymaterial
         */
        studyMaterialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewClickHandler(position);
            }
        });
        /**
         * Deletes a study material from the class
         */
        studyMaterialList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewDeleteHandler(position);
                return true;
            }
        });

        /**
         * When clicked starts a dialog which requests a title for a new studymaterial
         */
        addStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAddHandler();
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
    private void listViewClickHandler(int position) {
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

    private void listViewDeleteHandler(int position) {
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
    }

    private  void listViewAddHandler() {
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
                        Bundle bundle = new Bundle();
                        bundle.putString("id",schoolClassID);
                        bundle.putString("mode",mode);
                        AIGenerateFragment aiGenerateFragment = new AIGenerateFragment();
                        StaticHelper.switchFragment(getActivity().getSupportFragmentManager(),aiGenerateFragment,bundle);
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
