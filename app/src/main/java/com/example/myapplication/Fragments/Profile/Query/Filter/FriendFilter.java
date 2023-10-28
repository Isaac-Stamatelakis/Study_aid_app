package com.example.myapplication.Fragments.Profile.Query.Filter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

public abstract class FriendFilter extends FilterDecorator{
    protected String user_id;
    public FriendFilter(QueryFilter queryFilter, String user_id) {
        super(queryFilter);
        this.user_id = user_id;

    }

    protected boolean hasFriend(QueryDocumentSnapshot queryDocumentSnapshot) {
        Object value = queryDocumentSnapshot.get("Friends");
        if (value == null) {
            return false;
        }

        HashMap<String,String> friends = (HashMap<String,String>) queryDocumentSnapshot.get("Friends");
        if (friends == null) {
            return false;
        }
        return friends.containsKey(user_id);

    }
}
