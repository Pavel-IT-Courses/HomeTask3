package com.gmail.pavkascool.hw9_media_player;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_NEXT;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PREV;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_STOP;

public class PlayerFragment extends Fragment implements View.OnClickListener {

        private TextView info;
        private ImageButton prev, play, next, cancel;
        private MainActivity activity;

        public PlayerFragment() {
        }


    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        info = v.findViewById(R.id.title);
        prev = v.findViewById(R.id.prev);
        prev.setOnClickListener(this);
        play = v.findViewById(R.id.play);
        play.setOnClickListener(this);
        next = v.findViewById(R.id.next);
        next.setOnClickListener(this);
        cancel = v.findViewById(R.id.stop);
        cancel.setOnClickListener(this);
        update();

        return v;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(activity, MyService.class);
        MusicApp instance = MusicApp.getInstance();
        switch(v.getId()) {
            case R.id.stop:
                FragmentTransaction fragmentTransaction = activity.getManager().beginTransaction();
                fragmentTransaction.remove(this);
                fragmentTransaction.commit();
                activity.stopService(new Intent(activity, MyService.class));
                instance.setStatus(STATUS_STOP);
                break;
            case R.id.play:

                int status = instance.getStatus();
                if(status == STATUS_PLAY) {
                    instance.setStatus(STATUS_PAUSE);
                    update();
                    activity.update();
                    intent.setAction(ACTION_PAUSE);
                    activity.launchService(intent);
                }
                else {
                    intent.setAction(ACTION_PLAY);
                    activity.launchService(intent);
                    instance.setStatus(STATUS_PLAY);
                    update();
                    activity.update();
                }
                break;
            case R.id.next:

                if(instance.getNumber() + 1 < instance.getTracks().size()) {
                    instance.setNumber(instance.getNumber() + 1);
                    intent.setAction(ACTION_NEXT);
                    activity.launchService(intent);
                    instance.setStatus(STATUS_PLAY);
                    update();
                }
                break;
            case R.id.prev:

                if(instance.getNumber() > 0) {
                    instance.setNumber(instance.getNumber() - 1);
                    intent.setAction(ACTION_PREV);
                    activity.launchService(intent);
                    instance.setStatus(STATUS_PLAY);
                    update();
                }
                break;

        }
    }


    public void update() {
        MusicApp instance = MusicApp.getInstance();
        int track = instance.getNumber();
        if (track != -1) {
            Song song = instance.getTracks().get(track);
            String title = song.getName() + ": " + song.getArtist();
            info.setText(title);
        }

        int status = instance.getStatus();
        if (status == STATUS_PLAY) play.setImageResource(android.R.drawable.ic_media_pause);
        else play.setImageResource(android.R.drawable.ic_media_play);
    }

}
