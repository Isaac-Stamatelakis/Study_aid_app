package com.example.myapplication.Fragments.Social.Message;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> messages;
    private Context context;
    private HashMap<String, String> memberNames;
    private FragmentManager fragmentManager;
    String user_id;

    public MessageArrayAdapter(Context context, ArrayList<Message> messages, HashMap<String, String> memberNames, FragmentManager fragmentManager) {
        super(context,0,messages);
        this.messages = messages;
        this.context = context;
        this.memberNames = memberNames;
        this.fragmentManager = fragmentManager;
        user_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            if (messages.get(position) instanceof TextMessage) {
                view = new TextMessageViewFactory(context, messages.get(position), memberNames, parent).getView();
            } else if (messages.get(position) instanceof StudyMaterialMessage) {
                view = new StudyMaterialMessageViewFactory(context, messages.get(position), memberNames, parent, fragmentManager).getView();
            }
        }



        return view;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Context getMessageContext() {
        return context;
    }

    public HashMap<String, String> getMemberNames() {
        return memberNames;
    }

    public String getUser_id() {
        return user_id;
    }


}
