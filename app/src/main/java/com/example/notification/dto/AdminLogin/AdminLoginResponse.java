package com.example.notification.dto.AdminLogin;

public class AdminLoginResponse {
    private boolean success;
    private String message;
    private String schoolName;
    private String schoolUniqueId;
    private String email;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSchoolUniqueId() {
        return schoolUniqueId;
    }

    public String getEmail() {
        return email;
    }
}

