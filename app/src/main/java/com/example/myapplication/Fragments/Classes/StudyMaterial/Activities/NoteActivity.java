package com.example.myapplication.Fragments.Classes.StudyMaterial.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class NoteActivity extends Activity {
    String user_id;
    FirebaseFirestore db;
    EditText noteText;
    Note note;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);
        Bundle bundle = getIntent().getExtras();
        note = new Note(bundle.get("title").toString(), bundle.get("content").toString(),bundle.get("dbID").toString());
        noteText = findViewById(R.id.notes_activity_text);
        noteText.setText(note.getContent());
        user_id = (Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();

    }
    @Override
    public void onBackPressed() {
        db.collection("StudyMaterial").document(note.getdbID()).update("content", noteText.getText().toString());
        super.onBackPressed();
    }

}
