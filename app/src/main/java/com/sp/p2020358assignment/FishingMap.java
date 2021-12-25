package com.sp.p2020358assignment;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sp.p2020358assignment.databinding.ActivityFishingMapBinding;

public class FishingMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityFishingMapBinding binding;

    private double lat;
    private double lon;
    private String fishName;
    private double myLat;
    private double myLon;
    private LatLng FISHLOC;
    private LatLng ME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFishingMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lat = getIntent().getDoubleExtra("LATITUDE", 0);
        lon = getIntent().getDoubleExtra("LONGITUDE", 0);
        fishName = getIntent().getStringExtra("NAME");
        myLat = getIntent().getDoubleExtra("MYLATITUDE",0);
        myLon = getIntent().getDoubleExtra("MYLONGITUDE", 0);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fishing_map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FISHLOC = new LatLng(lat,lon);
        ME = new LatLng(myLat, myLon);
        Marker fishLoc = mMap.addMarker(new MarkerOptions().position(FISHLOC).title(fishName));
        Marker me = mMap.addMarker(new MarkerOptions().position(ME).title("ME")
                .snippet("My location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_me)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FISHLOC, 15));
    }
}