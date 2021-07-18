package com.eatyhero.customer.moretab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 22-03-2017.
 */

public class NewAddAddressScreen extends Fragment implements ServerListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequest;
    Utility utility;
    Bundle bundle = new Bundle();

    //Create xml objects
    EditText addressTitleEditText, addressPhoneEditText, addressEditText;
    Button submit;
    TextView actionBarTitleTextView;
    ImageView backIconImageView;

    //GoogleMap Object
    GoogleApiClient googleApiClient;
    public Location location;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    //Strings
    String addressid, screenName, addressTitle, addressPhone, address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_address_screen, container, false);

        //Initialize xml objects
        addressTitleEditText   = (EditText) rootView.findViewById(R.id.addressTitleEditText);
        backIconImageView      = (ImageView) rootView.findViewById(R.id.backIconImageView);
        actionBarTitleTextView = (TextView) rootView.findViewById(R.id.actionBarTitleTextView);
        addressPhoneEditText   = (EditText) rootView.findViewById(R.id.addressPhoneEditText);
        addressEditText        = (EditText) rootView.findViewById(R.id.addressEditText);
        submit                 = (Button) rootView.findViewById(R.id.submitButton);

        //Initialize Objects
        loginSession        = LoginSession.getInstance(getActivity());
        utility             = Utility.getInstance(getActivity());
        serverRequest       = ServerRequest.getInstance(getActivity());


        googleApiClient     = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(NewAddAddressScreen.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewAddressBookScreen newInfoScreen = new NewAddressBookScreen();
                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                ft3.replace(R.id.frameLayout, newInfoScreen);
                ft3.commit();


            }
        });

        bundle = getArguments();
        if (bundle != null) {

            addressid    = bundle.getString("addressId");
            screenName   = bundle.getString("screen");
            addressTitle = bundle.getString("title");
            addressPhone = bundle.getString("phone");
            address      = bundle.getString("address");

            actionBarTitleTextView.setText(screenName);

            if (screenName.equalsIgnoreCase("Edit Address")) {

                addressTitleEditText.setText(addressTitle);
                addressPhoneEditText.setText(addressPhone);
                addressEditText.setText(address);
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressTitle = addressTitleEditText.getText().toString();
                String addressPhone = addressPhoneEditText.getText().toString();
                String address      = addressEditText.getText().toString();

                //Validation Part
                if (addressTitle.isEmpty()) {

                    addressTitleEditText.setError(getResources().getString(R.string.enterTitle));

                } else if (addressPhone.isEmpty()) {

                    addressPhoneEditText.setError(getResources().getString(R.string.pleaseEnterPhoneNumber));

                } else if (!(addressPhone.length() == 10)) {

                    addressPhoneEditText.setError(getResources().getString(R.string.enterValidPhoneNumber));

                } else if (address.isEmpty()) {

                    addressEditText.setError(getResources().getString(R.string.enterAddress));

                } else if (!utility.isConnectingToInternet()) {

                    utility.noInternetAlertDialog();

                } else {

                    //Server request listener
                    if (screenName.equalsIgnoreCase("Edit Screen")) {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "MyAccount");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("page", "AddressBook");
                        params.put("address_title", addressTitle);
                        params.put("address_phone", addressPhone);
                        params.put("addressBookId", addressid);
                        params.put("google_address", address);
                        utility.showProgressDialog();
                        serverRequest.createRequest(NewAddAddressScreen.this, params, RequestID.REQ_EDIT_ADDRESS);

                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "MyAccount");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("page", "AddressBookAdd");
                        params.put("address_title", addressTitle);
                        params.put("address_phone", addressPhone);
                        params.put("address", address);
                        params.put("google_address", address);
                        utility.showProgressDialog();
                        serverRequest.createRequest(NewAddAddressScreen.this, params, RequestID.REQ_ADD_ADDRESS);
                    }


                }
            }
        });

        addressEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                } else {

                    openAutocompleteActivity();
                    // permission has been granted, continue as usual
                    Location myLocation =
                            LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                }


            }
        });

        return rootView;
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        utility.hideProgressDialog();
        switch (requestID) {

            case REQ_ADD_ADDRESS:

                utility.toast(result.toString());
                Fragment fragment = new NewAddressBookScreen();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
                break;

            case REQ_EDIT_ADDRESS:

                utility.toast(result.toString());
                Fragment fragment1 = new NewAddressBookScreen();
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.frameLayout, fragment1);
                fragmentTransaction1.commit();

                break;

        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        utility.hideProgressDialog();
        utility.toast(error);
    }

    private void openAutocompleteActivity() {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, 1);

        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {

            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);


            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                addressEditText.setText(String.valueOf(place.getAddress()));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e("Error: Status = ", "" + status.toString());
            } else if (resultCode == RESULT_CANCELED) {

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
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
