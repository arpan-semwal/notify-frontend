package com.example.notification.models;

public class AdminLoginRequest {
    private String schoolName;
    private String password;

    public AdminLoginRequest(String schoolName, String password) {
        this.schoolName = schoolName;
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getPassword() {
        return password;
    }
}
