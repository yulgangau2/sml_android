package com.eatyhero.customer.ordertab;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.fcm.Config;
import com.eatyhero.customer.service.ServerRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.MarkerOption;
import com.teliver.sdk.models.TrackingBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTracking extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Create class objects
    Dialog successDialog;
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;
    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker cusMarker, mDriverMarker;
    //Marker resMarker;
    LocationRequest mLocationRequest;

    String orderId = "", refNumber = "", orderStatus = "";

    //LatLang values
    double driverlatitude;
    double driverlongitude;
    double resLatitude;
    double resLongitude;
    double cusLatitude;
    double cusLongitude;

    //Create Polyline objects
    private static RequestQueue queue = null;
    PolylineOptions polylineOptions;
    Polyline polyline;
    List<LatLng> rout_points = new ArrayList<LatLng>();

    //Create xml file
    TextView actionBarTitleTextView;
    ImageView backIconImageView;
    TextView estimationKm, estimationTime;
    RelativeLayout estimationLayout;

    boolean screenAdjustOnFirstTime = true;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_route_screen);

        //Check runtime location permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize xml objects
        actionBarTitleTextView = (TextView) findViewById(R.id.actionBarTitleTextView);
        backIconImageView = (ImageView) findViewById(R.id.backIconImageView);
        estimationKm = (TextView) findViewById(R.id.estimationKm);
        estimationTime = (TextView) findViewById(R.id.estimationTime);

        //initialize objects
        utility = Utility.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);
        loginSession = LoginSession.getInstance(this);

        polylineOptions = new PolylineOptions();
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        loginSession.saveTrackLocation("", "");
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra("orderId");
            refNumber = intent.getStringExtra("ref_number");
            actionBarTitleTextView.setText(refNumber);
            orderStatus = intent.getStringExtra("status");
            resLatitude = Double.parseDouble(intent.getStringExtra("source_latitude"));
            resLongitude = Double.parseDouble(intent.getStringExtra("source_longitude"));
            cusLatitude = Double.parseDouble(intent.getStringExtra("destination_latitude"));
            cusLongitude = Double.parseDouble(intent.getStringExtra("destination_longitude"));

        }

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                boolean check_update = intent.getBooleanExtra("update", false);

                if (check_update) {

                    Log.e("messageOrderTrackiong", intent.getStringExtra("message"));

                    if (intent.getStringExtra("message").contains("Delivered")) {

                        successDialog = new Dialog(OrderTracking.this);
                        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        successDialog.setContentView(R.layout.layout_completed);
                        successDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                successDialog.dismiss();
                                finish();

                            }
                        }, 1000);

                        successDialog.setCancelable(false);
                        successDialog.show();

                    }

                }
            }
        };

    }

    private void getDriverCustomerRoot(double driverlatitude, double driverlongitude) {

        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + driverlatitude + "," + driverlongitude + "&destination=" + cusLatitude + "," + cusLongitude + "&sensor=false&units=metric&mode=driving";

        final Map<String, String> param = new HashMap<>();
        // POST parameters:
        param.put("action", "location");

        Log.e("server data", "" + param);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String overview_polylines = null;
                        String status;
                        Log.e("Processing orders", "" + response);

                        try {
                            JSONObject jsonObject2 = new JSONObject(response);

                            status = jsonObject2.getString("status");

                            if (status.equals("OK")) ;
                            {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray array = jsonObject.getJSONArray("routes");
                                JSONObject routes = array.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                estimationKm.setText(distance.getString("text"));
                                JSONObject duration = steps.getJSONObject("duration");
                                estimationTime.setText(duration.getString("text"));
                            }

                            if (status.equalsIgnoreCase("ZERO_RESULTS")) {
                                utility.toast(getResources().getString(R.string.routNotFound));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }
        };
        queue.add(postRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(false);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

                if(!String.valueOf(cusLatitude).isEmpty() && !String.valueOf(cusLongitude).isEmpty() ){

                    LatLng cuslatLng = new LatLng(cusLatitude, cusLongitude);
                    cusMarker = googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker))
                            .position(cuslatLng)
                            .draggable(true)
                            .title("Customer Position"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLng, 18));


                    MarkerOption option = new MarkerOption(orderId);
                    option.setMarkerTitle("Driver location");
                    option.setIconMarker(R.drawable.car_icon);
                    Teliver.startTracking(new TrackingBuilder(option).withYourMap(googleMap).build());

                    CameraUpdate zout = CameraUpdateFactory.zoomBy(15);
                    googleMap.animateCamera(zout);

                }

                /*if(!String.valueOf(driverlatitude).isEmpty() && !String.valueOf(driverlongitude).isEmpty() ){

                    LatLng reslatLng = new LatLng(driverlatitude,driverlongitude);
                    resMarker = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_marker))
                            .position(reslatLng)
                            .draggable(true)
                            .title("Driver Position"));
                }

                if(!String.valueOf(cusLatitude).isEmpty() && !String.valueOf(cusLongitude).isEmpty() ){

                    LatLng cuslatLng = new LatLng(cusLatitude, cusLongitude);
        cusMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker))
                .position(cuslatLng)
                .draggable(true)
                .title("Customer Position"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLng, 18));
                }

             //   getDriverCustomerRoot(driverlatitude,driverlongitude);

                //Check order status to change map view
                if(!orderStatus.equalsIgnoreCase("Driver Accepted")){

                    List<Marker> markersList = new ArrayList<Marker>();

                    markersList.add(cusMarker);
                    markersList.add(mDriverMarker);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker m : markersList) {
                        builder.include(m.getPosition());
                    }
                    *//**initialize the padding for map boundary*//*
                    int padding = 50;
                    *//**create the bounds from latlngBuilder to set into map camera*//*
                    LatLngBounds bounds = builder.build();
                    *//**create the camera with bounds and padding to set into map*//*
                    final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    *//**call the map call back to know map is loaded or not*//*
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            *//**set animated zoom camera into map*//*
                            mMap.animateCamera(cu);

                        }
                    });


                    LatLng latLngA = new LatLng(cusLatitude,cusLongitude);
                    LatLng latLngB = new LatLng(driverlatitude,driverlongitude);

                    Location locationA = new Location("point A");
                    locationA.setLatitude(latLngA.latitude);
                    locationA.setLongitude(latLngA.longitude);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(latLngB.latitude);
                    locationB.setLongitude(latLngB.longitude);

                    double distance = locationA.distanceTo(locationB);

                    Log.e("distance",""+distance);

                }*/

            }
        }
        else {
            buildGoogleApiClient();

            if(!String.valueOf(cusLatitude).isEmpty() && !String.valueOf(cusLongitude).isEmpty() ){

                LatLng cuslatLng = new LatLng(cusLatitude, cusLongitude);
                cusMarker = googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker))
                        .position(cuslatLng)
                        .draggable(true)
                        .title("Customer Position"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLng, 18));

                MarkerOption option = new MarkerOption(orderId);
                option.setMarkerTitle("Driver location");
                option.setIconMarker(R.drawable.car_icon);
                Teliver.startTracking(new TrackingBuilder(option).withYourMap(googleMap).build());

                CameraUpdate zout = CameraUpdateFactory.zoomBy(15);
                googleMap.animateCamera(zout);

            }
          /*  if(!String.valueOf(driverlatitude).isEmpty() && !String.valueOf(driverlongitude).isEmpty() ){

                LatLng reslatLng = new LatLng(driverlatitude,driverlongitude);
                resMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_marker))
                        .position(reslatLng)
                        .draggable(true)
                        .title("Driver Position"));
            }

            if(!String.valueOf(cusLatitude).isEmpty() && !String.valueOf(cusLongitude).isEmpty() ){

                LatLng cuslatLng = new LatLng(cusLatitude,cusLongitude);
                cusMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker))
                        .position(cuslatLng)
                        .draggable(true)
                        .title("Customer Position"));
            }

            //Check order status to change map view
            if(!orderStatus.equalsIgnoreCase("Driver Accepted")){

                List<Marker> markersList = new ArrayList<Marker>();

                markersList.add(cusMarker);
                markersList.add(mDriverMarker);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker m : markersList) {
                    builder.include(m.getPosition());
                }
                *//**initialize the padding for map boundary*//*
                int padding = 50;
                *//**create the bounds from latlngBuilder to set into map camera*//*
                LatLngBounds bounds = builder.build();
                *//**create the camera with bounds and padding to set into map*//*
                final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                *//**call the map call back to know map is loaded or not*//*
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        *//**set animated zoom camera into map*//*
                        mMap.animateCamera(cu);

                    }
                });


                LatLng latLngA = new LatLng(cusLatitude,cusLongitude);
                LatLng latLngB = new LatLng(driverlatitude,driverlongitude);

                Location locationA = new Location("point A");
                locationA.setLatitude(latLngA.latitude);
                locationA.setLongitude(latLngA.longitude);
                Location locationB = new Location("point B");
                locationB.setLatitude(latLngB.latitude);
                locationB.setLongitude(latLngB.longitude);

                double distance = locationA.distanceTo(locationB);

            }
*/
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }

        //Place current location marker
        /*LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
*/
        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission(){
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
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(false);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, getResources().getString(R.string.permissionDenied), Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void animateMarker(final LatLng toPosition, final boolean hideMarke) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(mDriverMarker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                mDriverMarker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarke) {
                        mDriverMarker.setVisible(false);
                    } else {
                        mDriverMarker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utility.update_check = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.update_check = false;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.update_check = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

    }

}
