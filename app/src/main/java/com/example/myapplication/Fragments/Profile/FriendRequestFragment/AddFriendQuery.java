package com.example.myapplication.Fragments.Profile.FriendRequestFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.Fragments.Social.FriendChatQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Takes two userAndroidIDs
 * i) Gets user information for both users
 * ii) Gets friendChat
 * iii) If both users are already friends, and friendChat exists does nothing
 * iv) If one user is friends, and friendChat doesn't exist, sets as friend and creates friendChat
 * v) If both users are friends and friendChat doesn't exist, creates friend chat.
 * vi) If both users are not friends and friendChat doesn't exist, creates friendChat
 * vii) If both users are friends and friendChat doesn't exist, creates friendChat
 */
public class AddFriendQuery {
    private static final String TAG = "AddFriendQuery";
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected User userA;
    protected User userB;
    protected String friendChatDocID = null;
    protected String userA_androidID;
    protected CollectionReference userCollection;
    protected String userB_androidID;
    protected Context context;
    public AddFriendQuery(String userA_androidID, String userB_androidID, Context context) {
        this.userA_androidID = userA_androidID;
        this.userB_androidID = userB_androidID;
        this.context = context;
        userCollection = db.collection("Users");
    }
    protected void execute() {
        new UserAQuery(userA_androidID).execute();
    }
    protected void checkChatGroup() {
        if (friendChatDocID == null) {
            Map<String, Object> chatInfo = new HashMap<>();
            ArrayList<String> ids = new ArrayList<>();
            ids.add(userA_androidID); ids.add(userB_androidID);
            chatInfo.put("members", ids);
            chatInfo.put("messages", new ArrayList<String>());
            chatInfo.put("type","friends");
            db.collection("Chats").add(chatInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG,"Added " + documentReference.getId() + " as a friend chat to " + userA_androidID + " and " + userB_androidID);
                    friendChatDocID = documentReference.getId();
                    updateUserA();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,e.toString());
                    sendErrorAlert();
                }
            });
        } else {
            updateUserA();
        }
    }
    protected void updateUserA() {
        if (!userA.getFriends().containsKey(userB_androidID)) {
            new AddAFriend(userA,userB_androidID).execute();
        } else {
            updateUserB();
        }
    }

    protected void updateUserB() {
        if (!userB.getFriends().containsKey(userA_androidID)) {
            new AddBFriend(userB,userA_androidID).execute();
        } else {
            removeFriendRequest();
        }
    }
    protected void removeFriendRequest() {
        ArrayList<String> friendRequests = userA.getFriendRequestAndroidIDs();
        friendRequests.remove(userB_androidID);
        userCollection.document(userA.getDocID()).update("FriendRequests",friendRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendAlert(userB.getName() + " added as friend!");
                addFriendQueryComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                sendErrorAlert();
                Log.e(TAG,e.toString());
            }
        });
    }
    protected void addFriendQueryComplete() {

    }
    protected void userAQueryComplete() {
        new UserBQuery(userB_androidID).execute();
    }

    protected void userBQueryComplete() {
        new AddFriendFriendChatQuery(userA_androidID,userB_androidID).execute();
    }
    protected void friendChatQueryCompleted() {
        checkChatGroup();
    }
    protected void sendAlert(String message) {
        new AlertDialog.Builder(context)
                .setTitle(message)
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    protected void sendErrorAlert() {
        sendAlert("Could not add friend");
    }


    protected class UserAQuery extends UserQueryAndroidID {
        public UserAQuery(String androidID) {
            super(androidID);
        }
        @Override
        protected void queryCompleted(ArrayList<User> users) {
            if (users.size() > 0) {
                userA = users.get(0);
                userAQueryComplete();
            } else {
                Log.e(TAG,"Could not set userA");
                sendErrorAlert();
            }
        }
    }
    protected class UserBQuery extends UserQueryAndroidID {
        public UserBQuery(String androidID) {
            super(androidID);
        }
        @Override
        protected void queryCompleted(ArrayList<User> users) {
            if (users.size() > 0) {
                userB = users.get(0);
                userBQueryComplete();
            } else {
                Log.e(TAG,"Could not set userB");
                sendErrorAlert();
            }
        }
    }
    protected static class UserQueryAndroidID extends UserQuery {
        protected String androidID;
        public UserQueryAndroidID(String androidID) {
            this.androidID = androidID;
        }
        @Override
        protected Query generateQuery() {
            return db.collection("Users").whereEqualTo("user_id",androidID);
        }
    }

    protected class AddFriendFriendChatQuery extends FriendChatQuery {
        public AddFriendFriendChatQuery(String userA_androidID, String userB_androidID) {
            super(userA_androidID, userB_androidID);
        }

        @Override
        protected void queryCompleted(ArrayList<FriendChatGroup> friendChatGroups) {
            if (friendChatGroups.size() > 0) {
                friendChatDocID = friendChatGroups.get(0).getDbID();
            }
            friendChatQueryCompleted();
        }
    }
    protected class AddFriendUpdate {
        protected User user;
        protected String otherUserAndroidID;
        public AddFriendUpdate(User user,String otherUserAndroidId) {
            this.user = user;
            this.otherUserAndroidID = otherUserAndroidId;
        }

        public void execute() {
            HashMap<String, String> userAFriends = user.getFriends();
            userAFriends.put(otherUserAndroidID,friendChatDocID);
            userCollection.document(user.getDocID()).update("Friends",userAFriends).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"Added " + otherUserAndroidID + " as a friend to " + user.getDocID());
                    complete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,e.toString());
                    sendErrorAlert();
                }
            });
        }
        protected void complete() {

        }
    }

    protected class AddAFriend extends AddFriendUpdate{
        public AddAFriend(User user, String otherUserAndroidId) {
            super(user, otherUserAndroidId);
        }
        @Override
        protected void complete() {
            updateUserB();
        }
    }

    protected class AddBFriend extends AddFriendUpdate{
        public AddBFriend(User user, String otherUserAndroidId) {
            super(user, otherUserAndroidId);
        }

        @Override
        protected void complete() {
            removeFriendRequest();
        }
    }



}
