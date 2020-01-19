package com.gmail.pavkascool.hometask3.weather;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.gmail.pavkascool.hometask3.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.gmail.pavkascool.hometask3.weather.WeatherActivity.CELSIUS;
import static com.gmail.pavkascool.hometask3.weather.WeatherActivity.FAHRENHEIT;

public class FragmentScale extends Fragment implements View.OnClickListener {

    private static FragmentScale instance;
    private RadioButton celsius, fahrenheit;
    private Button start;
    private WeatherActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setRetainInstance(true);

}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scale, null);
        celsius = v.findViewById(R.id.celsius);
        celsius.setOnClickListener(this);
        celsius.setChecked(true);
        fahrenheit = v.findViewById(R.id.fahrenheit);
        fahrenheit.setOnClickListener(this);
        start = v.findViewById(R.id.start);
        start.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (WeatherActivity)context;
    }

    public static FragmentScale getInstance() {
        if(instance == null) instance = new FragmentScale();
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start:
                activity.goToLocations();
                if(fahrenheit.isChecked()) activity.setScale(FAHRENHEIT );
                break;
            case R.id.celsius:
                activity.setScale(CELSIUS);
                break;
            case R.id.fahrenheit:
                activity.setScale(FAHRENHEIT);
        }
    }
}
