package com.example.myapplication.Fragments.Profile.Query.Filter;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class NoFriendFilter extends FriendFilter{

    public NoFriendFilter(QueryFilter queryFilter, String user_id) {
        super(queryFilter, user_id);
    }
    @Override
    public boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        return super.filter(queryDocumentSnapshot) && !hasFriend(queryDocumentSnapshot);
    }
}
