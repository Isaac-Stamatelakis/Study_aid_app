package com.example.myapplication.Fragments.ClassFragment;

public class SchoolClass {
    private String number;
    private String section;
    private String subject;

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
    public String getNumber() {
        return this.number;
    }
    public String getSection() {
        return this.section;
    }
    public String getSubject() {
        return this.subject;
    }

}
