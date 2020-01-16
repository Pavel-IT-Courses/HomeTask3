package com.gmail.pavkascool.hometask3;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDao {

    @Insert
    long insert(Locations loc);

    @Update
    int update(Locations loc);

    @Delete
    int delete(Locations loc);

    @Query("SELECT * FROM locations")
    List<Locations> getAll();

    @Query("SELECT * FROM locations WHERE location = :loc")
    List<Locations> get(String loc);
}

