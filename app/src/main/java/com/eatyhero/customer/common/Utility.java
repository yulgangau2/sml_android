package com.eatyhero.customer.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.eatyhero.customer.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

import static android.content.Context.LOCATION_SERVICE;


public class Utility {

    public static Utility utility = null;
    Activity activity;
    Toast toast;
    ProgressDialog progressDialog;
    Dialog progressdialog;
    public static boolean update_check = false;
    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    LocationManager locationManager;

    public static Utility getInstance(Activity activity) {
        utility = new Utility(activity);


        return utility;
    }

    /**********************************Constructor*************************************/
    public Utility(Activity activity) {
        super();
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

    }

    public boolean checkLocationService() {
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {

            return true;
        }

        return false;
    }

    public void hideStatusBar()
    {
        View decorView =  activity.getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.black));
        }
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.STATUS_BAR_HIDDEN;

        decorView.setSystemUiVisibility(uiOptions);
}

    public void showStatusBar()
    {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        decorView.setSystemUiVisibility(uiOptions);
    }



    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }
    }

        /**********************************Toast Method*************************************/
    public void toast(String message) {
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


    /**********************************Run on UI Toast Method*************************************/

    public void toastUI(final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

    }


    /**********************************Alert dialog Method*************************************/
    public void showAlertDialog(String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle(title);

        //alertDialog.setMessage(message);

        //  alertDialog.setIcon(R.drawable.caution);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    /**********************************No Internet Alert dialog*************************************/
    public void noInternetAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle("Please Check Internet Connection!!!");

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    /********************************Check Internet Connection Method*********************************/
    public boolean checkInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void getTotalHeightofHorizontalListView(HorizontalListView listView)
    {
        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++)
        {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (50* (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    /**********************************Set List View Height based on Size*************************************/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }




    /********************************Check Internet Connection Method*********************************/
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    /*********************************Check valid email Method**********************************/
    public boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    /**********************************Date valid Method***********************************/
    public boolean isdatevalid(String date) {
        Date datetest = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            datetest = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (sdf.format(datetest).equals(date)) {
            return true;
        }

        return false;
    }


    /********************************Set Spinner position Method**********************************/
    public int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }


    public void displayToast(String message) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    //check string contains special char
    public boolean CheckSpecialChar(String toCheck) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        Matcher matcher = pattern.matcher(toCheck);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    //play store dialog
    public void ConnectPlaystore() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Please update your google play service!!!");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms"));
                activity.startActivity(intent);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alert.show();

    }

    //String format 1.00
    public String formatString(String values) {
        double change = 0;
        try {
            change = Double.parseDouble(values);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.format("%.2f", change);
    }

    /*public void showProgressDialog() {

        if (progressDialog == null) {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    public  void hideProgressDialog() {

        if (progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
    }*/

    /**********************************Custom progressbar*************************************/
    public void showProgressDialog() {
        if (progressdialog == null) {
            progressdialog = new Dialog(activity);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressdialog.setContentView(R.layout.custom_progressbar);
            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        progressdialog.setCancelable(false);
        progressdialog.show();
    }

    public void hideProgressDialog() {
        try {

            if (progressdialog.isShowing()) {

                progressdialog.dismiss();
            }

        } catch (Exception e) {


        }

    }


    public String ratingConversion(String rating) {

        String count = "";

        if (Double.parseDouble(rating) == 0) {

            count = "0";

        } else if ((Double.parseDouble(rating) >= 1) && (Double.parseDouble(rating) <= 20)) {

            count = "1";

        } else if ((Double.parseDouble(rating) >= 20) && (Double.parseDouble(rating) <= 40)) {

            count = "2";

        } else if ((Double.parseDouble(rating) >= 40) && (Double.parseDouble(rating) <= 60)) {

            count = "3";

        } else if ((Double.parseDouble(rating) >= 60) && (Double.parseDouble(rating) <= 80)) {

            count = "4";

        } else if ((Double.parseDouble(rating) >= 80) && (Double.parseDouble(rating) <= 100)) {

            count = "5";
        }

        return count;
    }


    //SSL Certificate

    public OkHttpClient getUnsafeOkHttpClient() {
        try {

            return new OkHttpClient.Builder()
                    .sslSocketFactory(getSSLSocketFactory())
                    .hostnameVerifier(getHostnameVerifier()).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("www.foodorderingsystem.com", session);
            }
        };
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = activity.getResources().openRawResource(R.raw.random); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }


    //
    public static String CURRENT_SCREEN = "empty";
    public static Object listResponse = "empty";
    public static String filterDialogueopen = "no";
    public static String APP_START_FRAGMENT = "";


}
