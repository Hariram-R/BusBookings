package com.example.android.busbookings.Activitys;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.android.busbookings.Manifest;
import com.example.android.busbookings.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Polyline line;
    Marker driver_marker = null;
    LocationManager locationManager;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    int index, next;
    private List<LatLng> polyLineList;
    private float v;
    private double lat, lng;
    Handler handler;
    private LatLng startPosition, endPosition;
    LatLng newLatLng = null;

    LatLng LL1,LL2;
    String fromLL,toLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String fromDest = getIntent().getStringExtra("from");
        String toDest = getIntent().getStringExtra("to");

        LL1 = getLocationFromAddress(this,fromDest);
        LL2 = getLocationFromAddress(this,toDest);

        fromLL = LL1.latitude+","+LL1.longitude;
        toLL = LL2.latitude+","+LL2.longitude;


        if (checkLocationPermission()) {
            initMap();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                getRoute();

                LatLng latLng = new LatLng(LL1.latitude,LL1.longitude);

                driver_marker = mMap.addMarker(new MarkerOptions().position(latLng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus)));

                handler = new Handler();
                index = -1;
                next = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (index < polyLineList.size() - 1) {
                            index++;
                            next = index + 1;
                        }
                        if (index < polyLineList.size() - 1) {
                            startPosition = polyLineList.get(index);
                            endPosition = polyLineList.get(next);
                        }
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                        valueAnimator.setDuration(3000);
                        valueAnimator.setInterpolator(new LinearInterpolator());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                v = valueAnimator.getAnimatedFraction();
                                lng = v * endPosition.longitude + (1 - v)
                                        * startPosition.longitude;
                                lat = v * endPosition.latitude + (1 - v)
                                        * startPosition.latitude;
                                LatLng newPos = new LatLng(lat, lng);
                                driver_marker.setPosition(newPos);
                                driver_marker.setAnchor(0.5f, 0.5f);
                                driver_marker.setRotation(getBearing(startPosition, newPos));


                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(newPos).zoom(10.5f).build()));
                            }
                        });
                        valueAnimator.start();
                        handler.postDelayed(this, 3000);
                    }
                }, 3000);

                getLocation();
            }
        });
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork)
            {
                Toast.makeText(MapsActivity.this, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                this.canGetLocation = true;

                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {

                            if (driver_marker != null) {
                                driver_marker.remove();
                                driver_marker = null;
                            }

                            newLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                            driver_marker = mMap.addMarker(new MarkerOptions().position(newLatLng).flat(true));
                            driver_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void getRoute() {

        AndroidNetworking.get("https://maps.googleapis.com/maps/api/directions/json?origin=" + fromLL + "&" +
                "key= AIzaSyCnqn8Rxj2lRdGYppsCvEOFZbqUeeQiIxM" + "&" + "mode=driving&" + "destination=" + toLL).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            JSONArray jsonArray = jsonObject.getJSONArray("routes");

                            JSONObject object = jsonArray.getJSONObject(0).optJSONObject("overview_polyline");

                            String point = object.optString("points");
                            polyLineList = decodePoly(point);
                            List<LatLng> list = decodePoly(point);
                            LatLng src = null;
                            LatLng dest = null;
                            for (int z = 0; z < list.size() - 1; z++) {
                                src = list.get(z);
                                dest = list.get(z + 1);
                                line = mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(src.latitude, src.longitude),
                                                new LatLng(dest.latitude, dest.longitude))
                                        .width(10).color(Color.parseColor("#e74c3c")).geodesic(true));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(">>>>>ERROR");
                    }
                });

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;

    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }
}
