package com.example.notification.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "local_message")
public class LocalMessageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String schoolName;
    private String course;
    private String content;

    // Constructor
    public LocalMessageEntity(String schoolName, String course, String content) {
        this.schoolName = schoolName;
        this.course = course;
        this.content = content;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
