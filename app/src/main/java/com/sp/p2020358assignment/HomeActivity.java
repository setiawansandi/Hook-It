package com.sp.p2020358assignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public CardView input, info, about, exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.secondary));
        setContentView(R.layout.activity_home);

        input = findViewById(R.id.input);
        info = findViewById(R.id.info);
        about = findViewById(R.id.about);
        exit = findViewById(R.id.exit);

        input.setOnClickListener(this);
        info.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
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
                break;
            case R.id.about:
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
                .setIcon(android.R.drawable.ic_dialog_alert)
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