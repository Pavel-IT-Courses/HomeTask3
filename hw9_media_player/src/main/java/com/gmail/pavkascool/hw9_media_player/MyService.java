package com.gmail.pavkascool.hw9_media_player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media.app.NotificationCompat.MediaStyle;

import java.util.List;

import androidx.core.app.NotificationCompat;

import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_NEXT;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PREV;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_STOP;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_STOP;

public class MyService extends Service implements MediaPlayer.OnCompletionListener {

    private List<Song> songs;
    private MediaPlayer mediaPlayer;
    private boolean paused;
    private int track;
    private Notification notification;
    private PendingIntent piPlay, piPause, piOpenActivity, piStop;
    private NotificationCompat.Builder builder;


    @Override
    public void onCreate() {
        super.onCreate();
        songs = MusicApp.getInstance().getTracks();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        switch(action) {
            case ACTION_STOP:
                MusicApp.getInstance().setStatus(STATUS_STOP);
                paused = false;
                Intent stopIntent = new Intent(ACTION_STOP);
                LocalBroadcastManager.getInstance(this).sendBroadcast(stopIntent);
                stopSelf();
            case ACTION_PAUSE:
                MusicApp.getInstance().setStatus(STATUS_PAUSE);
                track = MusicApp.getInstance().getNumber();
                mediaPlayer.pause();
                paused = true;
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_PAUSE));

                Intent playIntent = new Intent(this, MyService.class);
                playIntent.setAction(ACTION_PLAY);
                piPlay = PendingIntent.getService(this, 0, playIntent, 0);

                initBuilderPlay();

                notification = builder.build();
                startForeground(1, notification);

                break;
            case ACTION_PLAY:
                if(paused) {
                    mediaPlayer.start();
                    MusicApp.getInstance().setStatus(STATUS_PLAY);
                    Intent play = new Intent(ACTION_PLAY);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(play);
                }
                else {
                    track = MusicApp.getInstance().getNumber();
                    releasePlayer();
                    initPlayer();

                    Intent openActivity = new Intent(this, MainActivity.class);
                    piOpenActivity = PendingIntent.getActivity(this, 0, openActivity, 0);

                    Intent pause = new Intent(this, MyService.class);
                    pause.setAction(ACTION_PAUSE);
                    piPause = PendingIntent.getService(this, 0, pause, 0);

                    Intent stop = new Intent(this, MyService.class);
                    stop.setAction(ACTION_STOP);
                    piStop = PendingIntent.getService(this, 0, stop, 0);}

                initBuilderPause();

                notification = builder.build();
                startForeground(1, notification);

                paused = false;
                break;
            case ACTION_NEXT:
                paused = false;
                track++;
                releasePlayer();
                initPlayer();

                initBuilderPause();

                notification = builder.build();
                startForeground(1, notification);
                break;
            case ACTION_PREV:
                paused = false;
                track--;
                releasePlayer();
                initPlayer();

                initBuilderPause();

                notification = builder.build();
                startForeground(1, notification);
                break;

        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        paused = false;
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_STOP));
        stopSelf();
    }

    private void initPlayer() {
        mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(MusicApp.getInstance().getNumber()).getId());
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
    }

    private void initBuilderPause() {
        builder = new NotificationCompat.Builder(this, "MyChannel")
                .setSmallIcon(R.drawable.music)
                .setContentTitle("Ozzy Player")
                .setContentText(songs.get(track).getName())
                .setContentIntent(piOpenActivity)
                .setStyle(new MediaStyle())
                .addAction(android.R.drawable.ic_media_pause, "Pause", piPause)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", piStop);

    }
    private void initBuilderPlay() {
        builder = new NotificationCompat.Builder(this, "MyChannel")
                .setSmallIcon(R.drawable.music)
                .setContentTitle("Ozzy Player")
                .setContentText(songs.get(track).getName())
                .setContentIntent(piOpenActivity)
                .setStyle(new MediaStyle())
                .addAction(android.R.drawable.ic_media_play, "Play", piPlay)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", piStop);

    }
}
