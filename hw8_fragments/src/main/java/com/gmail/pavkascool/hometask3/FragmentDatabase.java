package com.gmail.pavkascool.hometask3;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={Locations.class, Person.class}, version=1)
public abstract class FragmentDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();
    public abstract PersonDao personDao();
}
