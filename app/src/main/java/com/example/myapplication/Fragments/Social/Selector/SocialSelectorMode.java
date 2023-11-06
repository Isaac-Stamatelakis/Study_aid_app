package com.example.myapplication.Fragments.Social.Selector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.StaticHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    protected void onDeleteRequest(int position,String user_id) {
        new AlertDialog.Builder(socialSelectorFragment.getActivity())
                .setTitle("Do you want to delete")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Delete</font>"), new DialogInterface.OnClickListener() {
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
    protected void deleteUserFromChatGroup(int position, String user_id) {

        ChatGroup chatGroup = chatGroups.get(position);
        chatGroups.remove(position);
        socialSelectorFragment.chatGroupArrayAdapter.notifyDataSetChanged();
        DocumentReference documentReference = db.collection("Chats").document(chatGroup.getDbID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Object val = documentSnapshot.get("members");
                if (val == null) {
                    return;
                }
                ArrayList<String> memberIds = (ArrayList<String>) val;
                memberIds.remove(user_id);
                documentReference.update("members",memberIds).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ChatGroupSelector","Removed user " + user_id + " from chat " + chatGroup.getDbID());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ChatGroupSelector",e.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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
        ChatGroup chatGroup = new ChatGroup(getName(snapshot),null,snapshot.getId());
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
