package com.example.notification.dto.Message;

public class MessageRequest {
    private String schoolUniqueId;
    private String courseUniqueId; // ✅ Changed to courseUniqueId
    private String message;

    public MessageRequest() {}

    public MessageRequest(String schoolUniqueId, String courseUniqueId, String message) {
        this.schoolUniqueId = schoolUniqueId;
        this.courseUniqueId = courseUniqueId;
        this.message = message;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
