package com.example.notification.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "local_messages")
public class LocalMessage {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "school_unique_id")
    private String schoolUniqueId;

    @ColumnInfo(name = "course_unique_id")  // Updated field name
    private String courseUniqueId;

    @ColumnInfo(name = "message")
    private String message;

    public LocalMessage(String schoolUniqueId, String courseUniqueId, String message) {
        this.schoolUniqueId = schoolUniqueId;
        this.courseUniqueId = courseUniqueId;  // Updated constructor
        this.message = message;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSchoolUniqueId() { return schoolUniqueId; }
    public void setSchoolUniqueId(String schoolUniqueId) { this.schoolUniqueId = schoolUniqueId; }

    public String getCourseUniqueId() { return courseUniqueId; }  // Updated getter
    public void setCourseUniqueId(String courseUniqueId) { this.courseUniqueId = courseUniqueId; }  // Updated setter

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
