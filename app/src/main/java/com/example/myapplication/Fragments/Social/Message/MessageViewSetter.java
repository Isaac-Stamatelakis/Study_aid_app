package com.example.myapplication.Fragments.Social.Message;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Profile.ProfileFragment;
import com.example.myapplication.Fragments.Profile.PublicProfileFragment;
import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.StaticHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MessageViewSetter {
    protected View view;
    protected ImageView imageView;
    protected TextView dateText;
    protected TextView userText;
    protected Message message;
    protected ChatGroupFragment chatGroupFragment;
    public MessageViewSetter(View view, Message message, ChatGroupFragment chatGroupFragment) {
        this.view = view;
        this.message = message;
        this.chatGroupFragment = chatGroupFragment;
    }

    public void set() {
        User owner = message.getOwner();
        if (owner == null) {
            userText.setText("Unknown User");
        } else {
            userText.setText(owner.getName());
        }
        LocalDateTime date = message.getDate();
        if (date == null) {
            dateText.setText("Unknown Date");
        } else {
            dateText.setText(formatDate(message.getDate()));
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicProfileFragment profileFragment = new PublicProfileFragment(message.getOwner());
                StaticHelper.switchFragment(chatGroupFragment.getActivity().getSupportFragmentManager(),profileFragment,null);
            }
        });

    }

    protected String formatDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
