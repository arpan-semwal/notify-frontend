package com.example.notification.models;

public class StudentRegister {
    private String name;
    private String fatherName;
    private String mobileNumber;
    private String course;
    private String schoolUniqueId;

    // Constructor for registration
    public StudentRegister(String name, String fatherName, String course, String mobileNumber, String schoolUniqueId) {
        this.name = name;
        this.fatherName = fatherName;
        this.course = course;
        this.mobileNumber = mobileNumber;
        this.schoolUniqueId = schoolUniqueId;
    }

    // Getters and setters (if needed)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSchoolUniqueId() {
        return schoolUniqueId;
    }

    public void setSchoolUniqueId(String schoolUniqueId) {
        this.schoolUniqueId = schoolUniqueId;
    }
}
