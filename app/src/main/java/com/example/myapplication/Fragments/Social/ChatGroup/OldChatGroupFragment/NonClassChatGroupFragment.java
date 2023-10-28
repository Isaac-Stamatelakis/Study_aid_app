package com.example.myapplication.Fragments.Social.ChatGroup.OldChatGroupFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.ChatClassSelectorFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;

public class NonClassChatGroupFragment extends OldChatGroupFragment {
    public NonClassChatGroupFragment(ChatGroup chatGroup) {
        super(chatGroup);
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
        ChatClassSelectorFragment chatClassSelectorFragment = new ChatClassSelectorFragment(chatGroup);
        switchFragment(chatClassSelectorFragment, null);
    }
}
