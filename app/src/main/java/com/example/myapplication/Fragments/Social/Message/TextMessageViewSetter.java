package com.example.myapplication.Fragments.Social.Message;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.HashMap;

public class TextMessageViewSetter extends MessageViewSetter{
    protected TextMessage textMessage;
    protected HashMap<String, String> memberNames;
    public TextMessageViewSetter(View view, TextMessage textMessage, HashMap<String, String> memberNames) {
        super(view);
        this.textMessage = textMessage;
        this.memberNames = memberNames;
    }

    @Override
    public void set() {
        TextView messageText = view.findViewById(R.id.studymaterial_message_content_name);
        TextView userText = view.findViewById(R.id.studymaterial_message_content_user);
        TextView dateText = view.findViewById(R.id.studymaterial_message_content_date);
        messageText.setText(textMessage.getContent());
        userText.setText(memberNames.get(textMessage.getOwner()));
        dateText.setText(textMessage.getDate().toString());
    }
}
