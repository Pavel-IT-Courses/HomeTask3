package com.gmail.pavkascool.c15_google_map.repo.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhotoEntity photoEntity);

    @Query("SELECT * FROM photo_entity")
    List<PhotoEntity> getAll();

    @Query("SELECT * FROM photo_entity WHERE id=:id")
    PhotoEntity getEntity(long id);


}
