package sp.com.p2020358assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class EditRecord extends AppCompatActivity {
    private EditText fishname, fishdate, fishlength, fishweight;
    private TextView fishlocation;
    private ImageButton fishupload, fishgetloc;
    private Button done, cancel;
    private Bitmap bitmap, resizedBitmap;
    private Drawable oldDrawable;
    private DatabaseHelper dbHelper;

    private GPSTracker gpsTracker;
    private double latitude = 0.0d, longitude = 0.0d;

    private int mDate, mMonth, mYear;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    // for update data
    private String id, name, date, length, weight, addTimestamp, updateTimestamp;
    private byte[] bitMapBytes;
    private Bitmap bitmapR;
    double lat, lon;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_input);
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
        gpsTracker = new GPSTracker(EditRecord.this);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode",editMode);
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        date = intent.getStringExtra("DATE");
        length = intent.getStringExtra("LENGTH");
        weight = intent.getStringExtra("WEIGHT");
        lat = intent.getDoubleExtra("lAT", 5);
        lon = intent.getDoubleExtra("LON", 5);
        bitMapBytes = intent.getByteArrayExtra("BITMAP_BYTES");
        addTimestamp = intent.getStringExtra("ID");
        updateTimestamp = intent.getStringExtra("UPDATE_TIMESTAMP");
        bitmapR = BitmapFactory.decodeByteArray(bitMapBytes , 0, bitMapBytes .length);

        if (editMode) {
            editMode = intent.getBooleanExtra("editMode",editMode);
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            date = intent.getStringExtra("DATE");
            length = intent.getStringExtra("LENGTH");
            weight = intent.getStringExtra("WEIGHT");
            lat = intent.getDoubleExtra("LAT", 5);
            lon = intent.getDoubleExtra("LON", 5);
            bitMapBytes = intent.getByteArrayExtra("BITMAP_BYTES");
            addTimestamp = intent.getStringExtra("ID");
            updateTimestamp = intent.getStringExtra("UPDATE_TIMESTAMP");
            bitmapR = BitmapFactory.decodeByteArray(bitMapBytes , 0, bitMapBytes .length);
            latitude = lat;
            longitude = lon;

            fishname.setText(name);
            fishdate.setText(date);
            fishlength.setText(length);
            fishweight.setText(weight);
            fishlocation.setText(lat + ", " +lon);
            fishupload.setImageBitmap(bitmapR);
            latitude = lat;
            longitude = lon;

        }
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // onClickListener(s)
        fishdate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            mDate = calendar.get(Calendar.DATE);
            mMonth = calendar.get(Calendar.MONTH);
            mYear = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditRecord.this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    int setmonth = month+1;
                    fishdate.setText(dayOfMonth+"/"+setmonth+"/"+year);
                }
            }, mYear, mMonth, mDate);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();

        });

        fishupload.setOnClickListener(v -> {
            imagePickDialog();

        });

        cancel.setOnClickListener(v -> finish());

        done.setOnClickListener(onDone);
        fishgetloc.setOnClickListener(onGetLocation);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private void imagePickDialog() {

        //Additional Feature : Allow user to pick from either camera or gallery
        String[] options = {"Camera", "Gallery"};
        //                     0          1

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) { //if which == 0, camera mode
                    if(!checkCameraPermission())
                        requestCameraPermission();
                    else
                        pickFromCamera();
                }
                else if (which == 1) { //if which == 1, gallery mode
                    if (!checkStoragePermission())
                        requestStoragePermission();
                    else
                        pickFromStorage();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromCamera() {
        //get image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromStorage() {
        //get image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check which code is being requested
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted)
                        pickFromCamera();
                    else
                        Toast.makeText(this,"Camera permission required!", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted)
                        pickFromStorage();
                    else
                        Toast.makeText(this, "Storage permission required!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //added image crop library
        //Source : https://github.com/ArthurHub/Android-Image-Cropper

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE)
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            else if (requestCode == IMAGE_PICK_CAMERA_CODE)
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                Uri resultUri = result.getUri();
                imageUri = resultUri;
                fishupload.setImageURI(resultUri);
                // bitmap value to store in sql
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    resizedBitmap = getResizedBitmap(bitmap, 650, 650);
                    bitmap.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected  void onDestroy() {
        dbHelper.close();
        super.onDestroy();
        gpsTracker.stopUsingGPS();
    }

    View.OnClickListener onDone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get data
            String nameStr = fishname.getText().toString();
            String weightStr = fishweight.getText().toString();
            String lengthStr = fishlength.getText().toString();
            String dateStr = fishdate.getText().toString();


            if (nameStr.trim().length() == 0 || weightStr.trim().length() == 0 || lengthStr.trim().length() == 0 || dateStr.trim().length() == 0)
                Toast.makeText(v.getContext(), "Missing Inputs", Toast.LENGTH_LONG).show();
            else {
                byte[] image;
                if (resizedBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    image = byteArrayOutputStream.toByteArray();
                } else image = bitMapBytes;

                if (editMode) {
                    String newUpdateTime = "" + System.currentTimeMillis();

                    dbHelper.updateInfo(id, nameStr, dateStr, lengthStr, weightStr, latitude, longitude, image, addTimestamp, newUpdateTime);
                    Toast.makeText(EditRecord.this,"Updated", Toast.LENGTH_SHORT).show();

                } else {
                    String timestamp = "" + System.currentTimeMillis();

                    long id = dbHelper.insertInfo(nameStr, dateStr, lengthStr, weightStr, latitude, longitude, image, timestamp, timestamp);
                    Toast.makeText(EditRecord.this,"Added", Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(EditRecord.this, DisplayActivity.class));
                finish();
            }
        }

    };

    View.OnClickListener onGetLocation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                String output = latitude + ", " + longitude;
                fishlocation.setText(output);
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }
        }
    };

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
