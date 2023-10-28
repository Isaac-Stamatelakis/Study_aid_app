package com.example.myapplication.Fragments.Social.UserSearch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Fragments.Profile.Query.Filter.NameFilter;
import com.example.myapplication.Fragments.Profile.Query.Filter.QueryFilter;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.example.myapplication.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class StandardUserSearchFragment extends AUserSearchFragment {
    protected EditText searchBar;
    private final static String TAG = "StandardUserSearchFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    protected UserSearchQuery generateQuery(QueryDocumentSnapshot documentSnapshot,boolean clearOnComplete) {
        String searchBarText = searchBar.getText().toString();
        String search;
        if (searchBarText.isEmpty()) {
            search = null;
        } else {
            search = searchBarText.toLowerCase(Locale.CANADA);
        }
        QueryFilter queryFilter = new QueryFilter();
        queryFilter = new NameFilter(queryFilter,search);
        return new StandardUSQ(TAG,documentSnapshot,clearOnComplete,queryFilter);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.add_friend_fragment, container, false);
    }

    @Override
    protected void initializeViews(View view) {
        super.initializeViews(view);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchQuery();
                }
                return false;
            }
        });
    }
    protected void searchQuery() {
        executeQuery(null,true);
    }
    @Override
    protected void setViews(View view) {
        viewMoreButton = view.findViewById(R.id.add_friend_button);
        progressBar = view.findViewById(R.id.add_friend_progress);
        userList = view.findViewById(R.id.add_friend_fragment_list);
        searchBar = view.findViewById(R.id.add_friend_fragment_search);
    }

    protected class StandardUSQ extends UserSearchQuery {

        public StandardUSQ(String TAG, QueryDocumentSnapshot previousDocument, boolean clearOnComplete, QueryFilter queryFilter) {
            super(TAG, previousDocument, clearOnComplete,queryFilter);
        }

        @Override
        protected void executeCompleted() {
            super.executeCompleted();
            clearQueryCompleted(getUsers(),getSavedSnapshot(),getClearOnComplete());
        }
    }
}
