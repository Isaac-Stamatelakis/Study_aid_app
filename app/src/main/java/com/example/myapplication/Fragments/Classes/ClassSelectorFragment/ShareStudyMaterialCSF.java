package com.example.myapplication.Fragments.Classes.ClassSelectorFragment;

import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;

public class ShareStudyMaterialCSF extends ChatClassSelectorFragment{
    protected ChatGroup chatGroup;
    public ShareStudyMaterialCSF(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    @Override
    protected void documentLoaded(DocumentSnapshot documentSnapshot) {
        ChatStudyMaterialSelectorFragment chatStudyMaterialSelectorFragment = new ChatStudyMaterialSelectorFragment(documentSnapshot.getId(), chatGroup);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, chatStudyMaterialSelectorFragment)
                .addToBackStack(null)
                .commit();
    }
}
