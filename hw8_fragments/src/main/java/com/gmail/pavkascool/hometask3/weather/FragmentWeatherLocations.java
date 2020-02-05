package com.gmail.pavkascool.hometask3.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.LocationDao;
import com.gmail.pavkascool.hometask3.Locations;
import com.gmail.pavkascool.hometask3.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentWeatherLocations extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button add;
    private LocAdapter locAdapter;
    private List<String> locations;
    private static FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    private FragmentMediator fragmentMediator;

    public static FragmentWeatherLocations newInstance() {
        FragmentWeatherLocations fragment = new FragmentWeatherLocations();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weather_locations, container, false);
        add = v.findViewById(R.id.add);
        add.setOnClickListener(this);
        locations = new ArrayList<>();
        recyclerView = v.findViewById(R.id.rec);
        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(getContext(), cols);
        recyclerView.setLayoutManager(manager);
        locAdapter = new LocAdapter();
        recyclerView.setAdapter(locAdapter);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Locations> locs = db.locationDao().getAll();
                if (locs != null && !locs.isEmpty()) {
                    for (Locations lc : locs) {
                        locations.add(lc.getLocation());
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        t.start();

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentMediator = (FragmentMediator)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentMediator = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {

            fragmentMediator.goToForecast(null);
        }
        else {
            int pos = recyclerView.getChildLayoutPosition((LinearLayout)(v.getParent()));
            if(v instanceof Button) {
                final String s = locations.get(pos);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationDao dao = db.locationDao();
                        Locations location = dao.get(s).get(0);
                        dao.delete(location);
                    }
                });
                t.start();
                locations.remove(pos);
                locAdapter.notifyDataSetChanged();

            }
            else {
                String loc = ((TextView)v).getText().toString();
                fragmentMediator.goToForecast(loc);
            }
        }
    }

    private class LocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loc, parent, false);
            return new LocViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            LocViewHolder locViewHolder = (LocViewHolder) holder;
            String s = FragmentWeatherLocations.this.locations.get(position);
            locViewHolder.local.setText(s);
        }

        @Override
        public int getItemCount() {
            if (FragmentWeatherLocations.this.locations != null) {
                return FragmentWeatherLocations.this.locations.size();
            }
            return 0;
        }
    }

    private class LocViewHolder extends RecyclerView.ViewHolder {
        private TextView local;
        private Button remove;

        public LocViewHolder(@NonNull View itemView) {
            super(itemView);
            local = itemView.findViewById(R.id.local);
            remove = itemView.findViewById(R.id.remove);
            local.setOnClickListener(FragmentWeatherLocations.this);
            remove.setOnClickListener(FragmentWeatherLocations.this);

        }
    }

}