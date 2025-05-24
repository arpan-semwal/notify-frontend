package com.example.notification.dto.Message;

public class MessageResponse {
    private String schoolUniqueId;
    private String courseUniqueId;
    private String content;

    public String getSchoolUniqueId() {
        return schoolUniqueId;
    }

    public String getCourseUniqueId() {
        return courseUniqueId;
    }

    public String getContent() {
        return content;
    }

    public void setSchoolUniqueId(String schoolUniqueId) {
        this.schoolUniqueId = schoolUniqueId;
    }

    public void setCourseUniqueId(String courseUniqueId) {
        this.courseUniqueId = courseUniqueId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
