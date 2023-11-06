package com.example.myapplication.Fragments.Social.UserSearch;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Profile.Query.UserSearchQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public abstract class AUserSearchFragment extends Fragment{

    protected String user_id;
    protected FirebaseFirestore db;
    protected ListView userList;
    protected Button viewMoreButton;
    protected ProgressBar progressBar;
    protected UserArrayAdapter userArrayAdapter;
    protected QueryDocumentSnapshot lastSnapshot = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflateView(inflater,container);
        setViews(view);
        initializeViews(view);
        // General
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        executeQuery(lastSnapshot,true);
        return view;
    }
    protected void initializeViews(View view) {
        progressBar.setVisibility(View.GONE);
        initializeAdapter();
        userList.setAdapter(userArrayAdapter);

        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQuery(lastSnapshot,false);
            }
        });
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelected(userArrayAdapter.getItem(position));
            }
        });
    }

    protected void initializeAdapter() {
        userArrayAdapter = new UserArrayAdapter(getContext(),new ArrayList<>());
    }


    protected void setViews(View view) {

    }
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }
    protected UserSearchQuery generateQuery(QueryDocumentSnapshot documentSnapshot, boolean clearOnComplete) {
        return null;
    }
    protected void userSelected(User user) {

    }
    protected void executeQuery(QueryDocumentSnapshot documentSnapshot,boolean clearOnComplete) {
        if (clearOnComplete) {
            userArrayAdapter.clear();
            progressBar.setVisibility(View.VISIBLE);
        }
        UserSearchQuery userSearchQuery = generateQuery(documentSnapshot,clearOnComplete);
        userSearchQuery.execute();
    }
    protected void clearQueryCompleted(ArrayList<User> users,QueryDocumentSnapshot lastSnapshot,boolean clear) {
        if (clear) {
            progressBar.setVisibility(View.GONE);
            userArrayAdapter.clear();
        }
        this.lastSnapshot = lastSnapshot;
        userArrayAdapter.addAll(users);
        userArrayAdapter.notifyDataSetChanged();
    }
}
