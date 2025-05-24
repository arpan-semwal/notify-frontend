package com.example.notification.models;

public class GlobalMessage {
    private String schoolUniqueId;
    private String courseUniqueId;
    private String content;  // backend me 'content' field hai (message ka text)
    private String timestamp; // ya Date/long, jo backend bheje

    // Constructor
    public GlobalMessage(String schoolUniqueId, String courseUniqueId, String content, String timestamp) {
        this.schoolUniqueId = schoolUniqueId;
        this.courseUniqueId = courseUniqueId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Empty constructor (for Retrofit/Gson)
    public GlobalMessage() {}

    // Getters and Setters
    public String getSchoolUniqueId() {
        return schoolUniqueId;
    }

    public void setSchoolUniqueId(String schoolUniqueId) {
        this.schoolUniqueId = schoolUniqueId;
    }

    public String getCourseUniqueId() {
        return courseUniqueId;
    }

    public void setCourseUniqueId(String courseUniqueId) {
        this.courseUniqueId = courseUniqueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

