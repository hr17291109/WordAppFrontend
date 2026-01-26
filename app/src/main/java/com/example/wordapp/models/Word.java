package com.example.wordapp.models;

public class Word {
    private int id;
    private String text;     // 英単語
    private String meaning;  // 意味

    public Word(int id, String text, String meaning) {
        this.id = id;
        this.text = text;
        this.meaning = meaning;
    }

    public String getText() {
        return text;
    }

    public String getMeaning() {
        return meaning;
    }
}
