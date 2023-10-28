package com.example.myapplication.Fragments.Profile.Query.Filter;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FriendRequestFilter extends FilterDecorator {
    protected ArrayList<String> androidIDs;
    public FriendRequestFilter(QueryFilter queryFilter, ArrayList<String> androidIDs) {
        super(queryFilter);
        this.androidIDs = androidIDs;
        Log.e("test",androidIDs.size() + ".");
    }

    @Override
    public boolean filter(QueryDocumentSnapshot queryDocumentSnapshot) {
        Object value = queryDocumentSnapshot.get("user_id");
        if (value == null) {
            return false;
        }

        String queried_user_id = String.valueOf(value);
        return super.filter(queryDocumentSnapshot) && androidIDs.contains(queried_user_id);
    }
}
