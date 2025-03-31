package com.example.notification.models;

import java.util.List;

public class AdminRegister {
    private String schoolName;
    private String city;
    private String address;
    private String mobileNumber;
    private String email;
    private String password;
    private String institutionType;  // ✅ Added missing field
    private List<AdminCourse> courses;
    private String institutionId;

    // ✅ Updated Constructor
    public AdminRegister(String schoolName, String city, String address, String mobileNumber,
                         String email, String password, String institutionType, List<AdminCourse> courses) {
        this.schoolName = schoolName;
        this.city = city;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.institutionType = institutionType;  // ✅ Assigning value
        this.courses = courses;
    }

    public String getInstitutionId() {
        return institutionId;
    }
}
