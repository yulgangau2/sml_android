package com.eatyhero.customer.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.SettingsList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 9/26/2015.
 */
public class LaunchScreen extends BaseActivity implements ServerListener {

    //Create objects
    LoginSession loginSession;
    Utility utility;
    DatabaseManager databaseManager;
    ServerRequest createRequest;

    //Location Request
//    GoogleApiClient client;
    int REQUEST_LOCATION_PERMISSION;
    PackageInfo info;

    Dialog updatedialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launch_screen);

        //Initialize objects
        loginSession = LoginSession.getInstance(this);
        utility = Utility.getInstance(this);
        databaseManager = DatabaseManager.getInstance(this);
        createRequest = ServerRequest.getInstance(this);

        //empty  data
        utility.listResponse = "empty";

        //clear DB
        databaseManager.clearTable();
        int cartCount = 0;
        loginSession.saveCardCount(cartCount);
//        loginSession.setlanguage("3");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.eatyhero.customer",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        /*try {
            info = getPackageManager().getPackageInfo("com.eatyhero.customer", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/

        //Get device id
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginSession.saveDeviceId(android_id);

        //Initialize google apiclient
//        client = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .build();

        if (checkInternet()) {
            siteSettings();
        } else {
            noInternetAlertDialog();
        }


        /*if (checkInternet()) {
            try {
                new VersionChecker().execute();
                //checkPermission();
            } catch (Exception e) {
                e.printStackTrace();
                checkPermission();
            }

        } else {

            noInternetAlertDialog();
        }*/
    }

    public void checkPermission() {
        //Location permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LaunchScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                siteSettings();
                openBaseHomeScreen();
            } else {
                ActivityCompat.requestPermissions(LaunchScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                //request permisson
                return;
            }
        } else {
//            siteSettings();
            openBaseHomeScreen();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        SettingsList settingsList = (SettingsList) result;

        if (settingsList != null && settingsList.result != null && settingsList.result.sitesettings != null) {

            loginSession.setCurrencyCode(settingsList.result.sitesettings.site_currency);
            if (settingsList.result.sitesettings.stripe_mode.equalsIgnoreCase("test")) {
                loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkeyTest);
            } else {
                loginSession.setPusherKey(settingsList.result.sitesettings.stripe_publishkey);
            }

            if (settingsList.result.sitesettings.offline_status.equalsIgnoreCase("Yes")) {
                loginSession.setAppStatus("Yes");
            } else {
                loginSession.setAppStatus("No");
            }
        }

        if (checkInternet()) {
            try {
                new VersionChecker().execute();
            } catch (Exception e) {
                e.printStackTrace();
                checkPermission();
            }
        } else {
            noInternetAlertDialog();
        }

//        openBaseHomeScreen();

    }

    @Override
    public void onFailure(String error, RequestID requestID) {
//        openBaseHomeScreen();
        toast(error);
    }


    private class VersionChecker extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.eatyhero.customer&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                Log.e("newVersion", newVersion);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;

                Log.e("version", version);

                if (newVersion != null && !newVersion.isEmpty()) {
                    hideProgressDialog();
                    if (!newVersion.equals(version)) {
                        openUpdateDialogue();
                    } else {
                        checkPermission();
//                        openBaseHomeScreen();

                    }
                } else {
                    hideProgressDialog();
                        checkPermission();
//                    openBaseHomeScreen();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                hideProgressDialog();
                        checkPermission();
//                openBaseHomeScreen();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                siteSettings();
                openBaseHomeScreen();
            } else {
                //Handler event
                toast(getResources().getString(R.string.LocationFeatureDenied));
//                siteSettings();
                finish();
            }
        }
    }

    public void siteSettings() {

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
            createRequest.createRequest(LaunchScreen.this, params, RequestID.REQ_SETTINGS);

        } else {
            noInternetAlertDialog();
        }

    }

    public void openBaseHomeScreen() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(LaunchScreen.this, NewLocationScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }, 500);
    }

    private void openUpdateDialogue() {

        try {
            if (updatedialog == null) {
                updatedialog = new Dialog(LaunchScreen.this);
                updatedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                updatedialog.setContentView(R.layout.dialog_for_update);
                updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                updatedialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            }

            Button updateButton = (Button) updatedialog.findViewById(R.id.updateButton);
            Button notnowButton = (Button) updatedialog.findViewById(R.id.notnowButton);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // final String appPackageName = getActivity().getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });

            notnowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatedialog.dismiss();
                        checkPermission();
//                    openBaseHomeScreen();
                }
            });

            updatedialog.setCancelable(false);
            updatedialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
