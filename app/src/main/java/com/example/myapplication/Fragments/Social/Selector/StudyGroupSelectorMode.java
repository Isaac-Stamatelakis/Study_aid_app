package com.example.myapplication.Fragments.Social.Selector;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.StudyChatGroup;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class StudyGroupSelectorMode extends SocialSelectorMode{
    protected final static String TAG = "StudyGroupSelectorMode";
    public StudyGroupSelectorMode(String androidID, ImageView modeSelector, SocialSelectorFragment socialSelectorFragment) {
        super(androidID, modeSelector, socialSelectorFragment);
    }

    @Override
    protected void getFromDatabase() {
        new StudyGroupSocialQuery(TAG,androidID).execute();
    }


    @Override
    protected void onDelete() {
        super.onDelete();
    }

    @Override
    protected void onQueryCompleted(ArrayList<ChatGroup> chatGroups) {
        super.onQueryCompleted(chatGroups);
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

    @Override
    protected ChatGroup processChatGroup(QueryDocumentSnapshot snapshot) {
        return super.processChatGroup(snapshot);
    }

    protected class StudyGroupSocialQuery extends SocialQuery {
        public StudyGroupSocialQuery(String TAG, String user_id) {
            super(TAG, user_id);
        }
        @Override
        protected Query generateQuery() {
            return super.generateQuery().whereEqualTo("type","studygroup");
        }
    }
}
