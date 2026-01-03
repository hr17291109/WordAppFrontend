package com.example.wordapp.models;

public class Account {
    private String accountId;
    private String password;

    public Account(String aid, String pass) {
        accountId = aid;
        password = pass;
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

}
