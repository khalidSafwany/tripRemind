package com.example.tripremainder.notes;

public class NoteList {
    private String text;

    public NoteList(String text) {
        this.text = text;
    }

    public NoteList() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
