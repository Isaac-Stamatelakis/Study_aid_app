package com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Classes.ManualAddClassDialogFragment;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClass;
import com.example.myapplication.Fragments.Classes.SchoolClass.SchoolClassArrayAdapter;
import com.example.myapplication.Fragments.Classes.StudyMaterial.Selector.ChatStudyMaterialSelectorFragment;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.example.myapplication.Fragments.Social.Message.MessageArrayAdapter;
import com.example.myapplication.Fragments.Social.Message.TextMessage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class ChatGroupFragment extends Fragment {
    FirebaseFirestore db;
    Fragment thisFragment;
    String user_id;
    ChatGroup chatGroup;
    EditText messageText;
    ListView messageListView;
    MessageArrayAdapter messageArrayAdapter;
    ImageView shareStudyMaterialButton;

    public ChatGroupFragment(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatgroup_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        thisFragment = this;
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));

        messageText = view.findViewById(R.id.chatgroup_send_message);
        messageListView = view.findViewById(R.id.chatgroup_messages);
        shareStudyMaterialButton = view.findViewById(R.id.chatgroup_share_studymaterial);
        messageArrayAdapter = new MessageArrayAdapter(getContext(), chatGroup.getMessages(), chatGroup.getMemberNames());
        messageListView.setAdapter(messageArrayAdapter);
        messageArrayAdapter.notifyDataSetChanged();

        return view;


    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("TEST", "UPDATE");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d("TEST", "TEST");
                    sendTextMessage(messageText.getText().toString());
                    messageText.setText("");
                }
                return false;
            }
        });
        shareStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStudyMaterialShare();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void sendTextMessage(String messageText) {
        TextMessage textMessage = new TextMessage(user_id, LocalDateTime.now(), messageText);
        chatGroup.addMessage(textMessage);
        chatGroup.addMessageToDB(textMessage, user_id, "text");
        messageArrayAdapter.notifyDataSetChanged();
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

    public abstract void handleStudyMaterialShare();
}
