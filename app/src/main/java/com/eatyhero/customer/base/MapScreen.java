package com.eatyhero.customer.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapScreen extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ServerListener {


    LocationManager locationManager;
    View mapView;
    GoogleApiClient mGoogleApiClient;
    LoginSession loginSession;
    GoogleMap mMap;
    String address;
    double currentLat, currentLng;
    @BindView(R.id.addressText)
    TextView addressText;
    @BindView(R.id.mapResButton)
    Button mapResButton;
    @BindView(R.id.backIconImageView)
    ImageView backIconImageView;
    @BindView(R.id.actionBarTitleTextView)
    TextView actionBarTitleTextView;
    private Location mylocation;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    Utility utility;
    int screen;
    String latitude, longitude;
    //latitude of location
    double myLatitude;

    //longitude og location
    double myLongitude;

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
        ButterKnife.bind(this);
        utility = Utility.getInstance(this);
        loginSession = LoginSession.getInstance(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Intent intent = getIntent();
        if (intent != null) {
            screen = intent.getIntExtra("screen", 3);
            if (screen == 1) {
                latitude = intent.getStringExtra("latitude");
                longitude = intent.getStringExtra("longitude");
            }
        }

        if (screen == 1) {
            mapResButton.setText(getResources().getString(R.string.addAddress));
        } else if (screen == 2) {
            mapResButton.setText(getResources().getString(R.string.findRestaurant));
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();

            }
        } else {
            buildGoogleApiClient();

        }

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mapResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (screen == 1) {
                    loginSession.setcurrlat(String.valueOf(currentLat));
                    loginSession.setcurrlng(String.valueOf(currentLng));
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (screen == 2) {
                    utility.APP_START_FRAGMENT = "searchButton";
                    utility.CURRENT_SCREEN = "";

                    loginSession.setCityid("");
                    loginSession.setCityidRes("");
                    loginSession.setCityidCus("");
                    loginSession.setResid("");
                    loginSession.setcusid("");
                    loginSession.setSubDis("");
                    loginSession.saveLocation(address);
                    loginSession.setsearchType("");
                    loginSession.setcurrlat(String.valueOf(currentLat));
                    loginSession.setcurrlng(String.valueOf(currentLng));

                    Intent i = new Intent(MapScreen.this, NewBaseHomeScreen.class);
                    startActivity(i);
                    finish();
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            //layoutParams.setMargins(0, 0, 40, 200);
        }


        currentLat = mMap.getCameraPosition().target.latitude;
        currentLng = mMap.getCameraPosition().target.longitude;
        Log.e("current Latlng", mMap.getCameraPosition().target.latitude + " & " + mMap.getCameraPosition().target.longitude);
        if (currentLat == 0.0 && currentLng == 0.0) {

            // Create a criteria object to retrieve provider
            //Criteria criteria = new Criteria();

            // Get the name of the best provider
            //String provider = locationManager.getBestProvider(criteria, true);

            // Get Current Location
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                myLatitude = location.getLatitude();

                                //longitude og location
                                myLongitude = location.getLongitude(); //Logic to handle location object

                                LatLng editlocation = new LatLng(myLatitude, myLongitude);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(editlocation, 18));
                                Log.e("current Latlng", mMap.getCameraPosition().target.latitude + " & " + mMap.getCameraPosition().target.longitude);

                            }
                        }


                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                            getMyLocation();
                            e.printStackTrace();
                        }
                    });

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            if (screen == 1 && !(myLatitude == 0)) {
                LatLng editlocation = new LatLng(myLatitude, myLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(editlocation, 18));
                Log.e("current Latlng", mMap.getCameraPosition().target.latitude + " & " + mMap.getCameraPosition().target.longitude);

            } else {
                LatLng cuslatLng = new LatLng(myLatitude, myLongitude);
//                                                    cusMarker = mMap.addMarker(new MarkerOptions()
//                                                            .position(cuslatLng)
//                                                            .draggable(true)
//                                                            .title("My Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLng, 18));
                Log.e("current Latlng", mMap.getCameraPosition().target.latitude + " & " + mMap.getCameraPosition().target.longitude);

            }
        }


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                currentLat = mMap.getCameraPosition().target.latitude;
                currentLng = mMap.getCameraPosition().target.longitude;
                Log.e("current Latlng", mMap.getCameraPosition().target.latitude + " & " + mMap.getCameraPosition().target.longitude);
                if (currentLat == 0.0 && currentLng == 0.0) {

                } else {
                    getAddress();
                }

            }
        });

    }


    private void getAddress() {

        boolean fulladdress;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        if(!Geocoder.isPresent()) {
            toast("This device does not have geo coder.Unable to pin location.");
        }

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() == 0) {

                toast(getResources().getString(R.string.pleaseChoooseAnotherLocation));
            } else {
                Log.e("full address", "+" + addresses);
                if (addresses.get(0).getAddressLine(0).isEmpty()) {
                    fulladdress = false;
                } else {
                    fulladdress = true;
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                }
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                if (fulladdress) {
                    addressText.setText(address);
                } else {
                    addressText.setText(city + ", " + state + ", " + country);
                }
//            addressText.setText(city+", "+state+", "+country);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onConnected(@Nullable Bundle bundle) {

        checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

    }

    public void getCurrentLocation() {

    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MapScreen.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(MapScreen.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (mylocation == null) {
                                                    showProgressDialog();
                                                    getMyLocation();
                                                    Log.e("permission", "entered");
                                                } else {
                                                    hideProgressDialog();
                                                    if (screen == 1 && !latitude.equalsIgnoreCase("0")) {
                                                        LatLng editlocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(editlocation, 18));
                                                    } else {
                                                        LatLng cuslatLng = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
//                                                    cusMarker = mMap.addMarker(new MarkerOptions()
//                                                            .position(cuslatLng)
//                                                            .draggable(true)
//                                                            .title("My Location"));
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cuslatLng, 18));
                                                    }
                                                }

                                            }
                                        }, 1000);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MapScreen.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(MapScreen.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(MapScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }
}
