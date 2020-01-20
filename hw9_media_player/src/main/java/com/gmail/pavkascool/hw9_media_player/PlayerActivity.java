package com.gmail.pavkascool.hw9_media_player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button close, play;
    private TextView track;
    private MediaPlayer mediaPlayer;
    private int trackNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        track = findViewById(R.id.track);
        close = findViewById(R.id.close);
        close.setOnClickListener(this);
        play = findViewById(R.id.play);
        play.setOnClickListener(this);
        Intent intent = getIntent();
        trackNo = intent.getIntExtra("trackNo", -1);
        if(trackNo == -1) finish();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.close:
                releasePlayer();
                finish();
                break;
            case R.id.play:
                Song song = MusicApp.getInstance().getTracks().get(trackNo);
                String info = song.getArtist() + ": " + song.getName();
                track.setText(info);
                releasePlayer();
                mediaPlayer = MediaPlayer.create(this, song.getId());
                mediaPlayer.start();
        }

    }

    private void releasePlayer() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
