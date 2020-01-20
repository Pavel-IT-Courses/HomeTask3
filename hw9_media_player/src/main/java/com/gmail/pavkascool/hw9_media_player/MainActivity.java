package com.gmail.pavkascool.hw9_media_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Song> songs;
    private RecyclerView recyclerView;


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
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        int trackId = songs.get(pos).getId();
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("trackId", trackId);
        intent.putExtra("trackNo", pos);
        startActivity(intent);
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
