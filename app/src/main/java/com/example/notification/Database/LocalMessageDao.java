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

    @Query("SELECT * FROM local_messages WHERE school_name = :schoolName AND course = :course")
    List<LocalMessage> getMessages(String schoolName, String course);
}
