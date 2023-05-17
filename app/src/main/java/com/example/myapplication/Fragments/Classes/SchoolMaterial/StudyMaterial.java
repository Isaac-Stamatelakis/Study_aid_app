package com.example.myapplication.Fragments.Classes.SchoolMaterial;

public abstract class StudyMaterial {
    private String title;
    private String content;

    public StudyMaterial(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }
    public abstract void buildContent();


}
