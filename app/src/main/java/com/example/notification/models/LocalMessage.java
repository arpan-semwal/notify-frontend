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

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "message")
    private String message;

    // Default constructor (Room requires this)
    public LocalMessage() {}

    // Custom constructor (optional)
    public LocalMessage(String schoolUniqueId, String course, String message) {
        this.schoolUniqueId = schoolUniqueId;
        this.course = course;
        this.message = message;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
