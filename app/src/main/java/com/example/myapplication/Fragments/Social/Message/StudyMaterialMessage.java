package com.example.myapplication.Fragments.Social.Message;

import java.util.Date;

public class StudyMaterialMessage extends Message {

    private String studymaterialID;
    public StudyMaterialMessage(String owner, Date date, String studymaterialID) {
        super(owner, date);
        this.studymaterialID = studymaterialID;
    }
    public String getStudymaterialID() {
        return studymaterialID;
    }



}
