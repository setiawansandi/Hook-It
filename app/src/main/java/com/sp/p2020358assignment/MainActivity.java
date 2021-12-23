package com.sp.p2020358assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION  = 1000 * 3;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView name, studentid, schclass, appname;


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

        image.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        studentid.setAnimation(bottomAnim);
        schclass.setAnimation(bottomAnim);
        appname.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}