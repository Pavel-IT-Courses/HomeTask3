package com.gmail.pavkascool.c15_google_map.repo.database;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoEntity.class}, version = 1, exportSchema = false)
public abstract class PhotoDataBase extends RoomDatabase {

    public static volatile PhotoDataBase instance;

    public static PhotoDataBase getInstance(final Application application) {
        if(instance == null) {
            synchronized (PhotoDataBase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(application.getApplicationContext(), PhotoDataBase.class, "db_photo.db").build();
                }
            }
        }
        return instance;
    }


    public abstract PhotoDao photoDao();
}
