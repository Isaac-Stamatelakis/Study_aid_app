package com.example.myapplication.Fragments.Social.Selector;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Profile.Query.Filter.QueryFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupArrayAdapter;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.Fragments.Social.FriendChatQuery;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FriendSelectorMode extends SocialSelectorMode {
    protected final static String TAG = "FriendSelectMode";

    public FriendSelectorMode(String androidID, ImageView modeSelector, SocialSelectorFragment socialSelectorFragment) {
        super(androidID, modeSelector, socialSelectorFragment);
    }


    @Override
    protected void getFromDatabase() {
        new FriendSocialQuery(TAG,androidID).execute();
    }


    @Override
    protected void onQueryCompleted(ArrayList<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }

    @Override
    protected void onDelete() {
        super.onDelete();
    }

    @Override
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
        String friendID = "";
        if (members.size() > 1) {
            if (Objects.equals(members.get(0), androidID)) {
                friendID = members.get(1);
            } else {
                friendID = members.get(0);
            }
        }
        FriendChatGroup chatGroup = new FriendChatGroup("",members,null,snapshot.getId());
        new GetFriendNameQuery(TAG,friendID,chatGroup).execute();
        chatGroup.setMessageIDS(messageIDs);
        return chatGroup;
    }

    protected class FriendSocialQuery extends SocialQuery {
        public FriendSocialQuery(String TAG, String user_id) {
            super(TAG, user_id);
        }

        @Override
        protected Query generateQuery() {
            return super.generateQuery().whereEqualTo("type","friends");
        }
    }

    protected class GetFriendNameQuery extends CustomQuery {
        protected String androidID;
        protected FriendChatGroup friendChatGroup;

        public GetFriendNameQuery(String TAG, String androidID, FriendChatGroup friendChatGroup) {
            super(TAG);
            this.androidID = androidID;
            this.friendChatGroup = friendChatGroup;
        }

        @Override
        protected Query generateQuery() {
            return db.collection("Users").whereEqualTo("user_id",androidID);
        }

        @Override
        protected void processSnapshot(QueryDocumentSnapshot snapshot) {
            Object docVal = snapshot.get("Name");
            String name = "";
            if (docVal != null) {
                name = (String) docVal;
            }
            friendChatGroup.setName(name);
        }
    }
}
