package com.example.myapplication.Fragments.Social;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.AddClassFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupArrayAdapter;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ChatGroupFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.ClassChatGroupFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment.NonClassChatGroupFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ClassChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.FriendChatGroup;
import com.example.myapplication.Fragments.Social.ChatGroup.StudyChatGroup;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class SocialSelectorFragment extends Fragment{
    ListView chatGroupList;
    String user_id;
    String mode;
    FirebaseFirestore db;
    HashMap<String, ChatGroup> classes;
    HashMap<String, ChatGroup> studyGroups;
    HashMap<String, ChatGroup> friends;
    ChatGroupArrayAdapter chatGroupArrayAdapter;
    HashMap<String, ImageView> modeSelectors;
    ArrayList<ChatGroup> arrayAdapterList;
    FloatingActionButton addButton;
    ArrayList<ListenerRegistration> studyMaterialSnapshots;
    Fragment thisFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatgroup_selector_fragment, container, false);
        // Views
        chatGroupList = view.findViewById(R.id.chatgroup_selector_list);
        modeSelectors = new HashMap<>();
        modeSelectors.put("classes", view.findViewById(R.id.chatgroup_selector_class));
        modeSelectors.put("studyGroups", view.findViewById(R.id.chatgroup_selector_studygroup));
        modeSelectors.put("friends", view.findViewById(R.id.chatgroup_selector_friends));
        addButton = view.findViewById(R.id.chatgroup_add_button);

        // General
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        db = FirebaseFirestore.getInstance();
        thisFragment = this;

        // Array Adapter
        classes = new HashMap<>();
        studyGroups = new HashMap<>();
        friends = new HashMap<>();

        arrayAdapterList = new ArrayList<>();
        chatGroupArrayAdapter = new ChatGroupArrayAdapter(this.getActivity(), arrayAdapterList);
        mode = "classes";
        chatGroupList.setAdapter(chatGroupArrayAdapter);
        databaseListener();
        modeSelectorHandler();


        /**
         * Goes to the study material activity of the clicked studymaterial
         */
        chatGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (mode) {
                    case "classes":
                        ClassChatGroupFragment classChatGroupFragment = new ClassChatGroupFragment((ClassChatGroup) arrayAdapterList.get(position));
                        switchFragment(classChatGroupFragment, null);
                        break;
                    case "studyGroups":
                        NonClassChatGroupFragment nonClassChatGroupFragment = new NonClassChatGroupFragment(arrayAdapterList.get(position));
                        switchFragment(nonClassChatGroupFragment, null);
                        break;
                    case "friends":

                        break;
                }
            }
        });
        /**
         * Deletes a study material from the class
         */
        chatGroupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Do you want to delete")
                        .setPositiveButton(Html.fromHtml("<font color = '#AEB8FE'>Delete</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color = '#AEB8FE'>Cancel</font>"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode) {
                    case "classes":
                            AddClassFragment addClassFragment = new AddClassFragment();
                            switchFragment(addClassFragment, null);
                        break;
                    case "studyGroups":

                        break;
                    case "friends":

                        break;
                }
            }
        });
        return view;


    }

    /**
     * Is called every time an add studymaterial dialog concludes
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 964) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                switch (mode) {
                    case "classes":

                        break;
                    case "studyGroups":

                        break;
                    case "friends":

                        break;
                }
            }
        }
    }

    /**
     * Switches fragments
     * @param fragment: fragment to be switched to
     * @param bundle: bundle to be sent to new fragment
     */
    public void switchFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Sets the arrayAdapterList (the list which is displayed by the arrayadapter)
     * to whichever studymaterial is the current mode
     */
    public void setArrayAdapter() {
        arrayAdapterList.clear();
        switch (mode) {
            case "classes":
                for (String key: classes.keySet()) {
                    arrayAdapterList.add(classes.get(key));
                }
                break;
            case "studyGroups":
                for (String key: studyGroups.keySet()) {
                    arrayAdapterList.add(studyGroups.get(key));
                }
                break;
            case "friends":
                for (String key: friends.keySet()) {
                    arrayAdapterList.add(friends.get(key));
                }
                break;
        }
        chatGroupArrayAdapter.notifyDataSetChanged();
    }



    public void databaseListener() {
        /*
        Classes: In order for a class to appear in the chat, there must be atleast 2 people in the class.
        Two classes are the same if they are at the same university, have the same number, have the same subject and have the same section.
         */

        Query query = db.collection("Chats").whereArrayContains("members", user_id);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot doc : value) {
                        switch ((String) doc.get("type")) {
                            case "class":
                                ClassChatGroup classChatGroup = new ClassChatGroup((String) doc.get("name"), (ArrayList<String>) doc.get("members"), new ArrayList<>(), doc.getId());
                                classChatGroup.getFromDatabase();
                                classes.put(doc.getId(), classChatGroup);

                                break;
                            case "studygroup":
                                StudyChatGroup studyChatGroup = new StudyChatGroup((String) doc.get("name"), (ArrayList<String>) doc.get("members"), new ArrayList<>(),doc.getId());
                                studyChatGroup.getFromDatabase();
                                studyGroups.put(doc.getId(), studyChatGroup);

                                break;
                            case "friends":
                                ArrayList<String> ids = (ArrayList<String>) doc.get("members");
                                String friendID;
                                if (ids.get(0) == user_id) {
                                    friendID = ids.get(1);
                                } else {
                                    friendID = ids.get(0);
                                }
                                Query friendQuery = db.collection("Users").whereEqualTo("user_id", friendID);
                                friendQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                friends.put(doc.getId(), new FriendChatGroup((String) queryDocumentSnapshot.get("name"), (ArrayList<String>) doc.get("members"), new ArrayList<>(), doc.getId()));
                                                break;
                                            }
                                        }
                                    }
                                });
                                break;
                        }
                    }
                    setArrayAdapter();
            }
        });

    }

    /**
     * Sets the colors of the mode selectors.
     * The selected one is orange, the rest are light blue.
     */
    public void modeSelectorHandler() {
        for (String modeKey : modeSelectors.keySet()) {
            modeSelectors.get(modeKey).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == modeKey) {
                        return;
                    }
                    mode = modeKey;
                    for (String anotherKey: modeSelectors.keySet()) {
                        if (modeKey == anotherKey) {
                            modeSelectors.get(anotherKey).setImageResource(R.drawable.small_orange_rect);
                        } else {
                            modeSelectors.get(anotherKey).setImageResource(R.drawable.small_blue_rect);
                        }
                    }
                    setArrayAdapter();
                }
            });
        }
    }
}
