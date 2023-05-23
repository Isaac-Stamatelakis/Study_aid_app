package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Fragments.Classes.ManualAddClassDialogFragment;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class FlashCardActivity extends FragmentActivity implements AddFlashCardDialogFragment.OnInputListener {
    String user_id;
    FirebaseFirestore db;
    FlashCard flashCard;
    ImageView leftArrow;
    ImageView rightArrow;
    TextView flashcardProgression;
    EditText flashcardText;
    Integer totalFlashcards;
    ArrayList<HashMap> flashcardMapsArray;
    Integer currentFlashcard;
    TextView totalScreen;
    String mode;
    Boolean editMode;
    ImageView editModeButton;
    String que_ans_seperator = "&q&";
    String flashcard_separator = "&f&";
    ImageView addButton;
    ImageView deleteButton;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //What is 1+7?&q&8&f&What is 7*8?&q&56"&f&"What is the definition of the laplace transform?&q&"integral from 0 to infinity of e^(-s*t) * f(t)dt
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_activity);
        Bundle bundle = getIntent().getExtras();
        flashcardMapsArray = new ArrayList<>();
        flashCard = new FlashCard(bundle.get("title").toString(), bundle.get("content").toString(),bundle.get("dbID").toString());
        user_id = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        currentFlashcard = 1;
        mode = "question"; editMode=false;

        // Views
        leftArrow = findViewById(R.id.flashcard_arrow_left); leftArrow.bringToFront();
        rightArrow = findViewById(R.id.flashcard_arrow_right); rightArrow.bringToFront();
        flashcardProgression = findViewById(R.id.flashcard_text_total);
        flashcardText = findViewById(R.id.flashcard_text); flashcardText.bringToFront(); flashcardText.setEnabled(false);
        totalScreen = findViewById(R.id.flashcard_fullscreen);
        editModeButton= findViewById(R.id.flashcard_edit_mode); editModeButton.bringToFront();
        addButton = findViewById(R.id.flashcard_add); addButton.bringToFront();
        deleteButton = findViewById(R.id.flashcard_delete); deleteButton.bringToFront();
        // Build content
        try {
            String[] separatedFlashcards = flashCard.getContent().split(flashcard_separator, 0);
            for (String separatedFlashcard : separatedFlashcards) {
                HashMap<String, String> flashcardMap = new HashMap<String, String>();
                flashcardMap.put("question", separatedFlashcard.split(que_ans_seperator,0)[0]);
                flashcardMap.put("answer", separatedFlashcard.split(que_ans_seperator,0)[1]);
                flashcardMapsArray.add(flashcardMap);
            }
        } catch (Exception e) {
            Log.d("FlashcardActivity", e.toString());
            finish();
        }
        totalFlashcards = flashcardMapsArray.size();
        updateFlashcard();
        // Listeners
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    putChanged();
                    updateDatabaseContent();
                }
                if (currentFlashcard <= 1) {

                } else {
                    currentFlashcard -= 1;
                    updateFlashcard();
                }
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    putChanged();
                    updateDatabaseContent();
                }
                if (currentFlashcard < totalFlashcards) {
                    currentFlashcard += 1;
                    mode = "question";
                    updateFlashcard();
                }
            }
        });
        editModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editMode) {
                    flashcardText.setEnabled(true);
                    editMode = true;
                } else {
                    flashcardText.setEnabled(false);
                    putChanged();
                    updateDatabaseContent();
                    editMode = false;
                }
            }
        });
        totalScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode) {
                    putChanged();
                }
                switch (mode) {
                    case "question":
                        mode = "answer";
                        break;
                    case "answer":
                        mode = "question";
                        break;
                }
                updateFlashcard();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFlashCardDialogFragment addFlashCardDialogFragment = new AddFlashCardDialogFragment(FlashCardActivity.this);
                addFlashCardDialogFragment.show(getSupportFragmentManager(), "ShowingAddFlashCardDialog");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(FlashCardActivity.this)
                        .setTitle("Do you want to delete this flashcard?")
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flashcardMapsArray.remove(currentFlashcard-1);
                                totalFlashcards--;
                                if (currentFlashcard > 1) {
                                    currentFlashcard--;
                                }
                                updateDatabaseContent();
                                updateFlashcard();
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }


    public void updateFlashcard() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(currentFlashcard.toString())
                .append("/")
                .append(totalFlashcards.toString());
        flashcardProgression.setText(stringBuilder.toString());
        flashcardText.setText((String) flashcardMapsArray.get(currentFlashcard-1).get(mode));
    }
    public void updateDatabaseContent() {
        StringBuilder newContent = new StringBuilder();
        for (int i = 0; i < flashcardMapsArray.size(); i ++) {
            newContent
                .append(flashcardMapsArray.get(i).get("question"))
                .append(que_ans_seperator)
                .append(flashcardMapsArray.get(i).get("answer"));
            if (i != flashcardMapsArray.size()-1) {
                newContent.append(flashcard_separator);
            }
        }

        db.collection("StudyMaterial").document(flashCard.getdbID()).update("content", newContent.toString());
    }

    public void putChanged() {
        Log.d("FlashcardActivity", flashcardText.getText().toString());
        if (flashcardText.getText().toString().contains(flashcard_separator) || flashcardText.getText().toString().contains(que_ans_seperator)) {
            alertInvalidFlashcard();
            finish();
        }
        if (flashcardText.getText().toString().length() == 0) {
            alertInvalidFlashcard();
            finish();
        }
        flashcardMapsArray.get(currentFlashcard-1).put(mode, flashcardText.getText().toString());
    }
    @Override
    public void onBackPressed() {
        if (editMode) {
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to save your changes?")
                    .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            putChanged();
                            updateDatabaseContent();
                            finish();
                        }
                    })
                    .setNeutralButton(Html.fromHtml("<font color = '#AEB8FE'>Don't Leave</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void sendInput(HashMap<String, String> input) {
        Log.d("FlashcardActivity", "Got the input");
        Log.d("inputs", input.get("question").concat(" ").concat(input.get("answer")));
        for (String key: input.keySet()) {
            if (input.get(key).contains(que_ans_seperator) || input.get(key).contains(flashcard_separator) || input.get(key).length() == 0) {
                alertInvalidFlashcard();
                return;
            }
        }
        flashcardMapsArray.add(currentFlashcard, input);
        totalFlashcards ++;
        currentFlashcard ++;
        updateFlashcard();
        updateDatabaseContent();
    }

    public void alertInvalidFlashcard() {
        new AlertDialog.Builder(FlashCardActivity.this)
                .setTitle("New flashcard is invalid. It cannot be empty or contain illegal values.")
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}

