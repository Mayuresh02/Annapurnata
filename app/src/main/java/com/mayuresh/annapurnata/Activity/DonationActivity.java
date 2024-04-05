package com.mayuresh.annapurnata.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mayuresh.annapurnata.ModelClass.Donors;
import com.mayuresh.annapurnata.R;

import java.util.Locale;


public class DonationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    FirebaseDatabase database;
    DatabaseReference reference;
    Button donate;
    ProgressDialog pg;
    EditText quantity1, phone1, aadhar1, description1;
    String latlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        donate = findViewById(R.id.donate);
        quantity1 = findViewById(R.id.quantity);
        phone1 = findViewById(R.id.phone);
        aadhar1 = findViewById(R.id.aadhar);
        description1 = findViewById(R.id.description);

        database = FirebaseDatabase.getInstance();

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        pg = new ProgressDialog(this);
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userLocation).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    latlang = String.format(Locale.getDefault(), "(%.7f,%.7f)", userLocation.latitude, userLocation.longitude);
                }
            }
        };
        fetchUserLocation();

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.show();

                String quantity = quantity1.getText().toString();
                String phone = phone1.getText().toString();
                String aadhar = aadhar1.getText().toString();
                String description = description1.getText().toString();
                String map = mMap.toString();

                if(TextUtils.isEmpty(quantity) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(aadhar) || TextUtils.isEmpty(description))
                {
                    pg.dismiss();
                    Toast.makeText(DonationActivity.this,"Enter all details",Toast.LENGTH_LONG).show();
                }
                else if(quantity.equals("0") || quantity.equals("Zero") || quantity.equals("zero"))
                {
                    pg.dismiss();
                    Toast.makeText(DonationActivity.this,"Quantity should be greater than 0",Toast.LENGTH_LONG).show();
                }
                else if(phone.length()!=10)
                {
                    pg.dismiss();
                    Toast.makeText(DonationActivity.this,"Phone number should be 10 digit",Toast.LENGTH_LONG).show();
                }
                else if(aadhar.length()!=12)
                {
                    pg.dismiss();
                    Toast.makeText(DonationActivity.this,"Aadhar should be 12 digit",Toast.LENGTH_LONG).show();
                }
                else if(map.isEmpty())
                {
                    pg.dismiss();
                    Toast.makeText(DonationActivity.this,"Turn on the location",Toast.LENGTH_LONG).show();
                }
                else
                {
                    reference = database.getReference().child("Donors").child(aadhar);
                    Donors donor = new Donors(quantity, phone, aadhar, description, latlang);

                    reference.setValue(donor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                pg.dismiss();
                                Toast.makeText(DonationActivity.this,"You Saved a Life Today",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                pg.dismiss();
                                Toast.makeText(DonationActivity.this,"OOPS! Please try again later",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == PERMISSION_REQUEST_CODE)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start listening for location updates
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, (android.location.LocationListener) locationListener);
                    }
                } else {
                    // Permission denied, handle accordingly
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            startActivity(new Intent(DonationActivity.this,DonationActivity.class));
            finish();
        }
    }

    private void fetchUserLocation() {
        // Request location updates
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop listening for location updates
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates((android.location.LocationListener) locationListener);
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}