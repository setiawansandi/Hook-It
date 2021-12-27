package com.sp.p2020358assignment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class fetchAPIdata extends AsyncTask<Void, Void, Void> {
    // bg thread
    @Override
    protected Void doInBackground(Void... voids) {
        //https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=2021-12-26T08%3A06%3A19%2B08%3A00
        //2021-12-26T08:06:19+08:00

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss'%2B08%3A00'");
        String date = df.format(Calendar.getInstance().getTime());
        String urlString = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=" + date;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ui thread
    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }
}
