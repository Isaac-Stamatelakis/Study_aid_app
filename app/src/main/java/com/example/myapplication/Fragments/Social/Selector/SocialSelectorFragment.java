package com.example.myapplication.Fragments.Social.Selector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupArrayAdapter;
import com.example.myapplication.Fragments.Social.Selector.StudyGroup.StudyGroupSelectorMode;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class SocialSelectorFragment extends Fragment{
    protected int currentMode;
    protected ListView chatGroupList;
    protected String user_id;
    protected String mode;
    protected FirebaseFirestore db;
    protected ArrayList<SocialSelectorMode> socialSelectorModes = new ArrayList<>();
    protected ChatGroupArrayAdapter chatGroupArrayAdapter;
    protected FloatingActionButton addButton;
    protected Fragment thisFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatgroup_selector_fragment, container, false);
        // Views
        chatGroupList = view.findViewById(R.id.chatgroup_selector_list);
        addButton = view.findViewById(R.id.chatgroup_add_button);
        setViewListeners();

        // General
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        thisFragment = this;

        // Modes
        currentMode = 0;
        socialSelectorModes.add(new ClassSelectorMode(user_id, view.findViewById(R.id.chatgroup_selector_class),this));
        socialSelectorModes.add(new StudyGroupSelectorMode(user_id, view.findViewById(R.id.chatgroup_selector_studygroup),this));
        socialSelectorModes.add(new FriendSelectorMode(user_id,view.findViewById(R.id.chatgroup_selector_friends),this));
        setModeButtonClickListeners();
        return view;

    }
    @Override
    public void onResume() {
        setArrayAdapter();
        super.onResume();
    }

    protected void setViewListeners() {
        chatGroupArrayAdapter = new ChatGroupArrayAdapter(getContext(),new ArrayList<>());
        chatGroupList.setAdapter(chatGroupArrayAdapter);
        /**
         * Goes to the study material activity of the clicked studymaterial
         */
        chatGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                socialSelectorModes.get(currentMode).onClick(position);
            }
        });
        /**
         * Deletes a study material from the class
         */
        chatGroupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                socialSelectorModes.get(currentMode).onDeleteRequest(position,user_id);
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialSelectorModes.get(currentMode).onAddClick();
            }
        });
    }
    /**
     * Sets the arrayAdapterList (the list which is displayed by the arrayadapter)
     * to whichever studymaterial is the current mode
     */
    public void setArrayAdapter() {
        chatGroupArrayAdapter.clear();
        if (socialSelectorModes.get(currentMode).chatGroups != null) {
            chatGroupArrayAdapter.addAll(socialSelectorModes.get(currentMode).chatGroups);
            chatGroupArrayAdapter.notifyDataSetChanged();
        }

    }

    /**
     * Sets the colors of the mode selectors.
     * The selected one is orange, the rest are light blue.
     */
    public void setModeButtonClickListeners() {
        // First iteration sets the onClickListener
        // Second iteration checks if the button selected is different than the current mode
        for (SocialSelectorMode socialSelectorMode : socialSelectorModes) {
            ImageView selectedModeSelector = socialSelectorMode.getModeSelector();
            selectedModeSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedIndex = socialSelectorModes.indexOf(socialSelectorMode);
                    if (currentMode == selectedIndex) {
                        return;
                    }
                    if (socialSelectorModes.get(selectedIndex).chatGroups == null) {
                        return;
                    }
                    currentMode = selectedIndex;
                    setArrayAdapter();
                    // Second sets the colors of the buttons
                    for (SocialSelectorMode anotherSelectorMode : socialSelectorModes) {
                        ImageView modeSelector = anotherSelectorMode.getModeSelector();
                        int index = socialSelectorModes.indexOf(anotherSelectorMode);
                        if (index == currentMode) {
                            modeSelector.setImageResource(R.drawable.small_orange_rect);
                        } else {
                            modeSelector.setImageResource(R.drawable.small_blue_rect);
                        }
                    }
                }
            });
        }
    }
}
