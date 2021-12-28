package com.sp.p2020358assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION  = 1000 * 3;

    private Animation topAnim, bottomAnim;
    private ImageView image;
    private TextView name, studentid, schclass, appname;
    private GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Objects.requireNonNull(getSupportActionBar()).hide();   // hide action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // hide status bar
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.secondary));  // change status bar colour
        setContentView(R.layout.activity_main);

        // animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bot_anim);

        // hooks
        image = findViewById(R.id.logo);
        name = findViewById(R.id.name);
        studentid = findViewById(R.id.studentid);
        schclass = findViewById(R.id.schclass);
        appname = findViewById(R.id.appname);

        gpsTracker = new GPSTracker(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gpsTracker.canGetLocation()) {
            image.setAnimation(topAnim);
            name.setAnimation(bottomAnim);
            studentid.setAnimation(bottomAnim);
            schclass.setAnimation(bottomAnim);
            appname.setAnimation(bottomAnim);
            new fetchAPIdata().execute(gpsTracker);
        }
    }

    class fetchAPIdata extends AsyncTask<GPSTracker, Void, Void> {


        String jsonData = "", debug = "", forecast = "";
        List<Weather> weatherList;
        private double myLatitude = 0.0d, myLongitude = 0.0d, temperature = 0.0d;

        // bg thread
        @Override
        protected Void doInBackground(GPSTracker... gpsTracker) {
            //https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=2021-12-26T08%3A06%3A19%2B08%3A00
            //2021-12-26T08:06:19+08:00

            weatherList = new ArrayList<>();

            GPSTracker gpsTrackerAsync = gpsTracker[0];
            myLatitude = gpsTrackerAsync.getLatitude();
            myLongitude = gpsTrackerAsync.getLongitude();

            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'%2B08%3A00'");
            String date = df.format(Calendar.getInstance().getTime());
            String urlString = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=" + date;

            // get json file from api (url)
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    jsonData = jsonData + line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // select relevant data from json file
            try {
                JSONObject jsonObj = new JSONObject(jsonData);

                //extracting area_metadata array from json string
                JSONArray areaArray = jsonObj.getJSONArray("area_metadata");
                int length = areaArray.length();

                JSONArray itemsArray = jsonObj.getJSONArray("items");
                JSONObject itemsObject = itemsArray.getJSONObject(0);
                JSONArray forecastArray = itemsObject.getJSONArray("forecasts");


                //loop to get all json objects from data json array
                for (int i = 0; i < length; ++i) {
                    JSONObject areaObject = areaArray.getJSONObject(i);
                    //debug = debug + areaObject.getString("name") + "\n";

                    JSONObject locObject = areaObject.getJSONObject("label_location");
                    //debug = debug + locObject.getDouble("latitude") + "\n";
                    //debug = debug + locObject.getDouble("longitude") + "\n";

                    JSONObject forecastObject = forecastArray.getJSONObject(i);
                    //debug = debug + forecastObject.getString("forecast") + "\n\n";

                    String areaName = areaObject.getString("name");
                    double lat = locObject.getDouble("latitude");
                    double lon = locObject.getDouble("longitude");
                    String forecast = forecastObject.getString("forecast");
                    Weather tempWeather = new Weather(areaName, lat, lon, forecast);
                    weatherList.add(tempWeather);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // get forecast data from nearest location
            double minarea = Double.MAX_VALUE;
            double distance;
            for(int i = 0; i < weatherList.size(); ++i) {
                distance = getDistanceBetweenPoints (myLatitude, myLongitude, weatherList.get(i).getLat(), weatherList.get(i).getLon(), "Km");
                if(distance < minarea) {
                    minarea = distance;
                    forecast = weatherList.get(i).getForecast() + " " + weatherList.get(i).getAreaName();
                }
            }

            gpsTrackerAsync.stopUsingGPS();
            weatherList.clear();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // ui thread (output result)
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("FORECAST", forecast);
                startActivity(intent);
                finish();
            }, SPLASH_DURATION);
            //AboutActivity.output.setText(Arrays.toString(weatherList.toArray()));
        }


        private double getDistanceBetweenPoints (double lat1, double lon1, double lat2, double lon2, String unit) {
            // lat1/lon1 = ur location;
            double theta = lon1 - lon2;
            double distance;
            distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            distance = Math.acos(distance);
            distance = distance * 60 * 1.1515;

            switch (unit) {
                case "Mi" : break;
                case "Km" : distance = distance * 1.609344;
            }

            return distance;
        }
    }

}

