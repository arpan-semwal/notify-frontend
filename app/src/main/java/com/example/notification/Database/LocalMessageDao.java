package com.example.notification.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notification.models.LocalMessage;

import java.util.List;

@Dao
public interface LocalMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(LocalMessage message);

    @Query("SELECT * FROM local_messages WHERE school_unique_id = :schoolUniqueId AND course_unique_id = :courseUniqueId")
    List<LocalMessage> getMessages(String schoolUniqueId, String courseUniqueId);
}
