package com.example.myapplication;

public class ColorText {
    public static String colorText(String text, String color) {
        return new StringBuilder()
                .append("<font color = '")
                .append(color)
                .append("'>")
                .append(text)
                .append("</font>")
                .toString();
        //"<font color = '#AEB8FE'>Manually Create</font>"
    }
}
