package com.example.notification.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "local_messages")
public class LocalMessage {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "school_name")
    private String schoolName;

    @ColumnInfo(name = "course")
    private String course;

    @ColumnInfo(name = "message")
    private String message;

    // Default constructor (Room requires this)
    public LocalMessage() {}

    // Custom constructor (optional)
    public LocalMessage(String schoolName, String course, String message) {
        this.schoolName = schoolName;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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
