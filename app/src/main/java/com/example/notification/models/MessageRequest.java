package com.example.notification.models;

public class MessageRequest {

    private String schoolUniqueId;  // ✅ Changed from schoolName to schoolUniqueId
    private String course;
    private String message;

    public MessageRequest(String schoolUniqueId, String course, String message) {
        this.schoolUniqueId = schoolUniqueId;
        this.course = course;
        this.message = message;
    }

    public String getSchoolUniqueId() {
        return schoolUniqueId;
    }

    public void setSchoolUniqueId(String schoolUniqueId) {
        this.schoolUniqueId = schoolUniqueId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
