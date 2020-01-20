package com.gmail.pavkascool.hw9_media_player;

import android.os.Parcelable;

public class Song {

    private String artist;
    private String name;
    private int id;

    public Song(String artist, String name, int id) {
        this.artist = artist;
        this.name = name;
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
