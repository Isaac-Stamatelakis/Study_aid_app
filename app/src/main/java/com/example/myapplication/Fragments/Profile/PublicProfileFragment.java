package com.example.myapplication.Fragments.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Fragments.Profile.ProfileFragment;
import com.example.myapplication.Fragments.Profile.User;

public class PublicProfileFragment extends ProfileFragment {
    public PublicProfileFragment(User user) {
        super(user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        editProfileButton.setVisibility(View.GONE);
        addFriend.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void addFriendButtonClick() {

    }
}
