package com.example.myapplication.Fragments.Classes.SchoolClass;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.CustomQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SchoolClass {
    private String number;
    private String section;
    private String subject;
    protected String user_id;
    private String institution;

    public String getDbID() {
        return dbID;
    }

    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    private String dbID;

    public SchoolClass(String number, String section, String subject) {
        this.number = number;
        this.section = section;
        this.subject = subject;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getNumber() {
        return this.number;
    }
    public String getSection() {
        return this.section;
    }
    public String getSubject() {
        return this.subject;
    }
    public String getInstitution() {return this.institution;}

    public void addClassToDB(String user_id) {
        this.user_id = user_id;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> classInfo = new HashMap<>();
        classInfo.put("Subject", this.subject);
        classInfo.put("Number", this.number);
        classInfo.put("Section", this.section);
        classInfo.put("user_id", user_id);

        classInfo.put("study_material", Collections.emptyList());
        CollectionReference classCollection = db.collection("Classes");
        String id = classCollection.document().getId();
        classCollection.document(id).set(classInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("AddClassFragment", "Class successfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AddClassFragment", "Class not added".concat(e.toString()));
                    }
                });
        new ClassChatQuery("SchoolClass",this).execute();

    }

    protected void createClassChatRoom() {
        Map<String, Object> chatInfo = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(user_id);
        chatInfo.put("members", arrayList);
        chatInfo.put("messages",new ArrayList<>());
        chatInfo.put("type","class");
        chatInfo.put("name",subject + " " + number);
        chatInfo.put("Institution",institution);
        chatInfo.put("Number",number);
        chatInfo.put("Section",section);
        chatInfo.put("Subject",subject);
        FirebaseFirestore.getInstance().collection("Chats").add(chatInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("SchoolClass", "Added Class Chat id: " + documentReference.get());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("SchoolClass",e.toString());
            }
        });
    }
    protected void addUserToChat(QueryDocumentSnapshot chatSnapshot) {
        Object val = chatSnapshot.get("members");
        if (val == null) {
            return;
        }
        ArrayList<String> memberIDs = (ArrayList<String>) val;
        if (!memberIDs.contains(user_id)) {
            memberIDs.add(user_id);
            FirebaseFirestore.getInstance().collection("Chats").document(chatSnapshot.getId()).update("members",memberIDs).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("SchoolClass", "Added " + user_id + " to class chat " + chatSnapshot.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("SchoolClass",e.toString());
                }
            });
        }
    }
    public String getFullClassName() {
        StringBuilder string = new StringBuilder()
                .append(this.subject)
                .append(" ")
                .append(this.number);
        if (this.section != null) {
            string.append(this.section);
        }
        return string.toString();
    }
    public boolean equal(SchoolClass schoolClass) {
        return Objects.equals(schoolClass.getSubject(), this.subject) &&
                Objects.equals(schoolClass.getSection(), this.section) &&
                Objects.equals(schoolClass.getNumber(), this.number);
    }

    public String getSubjectAndNumber() {
        return this.subject + " " + this.number;
    }

    protected class ClassChatQuery extends CustomQuery {
        protected SchoolClass schoolClass;
        protected int copiesFound;
        public ClassChatQuery(String TAG,SchoolClass schoolClass) {
            super(TAG);
            this.schoolClass = schoolClass;
        }

        @Override
        public void execute() {
            super.execute();
        }

        @Override
        protected Query generateQuery() {
            return FirebaseFirestore.getInstance().collection("Chats")
                    .whereEqualTo("Number",schoolClass.getNumber())
                    .whereEqualTo("Institution",schoolClass.getInstitution())
                    .whereEqualTo("Subject",schoolClass.getSubject())
                    .whereEqualTo("Section",schoolClass.getSection());
        }

        @Override
        protected void processSnapshot(QueryDocumentSnapshot snapshot) {
            copiesFound++;
            if (snapshot != null) {
                addUserToChat(snapshot);
            }

        }

        @Override
        protected void queryComplete() {
            if (copiesFound == 0) {
                createClassChatRoom();
            }
        }

        @Override
        protected void queryFailure(Exception e) {
            super.queryFailure(e);
        }

    }


}
