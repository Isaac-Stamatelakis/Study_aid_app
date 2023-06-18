package com.example.myapplication.Fragments.Classes.StudyMaterial.Selector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ClassChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.StudyChatGroup;
import com.example.myapplication.Fragments.Social.Message.StudyMaterialMessage;
import com.example.myapplication.Fragments.Social.Message.TextMessage;

import java.time.LocalDateTime;

public class ChatStudyMaterialSelectorFragment extends StudyMaterialSelectorFragment {
    ChatGroup chatGroup;
    public ChatStudyMaterialSelectorFragment(String schoolClassID, ChatGroup chatGroup) {
        super(schoolClassID);
        this.chatGroup = chatGroup;
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
        return view;
    }

    /**
     * Should send a message to the given chat of the selected studymaterial
     * @param position
     */
    private void listViewClickHandler(int position) {
        StudyMaterialMessage studyMaterialMessage = new StudyMaterialMessage(user_id, LocalDateTime.now(), arrayAdapterList.get(position).getdbID());
        chatGroup.addMessage(studyMaterialMessage);
        chatGroup.addMessageToDB(studyMaterialMessage, user_id, "text");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (chatGroup instanceof ClassChatGroup) {
            fragmentManager.popBackStack();
        } else if (chatGroup instanceof StudyChatGroup || chatGroup instanceof FriendChatGroup) {
            fragmentManager.popBackStack(); fragmentManager.popBackStack();
        }

    }

}
