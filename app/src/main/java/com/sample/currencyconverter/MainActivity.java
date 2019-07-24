package com.sample.currencyconverter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sample.currencyconverter.fragments.FragmentMap;

public class MainActivity extends AppCompatActivity {

    CustomPageAdapter mCustomPageAdapter;
    ViewPager mViewPager;
    public static double latitude = 55.7522;
    public static double longitude = 37.6156;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomPageAdapter = new CustomPageAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPageAdapter);
        mViewPager.getCurrentItem();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)
                && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new GpsUtils(this).turnGPSOn();
        }
        callPositionRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)
                && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new GpsUtils(MainActivity.this).turnGPSOn();
        }
    }

    private void callPositionRequest() {
        LocationRequest req = new LocationRequest();
        if (checkLocationPermission() && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Permission has already been granted
            final FusedLocationProviderClient locationClient = LocationServices.
                    getFusedLocationProviderClient(this);

            locationClient.requestLocationUpdates(req, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    latitude = locationResult.getLastLocation().getLatitude();
                    longitude = locationResult.getLastLocation().getLongitude();
                }
            }, null);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            callPositionRequest();
            return false;
        } else {
            FragmentMap.permissionsIsGranted = true;
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            callPositionRequest();
        }
    }

}
