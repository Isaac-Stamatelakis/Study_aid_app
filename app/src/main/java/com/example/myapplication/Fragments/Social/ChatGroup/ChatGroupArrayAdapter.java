package com.example.myapplication.Fragments.Social.ChatGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatGroupArrayAdapter extends ArrayAdapter<ChatGroup> {
    private ArrayList<ChatGroup> chatGroups;
    private Context context;

    public ChatGroupArrayAdapter(Context context, ArrayList<ChatGroup> chatGroups) {
        super(context,0,chatGroups);
        this.chatGroups = chatGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.study_material_content, parent,false);
        }

        ChatGroup chatGroup = this.chatGroups.get(position);
        TextView chatGroupTitleText = view.findViewById(R.id.study_material_text);
        chatGroupTitleText.setText(chatGroup.getName());
        return view;
    }

    public void setChatGroups(ArrayList<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }
}
