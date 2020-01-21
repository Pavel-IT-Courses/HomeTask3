package com.gmail.pavkascool.hw9_media_player;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.List;

public class PlayerService extends IntentService {

    private static final String ACTION_FOO = "com.gmail.pavkascool.hw9_media_player.action.FOO";
    private static final String ACTION_BAZ = "com.gmail.pavkascool.hw9_media_player.action.BAZ";


    private static final String EXTRA_PARAM1 = "com.gmail.pavkascool.hw9_media_player.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.gmail.pavkascool.hw9_media_player.extra.PARAM2";

    private List<Song> songs;
    private MediaPlayer mediaPlayer;

    public PlayerService() {
        super("PlayerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songs = MusicApp.getInstance().getTracks();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int trackNo = intent.getIntExtra("trackNo", -1);
            if (trackNo != -1) {
                System.out.println("Track No is " + trackNo);
                Song song = songs.get(trackNo);
                releasePlayer();
                mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), song.getId());
                System.out.println("Media Player = " + mediaPlayer);
                mediaPlayer.start();
                System.out.println("Media Player Started");

            }
        }
    }

    private void releasePlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        releasePlayer();
//        System.out.println("MEDIA PLAYER DESTROYED");
//    }

}
