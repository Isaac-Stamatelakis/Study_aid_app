package com.example.myapplication.Fragments.Profile;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    String user_id;
    String name;
    String educationLevel;
    String faculty;
    String[] majors;
    String[] minors;
    String[] schools;
    String year;
    String docID;

    public User(String user_id) {
        this.user_id = user_id;
        this.setUserFromDB();
    }

    public void setUserFromDB() {
        Query user_query = FirebaseFirestore.getInstance().collection("Users").whereEqualTo("user_id", user_id);
        user_query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                    for (DocumentSnapshot documentSnapshot: documentSnapshots) {
                        // There will only ever be one user but whatever
                        name = (String) documentSnapshot.get("Name");
                        educationLevel = (String) documentSnapshot.get("Education_level");
                        faculty = (String) documentSnapshot.get("Faculty");
                        majors = (String[]) documentSnapshot.get("Major");
                        minors = (String[]) documentSnapshot.get("Minor");
                        schools = (String[]) documentSnapshot.get("School");
                        docID = documentSnapshot.getId();
                    }
                }
            }
        });
    }

    public void setDBToUser() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("Major", this.majors);
        userInfo.put("Minor", this.minors);
        userInfo.put("Name", this.name);
        userInfo.put("Education_level", this.educationLevel);
        userInfo.put("user_id", this.user_id);
        userInfo.put("School", this.schools);
        userInfo.put("Year", this.year);
        userInfo.put("Faculty", this.faculty);
        FirebaseFirestore.getInstance().collection("Users").document(docID).set(userInfo);
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public String getFaculty() {
        return faculty;
    }

    public String[] getMajors() {
        return majors;
    }

    public String[] getMinors() {
        return minors;
    }

    public String[] getSchools() {
        return schools;
    }

    public String getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setMajors(String[] majors) {
        this.majors = majors;
    }

    public void setMinors(String[] minors) {
        this.minors = minors;
    }

    public void setSchools(String[] schools) {
        this.schools = schools;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
