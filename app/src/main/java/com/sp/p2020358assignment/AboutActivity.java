package com.sp.p2020358assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AboutActivity extends AppCompatActivity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        output = (TextView) findViewById(R.id.output);
        //2021-12-26T08:06:19+08:00 //"EEE, d MMM yyyy, HH:mm"

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss");
        String date = df.format(Calendar.getInstance().getTime());
        String url = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast?date_time=" + date + "%2B08%3A00";

        output.setText(url);
    }
}