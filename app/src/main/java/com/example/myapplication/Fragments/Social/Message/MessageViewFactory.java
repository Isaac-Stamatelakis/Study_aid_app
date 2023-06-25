package com.example.myapplication.Fragments.Social.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.HashMap;

public abstract class MessageViewFactory {

    private Context context;
    private Message message;
    private HashMap<String, String> memberNames;
    private ViewGroup parent;

    public Context getContext() {
        return context;
    }

    public Message getMessage() {
        return message;
    }

    public HashMap<String, String> getMemberNames() {
        return memberNames;
    }

    public ViewGroup getParent() {
        return parent;
    }

    public MessageViewFactory(Context context, Message message, HashMap<String, String> memberNames, ViewGroup parent) {
        this.context = context;
        this.message = message;
        this.memberNames = memberNames;
        this.parent = parent;
    }

    public abstract View getView();


}
