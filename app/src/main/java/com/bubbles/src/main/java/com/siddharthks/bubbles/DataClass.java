package com.bubbles.src.main.java.com.siddharthks.bubbles;

import java.io.Serializable;

public class DataClass implements Serializable {
    String noteText;


    public DataClass(String noteText) {
        this.noteText = noteText;

    }


    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}

