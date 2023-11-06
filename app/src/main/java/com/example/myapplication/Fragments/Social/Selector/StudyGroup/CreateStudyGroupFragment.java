package com.example.myapplication.Fragments.Social.Selector.StudyGroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.UserSearch.MultiFriendSelectorFragment;
import com.example.myapplication.nInput.AddSingleTextDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateStudyGroupFragment extends MultiFriendSelectorFragment {
    private static String TAG = "MultiFriendSelectorFragment";
    protected static ArrayList<User> users;
    protected static String staticUserID;
    @Override
    protected void addFriendButtonClick() {
        staticUserID = user_id;
        users = getSelectedUsers();
        new StudyGroupNameDialog(getActivity(),"Study Group Name").show(getActivity().getSupportFragmentManager(), "Showing StudyGroupNameGetter");
    }

    @Override
    protected void setViews(View view) {
        super.setViews(view);
        addFriendsButton.setText("Create Study Group");
    }

    // Has to be public and static for some reason
    public static class StudyGroupNameDialog extends AddSingleTextDialogFragment {
        public StudyGroupNameDialog(Activity activity, String hint1) {
            super(activity, hint1);
        }

        @Override
        protected void positiveButtonClicked() {
            Map<String, Object> studyGroup = new HashMap<>();
            studyGroup.put("type", "studygroup");
            studyGroup.put("owner_id",staticUserID);
            ArrayList<String> userIDs = new ArrayList<>();
            for (User user : users) {
                userIDs.add(user.getUser_id());
            }
            userIDs.add(staticUserID);
            studyGroup.put("members",userIDs);
            studyGroup.put("messages",new ArrayList<>());
            studyGroup.put("name",hint1Text.getText().toString());
            FirebaseFirestore.getInstance().collection("Chats").add(studyGroup).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG,"Successfully added new Study Group with id: " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,e.toString());
                }
            });
        }
    }


}
