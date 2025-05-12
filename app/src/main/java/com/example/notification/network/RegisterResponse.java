package com.example.notification.network;

import com.example.notification.models.AdminCourse;
import java.util.List;

public class RegisterResponse {
    private String message;
    private String uniqueId;
    private String institutionType;
    private List<AdminCourse> courseList; // ✅ Add this line

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getInstitutionType() {
        return institutionType;
    }

    // ✅ Add this getter
    public List<AdminCourse> getCourseList() {
        return courseList;
    }
}
