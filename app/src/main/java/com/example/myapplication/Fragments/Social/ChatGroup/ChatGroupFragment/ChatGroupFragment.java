package com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.example.myapplication.Fragments.Social.Message.MessageArrayAdapter;
import com.example.myapplication.Fragments.Social.Message.StudyMaterialMessage;
import com.example.myapplication.Fragments.Social.Message.TextMessage;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatGroupFragment extends Fragment {
    protected final String TAG = "ChatGroupFragment";
    protected FirebaseFirestore db;
    protected Fragment thisFragment;
    protected String user_id;
    protected ChatGroup chatGroup;
    protected EditText messageText;
    protected ListView messageListView;
    protected MessageArrayAdapter messageArrayAdapter;
    protected ImageView shareStudyMaterialButton;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    protected int messagesLoaded = 0;

    public ChatGroupFragment(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatgroup_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        thisFragment = this;
        user_id = (Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));

        setViews(view);
        setViewListeners(view);
        chatGroup.setMessages(new ArrayList<>());
        messageArrayAdapter = new MessageArrayAdapter(getContext(), new ArrayList<>(), chatGroup.getMemberNames(), getActivity().getSupportFragmentManager());
        messageListView.setAdapter(messageArrayAdapter);
        retrieveMessages(0,10);
        initChatDatabaseListener();
        return view;


    }

    protected void setViews(View view) {
        messageText = view.findViewById(R.id.chatgroup_send_message);
        messageListView = view.findViewById(R.id.chatgroup_messages);
        shareStudyMaterialButton = view.findViewById(R.id.chatgroup_share_studymaterial);
    }
    protected void setViewListeners(View view) {
        messageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendTextMessage(messageText.getText().toString());
                    messageText.setText("");
                }
                return false;
            }
        });
        shareStudyMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStudyMaterial();
            }
        });
    }

    protected void sendTextMessage(String messageText) {
        TextMessage textMessage = new TextMessage(user_id, LocalDateTime.now(), messageText);
        chatGroup.addMessageToDB(textMessage, user_id, "text");
    }
    protected void shareStudyMaterial() {

    }
    protected String getFragmentTag() {
        return this.TAG;
    }

    protected void retrieveMessages(int startIndex, int endIndex) {
        ArrayList<String> messageIDs = chatGroup.getMessageIDS();
        if (startIndex >= messageIDs.size()) {
            return;
        }
        ArrayList<String> partitionedMessageIDDs = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i ++) {
            if (i < messageIDs.size()) {
                partitionedMessageIDDs.add(messageIDs.get(i));
            }
        }
        new MultiMessageRetriever(partitionedMessageIDDs).execute();
    }
    protected Message processMessage(DocumentSnapshot snapshot) {
        Object val = snapshot.get("type");
        if (val == null) {
            return null;
        }
        String type = (String) val;

        String content = "";
        val = snapshot.get("content");
        if (val != null) {
            content = (String) val;
        }
        String dateString = "";
        LocalDateTime localDateTime = null;
        val = snapshot.get("date");
        if (val != null) {
            dateString = (String) val;
            localDateTime = LocalDateTime.parse(dateString, formatter);
        }
        String owner = "";
        val = snapshot.get("owner");
        if (val != null) {
            owner = (String) val;
        }
        Message message = null;

        switch (type) {
            case "text":
                message = new TextMessage(owner,localDateTime,content);
                break;
            case "studymaterial":
                message = new StudyMaterialMessage(owner,localDateTime,content);
                break;
        }
        return message;
    }
    protected void displayMessages(ArrayList<Message> messageToAdd) {
        ArrayList<Message> sortedMessages = new ArrayList<>();
        while (messageToAdd.size() > 0) {
            Message minMessage = messageToAdd.get(0);
            for (int j = 0; j < messageToAdd.size(); j++) {
                if (messageToAdd.get(j).getDate().compareTo(minMessage.getDate()) < 0) {
                    minMessage = messageToAdd.get(j);
                }
            }
            sortedMessages.add(minMessage);
            messageToAdd.remove(minMessage);
        }

        messageArrayAdapter.addAll(sortedMessages);
        messageArrayAdapter.notifyDataSetChanged();
        messagesLoaded += sortedMessages.size();
    }
    protected void initChatDatabaseListener() {
        db.collection("Chats").document(chatGroup.getDbID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    ArrayList<String> newMessages = new ArrayList<>();
                    Object docVal = value.get("messages");
                    if (docVal != null) {
                        newMessages = (ArrayList<String>) docVal;
                    }
                    ArrayList<String> currentMessages = chatGroup.getMessageIDS();
                    for (String newMessage : newMessages) {
                        if (!currentMessages.contains(newMessage)) {
                            Log.e("TEST",newMessage);
                            chatGroup.getMessageIDS().add(newMessage);
                            new MessageRetriever(TAG,newMessage).execute();
                        }
                    }
                } else {
                    Log.e(getFragmentTag(),error.toString());
                }
            }
        });
    }

    protected class MessageRetriever extends CustomDocumentRetrieval {
        protected String messageID;
        public MessageRetriever(String TAG, String messageID) {
            super(TAG);
            this.messageID = messageID;
        }
        @Override
        protected DocumentReference getDocument() {
            return db.collection("Messages").document(messageID);
        }
        @Override
        protected void processSnapshot(DocumentSnapshot snapshot) {
            Message message = processMessage(snapshot);
            messageArrayAdapter.add(message);
        }
    }
    protected class MultiMessageRetriever {
        protected ArrayList<String> messageIDs;
        protected ArrayList<Message> messages = new ArrayList<>();
        protected int currentMessage = 0;
        protected int retrievedMessages = 0;
        public MultiMessageRetriever(ArrayList<String> messageIDs) {
            this.messageIDs = messageIDs;
        }
        public void execute() {
            while (currentMessage < messageIDs.size()) {
                retrieveCurrentMessage();
                currentMessage ++;
            }

        }
        protected void retrieveCurrentMessage() {
            new MessageRetrievalForMulti(TAG,messageIDs.get(currentMessage)).execute();
        }
        protected void singleRetrievalComplete() {
            retrievedMessages++;
            if (retrievedMessages == messageIDs.size()) {
                multiRetrievalComplete();
            }
        }
        protected void multiRetrievalComplete() {
            displayMessages(messages);
        }
        protected class MessageRetrievalForMulti extends MessageRetriever {
            public MessageRetrievalForMulti(String TAG, String messageID) {
                super(TAG, messageID);
            }
            @Override
            protected void processSnapshot(DocumentSnapshot snapshot) {
                messages.add(processMessage(snapshot));
            }

            @Override
            protected void retrievalComplete() {
                singleRetrievalComplete();
            }
        }
    }

}
