package com.gmail.pavkascool.hw9_media_player;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MusicApp extends Application {

    public static final String OZZY = "Ozzy Osbourn";
    public static final String ACTION_PLAY = "com.pavkascool.play";
    public static final String ACTION_PAUSE = "com.pavkascool.pause";
    public static final String ACTION_NEXT = "com.pavkascool.next";
    public static final String ACTION_PREV = "com.pavkascool.prev";
    public static final String ACTION_STOP = "com.pavkascool.stop";
    public static final int STATUS_STOP = 0;
    public static final int STATUS_PREP = 1;
    public static final int STATUS_PAUSE = 2;
    public static final int STATUS_PLAY = 3;

    private int status = STATUS_STOP;
    private int number = -1;

    private static MusicApp instance;
    private List<Song> tracks;

    public static MusicApp getInstance() {
        return instance;
    }
    public List<Song> getTracks() {
        return tracks;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("THE FULL APPLICATION STARTS");
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
