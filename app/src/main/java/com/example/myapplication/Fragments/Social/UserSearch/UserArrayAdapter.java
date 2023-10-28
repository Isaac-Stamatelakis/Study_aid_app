package com.example.myapplication.Fragments.Social.UserSearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.R;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public UserArrayAdapter(Context context, ArrayList<User> users) {
        super(context,0,users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_content, parent,false);
        }
        User user = this.users.get(position);
        TextView chatGroupTitleText = view.findViewById(R.id.user_name);
        chatGroupTitleText.setText(user.getName());
        return view;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
