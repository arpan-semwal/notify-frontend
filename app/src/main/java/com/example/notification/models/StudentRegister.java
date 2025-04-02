package com.example.notification.models;




public class StudentRegister {
    private String name;
    private String fatherName;
    private String schoolName;
    
    private String course;
    private String mobileNumber;

    // Constructor
    public StudentRegister(String name, String fatherName, String schoolName,  String course , String mobileNumber) {
        this.name = name;
        this.fatherName = fatherName;
        this.schoolName = schoolName;
        this.course = course;
        this.mobileNumber = mobileNumber;
        
    }

    // Getters
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public String getSchoolName() { return schoolName; }
    public String getCourse(){return course;}
    public String getMobileNumber() { return mobileNumber; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public void setCourse(String course){this.course = course;}
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
