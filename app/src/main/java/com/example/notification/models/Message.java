package com.example.notification.models;

public class Message {
    private Long id;
    private Long userId;
    private String content;
    private String schoolUniqueId;
    private String timestamp;

    public Message(Long id, Long userId, String content, String schoolUniqueId, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.schoolUniqueId = schoolUniqueId;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
    public String getSchoolUniqueId() { return schoolUniqueId; }
    public String getTimestamp() { return timestamp; }
}
