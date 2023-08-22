package com.example.myapplication;

public class User {
    private String email;
    private String uid;
    private String loginTime;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String email, String uid, String loginTime) {
        this.email = email;
        this.uid = uid;
        this.loginTime = loginTime;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
