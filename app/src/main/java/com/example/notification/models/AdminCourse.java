package com.example.notification.models;

public class AdminCourse {
    private String courseName;
    private String courseUniqueId;

    // Constructor for sending courseName only (backend generates courseUniqueId)
    public AdminCourse(String courseName) {
        this.courseName = courseName;
    }

    // Constructor when both fields are available (e.g., from backend)
    public AdminCourse(String courseName, String courseUniqueId) {
        this.courseName = courseName;
        this.courseUniqueId = courseUniqueId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseUniqueId() {
        return courseUniqueId;
    }

    public void setCourseUniqueId(String courseUniqueId) {
        this.courseUniqueId = courseUniqueId;
    }

    @Override
    public String toString() {
        return courseName;
    }
}
