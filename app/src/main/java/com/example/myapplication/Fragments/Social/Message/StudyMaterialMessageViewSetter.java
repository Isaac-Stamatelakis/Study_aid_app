package com.example.myapplication.Fragments.Social.Message;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.SaveStudyMaterialCSF;
import com.example.myapplication.Fragments.Classes.StudyMaterial.FlashCard;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Note;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Quiz;
import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;

public class StudyMaterialMessageViewSetter extends MessageViewSetter {
    protected FragmentManager fragmentManager;
    protected TextView typeText;
    protected TextView nameText;
    protected ChatGroupFragment chatGroupFragment;
    public StudyMaterialMessageViewSetter(View view, StudyMaterialMessage studyMaterialMessage, ChatGroupFragment chatGroupFragment) {
        super(view, studyMaterialMessage,chatGroupFragment);
    }


    @Override
    public void set() {
        nameText = view.findViewById(R.id.studymaterial_message_content_name);
        typeText = view.findViewById(R.id.studymaterial_message_type);
        userText = view.findViewById(R.id.studymaterial_message_content_user);
        dateText = view.findViewById(R.id.studymaterial_message_content_date);
        imageView = view.findViewById(R.id.studymaterial_message_content_image);

        if (nameText == null || typeText == null || userText == null || dateText == null) {
            return;
        }
        StudyMaterial studyMaterial = ((StudyMaterialMessage) message).getStudyMaterial();
        if (studyMaterial == null) {
            nameText.setText("Deleted StudyMaterial");
        } else {
            if (studyMaterial instanceof Quiz) {
                typeText.setText("Quiz");
            } else if (studyMaterial instanceof FlashCard) {
                typeText.setText("Flashcard");
            } else if (studyMaterial instanceof Note) {
                typeText.setText("Note");
            }

            nameText.setText(studyMaterial.getTitle());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studyMaterial != null) {
                    chatGroupFragment.clearMessages();
                    SaveStudyMaterialCSF saveStudyMaterialCSF = new SaveStudyMaterialCSF(studyMaterial);
                    StaticHelper.switchFragment(fragmentManager, saveStudyMaterialCSF, null);
                }
            }
        });
        super.set();
    }
}
