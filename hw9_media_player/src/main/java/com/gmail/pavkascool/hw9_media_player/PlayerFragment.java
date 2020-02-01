package com.gmail.pavkascool.hw9_media_player;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_NEXT;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PREV;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_STOP;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_STOP;

public class PlayerFragment extends Fragment implements View.OnClickListener {

        private TextView info;
        private ImageButton prev, play, next, cancel;
        private MainActivity activity;
        private BroadcastReceiver br;
        private LocalBroadcastManager lbm;


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

        lbm = LocalBroadcastManager.getInstance(getContext());

        br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                update();
                if(intent.getAction() == ACTION_STOP) {
                    MusicApp.getInstance().setStatus(STATUS_STOP);
                    activity.removeFragment();
                }

            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PREV);
        filter.addAction(ACTION_STOP);
        lbm.registerReceiver(br, filter);
        update();

        return v;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MyService.class);
        MusicApp instance = MusicApp.getInstance();
        switch(v.getId()) {
            case R.id.stop:
                FragmentTransaction fragmentTransaction = activity.getManager().beginTransaction();
                fragmentTransaction.remove(this);
                fragmentTransaction.commit();
                getContext().stopService(new Intent(getContext(), MyService.class));
                instance.setStatus(STATUS_STOP);
                break;
            case R.id.play:

                int status = instance.getStatus();
                if(status == STATUS_PLAY) {
                    instance.setStatus(STATUS_PAUSE);
                    update();
                    intent.setAction(ACTION_PAUSE);
                    launchService(intent);
                }
                else {
                    intent.setAction(ACTION_PLAY);
                    launchService(intent);
                    instance.setStatus(STATUS_PLAY);
                    update();
                }
                break;
            case R.id.next:

                if(instance.getNumber() + 1 < instance.getTracks().size()) {
                    instance.setNumber(instance.getNumber() + 1);
                    intent.setAction(ACTION_NEXT);
                    launchService(intent);
                    instance.setStatus(STATUS_PLAY);
                    update();
                }
                break;
            case R.id.prev:

                if(instance.getNumber() > 0) {
                    instance.setNumber(instance.getNumber() - 1);
                    intent.setAction(ACTION_PREV);
                    launchService(intent);
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

    public void launchService(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().startForegroundService(intent);
        }
        else getContext().startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(br);
    }
}
