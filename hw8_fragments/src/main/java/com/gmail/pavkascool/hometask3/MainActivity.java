package com.gmail.pavkascool.hometask3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmail.pavkascool.hometask3.contacts.ContactsActivity;
import com.gmail.pavkascool.hometask3.weather.WeatherActivityNew;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button toContacts;
    private Button toWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toContacts = findViewById(R.id.button_contacts);
        toContacts.setOnClickListener(this);
        toWeather = findViewById(R.id.button_weather);
        toWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            case R.id.button_contacts:
                intent = new Intent(this, ContactsActivity.class);
                break;
            case R.id.button_weather:
                intent = new Intent(this, WeatherActivityNew.class);
                break;
        }
        startActivity(intent);
    }
}
