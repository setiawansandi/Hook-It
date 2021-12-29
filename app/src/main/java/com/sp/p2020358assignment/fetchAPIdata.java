package com.sp.p2020358assignment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

class fetchAPIdata extends AsyncTask<GPSTracker, Void, Void> {

    private WeakReference<Context> contextRef;
    private int SPLASH_DURATION = 1;
    String jsonWeather = "", jsonTemp = "", debug = "", forecast = "", location = "";
    private List<Weather> weatherList;
    private List<Temperature> temperatureList;
    private double myLatitude = 0.0d, myLongitude = 0.0d, temperature = 0.0d;

    public fetchAPIdata(){}

    public fetchAPIdata(WeakReference<Context> contextRef, int SPLASH_DURATION) {
        this.contextRef = contextRef;
        this.SPLASH_DURATION = SPLASH_DURATION;
    }

    // bg thread
    @Override
    protected Void doInBackground(GPSTracker... gpsTracker) {
        //https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=2021-12-26T08%3A06%3A19%2B08%3A00
        //2021-12-26T08:06:19+08:00

        weatherList = new ArrayList<>();
        temperatureList = new ArrayList<>();

        GPSTracker gpsTrackerAsync = gpsTracker[0];
        myLatitude = gpsTrackerAsync.getLatitude();
        myLongitude = gpsTrackerAsync.getLongitude();

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'%2B08%3A00'");
        String date = df.format(Calendar.getInstance().getTime());
        String weatherUrl = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=" + date;
        String temperatureUrl = "https://api.data.gov.sg/v1/environment/air-temperature?date_time=" + date;

        // get weather json
        try {
            URL url1 = new URL(weatherUrl);
            HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
            InputStream inputStream1 = httpURLConnection1.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                jsonWeather = jsonWeather + line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //get temperature json
        try {
            URL url2 = new URL(temperatureUrl);
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
            InputStream inputStream2 = httpURLConnection2.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream2));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                jsonTemp = jsonTemp + line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // select relevant data from json file
        // Weather forecast JSON
        try {
            JSONObject jsonObj = new JSONObject(jsonWeather);
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

        // temperature JSON
        try {
            JSONObject jsonObj2 = new JSONObject(jsonTemp);
            //extracting lat and lon
            JSONObject metaData = jsonObj2.getJSONObject("metadata");
            int length = metaData.length();
            JSONArray stationsArray = metaData.getJSONArray("stations");
            JSONArray sItemsArray = jsonObj2.getJSONArray("items");
            JSONObject sItemsObject = sItemsArray.getJSONObject(0);
            JSONArray tempArray = sItemsObject.getJSONArray("readings");

            for (int i = 0; i < length; ++i) {
                JSONObject stationsObject = stationsArray.getJSONObject(i);
                JSONObject sLocObject = stationsObject.getJSONObject("location");
                JSONObject weatherObject = tempArray.getJSONObject(i);

                double lat = sLocObject.getDouble("latitude");
                double lon = sLocObject.getDouble("longitude");
                double temp = weatherObject.getDouble("value");
                Temperature temperature = new Temperature(temp, lat, lon);
                temperatureList.add(temperature);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get forecast data from nearest location
        double minarea = Double.MAX_VALUE;
        for(int i = 0; i < weatherList.size(); ++i) {
            double distance = getDistanceBetweenPoints (myLatitude, myLongitude, weatherList.get(i).getLat(), weatherList.get(i).getLon(), "Km");
            if(distance < minarea) {
                minarea = distance;
                forecast = weatherList.get(i).getForecast();
                location = weatherList.get(i).getAreaName();
            }
            //debug = debug + distance + " "  + weatherList.get(i).getAreaName() + ", ";
        }

        // get temperature data from nearest location
        minarea = Double.MAX_VALUE;
        for(int i = 0; i < temperatureList.size(); ++i) {
            double distance = getDistanceBetweenPoints (myLatitude, myLongitude, temperatureList.get(i).getLat(), temperatureList.get(i).getLon(), "Km");
            if(distance < minarea) {
                minarea = distance;
                temperature = temperatureList.get(i).getTemp();
            }
        }

        gpsTrackerAsync.stopUsingGPS();
        weatherList.clear();
        temperatureList.clear();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // ui thread (output result)
    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Context context = contextRef.get();
        if(context != null) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("FORECAST", forecast);
                intent.putExtra("LOCATION", location);
                intent.putExtra("TEMPERATURE", String.format("%.1f", temperature) + "°C");
                context.startActivity(intent);

                ((Activity) (context)).finish();
                ;
            }, SPLASH_DURATION);
        } else {
            HomeActivity.home_forecast.setText(forecast);
            HomeActivity.home_location.setText(location);
            HomeActivity.home_temperature.setText(String.format("%.1f", temperature) + "°C");
        }
        //AboutActivity.output.setText(Arrays.toString(weatherList.toArray()));
    }


    private double getDistanceBetweenPoints (double lat1, double lon1, double lat2, double lon2, String unit) {
        // lat1 & lon1 = your current location;
        double theta = lon1 - lon2;
        double distance;
        distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        distance = Math.acos(distance);
        distance = Math.toDegrees(distance);
        distance = distance * 60 * 1.1515;

        switch (unit) {
            case "Mi" : break;
            case "Km" : distance = distance * 1.609344;
            default   : break;
        }

        return distance;
    }
}


