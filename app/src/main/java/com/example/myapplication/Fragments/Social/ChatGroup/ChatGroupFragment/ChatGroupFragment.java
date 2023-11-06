package com.example.myapplication.Fragments.Social.ChatGroup.ChatGroupFragment;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.CustomDocumentRetrieval;
import com.example.myapplication.CustomQuery;
import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.ChatClassSelectorFragment;
import com.example.myapplication.Fragments.Classes.ClassSelectorFragment.ShareStudyMaterialCSF;
import com.example.myapplication.Fragments.Classes.StudyMaterial.DB.StudyMaterialRetriever;
import com.example.myapplication.Fragments.Profile.User;
import com.example.myapplication.Fragments.Social.ChatGroup.ChatGroup;
import com.example.myapplication.Fragments.Social.Message.Message;
import com.example.myapplication.Fragments.Social.Message.MessageArrayAdapter;
import com.example.myapplication.Fragments.Social.Message.StudyMaterialMessage;
import com.example.myapplication.Fragments.Social.Message.TextMessage;
import com.example.myapplication.R;
import com.example.myapplication.StaticHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatGroupFragment extends Fragment {
    protected final String TAG = "ChatGroupFragment";
    protected FirebaseFirestore db;
    protected Fragment thisFragment;
    protected String user_id;
    protected ChatGroup chatGroup;
    protected EditText messageText;
    protected ListView messageListView;
    protected Button viewMoreButton;
    protected MessageArrayAdapter messageArrayAdapter;
    protected ImageView shareStudyMaterialButton;
    protected ProgressBar progressBar;
    protected TextView emptyText;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    protected int messagesLoaded = 0;
    protected boolean retrieving = false;

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

        messageArrayAdapter = new MessageArrayAdapter(getContext(), chatGroup.getMessages(), getActivity().getSupportFragmentManager(),this);
        messageListView.setAdapter(messageArrayAdapter);
        retrieveLatestMessages(10);
        initChatDatabaseListener();
        return view;


    }



    protected void setViews(View view) {
        messageText = view.findViewById(R.id.chatgroup_send_message);
        messageListView = view.findViewById(R.id.chatgroup_messages);
        shareStudyMaterialButton = view.findViewById(R.id.chatgroup_share_studymaterial);
        progressBar = view.findViewById(R.id.chatgroup_progress_bar);
        viewMoreButton = view.findViewById(R.id.chatgroup_button);
        emptyText = view.findViewById(R.id.chatgroup_empty_text);
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
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveLatestMessages(10);
            }
        });
        emptyText.setVisibility(View.GONE);

    }

    protected void sendTextMessage(String messageText) {
        TextMessage textMessage = new TextMessage(new User(user_id), LocalDateTime.now(), messageText);
        chatGroup.addMessageToDB(textMessage, user_id, "text");
    }
    public void clearMessages() {
        messagesLoaded = 0;
        chatGroup.setMessages(new ArrayList<>());

    }
    protected void shareStudyMaterial() {
        clearMessages();
        ShareStudyMaterialCSF shareStudyMaterialCSF = new ShareStudyMaterialCSF(chatGroup);
        StaticHelper.switchFragment(requireActivity().getSupportFragmentManager(),shareStudyMaterialCSF, null);
    }
    protected String getFragmentTag() {
        return this.TAG;
    }

    protected void retrieveLatestMessages(int amount) {
        if (chatGroup.getMessageIDS().size() == 0) {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
        if (retrieving) {
            return;
        }

        retrieving = true;

        ArrayList<String> messageIDs = chatGroup.getMessageIDS();
        int startIndex = Math.max(messageIDs.size()-messagesLoaded-amount,0);
        int endIndex = messageIDs.size()-messagesLoaded-1;
        messagesLoaded += amount;

        if (startIndex >= messageIDs.size() || startIndex < 0) {
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
    protected void displayMessages(ArrayList<Message> messageToAdd) {

        retrieving = false;
        progressBar.setVisibility(View.GONE);
        ArrayList<Message> sortedMessages = new ArrayList<>();
        while (messageToAdd.size() > 0) {
            Message maxMessage = messageToAdd.get(0);
            for (int j = 0; j < messageToAdd.size(); j++) {
                if (messageToAdd.get(j).getDate().compareTo(maxMessage.getDate()) >= 0) {
                    maxMessage = messageToAdd.get(j);
                }
            }
            sortedMessages.add(maxMessage);
            messageToAdd.remove(maxMessage);
        }
        for (Message message : sortedMessages) {
            chatGroup.addMessageToFront(message);
        }
        messageArrayAdapter.notifyDataSetChanged();
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

    /**
     * Retrieves a message from the database
     * i) retrieves document with messageID
     * ii) retrieves user with ownerID
     * iii) If message is a studymaterial message, retrieves studymaterial with content (studymaterialID)
     */
    protected class MessageRetriever extends CustomDocumentRetrieval {
        protected String messageID;
        protected Message message;
        protected User user;
        protected int additionalRetrievalAmount = 1;
        protected int currentAdditionalRetrievals = 0;
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
            processMessage(snapshot);
        }
        protected void additionalRetrievalComplete() {
            currentAdditionalRetrievals ++;
            if (currentAdditionalRetrievals == additionalRetrievalAmount) {
                messageReceived();
            }
        }
        protected void messageReceived() {
            messageArrayAdapter.add(message);
        }
        protected void processMessage(DocumentSnapshot snapshot) {
            Object val = snapshot.get("type");
            if (val == null) {
                return;
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

            new MessageUserQuery(TAG,owner).execute();

            switch (type) {
                case "text":
                    message = new TextMessage(null,localDateTime,content);
                    break;
                case "studymaterial":
                    message = new StudyMaterialMessage(null,localDateTime,null);
                    additionalRetrievalAmount ++;
                    new MessageStudyMaterialRetriever(TAG,content).execute();
                    break;
            }
        }


        protected class MessageUserQuery extends CustomQuery {
            protected String dbID;
            protected User user = null;
            public MessageUserQuery(String TAG, String dbID) {
                super(TAG);
                this.dbID = dbID;
            }
            @Override
            protected Query generateQuery() {
                return db.collection("Users").whereEqualTo("user_id",dbID);
            }

            @Override
            protected void processSnapshot(QueryDocumentSnapshot snapshot) {
                user = new User(dbID);
                user.setUserToDocument(snapshot);
            }

            @Override
            protected void queryComplete() {
                message.setOwner(user);
                additionalRetrievalComplete();
            }
        }
        protected class MessageStudyMaterialRetriever extends StudyMaterialRetriever {

            public MessageStudyMaterialRetriever(String TAG, String studyMaterialID) {
                super(TAG, studyMaterialID);
            }

            @Override
            protected void retrievalComplete() {

                if (message instanceof StudyMaterialMessage) {
                    ((StudyMaterialMessage) message).setStudyMaterial(studyMaterial);
                } else {
                    Log.e(TAG,"Tried to add studymaterial to non-StudyMaterialMessage");
                }
                additionalRetrievalComplete();
            }
        }
    }
    protected class MultiMessageRetriever {
        protected ArrayList<String> messageIDs;
        protected ArrayList<Message> messages = new ArrayList<>();
        protected int currentMessage = 0;
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
        protected void singleRetrievalComplete(Message message) {
            messages.add(message);
            if (messages.size() == messageIDs.size()) {
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
            protected void messageReceived() {
                singleRetrievalComplete(message);
            }
        }
    }


}
