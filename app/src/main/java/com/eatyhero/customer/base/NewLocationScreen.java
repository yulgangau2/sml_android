package com.eatyhero.customer.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.LocationModel;
import com.eatyhero.customer.model.SearchOptionList;
import com.eatyhero.customer.model.SettingsList;
import com.eatyhero.customer.model.SubDistricList;
import com.eatyhero.customer.restauranttab.RestaurantDetailsScreen;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 22-01-2018.
 */

public class NewLocationScreen extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ServerListener, View.OnClickListener {

    // Create Objects
    LoginSession loginSession;
    Utility utility;
    GoogleApiClient googleApiClient;
    ServerRequest createRequest;
    public Location location;
    DatabaseManager databaseManager;

    //Create xml file
    @BindView(R.id.compassImage)
    ImageView compassImage;
    @BindView(R.id.fetchLocation)
    RelativeLayout fetchLocation;

    @BindView(R.id.cityLayout)
    RelativeLayout cityLayout;
    @BindView(R.id.restaurantLayout)
    RelativeLayout restaurantLayout;
    @BindView(R.id.cuisineLayout)
    RelativeLayout cuisineLayout;

    @BindView(R.id.cityImageView)
    ImageView cityImageView;
    @BindView(R.id.restaurantImageView)
    ImageView restaurantImageView;
    @BindView(R.id.cuisineImageView)
    ImageView cuisineImageView;

    @BindView(R.id.cityTextView)
    TextView cityTextView;
    @BindView(R.id.restaurantTextView)
    TextView restaurantTextView;
    @BindView(R.id.cuisineTextView)
    TextView cuisineTextView;
    @BindView(R.id.firstEdit)
    EditText firstEdit;
    @BindView(R.id.secondEdit)
    EditText secondEdit;
    @BindView(R.id.thirdEdit)
    EditText thirdEdit;
    @BindView(R.id.fourthEdit)
    EditText fourthEdit;
    @BindView(R.id.searchrestaurant)
    Button searchrestaurant;

    @BindView(R.id.thai)
    TextView thai;
    @BindView(R.id.english)
    TextView english;


    public static LinearLayout tablehistoryButton, orderhistoryButton, infoButton, favoriteButton;
    ImageView menuOpenButton;
    CardView baseTabLayout;

    boolean bottomOpen = true;

    String cityName = "";
    String cityid = "", resid = "", cusid = "", subdisid = "", restaturant_id = "";
    String searchBy = "1";

    Dialog citytListDialog;
    CityAdapter cityAdapter;
    CuisineAdapter cuisineAdapter;
    RestaurantAdapter restaurantAdapter;
    SubDisAdapter subDisAdapter;
    String languageToLoad;
    String mSearchText;
    ArrayList<SearchOptionList.Citylist> citiesArrayList = new ArrayList<>();
    ArrayList<SearchOptionList.Restaurantlist> resArrayList = new ArrayList<>();
    ArrayList<SearchOptionList.Cuisineslist> cuisineArrayList = new ArrayList<>();
    ArrayList<SubDistricList.Distlist> subDistric = new ArrayList<>();
    ArrayList<SubDistricList.Sub> branchDistric = new ArrayList<>();
    ArrayList<String> commonDistric = new ArrayList<>();
    int REQUEST_LOCATION_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.location_screen);

        //Initialize xml files
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        tablehistoryButton = (LinearLayout) findViewById(R.id.tablehistoryButton);
        orderhistoryButton = (LinearLayout) findViewById(R.id.orderhistoryButton);
        infoButton = (LinearLayout) findViewById(R.id.infoButton);
        favoriteButton = (LinearLayout) findViewById(R.id.favoriteButton);
        menuOpenButton = (ImageView) findViewById(R.id.menuOpenButton);
        baseTabLayout = (CardView) findViewById(R.id.baseTabLayout);

        //Initialize objects
        loginSession = LoginSession.getInstance(this);
        createRequest = ServerRequest.getInstance(this);
        databaseManager = DatabaseManager.getInstance(this);
        utility = Utility.getInstance(this);

        //empty  data
        utility.listResponse = "empty";

        if (loginSession.getlanguage().isEmpty()) {
            loginSession.setlanguage("2");

            setLanguage("th");
        }

        checkPermission();

        if (loginSession.getlanguage().equalsIgnoreCase("1")) {
            english.setTextColor(getResources().getColor(R.color.white));
            english.setBackground(getResources().getDrawable(R.color.colorPrimary));
            thai.setTextColor(getResources().getColor(R.color.black));
            thai.setBackground(getResources().getDrawable(R.color.white));
        } else {
//                language("th");
            thai.setTextColor(getResources().getColor(R.color.white));
            thai.setBackground(getResources().getDrawable(R.color.colorPrimary));
            english.setTextColor(getResources().getColor(R.color.black));
            english.setBackground(getResources().getDrawable(R.color.white));
        }
   /*
        if (loginSession.getlanguage().equals("3")) {
            loginSession.setlanguage("2");

            setLanguage("th");
        }*/

        //googleApiClient initialize
        try {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //SetClickEvents
        cityLayout.setOnClickListener(this);
        restaurantLayout.setOnClickListener(this);
        cuisineLayout.setOnClickListener(this);


       /* areaNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaNameEditText.setEnabled(false);
                openAutocompleteActivity();
            }
        });*/

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSession.setlanguage("1");

                setLanguage("en");
            }
        });

        thai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSession.setlanguage("2");

                setLanguage("th");
            }
        });

        //compassImage click listener
        compassImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getLatLng();
            }
        });

        fetchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    Intent intent = new Intent(NewLocationScreen.this, MapScreen.class);
                    intent.putExtra("screen", 2);
                    startActivity(intent);
                }


            }
        });

        tablehistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.APP_START_FRAGMENT = "tablehistoryButton";
                openBaseScreen();
            }
        });

        orderhistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.APP_START_FRAGMENT = "orderhistoryButton";
                openBaseScreen();

            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.APP_START_FRAGMENT = "infoButton";
                openBaseScreen();
            }
        });
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.APP_START_FRAGMENT = "favoriteButton";
                openBaseScreen();
            }
        });

        menuOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bottomOpen) {

                    bottomOpen = false;
                    Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.baseTabLayout);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);

                } else {

                    bottomOpen = true;
                    Animation bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    ViewGroup hiddenPanel = (ViewGroup) findViewById(R.id.baseTabLayout);
                    hiddenPanel.startAnimation(bottomDown);
                    hiddenPanel.setVisibility(View.GONE);

                }

            }
        });

        searchrestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utility.APP_START_FRAGMENT = "searchButton";
                utility.CURRENT_SCREEN = "";

//                if (cityid.equals("")&& resid.equals("")&&cusid.equals("")&& subdisid.equals("")) {
                if (searchBy.equalsIgnoreCase("1") && !cityid.equalsIgnoreCase("")) {


                    loginSession.setCityid(cityid);
                    loginSession.setCityidRes("");
                    loginSession.setCityidCus("");
                    loginSession.setResid("");
                    loginSession.setcusid("");
                    loginSession.setSubDis(subdisid);
                    loginSession.saveLocation("");
                    loginSession.setsearchType("city");

                    Intent i = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
                    startActivity(i);


                } else if (searchBy.equalsIgnoreCase("2") && (!cityid.equalsIgnoreCase("") || !resid.equalsIgnoreCase(""))) {

                    loginSession.setCityid("");
                    loginSession.setCityidRes(cityid);
                    loginSession.setCityidCus("");
                    loginSession.setResid(resid);
                    loginSession.setcusid("");
                    loginSession.setSubDis("");
                    loginSession.saveLocation("");
                    loginSession.setsearchType("restaurant");

                    Intent intent = new Intent(NewLocationScreen.this, RestaurantDetailsScreen.class);
                    intent.putExtra("res_id", restaturant_id);
                    startActivity(intent);


                } else if (searchBy.equalsIgnoreCase("3") && (!cityid.equalsIgnoreCase("") || !cusid.equalsIgnoreCase(""))) {


                    loginSession.setCityid("");
                    loginSession.setCityidRes("");
                    loginSession.setCityidCus(cityid);
                    loginSession.setResid("");
                    loginSession.setcusid(cusid);
                    loginSession.setSubDis("");
                    loginSession.saveLocation("");
                    loginSession.setsearchType("cuisines");

                    Intent i = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
                    startActivity(i);


                } else {
                    toast(getResources().getString(R.string.pleaseSelectAChoise));
                }
//                }else {
//                    toast("Please selectt a choise");
//                }
            }
        });
/*

        if (checkInternet()) {
            Log.e("sitesetting", "sitesetting");
            Map<String, String> params = new HashMap<>();
            if (loginSession.isLoggedIn()) {
                params.put("customer_id", loginSession.getUserId());
                params.put("device_id", loginSession.getFcmId());
                params.put("device_type", "ANDROID");
            } else {

                params.put("customer_id", "");
                params.put("device_id", "");
                params.put("device_type", "");

            }
            showProgressDialog();
            createRequest.createRequest(NewLocationScreen.this, params, RequestID.REQ_SETTINGS);

        } else {
            noInternetAlertDialog();
        }
*/

        firstEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                citytListDialog = new Dialog(NewLocationScreen.this);
                citytListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                citytListDialog.setContentView(R.layout.dialog_for_citylist);
                citytListDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                citytListDialog.setCancelable(true);
                citytListDialog.show();

                if (citiesArrayList != null) {

                    final EditText searchRestaurantEditText = (EditText) citytListDialog.findViewById(R.id.searchRestaurantEditText);
                    searchRestaurantEditText.setHint(getResources().getString(R.string.selectCity));
                    ListView restaurantListView = (ListView) citytListDialog.findViewById(R.id.restaurantListView);

                    cityAdapter = new CityAdapter(NewLocationScreen.this, citiesArrayList);
                    restaurantListView.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();

                    searchRestaurantEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            try {
                                String filter_text = searchRestaurantEditText.getText().toString().trim().toLowerCase(Locale.getDefault());
                                cityAdapter.filter(filter_text);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

//*************************************************************************************
        thirdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                citytListDialog = new Dialog(NewLocationScreen.this);
                citytListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                citytListDialog.setContentView(R.layout.dialog_for_citylist);
                citytListDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                citytListDialog.setCancelable(true);
                citytListDialog.show();

                if (resArrayList != null) {
                    final EditText searchRestaurantEditText = (EditText) citytListDialog.findViewById(R.id.searchRestaurantEditText);
                    searchRestaurantEditText.setHint(getResources().getString(R.string.selectRestaurant));
                    ListView restaurantListView = (ListView) citytListDialog.findViewById(R.id.restaurantListView);

                    restaurantAdapter = new RestaurantAdapter(NewLocationScreen.this, resArrayList);
                    restaurantListView.setAdapter(restaurantAdapter);
                    restaurantAdapter.notifyDataSetChanged();

                    searchRestaurantEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            try {
                                String filter_text = searchRestaurantEditText.getText().toString().trim().toLowerCase(Locale.getDefault());
                                restaurantAdapter.filter(filter_text);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //*************************************************************************************

        fourthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                citytListDialog = new Dialog(NewLocationScreen.this);
                citytListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                citytListDialog.setContentView(R.layout.dialog_for_citylist);
                citytListDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                citytListDialog.setCancelable(true);
                citytListDialog.show();

                if (cuisineArrayList != null) {
                    final EditText searchRestaurantEditText = (EditText) citytListDialog.findViewById(R.id.searchRestaurantEditText);
                    searchRestaurantEditText.setHint(getResources().getString(R.string.selectCuisine));
                    ListView restaurantListView = (ListView) citytListDialog.findViewById(R.id.restaurantListView);

                    cuisineAdapter = new CuisineAdapter(NewLocationScreen.this, cuisineArrayList);
                    restaurantListView.setAdapter(cuisineAdapter);
                    cuisineAdapter.notifyDataSetChanged();

                    searchRestaurantEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            try {
                                String filter_text = searchRestaurantEditText.getText().toString().trim().toLowerCase(Locale.getDefault());
                                cuisineAdapter.filter(filter_text);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        secondEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cityName.equalsIgnoreCase("")) {
                    toast(getResources().getString(R.string.pleaseSelectCity));
                } else {

                    citytListDialog = new Dialog(NewLocationScreen.this);
                    citytListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    citytListDialog.setContentView(R.layout.dialog_for_citylist);
                    citytListDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    citytListDialog.setCancelable(true);
                    citytListDialog.show();

                    final EditText searchRestaurantEditText = (EditText) citytListDialog.findViewById(R.id.searchRestaurantEditText);
                    searchRestaurantEditText.setHint(getResources().getString(R.string.selectSubdistric));
                    ListView restaurantListView = (ListView) citytListDialog.findViewById(R.id.restaurantListView);

                    subDisAdapter = new SubDisAdapter(NewLocationScreen.this, commonDistric);
                    restaurantListView.setAdapter(subDisAdapter);
                    subDisAdapter.notifyDataSetChanged();

                }

            }
        });

        if (loginSession.getAppStatus().equalsIgnoreCase("Yes")) {
            findViewById(R.id.underConstruction).setVisibility(View.VISIBLE);
        } else {
            /*citiesArrayList = SearchOptionSingleton.getInstance().searchOptionList.result.citylist;
            resArrayList = SearchOptionSingleton.getInstance().searchOptionList.result.restaurantlist;
            cuisineArrayList = SearchOptionSingleton.getInstance().searchOptionList.result.cuisineslist;*/
            searchOptions();
        }

    }

    public void checkPermission() {
        //Location permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(NewLocationScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                openBaseHomeScreen();
            } else {
                ActivityCompat.requestPermissions(NewLocationScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                //request permisson
                return;
            }
        } else {
//            openBaseHomeScreen();
        }
    }

    public void openBaseHomeScreen() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(NewLocationScreen.this, NewLocationScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }, 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openBaseHomeScreen();
            } else {
                //Handler event
                toast(getResources().getString(R.string.LocationFeatureDenied));
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void language(String code) {
        languageToLoad = code;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(NewLocationScreen.this, NewLocationScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private class CityAdapter extends BaseAdapter {

        Activity activity;
        ArrayList<SearchOptionList.Citylist> citiesOriginal;
        ArrayList<SearchOptionList.Citylist> citiesDummy;

        public CityAdapter(Activity activity, ArrayList<SearchOptionList.Citylist> citiesOriginal) {
            this.activity = activity;
            this.citiesOriginal = citiesOriginal;
            this.citiesDummy = new ArrayList<>();
            this.citiesDummy.addAll(citiesOriginal);
        }

        @Override
        public int getCount() {
            return citiesOriginal.size();
        }

        @Override
        public Object getItem(int i) {
            return citiesOriginal.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)

                convertView = inflater.inflate(R.layout.new_custom_spinner_text, null);

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(citiesOriginal.get(i).city_name);

//            toast(String.valueOf(citiesOriginal.size()));
            // highlight search text
            if (mSearchText != null && !mSearchText.isEmpty()) {
                int startPos = citiesOriginal.get(i).city_name.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
                int endPos = startPos + mSearchText.length();

                if (startPos != -1) {
                    Spannable spannable = new SpannableString(citiesOriginal.get(i).city_name);
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{activity.getResources().getColor(R.color.colorPrimary)});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spannable);
                } else {
                    textView.setText(citiesOriginal.get(i).city_name);
                }
            } else {
                textView.setText(citiesOriginal.get(i).city_name);
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    secondEdit.setText("");
                    firstEdit.setText(citiesOriginal.get(i).city_name);
                    cityName = String.valueOf(firstEdit.getText());
                    cityid = citiesOriginal.get(i).id;
                    subDistric.clear();

                    if (searchBy.equals("3") || searchBy.equals("2")) {
                        searchrestaurant.performClick();
                    } else {
                        if (checkInternet()) {
                            Map<String, String> params = new HashMap<>();
                            params.put("action", "getSubdistList");
                            params.put("city_id", citiesOriginal.get(i).id);
                            showProgressDialog();
                            createRequest.createRequest(NewLocationScreen.this, params, RequestID.REQ_SUB_DISTRIC);
                        } else {
                            noInternetAlertDialog();
                        }
                    }

                    citytListDialog.dismiss();
                }
            });

            return convertView;
        }

        public void filter(String searchtext) {

            Log.e("searchtext", searchtext);
            mSearchText = searchtext;
            searchtext = searchtext.toLowerCase(Locale.getDefault());
            citiesOriginal.clear();

            if (searchtext.length() == 0) {
                citiesOriginal.addAll(citiesDummy);

            } else {
                Log.e("else part", "call");

                for (SearchOptionList.Citylist cities : citiesDummy) {
                    if (cities.city_name.toLowerCase(Locale.getDefault()).contains(searchtext)) {
                        citiesOriginal.add(cities);
                    }
                }
                if (citiesOriginal.size() == 0) {

                    toast(getResources().getString(R.string.noCitiesFound));
                }

            }
            notifyDataSetChanged();
        }
    }

    private class RestaurantAdapter extends BaseAdapter {

        Activity activity;
        ArrayList<SearchOptionList.Restaurantlist> resOriginal;
        ArrayList<SearchOptionList.Restaurantlist> resDummy;

        public RestaurantAdapter(Activity activity, ArrayList<SearchOptionList.Restaurantlist> resDummy) {
            this.activity = activity;
            this.resDummy = resDummy;
            resOriginal = new ArrayList<>();
            resOriginal.addAll(resDummy);
        }

        @Override
        public int getCount() {
            return resOriginal.size();
        }

        @Override
        public Object getItem(int i) {
            return resOriginal.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)

                convertView = inflater.inflate(R.layout.new_custom_spinner_text, null);

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(resOriginal.get(i).restaurant_name);

//            toast(String.valueOf(citiesOriginal.size()));
            // highlight search text
            if (mSearchText != null && !mSearchText.isEmpty()) {
                int startPos = resOriginal.get(i).restaurant_name.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
                int endPos = startPos + mSearchText.length();

                if (startPos != -1) {
                    Spannable spannable = new SpannableString(resOriginal.get(i).restaurant_name);
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{activity.getResources().getColor(R.color.colorPrimary)});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spannable);
                } else {
                    textView.setText(resOriginal.get(i).restaurant_name);
                }
            } else {
                textView.setText(resOriginal.get(i).restaurant_name);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (databaseManager.getCount() > 0) {

                        if (!databaseManager.getResId().equalsIgnoreCase(resOriginal.get(i).id)) {
                            androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(NewLocationScreen.this);
                            alertDialog.setIcon(R.drawable.caution);
                            alertDialog.setTitle(getResources().getString(R.string.note));
                            alertDialog.setMessage(getResources().getString(R.string.wantToEmptyCart));

                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    databaseManager.clearTable();
                                    loginSession.saveCardCount(0);
                                    dialog.cancel();

                                    thirdEdit.setText(resOriginal.get(i).restaurant_name);
                                    resid = resOriginal.get(i).seo_url;
                                    restaturant_id = resOriginal.get(i).id;

                                    loginSession.setCityid("");
                                    loginSession.setCityidRes(cityid);
                                    loginSession.setCityidCus("");
                                    loginSession.setResid(resid);
                                    loginSession.setcusid("");
                                    loginSession.setSubDis("");
                                    loginSession.saveLocation("");
                                    loginSession.setsearchType("restaurant");

                                    Intent intent = new Intent(NewLocationScreen.this, RestaurantDetailsScreen.class);
                                    intent.putExtra("res_id", restaturant_id);
                                    startActivity(intent);
                                    citytListDialog.dismiss();

                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        } else {
                            thirdEdit.setText(resOriginal.get(i).restaurant_name);
                            resid = resOriginal.get(i).seo_url;
                            restaturant_id = resOriginal.get(i).id;

                            loginSession.setCityid("");
                            loginSession.setCityidRes(cityid);
                            loginSession.setCityidCus("");
                            loginSession.setResid(resid);
                            loginSession.setcusid("");
                            loginSession.setSubDis("");
                            loginSession.saveLocation("");
                            loginSession.setsearchType("restaurant");

                            Intent intent = new Intent(NewLocationScreen.this, RestaurantDetailsScreen.class);
                            intent.putExtra("res_id", restaturant_id);
                            startActivity(intent);
                            citytListDialog.dismiss();
                        }

                    } else {
                        thirdEdit.setText(resOriginal.get(i).restaurant_name);
                        resid = resOriginal.get(i).seo_url;
                        restaturant_id = resOriginal.get(i).id;

                        loginSession.setCityid("");
                        loginSession.setCityidRes(cityid);
                        loginSession.setCityidCus("");
                        loginSession.setResid(resid);
                        loginSession.setcusid("");
                        loginSession.setSubDis("");
                        loginSession.saveLocation("");
                        loginSession.setsearchType("restaurant");

                        Intent intent = new Intent(NewLocationScreen.this, RestaurantDetailsScreen.class);
                        intent.putExtra("res_id", restaturant_id);
                        startActivity(intent);
                        citytListDialog.dismiss();
                    }


                }
            });

            return convertView;
        }

        public void filter(String searchtext) {

            Log.e("searchtext", searchtext);
            mSearchText = searchtext;
            searchtext = searchtext.toLowerCase(Locale.getDefault());
            resOriginal.clear();

            if (searchtext.length() == 0) {
                resOriginal.addAll(resDummy);

            } else {
                Log.e("else part", "call");

                for (SearchOptionList.Restaurantlist restaurant : resDummy) {
                    if (restaurant.restaurant_name.toLowerCase(Locale.getDefault()).contains(searchtext)) {
                        resOriginal.add(restaurant);
                    }
                }
                if (resOriginal.size() == 0) {

                    toast(getResources().getString(R.string.noRestaurantFound));
                }

            }
            notifyDataSetChanged();
        }
    }

    private class CuisineAdapter extends BaseAdapter {

        Activity activity;
        ArrayList<SearchOptionList.Cuisineslist> cuisineOriginal;
        ArrayList<SearchOptionList.Cuisineslist> cuisineDummy;

        public CuisineAdapter(Activity activity, ArrayList<SearchOptionList.Cuisineslist> cuisineOriginal) {
            this.activity = activity;
            this.cuisineOriginal = cuisineOriginal;
            this.cuisineDummy = new ArrayList<>();
            this.cuisineDummy.addAll(cuisineOriginal);
        }

        @Override
        public int getCount() {
            return cuisineOriginal.size();
        }

        @Override
        public Object getItem(int i) {
            return cuisineOriginal.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)

                convertView = inflater.inflate(R.layout.new_custom_spinner_text, null);

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(cuisineOriginal.get(i).cuisine_name);

//            toast(String.valueOf(citiesOriginal.size()));
            // highlight search text
            if (mSearchText != null && !mSearchText.isEmpty()) {
                int startPos = cuisineOriginal.get(i).cuisine_name.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
                int endPos = startPos + mSearchText.length();

                if (startPos != -1) {
                    Spannable spannable = new SpannableString(cuisineOriginal.get(i).cuisine_name);
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{activity.getResources().getColor(R.color.colorPrimary)});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spannable);
                } else {
                    textView.setText(cuisineOriginal.get(i).cuisine_name);
                }
            } else {
                textView.setText(cuisineOriginal.get(i).cuisine_name);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fourthEdit.setText(cuisineOriginal.get(i).cuisine_name);
                    cusid = cuisineOriginal.get(i).cuisine_name;

//                    responseCityId = citiesOriginal.get(i).cityId;
//                    loginSession.setReataurantName(citiesOriginal.get(i).seo_url);
//                    restaurantNameEditText.setText(citiesOriginal.get(i).restaurant_name);

                    //Open baseHomeScreen
//                    Intent i = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
//                    startActivity(i);
//                    finish();

                    citytListDialog.dismiss();
                }
            });

            return convertView;
        }

        public void filter(String searchtext) {

            Log.e("searchtext", searchtext);
            mSearchText = searchtext;
            searchtext = searchtext.toLowerCase(Locale.getDefault());
            cuisineOriginal.clear();

            if (searchtext.length() == 0) {
                cuisineOriginal.addAll(cuisineDummy);

            } else {
                Log.e("else part", "call");

                for (SearchOptionList.Cuisineslist cuisine : cuisineDummy) {
                    if (cuisine.cuisine_name.toLowerCase(Locale.getDefault()).contains(searchtext)) {
                        cuisineOriginal.add(cuisine);
                    }
                }
                if (cuisineOriginal.size() == 0) {

                    toast(getResources().getString(R.string.noCuisineFound));
                }

            }
            notifyDataSetChanged();
        }
    }

    private class SubDisAdapter extends BaseAdapter {

        Activity activity;
        //        ArrayList<SubDistricList.Distlist> subDisOriginal;
        ArrayList<String> CommonDisDummy;

        public SubDisAdapter(Activity activity, ArrayList<String> CommonDisDummy) {
            this.activity = activity;
            this.CommonDisDummy = CommonDisDummy;
//            this.subDisDummy = new ArrayList<>();
//            this.subDisDummy.addAll(subDisOriginal);
        }

        @Override
        public int getCount() {
            return CommonDisDummy.size();
        }

        @Override
        public Object getItem(int i) {
            return CommonDisDummy.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)

                convertView = inflater.inflate(R.layout.new_custom_spinner_text, null);

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setBackgroundColor(getResources().getColor(R.color.dim_dim_black));


            for (SubDistricList.Sub commonDiss : branchDistric) {
                if (commonDiss.subdistrict_name.equalsIgnoreCase(CommonDisDummy.get(i))) {
                    if (!commonDiss.rstCount.equalsIgnoreCase("0")) {
                        textView.setBackgroundColor(getResources().getColor(R.color.white));
                        textView.setText(CommonDisDummy.get(i) + " (" + commonDiss.rstCount + ") ");
                        textView.setTypeface(null, Typeface.NORMAL);
                        break;
                    } else {
                        textView.setBackgroundColor(getResources().getColor(R.color.white));
                        textView.setText(CommonDisDummy.get(i));
                        textView.setTypeface(null, Typeface.NORMAL);
                        break;
                    }

                } else {

                    textView.setText(CommonDisDummy.get(i));
                    textView.setTypeface(null, Typeface.BOLD);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    for (SubDistricList.Distlist commonDis : subDistric) {
                        if (commonDis.district_name.equalsIgnoreCase(CommonDisDummy.get(i))) {
                        } else {
                            for (SubDistricList.Sub subdis : branchDistric) {
                                if (subdis.subdistrict_name.equalsIgnoreCase(CommonDisDummy.get(i))) {
                                    secondEdit.setText(CommonDisDummy.get(i));
                                    subdisid = subdis.id;
                                    citytListDialog.dismiss();
                                    searchrestaurant.performClick();
                                    break;
                                }
                            }

                        }
                        break;
                    }

                }
            });

            return convertView;
        }

//        public void filter(String searchtext) {
//
//            Log.e("searchtext",searchtext);
//            mSearchText=searchtext;
//            searchtext = searchtext.toLowerCase(Locale.getDefault());
//            CommonDisDummy.clear();
//
//            if (searchtext.length() == 0)
//            {
//                CommonDisDummy.addAll(subDisDummy);
//
//            } else
//            {
//                Log.e("else part","call");
//
//                for (SubDistricList.Distlist cities : subDisDummy)
//                {
//                    if (cities.district_name.toLowerCase(Locale.getDefault()).contains(searchtext)) {
//                        CommonDisDummy.add(cities);
//                    }
//                }
//                if (CommonDisDummy.size() == 0) {
//
//                    toast("No Cities found");
//                }
//
//            }
//            notifyDataSetChanged();
//        }
    }

    /*private void searchOptions() {
        Log.e("searchOptions", "searchOptions");
        if (checkInternet()) {
            citiesArrayList.clear();
            Map<String, String> params = new HashMap<>();
            params.put("action", "searchOptions");
            showProgressDialog();
            createRequest.createRequest(NewLocationScreen.this, params, RequestID.REQ_SEARCH_OPTIONS);
        } else {
            noInternetAlertDialog();
        }
    }*/

    private void searchOptions() {
        if (checkInternet()) {

            String url = "https://www.pintogogo.com/v1/searchOptions";

            JsonObject object = new JsonObject();
            object.addProperty("action", "searchOptions");

            String jsonString = new Gson().toJson(object);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("Authorization url ", "params : " + url);
            showProgressDialog();

            Log.e("Authorization", "params : " + jsonObject.toString());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("URL : ", "response : " + response);
                    hideProgressDialog();
                    if (response != null) {
//                        toast(response.toString());

                        try {

                            JSONObject jsonObject = new JSONObject(response.toString());
//                            String success = jsonObject.getString("success");
                            JSONObject result = jsonObject.getJSONObject("result");

                            if (result != null) {

                                Gson gson4 = new Gson();
                                Object result1 = gson4.fromJson(jsonObject.toString(), SearchOptionList.class);

                                SearchOptionList searchOptionList = (SearchOptionList) result1;
                                citiesArrayList = searchOptionList.result.citylist;
                                resArrayList = searchOptionList.result.restaurantlist;
                                cuisineArrayList = searchOptionList.result.cuisineslist;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("URL : ", "URL Not found");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressDialog();
                    try {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            JSONObject jsonObject = new JSONObject(jsonError);
                            Log.e("jsonError", "jsonError" + jsonError);
                            if (jsonObject.has("error")) {
                                toast(jsonObject.getJSONObject("error").getString("message"));
                            } else {
                                toast(jsonObject.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast("Please try again!!");
                    }
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    return headers;
                }
            };

            objectRequest.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(NewLocationScreen.this).add(objectRequest);

        }
    }

    private void openBaseScreen() {

        //Open baseHomeScreen
        utility.CURRENT_SCREEN = "";
        Intent intent = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
        startActivity(intent);
    }

    private void getLatLng() {

        if (checkLocationService()) {
            //getting values from radio button
            try {
                double Lat = location.getLatitude();
                double Lon = location.getLongitude();
                Log.e("lattitude", "" + Lat);
                Log.e("lattitude", "" + Lon);
                if (checkInternet()) {
                    Map<String, String> params = new HashMap<>();
                    params.put("latitude", String.valueOf(Lat));
                    params.put("longitude", String.valueOf(Lon));
                    showProgressDialog();
                    createRequest.createRequest(NewLocationScreen.this, params, RequestID.REQ_GET_CURRENTADDRESS);

                } else {

                    noInternetAlertDialog();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }


        } else {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gpsSettings));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsMessage));
            // On pressing Settings button
            alertDialog.setPositiveButton(getResources().getString(R.string.settings),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            // on pressing cancel button
            alertDialog.setNegativeButton(getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }

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
        try {
            if (this != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            if (resultCode != RESULT_CANCELED) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String areaName = String.valueOf(place.getAddress());
                //areaNameEditText.setEnabled(true);
                //areaNameEditText.setText(areaName);
                loginSession.saveLocation(areaName);

                //Open baseHomeScreen
                utility.APP_START_FRAGMENT = "restaurantButton";
                utility.CURRENT_SCREEN = "";
                Intent intent = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
                startActivity(intent);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //areaNameEditText.setEnabled(true);
        try {
            if (!googleApiClient.isConnected()) {
                googleApiClient.connect();
                Log.e("googleApiClient", "connected");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } else {
                Log.e("googleApiClient", "notconnect");
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        switch (requestID) {

            case REQ_GET_CURRENTADDRESS:

                try {
                    LocationModel locationModel = (LocationModel) result;
                    String gpsAdderss = locationModel.result.address;
                    loginSession.saveLocation(gpsAdderss);
                    //areaNameEditText.setText(gpsAdderss);

                    //Open baseHomeScreen
                    utility.APP_START_FRAGMENT = "restaurantButton";
                    Intent intent = new Intent(NewLocationScreen.this, NewBaseHomeScreen.class);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                break;

            case REQ_SETTINGS:

                SettingsList settingsList = (SettingsList) result;

                if (settingsList != null && settingsList.result != null && settingsList.result.sitesettings != null) {

                    loginSession.setCurrencyCode(settingsList.result.sitesettings.site_currency);
                    if (settingsList.result.sitesettings.stripe_mode.equalsIgnoreCase("test")) {
                        loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkeyTest);
                    } else {
                        loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkey);
                    }

                    if (settingsList.result.sitesettings.offline_status.equalsIgnoreCase("Yes")) {
                        findViewById(R.id.underConstruction).setVisibility(View.VISIBLE);
                    }
                }

                Log.e("sitesetting", "onsuccess");
                hideProgressDialog();

                searchOptions();

                break;

            case REQ_SEARCH_OPTIONS:

                hideProgressDialog();

                SearchOptionList searchOptionList = (SearchOptionList) result;
                citiesArrayList = searchOptionList.result.citylist;
                resArrayList = searchOptionList.result.restaurantlist;
                cuisineArrayList = searchOptionList.result.cuisineslist;


//                loginSession.setCurrencyCode(settingsList.result.sitesettings.site_currency);
//                if(settingsList.result.sitesettings.stripe_mode.equalsIgnoreCase("test")){
//                    loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkeyTest);
//                }else{
//                    loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkey);
//                }


                break;

            case REQ_SUB_DISTRIC:

                commonDistric.clear();
                SubDistricList subDistricList = (SubDistricList) result;
                subDistric = subDistricList.result.distlist;
                if (subDistric != null && !subDistric.isEmpty()) {
                    for (SubDistricList.Distlist commonDis : subDistricList.result.distlist) {
                        commonDistric.add(commonDis.district_name);
                        for (SubDistricList.Sub sub : commonDis.subdistricts) {
                            commonDistric.add(sub.subdistrict_name);
                            branchDistric.add(sub);
                        }
                    }

                    for (String s : commonDistric) {
                        Log.e("list", s);
                    }

                }

                secondEdit.performClick();
                hideProgressDialog();

                break;
        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();

        switch (requestID) {

            case REQ_GET_CURRENTADDRESS:

                toast(error);
                break;

            case REQ_SETTINGS:
                searchOptions();
                Log.e("sitesetting", "onfailure");
                break;

            case REQ_SEARCH_OPTIONS:

                toast(error);
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                googleApiClient.stopAutoManage(this);
                googleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onStop() {
        super.onStop();
        try {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                googleApiClient.stopAutoManage(this);
                googleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.cityLayout:

                cityLayout.setBackgroundColor(getResources().getColor(R.color.white));
                restaurantLayout.setBackgroundColor(getResources().getColor(R.color.black));
                cuisineLayout.setBackgroundColor(getResources().getColor(R.color.black));

                cityImageView.setImageDrawable(getResources().getDrawable(R.drawable.city_icon_black));
                restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.food_icon));
                cuisineImageView.setImageDrawable(getResources().getDrawable(R.drawable.cuision_icon));

                cityTextView.setTextColor(getResources().getColor(R.color.black));
                restaurantTextView.setTextColor(getResources().getColor(R.color.white));
                cuisineTextView.setTextColor(getResources().getColor(R.color.white));

                firstEdit.setVisibility(View.VISIBLE);
                secondEdit.setVisibility(View.VISIBLE);
                thirdEdit.setVisibility(View.GONE);
                fourthEdit.setVisibility(View.GONE);

                searchBy = "1";
                cityName = "";
                cityid = "";
                resid = "";
                cusid = "";
                subdisid = "";
                firstEdit.setText("");
                secondEdit.setText("");
                thirdEdit.setText("");
                fourthEdit.setText("");
//                if (!subDistric.isEmpty()) {
                subDistric.clear();
//                }
                break;

            case R.id.restaurantLayout:

                cityLayout.setBackgroundColor(getResources().getColor(R.color.black));
                restaurantLayout.setBackgroundColor(getResources().getColor(R.color.white));
                cuisineLayout.setBackgroundColor(getResources().getColor(R.color.black));

                cityImageView.setImageDrawable(getResources().getDrawable(R.drawable.city_icon));
                restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.food_icon_color));
                cuisineImageView.setImageDrawable(getResources().getDrawable(R.drawable.cuision_icon));

                cityTextView.setTextColor(getResources().getColor(R.color.white));
                restaurantTextView.setTextColor(getResources().getColor(R.color.black));
                cuisineTextView.setTextColor(getResources().getColor(R.color.white));

                firstEdit.setVisibility(View.VISIBLE);
                secondEdit.setVisibility(View.GONE);
                thirdEdit.setVisibility(View.VISIBLE);
                fourthEdit.setVisibility(View.GONE);

                searchBy = "2";
                cityid = "";
                resid = "";
                cusid = "";
                subdisid = "";
                firstEdit.setText("");
                secondEdit.setText("");
                thirdEdit.setText("");
                fourthEdit.setText("");

                break;

            case R.id.cuisineLayout:

                cityLayout.setBackgroundColor(getResources().getColor(R.color.black));
                restaurantLayout.setBackgroundColor(getResources().getColor(R.color.black));
                cuisineLayout.setBackgroundColor(getResources().getColor(R.color.white));

                cityImageView.setImageDrawable(getResources().getDrawable(R.drawable.city_icon));
                restaurantImageView.setImageDrawable(getResources().getDrawable(R.drawable.food_icon));
                cuisineImageView.setImageDrawable(getResources().getDrawable(R.drawable.cuision_icon_black));

                cityTextView.setTextColor(getResources().getColor(R.color.white));
                restaurantTextView.setTextColor(getResources().getColor(R.color.white));
                cuisineTextView.setTextColor(getResources().getColor(R.color.black));

                firstEdit.setVisibility(View.VISIBLE);
                secondEdit.setVisibility(View.GONE);
                thirdEdit.setVisibility(View.GONE);
                fourthEdit.setVisibility(View.VISIBLE);

                searchBy = "3";
                cityid = "";
                resid = "";
                cusid = "";
                subdisid = "";
                firstEdit.setText("");
                secondEdit.setText("");
                thirdEdit.setText("");
                fourthEdit.setText("");


                break;
        }
    }
}
