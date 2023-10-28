package com.example.myapplication.Fragments.Profile.Query.Filter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class HasFriendFilter extends FriendFilter{
    public HasFriendFilter(QueryFilter queryFilter, String user_id) {
        super(queryFilter, user_id);
    }

    @Override
    public boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        return super.filter(queryDocumentSnapshot) && hasFriend(queryDocumentSnapshot);
    }
}
