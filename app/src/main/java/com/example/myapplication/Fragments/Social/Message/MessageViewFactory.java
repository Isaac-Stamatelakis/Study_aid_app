package com.example.myapplication.Fragments.Social.Message;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.HashMap;

public abstract class MessageViewFactory {
    private MessageViewFactory() {

    }
    public static View getView(Message message, Context context, ViewGroup parent) {
        if (message instanceof TextMessage) {
            return generateTextMessage(context,parent);
        } else if (message instanceof StudyMaterialMessage) {
            return generateStudyMaterialMessage(context,parent);
        }
        return null;
    }
    protected static View generateTextMessage(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.text_message_content, parent,false);
    }
    protected static View generateStudyMaterialMessage(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.studymaterial_message_content, parent,false);
    }

}
