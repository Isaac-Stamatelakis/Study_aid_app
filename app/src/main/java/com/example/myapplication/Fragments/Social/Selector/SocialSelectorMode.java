package com.example.myapplication.Fragments.Social.Selector;

import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.StaticHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public abstract class SocialSelectorMode {
    protected ImageView modeSelector;
    protected String androidID;
    protected SocialSelectorFragment socialSelectorFragment;
    public SocialSelectorMode(String androidID, ImageView modeSelector,SocialSelectorFragment socialSelectorFragment) {
        this.modeSelector = modeSelector;
        this.androidID = androidID;
        this.socialSelectorFragment = socialSelectorFragment;
        getFromDatabase();
    }
    protected ArrayList<ChatGroup> chatGroups;
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected void getFromDatabase() {

    }

    protected void onClick(int position) {
        ChatGroupFragment chatGroupFragment = new ChatGroupFragment(chatGroups.get(position));
        StaticHelper.switchFragment(socialSelectorFragment.getActivity().getSupportFragmentManager(),chatGroupFragment,null);
    }

    protected void onDelete() {

    }
    protected void onQueryCompleted(ArrayList<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
        if (socialSelectorFragment.currentMode == socialSelectorFragment.socialSelectorModes.indexOf(this)) {
            socialSelectorFragment.setArrayAdapter();
        }
    }
    protected void onAddClick() {

    }
    protected ChatGroup processChatGroup(QueryDocumentSnapshot snapshot) {
        Object docVal = snapshot.get("members");
        ArrayList<String> members = new ArrayList<>();
        if (docVal != null) {
            members = (ArrayList<String>) docVal;
        }
        docVal = snapshot.get("messages");
        ArrayList<String> messageIDs = new ArrayList<>();
        if (docVal != null) {
            messageIDs = (ArrayList<String>) docVal;
        }
        ChatGroup chatGroup = new ChatGroup(getName(snapshot),members,null,snapshot.getId());
        chatGroup.setMessageIDS(messageIDs);
        return chatGroup;
    }

    protected String getName(QueryDocumentSnapshot snapshot) {
        return "";
    }
    protected ImageView getModeSelector() {
        return this.modeSelector;
    }
    protected class SocialQuery extends CustomQuery {
        protected String user_id;
        protected ArrayList<ChatGroup> chatGroups = new ArrayList<>();
        public SocialQuery(String TAG, String user_id) {
            super(TAG);
            this.user_id = user_id;
        }

        @Override
        protected Query generateQuery() {
            return db.collection("Chats").whereArrayContains("members",user_id);
        }

        @Override
        protected void processSnapshot(QueryDocumentSnapshot snapshot) {
            chatGroups.add(processChatGroup(snapshot));
        }

        @Override
        protected void queryComplete() {
            onQueryCompleted(chatGroups);
        }
    }
}
