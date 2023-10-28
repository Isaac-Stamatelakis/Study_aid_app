package com.example.myapplication.Fragments.Social.ChatGroup.OldChatGroupFragment;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.Message.MessageArrayAdapter;
import com.example.myapplication.Fragments.Social.Message.TextMessage;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;

public abstract class OldChatGroupFragment extends Fragment {
    FirebaseFirestore db;
    Fragment thisFragment;
    String user_id;
    ChatGroup chatGroup;
    EditText messageText;
    ListView messageListView;
    MessageArrayAdapter messageArrayAdapter;
    ImageView shareStudyMaterialButton;

    public OldChatGroupFragment(ChatGroup chatGroup) {
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
        messageArrayAdapter = new MessageArrayAdapter(getContext(), chatGroup.getMessages(), chatGroup.getMemberNames(), getActivity().getSupportFragmentManager());
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

    public void updateAdapter() {
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
