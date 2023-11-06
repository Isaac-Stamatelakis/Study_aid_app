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

import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageArrayAdapter extends ArrayAdapter<Message> {
    protected ArrayList<Message> messages;
    protected Context context;
    protected FragmentManager fragmentManager;
    protected ChatGroupFragment chatGroupFragment;
    protected String user_id;

    public MessageArrayAdapter(Context context, ArrayList<Message> messages, FragmentManager fragmentManager, ChatGroupFragment chatGroupFragment) {
        super(context,0,messages);
        this.messages = messages;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.chatGroupFragment = chatGroupFragment;
        user_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Message message = messages.get(position);
        if(view == null) {
            view = MessageViewFactory.getView(message,context,parent);
        }
        if (view != null) {
            if (message instanceof TextMessage) {
                new TextMessageViewSetter(view,(TextMessage) message,chatGroupFragment).set();
            } else if (message instanceof StudyMaterialMessage) {
                new StudyMaterialMessageViewSetter(view,(StudyMaterialMessage) message,chatGroupFragment).set();
            }
        }
        return view;
    }
    public String getUser_id() {
        return user_id;
    }


}
