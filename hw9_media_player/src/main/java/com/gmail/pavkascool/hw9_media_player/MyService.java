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

import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_NEXT;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PREV;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_STOP;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_STOP;

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
//        int trackNo = intent.getIntExtra("trackNo", -1);
//        if(trackNo == -1 || action == "com.gmail.pavkascool.pause") {
//            mediaPlayer.pause();
//            paused = true;
//        }
//        else {
//            if(paused) {
//                mediaPlayer.start();
//            }
//            else {
//                releasePlayer();
//                mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(trackNo).getId());
//                mediaPlayer.start();
//                Intent openActivity = new Intent(this, MainActivity.class);
//                openActivity.putExtra("trackNo", trackNo);
//                PendingIntent piOpenActivity = PendingIntent.getActivity(this,0, openActivity, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                Intent pause = new Intent("com.gmail.pavkascool.pause");
//                PendingIntent piPause = PendingIntent.getService(this, 0, pause, 0);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannel")
//                        .setSmallIcon(R.drawable.music)
//                        .setContentTitle("Ozzy Player")
//                        .setContentText(songs.get(trackNo).getName())
//                        .setStyle(new MediaStyle())
//                        .setContentIntent(piOpenActivity)
//                        .addAction(android.R.drawable.ic_media_pause, "Pause", piPause);
//
//                Notification notification = builder.build();
//                startForeground(1, notification);
//            }
//            paused = false;
//        }

        switch(action) {
            case ACTION_STOP:
                System.out.println("STOP ACTION RECEIVED");
                MusicApp.getInstance().setStatus(STATUS_STOP);
                paused = false;
                System.out.println("INSIDE SERVICE STATUS = " + MusicApp.getInstance().getStatus());
                Intent stopIntent = new Intent(ACTION_STOP);
                sendBroadcast(stopIntent);
                stopSelf();
            case ACTION_PAUSE:
                MusicApp.getInstance().setStatus(STATUS_PAUSE);
                mediaPlayer.pause();
                paused = true;
                Intent pauseIntent = new Intent(ACTION_PAUSE);
                sendBroadcast(pauseIntent);

                Intent playIntent = new Intent(this, MyService.class);
                playIntent.setAction(ACTION_PLAY);
                PendingIntent piPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

                break;
            case ACTION_PLAY:
                if(paused) {
                    mediaPlayer.start();
                }
                else {
                    int track = MusicApp.getInstance().getNumber();
                    releasePlayer();
                    mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(track).getId());
                    mediaPlayer.start();

                    Intent openActivity = new Intent(this, MainActivity.class);
                    PendingIntent piOpenActivity = PendingIntent.getActivity(this, 0, openActivity, 0);

                    Intent pause = new Intent(this, MyService.class);
                    pause.setAction(ACTION_PAUSE);
                    PendingIntent piPause = PendingIntent.getService(this, 0, pause, 0);

                    Intent stop = new Intent(this, MyService.class);
                    stop.setAction(ACTION_STOP);
                    PendingIntent piStop = PendingIntent.getService(this, 0, stop, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannel")
                        .setSmallIcon(R.drawable.music)
                        .setContentTitle("Ozzy Player")
                        .setContentText(songs.get(track).getName())
                        .setContentIntent(piOpenActivity)
                        .setStyle(new MediaStyle())
                        .addAction(android.R.drawable.ic_media_pause, "Pause", piPause)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", piStop);


                Notification notification = builder.build();
                startForeground(1, notification);
                }
                paused = false;
                break;
            case ACTION_NEXT:
                paused = false;
                releasePlayer();
                mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(MusicApp.getInstance().getNumber()).getId());
                mediaPlayer.start();
                break;
            case ACTION_PREV:
                paused = false;
                releasePlayer();
                mediaPlayer = MediaPlayer.create(MusicApp.getInstance(), songs.get(MusicApp.getInstance().getNumber()).getId());
                mediaPlayer.start();
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
}
