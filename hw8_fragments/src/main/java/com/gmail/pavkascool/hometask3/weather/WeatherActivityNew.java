package com.gmail.pavkascool.hometask3.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.gmail.pavkascool.hometask3.R;

public class WeatherActivityNew extends AppCompatActivity implements View.OnClickListener, FragmentMediator {
    private Button go;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_new);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new FragmentScaleNew()).commit();

        }
        if(savedInstanceState == null || getFragmentManager().findFragmentById(R.id.frame_layout) instanceof FragmentScaleNew) {
            go = new Button(this);
            go.setText("Save Preferences");

            ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ll = findViewById(R.id.layout_for_button);
            go.setLayoutParams(lpView);
            ll.addView(go);
            go.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        ll.removeView(go);
        FragmentScaleNew fragmentScaleNew = (FragmentScaleNew)getFragmentManager().findFragmentById(R.id.frame_layout);
        getFragmentManager().beginTransaction()
                .remove(fragmentScaleNew).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, FragmentWeatherLocationsNew.newInstance()).commit();
    }

    @Override
    public void goToForecast(String location) {
        FragmentWeatherForecastNew fragment = FragmentWeatherForecastNew.newInstance(location);
        installFragment(fragment);
    }

    @Override
    public void goToLocations() {
        FragmentWeatherLocationsNew fragment = FragmentWeatherLocationsNew.newInstance();
        installFragment(fragment);
    }

    private void installFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
