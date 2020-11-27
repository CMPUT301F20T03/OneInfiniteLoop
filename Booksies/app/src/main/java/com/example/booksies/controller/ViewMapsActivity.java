package com.example.booksies.controller;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.example.booksies.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
        mMap = googleMap;
        mIntent = getIntent();
        double latitude = mIntent.getExtras().getDouble("lat");
        double longitude = mIntent.getExtras().getDouble("lon");
//        String bookID = mIntent.getStringExtra("bookID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();





        Log.d("LOCATION", "---------------- " + latitude + " " + longitude);


        // Add a user marker and move the camera
        LatLng pickup_marker = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pickup_marker);
        String address = getMarkerAddress(pickup_marker);
        markerOptions.title(address);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pickup_marker));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickup_marker, 15));
    }

    private String getMarkerAddress(LatLng latLng) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(ViewMapsActivity.this, Locale.getDefault());
        String address = null;
        try {
            //gets a single address from location
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            //set title as address of location
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}