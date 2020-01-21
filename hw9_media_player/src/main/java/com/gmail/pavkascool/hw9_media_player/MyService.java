package com.gmail.pavkascool.hw9_media_player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.List;

public class MyService extends Service {

    private List<Song> songs;
    private MediaPlayer mediaPlayer;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songs = MusicApp.getInstance().getTracks();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        releasePlayer();
        int trackNo = intent.getIntExtra("trackNo", -1);
        mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(trackNo).getId());
        mediaPlayer.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void releasePlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
