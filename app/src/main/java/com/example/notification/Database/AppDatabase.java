package com.example.notification.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.room.migration.Migration;

import com.example.notification.models.LocalMessage;

@Database(entities = {LocalMessage.class}, version = 2)  // Incremented version number to 2
public abstract class AppDatabase extends RoomDatabase {

    public abstract LocalMessageDao localMessageDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "notify_local")
                            .addMigrations(MIGRATION_1_2)  // Add migration if necessary
                            .fallbackToDestructiveMigration()  // Optionally, delete data on schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Migration from version 1 to version 2 (optional example, modify according to your needs)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Example migration: adding a new column if you changed the schema (modify based on actual changes)
            // Example: database.execSQL("ALTER TABLE local_messages ADD COLUMN new_column TEXT");
        }
    };
}
