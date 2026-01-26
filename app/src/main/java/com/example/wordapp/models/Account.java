package com.example.wordapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public class Account {
    private String accountId;
    @JsonIgnore
    private String password;
    private HashMap<String, String> wordDic = new HashMap<String, String>();

    public Account () {
    }
    public Account(String aid, HashMap<String, String> words) {
        accountId = aid;
        wordDic = words;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPassword(String p) {
        password = p;
    }

    public String getPassword() {
        return password;
    }

    public HashMap<String, String> getWords() {
        return wordDic;
    }

    public void setWords(HashMap<String, String> words) {
        wordDic = words;
    }

}
