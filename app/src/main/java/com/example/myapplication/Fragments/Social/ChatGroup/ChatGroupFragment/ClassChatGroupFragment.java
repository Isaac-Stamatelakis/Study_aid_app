package com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ClassChatGroup;

public class ClassChatGroupFragment extends ChatGroupFragment {
    private ClassChatGroup classChatGroup;
    public ClassChatGroupFragment(ClassChatGroup classChatGroup) {
        super(classChatGroup);
        this.classChatGroup = classChatGroup;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void handleStudyMaterialShare() {
        ChatStudyMaterialSelectorFragment studyMaterialSelectorFragmentChat = new ChatStudyMaterialSelectorFragment(classChatGroup.getClassID(), chatGroup);
        switchFragment(studyMaterialSelectorFragmentChat, null);
    }
}
