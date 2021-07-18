package com.eatyhero.customer.base;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends BaseActivity implements ServerListener {

    //Create objects
    LoginSession loginSession;
    Utility utility;
    DatabaseManager databaseManager;
    ServerRequest createRequest;

    Dialog updatedialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

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

        //Get device id
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginSession.saveDeviceId(android_id);

        /*if (checkInternet()) {
            siteSettings();
        } else {
            noInternetAlertDialog();
        }*/

        if (checkInternet()) {
            try {
                new VersionChecker().execute();
            } catch (Exception e) {
                e.printStackTrace();
                openBaseHomeScreen();
            }
        } else {
            noInternetAlertDialog();
        }

    }

    public void checkPermission() {
        openBaseHomeScreen();
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
                openBaseHomeScreen();
            }
        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {
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
                        openBaseHomeScreen();
                    }
                } else {
                    hideProgressDialog();
                    openBaseHomeScreen();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                hideProgressDialog();
                openBaseHomeScreen();
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
            createRequest.createRequest(SplashScreen.this, params, RequestID.REQ_SETTINGS);

        } else {
            noInternetAlertDialog();
        }

    }

    public void openBaseHomeScreen() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(SplashScreen.this, NewLocationScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }, 500);
    }

    private void openUpdateDialogue() {

        try {
            if (updatedialog == null) {
                updatedialog = new Dialog(SplashScreen.this);
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
                    openBaseHomeScreen();
                }
            });

            updatedialog.setCancelable(false);
            updatedialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}