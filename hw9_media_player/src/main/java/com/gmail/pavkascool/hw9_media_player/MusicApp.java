package com.gmail.pavkascool.hw9_media_player;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MusicApp extends Application {

    public final String OZZY = "Ozzy Osbourn";
    private static MusicApp instance;
    private List<Song> tracks;

    public static MusicApp getInstance() {
        return instance;
    }
    public List<Song> getTracks() {
        return tracks;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        tracks = new ArrayList<>();
        Song song = new Song(OZZY, "Crazy Train", R.raw.crazy_train);
        tracks.add(song);
        song = new Song(OZZY, "Fire in the Sky", R.raw.fire_in_the_sky);
        tracks.add(song);
        song = new Song(OZZY, "Mr. Crowley", R.raw.mr_crowley);
        tracks.add(song);
        song = new Song(OZZY, "Perry Mason", R.raw.perry_mason);
        tracks.add(song);
    }
}
