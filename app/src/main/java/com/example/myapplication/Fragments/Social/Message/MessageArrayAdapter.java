package com.example.myapplication.Fragments.Social.Message;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;

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
        Log.e("TESTING",position + ";");
        Message message = messages.get(position);
        if(view == null) {
            view = MessageViewFactory.getView(message,context,parent);
        }
        if (view != null) {
            if (message instanceof TextMessage) {
                new TextMessageViewSetter(view,(TextMessage) message,memberNames).set();
                Log.e("TEST","No");
            } else if (message instanceof StudyMaterialMessage) {
                Log.e("TEST","TRUe");
                new StudyMaterialMessageViewSetter(view,(StudyMaterialMessage) message,fragmentManager,memberNames).set();
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
