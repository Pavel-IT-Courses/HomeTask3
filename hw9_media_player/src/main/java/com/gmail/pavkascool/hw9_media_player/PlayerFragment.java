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

public class PlayerFragment extends Fragment implements View.OnClickListener {

    private TextView info;
    private ImageButton prev, play, next, cancel;
    private MainActivity activity;
    private boolean isPlayed;
    private String title;

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
        if(savedInstanceState != null) {
            isPlayed = savedInstanceState.getBoolean("isPlayed");
            title = savedInstanceState.getString("title");
        }
        else title = activity.getHeader();
        info.setText(title);
        setPlayIcon();
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isPlayed", isPlayed);
        outState.putString("title", title);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.stop:
                FragmentTransaction fragmentTransaction = activity.getManager().beginTransaction();
                fragmentTransaction.remove(this);
                fragmentTransaction.commit();
                break;
            case R.id.play:
                isPlayed = !isPlayed;
                setPlayIcon();
                Intent intent = new Intent(activity, MyService.class);
                intent.putExtra("trackNo", activity.getTrackId());
                activity.startService(intent);
                break;
        }
    }

    public void setSong() {
        title = activity.getHeader();
        info.setText(title);
    }

    private void setPlayIcon() {
        if(isPlayed) play.setImageResource(android.R.drawable.ic_media_pause);
        else play.setImageResource(android.R.drawable.ic_media_play);
    }

}
