package com.example.myapplication.Fragments.Social.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.HashMap;

public class TextMessageViewFactory extends MessageViewFactory{


    public TextMessageViewFactory(Context context, Message message, HashMap<String, String> memberNames, ViewGroup parent) {
        super(context, message, memberNames, parent);
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.text_message_content, getParent(),false);
        Message message = getMessage();

        TextView messageText = view.findViewById(R.id.studymaterial_message_content_name);
        TextView userText = view.findViewById(R.id.studymaterial_message_content_user);
        TextView dateText = view.findViewById(R.id.studymaterial_message_content_date);
        messageText.setText(message.getContent());
        userText.setText(getMemberNames().get(message.getOwner()));
        dateText.setText(message.getDate().toString());

        return view;
    }

}
