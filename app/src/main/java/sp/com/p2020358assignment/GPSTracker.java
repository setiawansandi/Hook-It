package sp.com.p2020358assignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import sp.com.p2020358assignment.BuildConfig;

public class GPSTracker extends Service implements LocationListener {
    private Context mContext = null;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    // the min distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // the min time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // declaring a location manager
    protected LocationManager locationManager;

    public GPSTracker() { checkGPSPermissions(); }

    public GPSTracker(Context context) {
        this.mContext = context;
        //checkGPSPermissions();
    }

    public Location getLocation () {
        this.canGetLocation = false;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                // prompt user to enable location services
                showEnableLocationAlert();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    //Permission granted
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }

    // Function to get latitude
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    // Function to get longitude
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }
    // Stop using GPS listener
    // Calling this function will stop using GPS in your app

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    // Function to check GPS/wifi enabled
    // @return Boolean
    public boolean canGetLocation() {
        checkGPSPermissions();
        return canGetLocation;
    }

    // check for location permission
    public void checkGPSPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionState1 == PackageManager.PERMISSION_GRANTED &&
                permissionState2 == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get GPS location
            getLocation();
        } else {
            //Prompt user to enable location permission
            showEnablePermissionAlert();
        }
    }

    // function to show setting alert dialog. On pressing Settings button will launch Settings option
    public void showEnablePermissionAlert() {
        AlertDialog. Builder alertDialog = new AlertDialog. Builder (mContext);
        // Setting Dialog Title
        alertDialog.setTitle ("Location Permission Settings");
        // Setting Dialog Message
        alertDialog.setMessage("Location Permission is not enabled." +
                " Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton ("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                // Goto Application Setting
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton ("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
        });
        // Shoving Alert Message
        alertDialog.show();
    }

    public void showEnableLocationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialogBuilder.setTitle("Location Service Settings");
        alertDialogBuilder.setMessage("Location service is disabled in your device." +
                "Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Go to Settings Page To Enable Location Service",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // go to location service setting
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                mContext.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(Location location) { getLocation();}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}