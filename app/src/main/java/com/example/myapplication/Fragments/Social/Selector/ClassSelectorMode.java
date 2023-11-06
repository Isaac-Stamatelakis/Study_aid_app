package com.example.myapplication.Fragments.Social.Selector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Classes.AddClassFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ClassChatGroup;
import com.example.myapplication.StaticHelper;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ClassSelectorMode extends SocialSelectorMode {
    protected final static String TAG = "ClassSelectorMode";

    public ClassSelectorMode(String androidID, ImageView modeSelector, SocialSelectorFragment socialSelectorFragment) {
        super(androidID, modeSelector, socialSelectorFragment);
    }


    @Override
    protected void getFromDatabase() {
        new ClassSocialQuery(TAG,androidID).execute();
    }


    @Override
    protected void onDeleteRequest(int position,String user_id) {
        new AlertDialog.Builder(socialSelectorFragment.getActivity())
                .setTitle("Do you want to leave " + chatGroups.get(position).getName() + "'s chat?")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Leave</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserFromChatGroup(position,user_id);
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    @Override
    protected void onQueryCompleted(ArrayList<ChatGroup> chatGroups) {
        super.onQueryCompleted(chatGroups);
    }

    @Override
    protected ChatGroup processChatGroup(QueryDocumentSnapshot snapshot) {
        return super.processChatGroup(snapshot);
    }

    @Override
    protected String getName(QueryDocumentSnapshot snapshot) {
        Object docVal = snapshot.get("name");
        String name = "";
        if (docVal != null) {
            name = (String) docVal;
        }
        return name;
    }

    protected class ClassSocialQuery extends SocialQuery {
        public ClassSocialQuery(String TAG, String user_id) {
            super(TAG, user_id);
        }
        @Override
        protected Query generateQuery() {
            return super.generateQuery().whereEqualTo("type","class");
        }
    }

    @Override
    protected void onAddClick() {
        AddClassFragment addClassFragment = new AddClassFragment();
        StaticHelper.switchFragment(socialSelectorFragment.getActivity().getSupportFragmentManager(),addClassFragment,null);
    }

}
