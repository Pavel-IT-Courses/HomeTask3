package com.gmail.pavkascool.hometask3;

import android.app.Application;

import androidx.room.Room;

public class FragmentApplication extends Application {

    private static FragmentApplication instance;
    private static FragmentDatabase database;

    public void onCreate() {
        super.onCreate();
        System.out.println("Creating the Application...");
        instance = this;
        database = Room.databaseBuilder(this, FragmentDatabase.class, "fragment_database")
                .build();
    }

    public static FragmentApplication getInstance() {
        return instance;
    }

    public static FragmentDatabase getDatabase() {
        return database;
    }

}
