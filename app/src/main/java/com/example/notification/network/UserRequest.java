package com.example.notification.network;

public class UserRequest {
    private final String schoolName;
    private final String registrationNumber;
    private final String mobileNumber;

    public UserRequest(String schoolName, String registrationNumber, String mobileNumber) {
        this.schoolName = schoolName;
        this.registrationNumber = registrationNumber;
        this.mobileNumber = mobileNumber;
    }

    public String getSchoolName() { return schoolName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getMobileNumber() { return mobileNumber; }
}
