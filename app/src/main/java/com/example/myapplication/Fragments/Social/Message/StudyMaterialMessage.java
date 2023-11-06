package com.example.myapplication.Fragments.Social.Message;

import com.example.myapplication.Fragments.Classes.StudyMaterial.StudyMaterial;
import com.example.myapplication.Fragments.Profile.User;

import java.time.LocalDateTime;
import java.util.Date;

public class StudyMaterialMessage extends Message {
    protected StudyMaterial studyMaterial;

    public StudyMaterialMessage(User owner, LocalDateTime date, StudyMaterial studyMaterial) {
        super(owner, date);
        this.studyMaterial = studyMaterial;
    }
    public void setStudyMaterial(StudyMaterial studyMaterial) {
        this.studyMaterial = studyMaterial;
    }

    @Override
    public String getContent() {
        return studyMaterial.getdbID();
    }
    public StudyMaterial getStudyMaterial() {
        return studyMaterial;
    }
}
