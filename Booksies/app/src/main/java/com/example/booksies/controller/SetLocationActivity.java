package com.example.booksies.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.booksies.R;

import static com.example.booksies.model.database.FirestoreHandler.setPickupLocation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//Acknowledgements: https://developers.google.com/maps/documentation/android-sdk/start
/**
 * This class allows a user to place a marker on a map
  */

public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String bookId;
    private double lat;
    private double lon;
    private boolean markerPlaced = false;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        //Enable the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Retrieve the lat and lon passed in from maps adapter
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
        //If the book already has a location set place a marker on that location and zoom in
        if (getIntent().hasExtra("lat")) {
            LatLng currentlySetLocation = new LatLng(lat, lon);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentlySetLocation);
            String address = getMarkerAddress(currentlySetLocation);
            markerOptions.title(address);
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlySetLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlySetLocation, 15));
        }
        else {
            //if owner has location enabled, place marker on last location
            if (ActivityCompat.checkSelfPermission(SetLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
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
                //if there is a valid address associated with marker
                if (address != null) {
                    markerOptions.title(address);
                }
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                mMap.addMarker(markerOptions);
                markerPlaced = true;
                lat = latLng.latitude;
                lon = latLng.longitude;
            }
        });

        //Save location button listener
        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerPlaced) {
                    //Firestore handler method that adds geopoint as a field in database
                    setPickupLocation(bookId, lat, lon);
                    Toast toast = Toast.makeText(SetLocationActivity.this,
                            "Pickup location has been updated", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(SetLocationActivity.this,
                            "Place a marker first", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
        //gets a single address from location
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //makes sure there's an address otherwise address will be null
        if (addresses.size() > 0) {
            address = addresses.get(0).getAddressLine(0);
        }
        return address;
    }
}

