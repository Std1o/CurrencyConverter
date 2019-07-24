package com.sample.currencyconverter.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sample.currencyconverter.MainActivity;
import com.sample.currencyconverter.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sample.currencyconverter.MainActivity.latitude;
import static com.sample.currencyconverter.MainActivity.longitude;


public class FragmentMap extends Fragment implements OnMapReadyCallback {

    Boolean isStarted = false;
    Boolean isVisible = false;
    private GoogleMap mMap;
    public static boolean permissionsIsGranted = false;
    TextView textView;
    String maxtemp_c, mintemp_c, avgtemp_c, maxwind_kph, totalprecip_mm, condition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SaveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment2, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible && isStarted){

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isStarted && isVisible) {
            if (permissionsIsGranted && MainActivity.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                initMap();
                getCurrentLocation();
                textView = getActivity().findViewById(R.id.textView);
                getCurrentWeather();
            }
        }
    }

    private void getCurrentWeather() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://api.apixu.com/v1//forecast.json?key=a66fd22f5d0e48f4a4b203035192307&q=Moscow&days=2&lang=ru";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj;

                        try {
                            obj = new JSONObject(response);
                            JSONObject firstItem = obj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day");
                            maxtemp_c = (firstItem.getString("maxtemp_c"));
                            mintemp_c = (firstItem.getString("mintemp_c"));
                            avgtemp_c = (firstItem.getString("avgtemp_c"));
                            maxwind_kph = (firstItem.getString("maxwind_kph"));
                            totalprecip_mm = (firstItem.getString("totalprecip_mm"));
                            condition = firstItem.getJSONObject("condition").getString("text");
                            showWeather();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void showWeather() {
        textView.setText("Состояние: "  + condition + "\nМинимальная температура: " + mintemp_c + "°C" + "\nМаксимальная температура: " + maxtemp_c + "°C\nСредняя температура: " + avgtemp_c + "°C\nКоличество осадков: " + totalprecip_mm + " мм\nМаксимальная скорость ветра: " + maxwind_kph + " км/ч");
    }

    private void getCurrentLocation() {
        final FusedLocationProviderClient locationClient = LocationServices.
                getFusedLocationProviderClient(getContext());
        try {
            Task<Location> location = locationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.getResult() == null) {
                        getCurrentLocation();
                    }
                    else {
                        latitude = task.getResult().getLatitude();
                        longitude = task.getResult().getLongitude();
                    }
                    initMap();
                }
            });
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void initMap() {
        String MAP_FRAGMENT = "map_fragment";

        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
                .findFragmentByTag(MAP_FRAGMENT); // Check if map already exists

        if(mapFragment == null){
            // Create new Map instance if it doesn't exist
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.map, mapFragment, MAP_FRAGMENT)
                    .commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentPosition = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("Ваше местоположение"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
    }
}
