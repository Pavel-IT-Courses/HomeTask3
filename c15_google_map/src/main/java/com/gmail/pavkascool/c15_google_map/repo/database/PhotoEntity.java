package com.gmail.pavkascool.c15_google_map.repo.database;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(tableName = "photo_entity")
@TypeConverters(Converters.class)

public class PhotoEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private LatLng position;
    private File photoFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }
}
