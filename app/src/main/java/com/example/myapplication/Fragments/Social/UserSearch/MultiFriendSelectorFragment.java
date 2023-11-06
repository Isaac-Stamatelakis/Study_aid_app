package com.example.myapplication.Fragments.Social.UserSearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MultiFriendSelectorFragment extends SelectFriendUSQFragment {
    private final static String TAG = "MultiFriendSelectorFragment";
    protected ArrayList<User> selectedUsers = new ArrayList<>();
    protected Button addFriendsButton;
    @Override
    protected void userSelected(User user) {
        if (selectedUsers.contains(user)) {
            selectedUsers.remove(user);
        } else {
            selectedUsers.add(user);
        }
    }

    @Override
    protected void setViews(View view) {
        super.setViews(view);
        addFriendsButton = view.findViewById(R.id.multi_add_friend_button);
    }


    @Override
    protected void initializeViews(View view) {
        super.initializeViews(view);
        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendButtonClick();
            }
        });
    }

    @Override
    protected void initializeAdapter() {
        userArrayAdapter = new MultiUserArrayAdapter(getContext(),new ArrayList<>());
    }

    protected void addFriendButtonClick() {

    }
    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.multi_add_friend_fragment, container, false);
    }

    public ArrayList<User> getSelectedUsers() {
        return this.selectedUsers;
    }
    protected class MultiUserArrayAdapter extends UserArrayAdapter {
        public MultiUserArrayAdapter(Context context, ArrayList<User> users) {
            super(context, users);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view != null) {
                ImageView imageView = view.findViewById(R.id.user_content_box_image);
                imageView.setTag(R.drawable.screen_width_light_grey_rectangle);
                if (imageView != null) {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userSelected(userArrayAdapter.getItem(position));
                            int tag = (int) imageView.getTag();
                            if (tag == R.drawable.screen_width_light_grey_rectangle) {
                                imageView.setImageResource(R.drawable.screen_width_light_grey_rectangle2);
                                imageView.setTag(R.drawable.screen_width_light_grey_rectangle2);
                            } else {
                                imageView.setImageResource(R.drawable.screen_width_light_grey_rectangle);
                                imageView.setTag(R.drawable.screen_width_light_grey_rectangle);
                            }

                        }
                    });
                }
            }

            return view;
        }
    }
}
