package com.example.myapplication.Fragments.Social.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.ClassSelectorFragment;
import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.StudyMaterialAddClassSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class StudyMaterialMessageViewFactory extends MessageViewFactory{
    private StudyMaterial studyMaterial;
    private FragmentManager fragmentManager;
    private final String TAG = "StudyMaterialMessageViewFactory";
    public StudyMaterialMessageViewFactory(Context context, Message message, HashMap<String, String> memberNames, ViewGroup parent, FragmentManager fragmentManager) {
        super(context, message, memberNames, parent);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.studymaterial_message_content, getParent(),false);
        TextView nameText = view.findViewById(R.id.studymaterial_message_content_name);
        TextView typeText = view.findViewById(R.id.studymaterial_message_type);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("StudyMaterial").document(getMessage().getContent()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String title = (String) documentSnapshot.get("title");
                    String content = (String) documentSnapshot.get("content");
                    switch ((String) documentSnapshot.get("type")) {
                        case "note":
                            typeText.setText("Note");
                            studyMaterial = new Note(title, content, getMessage().getContent());
                            break;
                        case "flashcard":
                            typeText.setText("Flashcard");
                            studyMaterial = new FlashCard(title, content, getMessage().getContent());
                            break;
                        case "quiz":
                            typeText.setText("Quiz");
                            studyMaterial = new Quiz(title, content, getMessage().getContent());
                            break;
                    }
                    nameText.setText(title);
                } else {
                    Log.e(TAG,"Could not get studymaterial");
                }
            }
        });
        Message message = getMessage();
        TextView userText = view.findViewById(R.id.studymaterial_message_content_user);
        TextView dateText = view.findViewById(R.id.studymaterial_message_content_date);
        nameText.setText(message.getContent());
        userText.setText(getMemberNames().get(message.getOwner()));
        dateText.setText(message.getDate().toString());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler();
            }
        });
        return view;
    }

    private void clickHandler() {

        StudyMaterialAddClassSelectorFragment studyMaterialAddClassSelectorFragment = new StudyMaterialAddClassSelectorFragment(studyMaterial);
        StaticHelper.switchFragment(fragmentManager, studyMaterialAddClassSelectorFragment, null);
    }
}
