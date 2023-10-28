package com.example.myapplication.Fragments.Social.UserSearch;

import com.example.myapplication.Fragments.Profile.Query.Filter.HasFriendFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SelectFriendUSQFragment extends StandardUserSearchFragment {
    private final static String TAG = "SelectFriendUSQFragment";
    @Override
    protected UserSearchQuery generateQuery(QueryDocumentSnapshot documentSnapshot, boolean clearOnComplete) {
        UserSearchQuery userSearchQuery = super.generateQuery(documentSnapshot,clearOnComplete);
        userSearchQuery.queryFilter = new HasFriendFilter(userSearchQuery.queryFilter,user_id);
        return userSearchQuery;
    }

}
