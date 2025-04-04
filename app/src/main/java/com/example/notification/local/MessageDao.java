package com.example.notification.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(LocalMessageEntity message);

    @Query("SELECT * FROM local_message WHERE schoolName = :schoolName AND course = :course")
    List<LocalMessageEntity> getMessages(String schoolName, String course);
}
