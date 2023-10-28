package com.example.myapplication.Fragments.Profile.FriendRequestFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.Fragments.Profile.Query.Filter.FriendRequestFilter;
import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.UserSearch.AUserSearchFragment;
import com.example.myapplication.Fragments.Profile.Query.Filter.QueryFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FriendRequestFragment extends AUserSearchFragment {
    public FriendRequestFragment(User user) {
        this.user = user;
    }
    private static String TAG = "FriendRequestFragment";
    protected TextView noFriendRequests;
    protected User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }
    @Override
    protected UserSearchQuery generateQuery(QueryDocumentSnapshot documentSnapshot,boolean clearOnComplete) {
        QueryFilter queryFilter = new QueryFilter();
        queryFilter = new FriendRequestFilter(queryFilter,user.getFriendRequestAndroidIDs());
        return new GetFriendRequestUSQ(TAG,documentSnapshot,clearOnComplete,queryFilter);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.friend_request_fragment, container, false);
    }

    @Override
    protected void initializeViews(View view) {
        super.initializeViews(view);
        noFriendRequests.setVisibility(View.GONE);
    }

    @Override
    protected void setViews(View view) {
        viewMoreButton = view.findViewById(R.id.friend_request_button);
        progressBar = view.findViewById(R.id.friend_request_progress);
        userList = view.findViewById(R.id.friend_request_list);
        noFriendRequests = view.findViewById(R.id.friend_request_no);
    }


    @Override
    protected void clearQueryCompleted(ArrayList<User> users, QueryDocumentSnapshot lastSnapshot, boolean clear) {
        super.clearQueryCompleted(users, lastSnapshot, clear);
        if (users.size() == 0) {
            noFriendRequests.setVisibility(View.VISIBLE);
        } else {
            noFriendRequests.setVisibility(View.GONE);
        }
    }

    @Override
    protected void userSelected(User selectedUser) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to add " + selectedUser.getName() + " as a friend?")
                .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addFriend(selectedUser);
                    }
                })
                .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    protected void addFriend(User selectedUser) {
        new AddFriendQuery(user_id,selectedUser.getUser_id(),getContext()).execute();
    }
    protected class GetFriendRequestUSQ extends UserSearchQuery {
        public GetFriendRequestUSQ(String TAG, QueryDocumentSnapshot previousDocument, boolean clearOnComplete, QueryFilter queryFilter) {
            super(TAG, previousDocument, clearOnComplete, queryFilter);
        }

        @Override
        protected void executeCompleted() {
            super.executeCompleted();
            clearQueryCompleted(getUsers(),getSavedSnapshot(),getClearOnComplete());
        }
    }
}
