package com.example.booksies.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.booksies.R;

import static com.example.booksies.model.FirestoreHandler.setPickupLocation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Acknowledgements: https://developers.google.com/maps/documentation/android-sdk/start

public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String bookId;
    private double lat;
    private double lon;
    private FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bookId = getIntent().getStringExtra("bookId");
        if (getIntent().hasExtra("lat")) {
            lat = getIntent().getExtras().getDouble("lat");
            lon = getIntent().getExtras().getDouble("lon");
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SetLocationActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //If the book already has a location set
        if (getIntent().hasExtra("lat")) {
            LatLng currentlySetLocation = new LatLng(lat, lon);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentlySetLocation);
            String address = getMarkerAddress(currentlySetLocation);
            markerOptions.title(address);
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlySetLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlySetLocation, 15));
        } else {
            if (ActivityCompat.checkSelfPermission(SetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                                    String address = getMarkerAddress(currentLocation);
                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title(address));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                }
                            }
                        });
            }
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                String address = getMarkerAddress(latLng);
                markerOptions.title(address);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.addMarker(markerOptions);
                lat = latLng.latitude;
                lon = latLng.longitude;
            }
        });

        //Save location button listener
        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPickupLocation(bookId, lat, lon);
                Toast toast = Toast.makeText(SetLocationActivity.this,
                        "Pickup location has been updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    //Go back to whatever opened this activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String getMarkerAddress(LatLng latLng) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(SetLocationActivity.this, Locale.getDefault());
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