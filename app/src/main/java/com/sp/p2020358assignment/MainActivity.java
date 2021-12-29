package com.sp.p2020358assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.lang.ref.WeakReference;
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
    private WeakReference<Context> contextRef;

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

        // this values will be passed to fetchAPIdata
        gpsTracker = new GPSTracker(MainActivity.this);
        contextRef = new WeakReference<Context>(MainActivity.this);

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
            // fetchAPIdata constructor for splash screen only with weak ref and splash duration params
            new fetchAPIdata(contextRef, SPLASH_DURATION).execute(gpsTracker);
        }
    }


}