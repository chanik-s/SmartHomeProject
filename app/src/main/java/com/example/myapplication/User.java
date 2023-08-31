package com.example.myapplication;
import java.util.Map;

public class User {
    private String email;
    private String uid;

    public User() {
        // Default constructor required for Firebase
    }

    /*
    public User(String email, String uid, String loginTime) {
        this.email = email;
        this.uid = uid;
        this.loginTime = loginTime;
    }*/

    public User(String email,String uid){
        this.email=email;
        this.uid=uid;
    }
    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }


}
