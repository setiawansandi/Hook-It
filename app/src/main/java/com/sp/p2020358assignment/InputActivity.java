package com.sp.p2020358assignment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class InputActivity extends AppCompatActivity {
    private EditText fishname, fishdate, fishlength, fishweight;
    private TextView fishlocation;
    private ImageButton fishupload, fishgetloc;
    private Button done, cancel;
    private Bitmap bitmap;
    private Drawable oldDrawable;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Objects.requireNonNull(getSupportActionBar()).hide();     // hide action bar

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
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

        setContentView(R.layout.activity_input);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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
        oldDrawable = fishupload.getDrawable();
        dbHelper = new DatabaseHelper(this);


        // onClickListener
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done.setOnClickListener(onDone);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    View.OnClickListener onDone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get data
            if (fishupload.getDrawable() == oldDrawable) Toast.makeText(v.getContext(), "You haven't chosen an image yet", Toast.LENGTH_LONG).show();
            else {

                String nameStr = fishname.getText().toString();
                String weightStr = fishweight.getText().toString();
                String lengthStr = fishlength.getText().toString();
                String dateStr = fishdate.getText().toString();
                String timestamp = "" + System.currentTimeMillis();
                double lon = 0.0;
                double lat = 0.0;

                if (nameStr.trim().length() == 0 || weightStr.trim().length() == 0 || lengthStr.trim().length() == 0 || dateStr.trim().length() == 0)
                    Toast.makeText(v.getContext(), "Missing Inputs", Toast.LENGTH_LONG).show();
                else {
                    // convert bitmap to bytearray + vice versa

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image = byteArrayOutputStream .toByteArray();

                    // send data to sql database
                    long id = dbHelper.insertInfo(nameStr, dateStr, lengthStr, weightStr, lat, lon, image, timestamp, timestamp);

                    //Toast.makeText(v.getContext(), "Record added to id: " + id, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(InputActivity.this, DisplayActivity.class));
                }
            }
        }
    };
}