package com.example.notification.network;

import java.util.List;

public class AdminRegister {
    private String schoolName;
    private String city;
    private String address;
    private String mobileNumber;
    private String email;
    private String password;
    private List<AdminCourse> courses;
    private String institutionId;  // ✅ New field

    // Constructor
    public AdminRegister(String schoolName, String city, String address, String mobileNumber, String email, String password, List<AdminCourse> courses) {
        this.schoolName = schoolName;
        this.city = city;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.courses = courses;
    }

    // Getter for institutionId
    public String getInstitutionId() {
        return institutionId;
    }
}
