package com.gmail.pavkascool.hometask3;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Locations {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
