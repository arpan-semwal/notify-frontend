package com.example.notification.models;

public class MessageRequest {
    private String schoolName;  // ✅ Add this field
    private String course;
    private String message;

    // ✅ Constructor
    public MessageRequest(String schoolName, String course, String message) {
        this.schoolName = schoolName;
        this.course = course;
        this.message = message;
    }

    // ✅ Getters
    public String getSchoolName() {
        return schoolName;
    }

    public String getCourse() {
        return course;
    }

    public String getMessage() {
        return message;
    }

    // ✅ Setters
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
