package com.example.myapplication.Fragments.Social.Selector;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ClassChatGroup;
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
    protected void onDelete() {
        super.onDelete();
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
}
