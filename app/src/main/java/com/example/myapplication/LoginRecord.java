package com.example.myapplication;

public class LoginRecord {
    private double latitude;
    private double longitude;
    private String loginTime;

    public LoginRecord() {
        // Default constructor required for Firebase
    }

    public LoginRecord(double latitude, double longitude, String loginTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.loginTime = loginTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLoginTime() {
        return loginTime;
    }
}