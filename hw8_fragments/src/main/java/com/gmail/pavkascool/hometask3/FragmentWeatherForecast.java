package com.gmail.pavkascool.hometask3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.*;

import static com.gmail.pavkascool.hometask3.WeatherActivity.CELSIUS;
import static com.gmail.pavkascool.hometask3.WeatherActivity.FAHRENHEIT;


public class FragmentWeatherForecast extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private ImageButton search;
    private Button save, back;
    private EditText city;
    private TextView town, time, temp, desc;
    private ImageView weatherImage;
    private IconHolder iconHolder;

    private List<Weather> weathers;
    private WeatherActivity activity;
    private final static String TAG = "MyTag";


    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&&APPID=%s";
    private final static String FORECAST = "https://api.openweathermap.org/data/2.5/forecast?q=%s&&APPID=%s";
    private final static String ID = "da89ad0f2cb19fb309267dddfb2ac0a9";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (WeatherActivity)context;
    }


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather_forecast, null);
        weathers = activity.getWeathers();
        iconHolder = IconHolder.getInstance();

        recyclerView = v.findViewById(R.id.recycler);
        search = v.findViewById(R.id.search);
        System.out.println("SEARCH IS " + search);
        save = v.findViewById(R.id.save);
        back = v.findViewById(R.id.back);
        town = v.findViewById(R.id.town);
        city = v.findViewById(R.id.city);
        time = v.findViewById(R.id.time);
        temp = v.findViewById(R.id.temp);
        desc = v.findViewById(R.id.desc);
        weatherImage = v.findViewById(R.id.weather);


        if(!weathers.isEmpty()) {
            Weather current = weathers.get(0);
            town.setText(current.getLocation().toUpperCase());
            time.setText(current.getTime());
            temp.setText(current.getTemp());
            desc.setText(current.getDesc());
            if(iconHolder.get(current.getIconCode()) != null) {
                weatherImage.setImageBitmap(iconHolder.get(current.getIconCode()));
                Log.d(TAG, "ICON Already Exists");
            }
            else {
                Picasso.with(activity).load("http://openweathermap.org/img/wn/" + current.getIconCode() + "@2x.png").into(weatherImage);
                Log.d(TAG, "ICON Is To Be Loaded");
            }
        }

        search.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(activity, cols);
        recyclerView.setLayoutManager(manager);
        weatherAdapter = new WeatherAdapter();
        recyclerView.setAdapter(weatherAdapter);

        String s = activity.getLocation();
        if(s != null) {
            city.setText(s);
            try {
                searchWeatherForLocation(s);
                searchForecastForLocation(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return v;}


    private class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new WeatherViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder)holder;
            Weather w = weathers.get(position + 1);
            weatherViewHolder.time.setText(w.getTime());
            weatherViewHolder.temp.setText(w.getTemp());
            weatherViewHolder.desc.setText(w.getDesc());
            String iconCode = w.getIconCode();
            if(iconHolder.containsKey(iconCode)) {
                weatherViewHolder.weath.setImageBitmap(iconHolder.get(iconCode));
            }
            else {
                Picasso.with(activity).load("http://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                        .into(weatherViewHolder.weath);
            }
        }

        @Override
        public int getItemCount() {
            return weathers.size() - 1;
        }
    }

    private class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView time, temp, desc;
        private ImageView weath;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.temp);
            desc = itemView.findViewById(R.id.desc);
            weath = itemView.findViewById(R.id.weather);
        }
    }

    @Override
    public void onClick(View v) {
        String loc = city.getText().toString().toUpperCase();

        if(v.getId() == R.id.search) {
            weathers = new ArrayList<Weather>();
            try {
                searchWeatherForLocation(loc);
                searchForecastForLocation(loc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(v.getId() == R.id.save) {
            activity.goToLocations();
            activity.saveLocation(loc);
        }
        else {
            activity.goToLocations();
        }
    }

    private void searchWeatherForLocation(final String location) throws IOException {

        Date date = new Date();
        final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(date);
        Log.d(TAG, "Current Time is " + t);

        final Weather currentWeather = new Weather(location);
        currentWeather.setTime(t);
        Log.d(TAG, "Set Time is " + currentWeather.getTime());

        String address = String.format(BASE_URL, location, ID);
        Log.d(TAG, address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String answer = response.body().string();


                try {
                    JSONObject object = new JSONObject(answer);

                    JSONArray weatherArray = object.getJSONArray("weather");
                    final String description = weatherArray.getJSONObject(0).getString("description");
                    currentWeather.setDesc(description);

                    final String iconCode = weatherArray.getJSONObject(0).getString("icon");
                    final String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                    currentWeather.setIconCode(iconCode);

                    JSONObject main = object.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    final String temper = obtainTemperature(temperature);
                    currentWeather.setTemp(temper);



                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            town.setText(location.toUpperCase());

                            desc.setText(description);

                            temp.setText(temper);

                            time.setText(t);


                            if (!iconHolder.containsKey(iconCode)) {
                                Log.d(TAG, iconHolder.size() + " and doesn't contain this icon");
                                Picasso.with(activity).load(iconUrl).into(weatherImage);
                                Log.d(TAG, "Finished loading to UI and array size of weathers is " + weathers.size());
                                Picasso.with(activity).load(iconUrl).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        iconHolder.put(iconCode, bitmap);
                                        Log.d(TAG, iconHolder.size() + " updated");
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        Log.d(TAG, iconHolder.size() + " not updated");
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                            }
                            else {
                                weatherImage.setImageBitmap(iconHolder.get(iconCode));
                                Log.d(TAG, iconHolder.size() + " and CONTAINS this icon");
                            }
                        }
                    });

                } catch (JSONException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                weathers.add(currentWeather);
                System.out.println("SIZE OF WEATHERS = " + weathers);
            }
        });
    }

    private void searchForecastForLocation(final String location) {
        String address = String.format(FORECAST, location, ID);
        Log.d(TAG, address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String answer = response.body().string();

                Date date = new Date();
                final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(date);

                try {
                    JSONObject object = new JSONObject(answer);
                    JSONArray list = object.getJSONArray("list");
                    for(int i = 0; i < list.length(); i++) {
                        Weather w = new Weather(location);
                        JSONObject main = list.getJSONObject(i).getJSONObject("main");
                        String temper = main.getString("temp");
                        double d = Double.parseDouble(temper);
                        String tt = obtainTemperature(d);
                        w.setTemp(tt);
                        JSONArray wthrs = list.getJSONObject(i).getJSONArray("weather");
                        String s = wthrs.getJSONObject(0).getString("description");
                        w.setDesc(s);
                        String iCode = wthrs.getJSONObject(0).getString("icon");
                        w.setIconCode(iCode);

                        long dt = list.getJSONObject(i).getLong("dt") * 1000;
                        Date arrayDate = new Date(dt);
                        String aDate = new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(arrayDate);
                        w.setTime(aDate);

                        weathers.add(w);
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherAdapter.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(city.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        }
                    });
                } catch (JSONException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private String obtainTemperature(double kelvin) {
        String result = null;
        switch(activity.getScale()) {
            case CELSIUS:
                result = "t = " + Math.round(kelvin - 273.15) + " C";
                break;
            case FAHRENHEIT:
                result = "t = " + Math.round((kelvin - 273.15) * 9 / 5 + 32) + " F";
        }
        return result;
    }

}
