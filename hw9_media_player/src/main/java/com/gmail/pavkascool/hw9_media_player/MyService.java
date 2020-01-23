package com.gmail.pavkascool.hw9_media_player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.media.app.NotificationCompat.MediaStyle;

import java.util.List;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private List<Song> songs;
    private MediaPlayer mediaPlayer;
    private boolean paused;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songs = MusicApp.getInstance().getTracks();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        int trackNo = intent.getIntExtra("trackNo", -1);
        if(trackNo == -1 || action == "com.gmail.pavkascool.pause") {
            mediaPlayer.pause();
            paused = true;
        }
        else {
            if(paused) {
                mediaPlayer.start();
            }
            else {
                releasePlayer();
                mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(trackNo).getId());
                mediaPlayer.start();
                Intent openActivity = new Intent(this, MainActivity.class);
                openActivity.putExtra("trackNo", trackNo);
                PendingIntent piOpenActivity = PendingIntent.getActivity(this,0, openActivity, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent pause = new Intent("com.gmail.pavkascool.pause");
                PendingIntent piPause = PendingIntent.getService(this, 0, pause, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannel")
                        .setSmallIcon(R.drawable.music)
                        .setContentTitle("Ozzy Player")
                        .setContentText(songs.get(trackNo).getName())
                        .setStyle(new MediaStyle())
                        .setContentIntent(piOpenActivity)
                        .addAction(android.R.drawable.ic_media_pause, "Pause", piPause);

                Notification notification = builder.build();
                startForeground(1, notification);
            }
            paused = false;
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
}
