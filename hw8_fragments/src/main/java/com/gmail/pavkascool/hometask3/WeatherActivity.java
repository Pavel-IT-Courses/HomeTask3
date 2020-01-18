package com.gmail.pavkascool.hometask3;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    private FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    public final static int CELSIUS = 0;
    public final static int FAHRENHEIT = 1;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private int scale;
    private List<String> places;
    private List<Weather> weathers;
    private String location;

    public String getLocation() {
        return location;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weathers = (List<Weather>)getLastCustomNonConfigurationInstance();
        if(weathers == null) weathers = new ArrayList<>();
        places = new ArrayList<>();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Locations> locs = db.locationDao().getAll();
                if (locs != null && !locs.isEmpty()) {
                    for (Locations lc : locs) {
                        places.add(lc.getLocation());
                    }
                }
            }
        });
        t.start();

        if(savedInstanceState == null) {
            fragment = FragmentScale.getInstance();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, fragment, fragment.getClass().getName());
            fragmentTransaction.commit();
        }
        else {
            scale = savedInstanceState.getInt("scale");
        }
    }

    public List<String> getPlaces() {
        return places;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void goToForecast(String loc) {
        location = loc;
        fragment = new FragmentWeatherForecast();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }


    public void goToLocations() {

        fragment = new FragmentWeatherLocations();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        System.out.println("Retaining Custom Non Config Instance, weathers = " + weathers);
        return weathers;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scale", scale);
    }

    public void saveLocation(String loc) {
        for(String s: places) {
            if(s.equals(loc)) {
                Toast.makeText(this, "The Location is already in the list", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        final Locations newLocation = new Locations();
        newLocation.setLocation(loc);
        final LocationDao dao = db.locationDao();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insert(newLocation);
            }
        });
        t.start();
        places.add(loc);
    }

}
