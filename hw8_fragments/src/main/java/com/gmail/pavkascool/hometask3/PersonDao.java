package com.gmail.pavkascool.hometask3;

import android.database.Cursor;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Query("SELECT * FROM person")
    Cursor getAllRecords();

    @Query("SELECT * FROM person WHERE ID = :id")
    Person getById(long id);

    @Query("DELETE FROM person")
    void deleteAll();

    @Insert
    long insert(Person person);

    @Update
    int update(Person person);

    @Delete
    int delete(Person person);
}
