package com.sp.p2020358assignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView input, info, about, exit;
    // display result from api
    public static TextView home_forecast, home_location, home_temperature;
    private TextView home_calendar;
    private ImageView weather_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));
        setContentView(R.layout.activity_home);

        input = findViewById(R.id.input);
        info = findViewById(R.id.info);
        about = findViewById(R.id.about);
        exit = findViewById(R.id.exit);

        home_forecast = (TextView) findViewById(R.id.home_forecast);
        home_location = (TextView) findViewById(R.id.home_location);
        home_calendar = (TextView) findViewById(R.id.home_calendar);
        home_temperature = (TextView) findViewById(R.id.home_temp);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

        // get intent data from AsyncTask
        String forecast = getIntent().getStringExtra("FORECAST");
        String location = getIntent().getStringExtra("LOCATION");
        String temperature = getIntent().getStringExtra("TEMPERATURE");

        // set data from intent to textViews
        home_forecast.setText(forecast);
        home_location.setText(location);
        home_temperature.setText(temperature);

        switch (forecast){
            case "Fair (Day)" :
                weather_icon.setImageResource(R.drawable.ic_wi_day_sunny);
                break;
            case "Fair (Night)" :
                weather_icon.setImageResource(R.drawable.ic_wi_night_clear);
                break;
            case "Fair & Warm" :
                weather_icon.setImageResource(R.drawable.ic_wi_hot);
                break;
            case "Partly Cloudy (Day)" :
                weather_icon.setImageResource(R.drawable.ic_wi_day_cloudy);
                break;
            case "Partly Cloudy (Night)" :
                weather_icon.setImageResource(R.drawable.ic_wi_night_alt_cloudy);
                break;
            case "Cloudy" :
                weather_icon.setImageResource(R.drawable.ic_wi_cloudy);
                break;
            case "Hazy" :
                weather_icon.setImageResource(R.drawable.ic_wi_fog);
                break;
            case "Slightly Hazy" :
                weather_icon.setImageResource(R.drawable.ic_wi_dust);
                break;
            case "Windy" :
                weather_icon.setImageResource(R.drawable.ic_wi_cloudy_gusts);
                break;
            case "Mist" :
                weather_icon.setImageResource(R.drawable.ic_wi_windy);
                break;
            case "Light Rain" :
                weather_icon.setImageResource(R.drawable.ic_wi_sprinkle);
                break;
            case "Moderate Rain" :
            case "Heavy Rain" :
                weather_icon.setImageResource(R.drawable.ic_wi_sleet);
                break;
            case "Passing Showers" :
                weather_icon.setImageResource(R.drawable.ic_wi_rain);
                break;
            case "Light Showers" :
            case "Showers" :
                weather_icon.setImageResource(R.drawable.ic_wi_rain_mix);
                break;
            case "Heavy Showers" :
                weather_icon.setImageResource(R.drawable.ic_wi_showers);
                break;
            case "Thundery Showers" :
                weather_icon.setImageResource(R.drawable.ic_wi_storm_showers);
                break;
            case "Heavy Thundery Showers" :
            case "Heavy Thundery Showers with Gusty Winds" :
                weather_icon.setImageResource(R.drawable.ic_wi_thunderstorm);
                break;
            default :
                weather_icon.setImageResource(R.drawable.ic_wi_day_sunny);
                break;
        }

        // buttons
        input.setOnClickListener(this);
        info.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE d MMMM, yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        home_calendar.setText(date);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.input:
                intent = new Intent(this, InputActivity.class);
                startActivity(intent);
                break;
            case R.id.info:
                intent = new Intent(this, DisplayActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                exit_function();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        exit_function();
    }

    private void exit_function() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_action_alert)
                .setTitle("Exit Notification")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton(Html.fromHtml("<font color='#3F4581'>Yes</font>"), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }

                })
                .setNegativeButton(Html.fromHtml("<font color='#3F4581'>No</font>"), null)
                .show();
    }
}