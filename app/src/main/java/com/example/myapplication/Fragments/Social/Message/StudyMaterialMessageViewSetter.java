package com.example.myapplication.Fragments.Social.Message;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.StudyMaterialAddClassSelectorFragment;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class StudyMaterialMessageViewSetter extends MessageViewSetter {
    protected StudyMaterialMessage studyMaterialMessage;
    protected FragmentManager fragmentManager;
    protected HashMap<String, String> memberNames;
    public StudyMaterialMessageViewSetter(View view, StudyMaterialMessage studyMaterialMessage, FragmentManager fragmentManager,HashMap<String, String> memberNames) {
        super(view);
        this.studyMaterialMessage = studyMaterialMessage;
        this.fragmentManager = fragmentManager;
        this.memberNames = memberNames;
    }


    @Override
    public void set() {
        new StudyMaterialMessageQuery("StudyMaterialMessageQuery",studyMaterialMessage,view,fragmentManager).execute();
        TextView userText = view.findViewById(R.id.studymaterial_message_content_user);
        userText.setText(memberNames.get(studyMaterialMessage.getOwner()));
    }

    protected class StudyMaterialMessageQuery extends CustomDocumentRetrieval {
        protected View view;
        protected StudyMaterial studyMaterial = null;
        protected StudyMaterialMessage studyMaterialMessage;
        protected FragmentManager fragmentManager;
        public StudyMaterialMessageQuery(String TAG, StudyMaterialMessage studyMaterialMessage, View view, FragmentManager fragmentManager) {
            super(TAG);
            this.studyMaterialMessage = studyMaterialMessage;
            this.view = view;
            this.fragmentManager = fragmentManager;
        }

        public StudyMaterialMessageQuery(String TAG) {
            super(TAG);
        }

        @Override
        public void execute() {
            super.execute();
        }

        @Override
        protected DocumentReference getDocument() {
            return FirebaseFirestore.getInstance().collection("StudyMaterial").document(studyMaterialMessage.getContent());
        }

        @Override
        protected void processSnapshot(DocumentSnapshot snapshot) {
            TextView nameText = view.findViewById(R.id.studymaterial_message_content_name);
            TextView typeText = view.findViewById(R.id.studymaterial_message_type);
            String title = (String) snapshot.get("title");
            String content = (String) snapshot.get("content");
            String type = (String) snapshot.get("type");
            if (title == null && content == null && type == null) {
                nameText.setText("Deleted StudyMaterial");
            } else {
                switch (type) {
                    case "note":
                        typeText.setText("Note");
                        studyMaterial = new Note(title, content, studyMaterialMessage.getContent());
                        break;
                    case "flashcard":
                        typeText.setText("Flashcard");
                        studyMaterial = new FlashCard(title, content, studyMaterialMessage.getContent());
                        break;
                    case "quiz":
                        typeText.setText("Quiz");
                        studyMaterial = new Quiz(title, content, studyMaterialMessage.getContent());
                        break;
                }
                nameText.setText(title);
            }
        }

        @Override
        protected void retrievalComplete() {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudyMaterialAddClassSelectorFragment studyMaterialAddClassSelectorFragment = new StudyMaterialAddClassSelectorFragment(studyMaterial);
                    StaticHelper.switchFragment(fragmentManager, studyMaterialAddClassSelectorFragment, null);
                }
            });
        }
    }
}
