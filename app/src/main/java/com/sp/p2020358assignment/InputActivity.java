package com.sp.p2020358assignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {
    private EditText fishname, fishdate, fishlength, fishweight;
    private TextView fishlocation;
    private ImageButton fishupload, fishgetloc;
    private Button done, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        // assignment of activity launcher (inside onAttach or onCreate, i.e, before the activity is displayed)
        ActivityResultLauncher<Intent> imgResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {     // new ActivityResultCallback<ActivityResult>("result") shortcut
                    // There are no request codes
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData(); // deprecated >>> super.onActivityResult(requestCode, resultCode, data);
                        // Get the url of the image from data
                        assert data != null; // prevent nullpointerexception
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // update the preview image in the layout
                            fishupload.setImageURI(selectedImageUri);
                        }
                    }
                });

        setContentView(R.layout.activity_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setHomeButtonEnabled(true); // ?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // will make the icon clickable and add the < at the left of the icon. (clickable or not)

        // hooks
        fishname = (EditText) findViewById(R.id.edit_fish);
        fishdate = (EditText) findViewById(R.id.edit_date);
        fishlength = (EditText) findViewById(R.id.edit_length);
        fishweight = (EditText) findViewById(R.id.edit_weight);
        fishlocation = (TextView) findViewById(R.id.text_location);
        fishupload = (ImageButton) findViewById(R.id.imgbtn_upload);
        fishgetloc = (ImageButton) findViewById(R.id.imgbtn_getloc);
        done = (Button) findViewById(R.id.button_done);
        cancel = (Button) findViewById(R.id.button_cancel);

        fishupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an instance of the intent of the type image
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the intent to activity launcher
                imgResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });
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