package com.example.booksies.controller;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.booksies.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent mIntent;
    String documentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        mIntent = getIntent();
        documentID = mIntent.getStringExtra("documentID");
        mMap = googleMap;
        String lat_str = mIntent.getStringExtra("latitude") ;
        String lon_str = mIntent.getStringExtra("longitude");
        String title = mIntent.getStringExtra("title");
        double latitude = Double.parseDouble(lat_str);
        double longitude = Double.parseDouble(lon_str);


        // Add a user marker and move the camera
        LatLng pickup_marker = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(pickup_marker).title(title + " pickup location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pickup_marker));
    }
}