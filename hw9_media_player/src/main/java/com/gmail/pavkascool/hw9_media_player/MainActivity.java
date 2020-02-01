package com.gmail.pavkascool.hw9_media_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_NEXT;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PAUSE;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_PREV;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.ACTION_STOP;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PLAY;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_PREP;
import static com.gmail.pavkascool.hw9_media_player.MusicApp.STATUS_STOP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Song> songs;
    private RecyclerView recyclerView;
    private PlayerFragment fragment;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private BroadcastReceiver br;

    public FragmentManager getManager() {
        return fragmentManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songs = MusicApp.getInstance().getTracks();
        recyclerView = findViewById(R.id.rec);
        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(this, cols);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MusicAdapter());
        fragment = PlayerFragment.newInstance();

        br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() == ACTION_STOP) MusicApp.getInstance().setStatus(STATUS_STOP);
                MainActivity.this.update();
                PlayerFragment pf = ((PlayerFragment)MainActivity.this.fragmentManager.findFragmentById(R.id.frame));
                if(pf != null) pf.update();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PREV);
        filter.addAction(ACTION_STOP);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, filter);
        update();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);

    }

    @Override
    public void onClick(View v) {
        MusicApp instance = MusicApp.getInstance();
        int track = recyclerView.getChildLayoutPosition(v);
        instance.setNumber(track);
        if(instance.getStatus() == STATUS_PLAY) stopService(new Intent(this, MyService.class));
        instance.setStatus(STATUS_PREP);
        update();
        ((PlayerFragment)(fragmentManager.findFragmentById(R.id.frame))).update();
    }


    public void update() {
        int status = MusicApp.getInstance().getStatus();
        if (status == STATUS_STOP) {
            removeFragment();
        } else {
            installFragment();
        }
    }

    public void launchService(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else startService(intent);
    }

    private void installFragment() {

        if (fragmentManager.findFragmentById(R.id.frame) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame, fragment);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }

    private void removeFragment() {
        PlayerFragment pFragment = (PlayerFragment)fragmentManager.findFragmentById(R.id.frame);
        if(pFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(pFragment);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }

    private class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            v.setOnClickListener(MainActivity.this);
            return new TrackHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Song song = songs.get(position);
            TrackHolder trackHolder = (TrackHolder)holder;
            trackHolder.artist.setText(song.getArtist());
            trackHolder.song.setText(song.getName());

        }

        @Override
        public int getItemCount() {
            return songs.size();
        }
    }

    private class TrackHolder extends RecyclerView.ViewHolder {
        TextView artist;
        TextView song;


        public TrackHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.artist);
            song = itemView.findViewById(R.id.song);
        }
    }
}
