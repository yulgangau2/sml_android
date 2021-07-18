package com.eatyhero.customer.moretab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.base.MapScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 08-09-2016.
 */
public class AddAddressScreen extends BaseActivity implements ServerListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequest;

    //Create xml objects
    EditText addressTitleEditText,addressPhoneEditText,addressEditText,dorrNumberEditText;
    Button submit;
    ImageView backIconImageView;
    TextView actionBarTitleTextView;

    //GoogleMap Object
    GoogleApiClient googleApiClient;
    public Location location;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    String addressid,screenName="",addressTitle="",addressPhone="",address="",doorNumber="",latitude="0",longitude="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_screen);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        addressTitleEditText     = (EditText)findViewById(R.id.addressTitleEditText);
        addressPhoneEditText     = (EditText)findViewById(R.id.addressPhoneEditText);
        addressEditText          = (EditText)findViewById(R.id.addressEditText);
        dorrNumberEditText       = (EditText)findViewById(R.id.dorrNumberEditText);
        submit                   = (Button)findViewById(R.id.submitButton);
        backIconImageView        =(ImageView)toolbar.findViewById(R.id.backIconImageView);
        actionBarTitleTextView        = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);


        //Initialize Objects
        loginSession     = LoginSession.getInstance(this);
        serverRequest    = ServerRequest.getInstance(this);
        googleApiClient   = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(AddAddressScreen.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Intent intent = getIntent();

        if(intent!=null){

            addressid    = intent.getStringExtra("addressId");
            screenName   = intent.getStringExtra("screen");
            addressTitle = intent.getStringExtra("title");
            addressPhone = intent.getStringExtra("phone");
            address      = intent.getStringExtra("address");
            doorNumber   = intent.getStringExtra("doorNumber");
            latitude   = intent.getStringExtra("latitude");
            longitude   = intent.getStringExtra("longitude");

            if(screenName.equalsIgnoreCase("Edit Address")){

                addressTitleEditText.setText(address);
                addressPhoneEditText.setText(addressPhone);
                addressEditText.setText(addressTitle);
                dorrNumberEditText.setText(doorNumber);
                actionBarTitleTextView.setText(getResources().getString(R.string.editAddress));

            }else{
                actionBarTitleTextView.setText(getResources().getString(R.string.addAddress));
                latitude = "0";
                longitude="0";
            }
        }

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addressTitleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddAddressScreen.this, MapScreen.class);
                intent.putExtra("screen",1);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivityForResult(intent, 2);
//                startActivity(intent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressTitle = addressTitleEditText.getText().toString().trim();
                String address      = addressEditText.getText().toString();
                  doorNumber        = dorrNumberEditText.getText().toString();

                //Validation Part
                if (addressTitle.isEmpty()) {

                    addressTitleEditText.setError(getResources().getString(R.string.enterTitle));

                }else if(doorNumber.isEmpty()){

                    dorrNumberEditText.setError(getResources().getString(R.string.enterDoorNumber));

                }else if(address.isEmpty()){

                    Log.e("address","address");

                    addressEditText.setError(getResources().getString(R.string.enterAddress));

                }else if(!checkInternet()){

                    noInternetAlertDialog();

                } else{

                    //Server request listener

                    if(screenName.equalsIgnoreCase("Edit Address")){

                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id",loginSession.getUserId());
                        params.put("page","AddressBook");
                        params.put("action", "MyAccount");
                        params.put("title",address);
                        params.put("addressBookId",addressid);
                        params.put("address", addressTitle);
                        params.put("addressAction", "AddressBookEdit");
                        params.put("flat_no", doorNumber);
                        params.put("latitude", loginSession.getcurrlat());
                        params.put("longitude", loginSession.getcurrlng());
                        showProgressDialog();
                        serverRequest.createRequest(AddAddressScreen.this,params, RequestID.REQ_EDIT_ADDRESS);

                    }else{

                        Map<String, String> params = new HashMap<>();
                        params.put("action", "MyAccount");
                        params.put("page","AddressBookAdd");
                        params.put("title",address);
                        params.put("flat_no", doorNumber);
                        params.put("address", addressTitle);
                        params.put("customer_id",loginSession.getUserId());
                        params.put("latitude", loginSession.getcurrlat());
                        params.put("longitude", loginSession.getcurrlng());
                        showProgressDialog();
                        serverRequest.createRequest(AddAddressScreen.this,params, RequestID.REQ_ADD_ADDRESS);
                    }

                }
            }
        });

//        addressEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (ActivityCompat.checkSelfPermission(AddAddressScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    // Check Permissions Now
//                    ActivityCompat.requestPermissions(AddAddressScreen.this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                            REQUEST_LOCATION_PERMISSION);
//                } else {
//
//                    openAutocompleteActivity();
//                    // permission has been granted, continue as usual
//                    Location myLocation =
//                            LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                }
//
//            }
//        });


    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID){

            case REQ_ADD_ADDRESS:

                toast(result.toString());
                setResult(1);
                finish();

                break;

            case REQ_EDIT_ADDRESS:

                toast(result.toString());
                finish();

                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
    }

    private void openAutocompleteActivity() {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {

            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);


            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        googleApiClient.connect();
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        SharedPreferences sp = getSharedPreferences("LoginInfos", 0);
//        addressTitleEditText.setText(sp.getString("address", ""));
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                addressEditText.setText(String.valueOf(place.getAddress()));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("Error: Status = ", "" + status.toString());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                addressTitleEditText.setText(data.getStringExtra("address"));

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
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

                openAutocompleteActivity();
                Location myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}
