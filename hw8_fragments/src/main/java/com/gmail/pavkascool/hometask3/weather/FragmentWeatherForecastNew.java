package com.gmail.pavkascool.hometask3.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.LocationDao;
import com.gmail.pavkascool.hometask3.Locations;
import com.gmail.pavkascool.hometask3.R;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentWeatherForecastNew extends Fragment implements View.OnClickListener {
    private static FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    private String location;

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private ImageButton search;
    private Button save, back;
    private EditText city;
    private TextView town, time, temp, desc;
    private ImageView weatherImage;
    private FragmentMediator fragmentMediator;
    private List<Weather> weathers;




    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&&APPID=%s";
    private final static String FORECAST = "https://api.openweathermap.org/data/2.5/forecast?q=%s&&APPID=%s";
    private final static String ID = "da89ad0f2cb19fb309267dddfb2ac0a9";

    public static FragmentWeatherForecastNew newInstance(String location) {
        FragmentWeatherForecastNew fragment = new FragmentWeatherForecastNew();
        fragment.location = location;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weathers = new ArrayList<>();
        setRetainInstance(true);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentMediator = (FragmentMediator) context;
    }


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather_forecast, null);
        //weathers = activity.getWeathers();


        recyclerView = v.findViewById(R.id.recycler);
        search = v.findViewById(R.id.search);

        save = v.findViewById(R.id.save);
        back = v.findViewById(R.id.back);
        town = v.findViewById(R.id.town);
        city = v.findViewById(R.id.city);
        time = v.findViewById(R.id.time);
        temp = v.findViewById(R.id.temp);
        desc = v.findViewById(R.id.desc);
        weatherImage = v.findViewById(R.id.weather);


        if (!weathers.isEmpty()) {
            Weather current = weathers.get(0);
            town.setText(current.getLocation().toUpperCase());
            time.setText(current.getTime());
            temp.setText(current.getTemp());
            desc.setText(current.getDesc());
            Picasso.with(getContext()).load("http://openweathermap.org/img/wn/" + current.getIconCode() + "@2x.png")
                    .into(weatherImage);
        }

        search.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(getContext(), cols);
        recyclerView.setLayoutManager(manager);
        weatherAdapter = new WeatherAdapter();
        recyclerView.setAdapter(weatherAdapter);

        if(location != null) {
            city.setText(location);
            try {
                searchWeatherForLocation(location);
                searchForecastForLocation(location);
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
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder) holder;
            Weather w = weathers.get(position + 1);
            weatherViewHolder.time.setText(w.getTime());
            weatherViewHolder.temp.setText(w.getTemp());
            weatherViewHolder.desc.setText(w.getDesc());
            String iconCode = w.getIconCode();
            Picasso.with(getContext()).load("http://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                    .into(weatherViewHolder.weath);

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
            saveLocation(loc);
            fragmentMediator.goToLocations();
        }
        else {
            fragmentMediator.goToLocations();
        }
    }

    private void saveLocation(final String loc) {

        final Locations newLocation = new Locations();
        newLocation.setLocation(loc);
        final LocationDao dao = db.locationDao();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Locations> places = dao.getAll();
                for(Locations p: places) {
                    if(p.getLocation().equals(loc)) {
                        return;
                    }
                }
                dao.insert(newLocation);
            }
        });
        t.start();
    }

    private void searchWeatherForLocation(final String location) throws IOException {

        Date date = new Date();
        final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(date);


        final Weather currentWeather = new Weather(location);
        currentWeather.setTime(t);


        String address = String.format(BASE_URL, location, ID);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT).show();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            town.setText(location.toUpperCase());
                            desc.setText(description);
                            temp.setText(temper);
                            time.setText(t);

                            Picasso.with(getContext()).load(iconUrl).into(weatherImage);
                        }
                    });

                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                weathers.add(currentWeather);
            }
        });
    }

    private void searchForecastForLocation(final String location) {
        String address = String.format(FORECAST, location, ID);
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherAdapter.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(city.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        }
                    });
                } catch (JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private String obtainTemperature(double kelvin) {
        String result = null;
        Boolean fahr = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("scale", false);
        result = fahr? "t = " + Math.round((kelvin - 273.15) * 9 / 5 + 32) + " F": "t = " + Math.round(kelvin - 273.15) + " C";
        return result;
    }

}
