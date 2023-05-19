package com.example.myapplication.Fragments.Classes.StudyMaterial;

public class Quiz extends StudyMaterial {
    private final String format = "1&What is the meaning of life?&who knows&42&find it&yes&all of the above$2&What is 2 + 3?&1&4&8&5";
    public Quiz(String title, String content, String dbID) {
        super(title, content, dbID);
    }

    /*
    Format:
    1&What is the meaning of life?&who knows&42&find it&yes&all of the above$
    2&What is 2 + 3?&1&4&8&5$
     */
    public void buildContent() {

    }


}
