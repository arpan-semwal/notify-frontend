package com.example.notification.models;

public class Message {
    private Long id;
    private Long userId;
    private String content;
    private String schoolName;
    private String timestamp;

    public Message(Long id, Long userId, String content, String schoolName, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.schoolName = schoolName;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; } // ✅ Correct method
    public String getSchoolName() { return schoolName; } // ✅ Correct method
    public String getTimestamp() { return timestamp; }
}
