package com.example.notification.models;

public class Message {
    private Long id;
    private Long userId;  // Add this if missing
    private String content;
    private String schoolName;
    private String timestamp;

    public Long getId() { return id; }
    public Long getUserId() { return userId; } // Add getter
    public String getContent() { return content; }
    public String getSchoolName() { return schoolName; }
    public String getTimestamp() { return timestamp; }
}

