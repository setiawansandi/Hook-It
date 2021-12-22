package com.sp.p2020358assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setHomeButtonEnabled(true); // ?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // will make the icon clickable and add the < at the left of the icon. (clickable or not)

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}