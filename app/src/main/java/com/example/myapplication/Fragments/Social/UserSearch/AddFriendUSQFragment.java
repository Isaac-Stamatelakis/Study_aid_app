package com.example.myapplication.Fragments.Social.UserSearch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Profile.Query.Filter.NoFriendFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AddFriendUSQFragment extends StandardUserSearchFragment {
    private final static String TAG = "AddFriendFragment";

    @Override
    protected UserSearchQuery generateQuery(QueryDocumentSnapshot documentSnapshot, boolean clearOnComplete) {
        UserSearchQuery userSearchQuery = super.generateQuery(documentSnapshot,clearOnComplete);
        userSearchQuery.queryFilter = new NoFriendFilter(userSearchQuery.queryFilter,user_id);
        return userSearchQuery;
    }

    @Override
    protected void userSelected(User user) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to send " + user.getName() + " a friend request?")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendFriendRequest(user);
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    protected void sendFriendRequest(User user) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getDocID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Object friendRequestValue = documentSnapshot.get("FriendRequests");
                ArrayList<String> friendRequests;
                if (friendRequestValue == null) {
                    friendRequests = new ArrayList<>();
                    friendRequests.add(user_id);
                } else {
                    friendRequests = (ArrayList<String>) documentSnapshot.get("FriendRequests");
                    if (friendRequests.contains(user_id)) {
                        alertAlreadyRequested();
                        return;
                    } else {
                        friendRequests.add(user_id);
                    }
                }

                documentReference.update("FriendRequests", friendRequests).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        alertRequested();
                        Log.d(TAG,"Friend request sent to " +  documentSnapshot.getId() + " from " + user_id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertFailure();
                        Log.e(TAG,e.toString());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    protected void alertAlreadyRequested() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Request already sent")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
    protected void alertRequested() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Request sent")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    protected void alertFailure() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Request could not be sent")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Continue</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
