package com.example.myapplication.Fragments.Social.Message;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messages;
    private Context context;
    private HashMap<String, String> memberNames;
    String user_id;

    public MessageArrayAdapter(Context context, ArrayList<Message> messages, HashMap<String, String> memberNames) {
        super(context,0,messages);
        this.messages = messages;
        this.context = context;
        this.memberNames = memberNames;
        user_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Message message = this.messages.get(position);
        if (Objects.equals(user_id, message.getOwner())) {
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.message_content, parent,false);
            }
            TextView messageText = view.findViewById(R.id.message_content_text);
            TextView userText = view.findViewById(R.id.message_content_user);
            TextView dateText = view.findViewById(R.id.message_content_date);
            messageText.setText(message.getContent());
            userText.setText(memberNames.get(message.getOwner()));
            dateText.setText(message.getDate().toString());

        } else {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.class_content, parent, false);
            }
        }




        return view;
    }

    private void wrapImageToText() {

    }

}
