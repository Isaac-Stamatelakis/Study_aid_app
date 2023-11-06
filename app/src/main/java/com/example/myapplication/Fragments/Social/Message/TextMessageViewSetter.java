package com.example.myapplication.Fragments.Social.Message;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.R;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

public class TextMessageViewSetter extends MessageViewSetter{
    public TextMessageViewSetter(View view, TextMessage textMessage, ChatGroupFragment chatGroupFragment) {
        super(view,textMessage,chatGroupFragment);
    }

    @Override
    public void set() {
        userText = view.findViewById(R.id.text_message_content_user);
        dateText = view.findViewById(R.id.text_message_content_date);
        imageView = view.findViewById(R.id.text_message_content_image);

        TextView messageText = view.findViewById(R.id.text_message_content_text);
        if (messageText == null || userText == null || dateText == null) {
            return;
        }
        messageText.setText(message.getContent());
        super.set();
    }
}
