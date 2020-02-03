package com.gmail.pavkascool.c15_google_map.repo.database;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String latLngToString(LatLng position) {
        return position.latitude + ";" + position.longitude;
    }

    @TypeConverter
    public static LatLng stringToLatLng(String position) {
        String[] positionTextArray = position.split(";");
        return new LatLng(Double.valueOf(positionTextArray[0]), Double.valueOf(positionTextArray[1]));
    }

    @TypeConverter
    public static String fileToString(File file) {
        return file.getAbsolutePath();
    }

    @TypeConverter
    public static File stringToFile(String path) {
        return new File(path);
    }
}
