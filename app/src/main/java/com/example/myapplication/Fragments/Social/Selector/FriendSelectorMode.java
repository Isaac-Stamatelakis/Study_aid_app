package com.example.myapplication.Fragments.Social.Selector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Profile.Query.Filter.QueryFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupArrayAdapter;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.Fragments.Social.FriendChatQuery;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.example.myapplication.Fragments.Social.UserSearch.AddFriendUSQFragment;
import com.example.myapplication.StaticHelper;
import com.example.myapplication.databinding.ChatgroupSelectorFragmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
    protected void onDeleteRequest(int position,String user_id) {
        new AlertDialog.Builder(socialSelectorFragment.getActivity())
                .setTitle("Do you want to remove " + chatGroups.get(position).getName() + " as a friend?")
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
        FriendChatGroup chatGroup = new FriendChatGroup("",null,snapshot.getId());
        new GetFriendNameQuery(TAG,friendID,chatGroup).execute();
        chatGroup.setMessageIDS(messageIDs);
        return chatGroup;
    }

    @Override
    protected void onAddClick() {
        AddFriendUSQFragment addFriendUSQFragment = new AddFriendUSQFragment();
        StaticHelper.switchFragment(socialSelectorFragment.getActivity().getSupportFragmentManager(),addFriendUSQFragment,null);
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

    @Override
    protected void deleteUserFromChatGroup(int position, String user_id) {
        /*
        In order to remove someone as a friend, must
        i) Get memberIDs from db document retrieval
        (Below doesn't happen in order)
        ii) Remove both people as eachothers friends
        iii) Delete the chat from database
         */
        FriendChatGroup friendChatGroup = (FriendChatGroup) chatGroups.get(position);
        new FriendChatRetrieval("FriendChatRetrieval",friendChatGroup.getDbID()).execute();
        chatGroups.remove(position);
        socialSelectorFragment.chatGroupArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Called after FriendChatRetrieval execute finishes
     * Deletes hashmap entry with value chatGroupDBID from friends of userDBID given
     * @param userDBID
     * @param friendToRemoveUserID
     */
    protected void deleteFriend(String userDBID, String friendToRemoveUserID) {
        new UserDeleteFriendQuery("DeleteFriendQuery",userDBID,friendToRemoveUserID).execute();
    }

    /**
     * Called after FriendChatRetrieval execute finishes
     * Deletes the chat with chatGroupDBID from database
     * @param chatGroupDBID
     */
    protected void deleteSelfFromDB(String chatGroupDBID) {
        db.collection("Chats").document(chatGroupDBID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Deleted " + chatGroupDBID + " from DB");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
            }
        });
    }

    /**
     * First query executed when deleting user as friend
     */
    protected class FriendChatRetrieval extends CustomDocumentRetrieval {
        protected String dbID;
        protected ArrayList<String> memberIDs;
        public FriendChatRetrieval(String TAG, String dbID) {
            super(TAG);
            this.dbID = dbID;
        }

        @Override
        public void execute() {
            super.execute();
        }

        @Override
        protected DocumentReference getDocument() {
            return db.collection("Chats").document(dbID);
        }

        @Override
        protected void processSnapshot(DocumentSnapshot snapshot) {
            Object val = snapshot.get("members");
            if (val == null) {
                return;
            }
            memberIDs = (ArrayList<String>) val;

        }

        @Override
        protected void retrievalComplete() {
            super.retrievalComplete();
            if (memberIDs.size() > 1) {
                deleteFriend(memberIDs.get(0),memberIDs.get(1));
                deleteFriend(memberIDs.get(1),memberIDs.get(0));
                deleteSelfFromDB(dbID);
            }

        }
    }

    /**
     * Second order query executed from deleteFriend
     */
    protected class UserDeleteFriendQuery extends CustomQuery {
        protected String user_id;
        protected String friendToRemoveUserID;
        public UserDeleteFriendQuery(String TAG, String user_id, String friendToRemoveUserID) {
            super(TAG);
            this.user_id = user_id;
            this.friendToRemoveUserID = friendToRemoveUserID;
        }


        @Override
        protected Query generateQuery() {
            return db.collection("Users").whereEqualTo("user_id",user_id);
        }

        @Override
        protected void processSnapshot(QueryDocumentSnapshot snapshot) {
            Object val = snapshot.get("Friends");
            if (val == null) {
                return;
            }
            HashMap<String,String> friends = (HashMap<String, String>) val;
            friends.remove(friendToRemoveUserID);
            db.collection("Users").document(snapshot.getId()).update("Friends",friends).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"Removed " + friendToRemoveUserID + " as a friend of " + user_id);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,e.toString());
                }
            });

        }

        @Override
        protected void queryComplete() {
            super.queryComplete();
        }



    }
}

