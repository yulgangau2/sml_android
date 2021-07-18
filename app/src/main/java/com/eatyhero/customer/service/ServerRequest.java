package com.eatyhero.customer.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eatyhero.customer.R;
import com.eatyhero.customer.common.Constens;
import com.eatyhero.customer.model.AddWalletBalance;
import com.eatyhero.customer.model.AddonList;
import com.eatyhero.customer.model.AddressList;
import com.eatyhero.customer.model.AddressListCheckout;
import com.eatyhero.customer.model.AreaList;
import com.eatyhero.customer.model.BannerImageModel;
import com.eatyhero.customer.model.BookTableHistory;
import com.eatyhero.customer.model.CityList;
import com.eatyhero.customer.model.CustomerDetail;
import com.eatyhero.customer.model.DealsManagement;
import com.eatyhero.customer.model.FavouritModel;
import com.eatyhero.customer.model.LocationModel;
import com.eatyhero.customer.model.LoginDetails;
import com.eatyhero.customer.model.MyFavoriteListModel;
import com.eatyhero.customer.model.OrderHistoryList;
import com.eatyhero.customer.model.OrderSuccess;
import com.eatyhero.customer.model.OrderTrackDetails;
import com.eatyhero.customer.model.ReferHistory;
import com.eatyhero.customer.model.RestaurantDetailsList;
import com.eatyhero.customer.model.RestaurantListResponse;
import com.eatyhero.customer.model.RestaurantMenuListResponse;
import com.eatyhero.customer.model.RestaurantsSubItems;
import com.eatyhero.customer.model.RewardHistoryList;
import com.eatyhero.customer.model.RewardList;
import com.eatyhero.customer.model.SearchOptionList;
import com.eatyhero.customer.model.SettingsList;
import com.eatyhero.customer.model.StripeCardList;
import com.eatyhero.customer.model.SubAddonList;
import com.eatyhero.customer.model.SubDistricList;
import com.eatyhero.customer.model.UserImage;
import com.eatyhero.customer.model.ViewReviews;
import com.eatyhero.customer.model.VoucherDetailsList;
import com.eatyhero.customer.model.WalletHistoryList;
import com.eatyhero.customer.moretab.OrderViewResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class ServerRequest {


    private static RequestQueue queue = null;
    public static ServerRequest serverRequest=null;
    public static ServerListener serverListener=null;

    Map<String,String> param;
    RequestID requestID;
    Context context;

    public ServerRequest(Context context) {

        if (queue == null) {

            queue = Volley.newRequestQueue(context);

        }
        this.context = context;
    }

    public static ServerRequest getInstance(Context context) {

        if (serverRequest == null) {

            serverRequest = new ServerRequest(context);
        }

        return serverRequest;
    }


    public void createRequest(ServerListener serverListener1, Map<String,String> params, final RequestID requestid) {

        param = params;

        serverListener=serverListener1;

        requestID = requestid;

        Log.e("pass values", "" + param);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getURL(),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response ::", "" + response);
                        Log.e("URL :: ", "" + getURL());

                        if (response != null) {

                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");

                                if(status.equalsIgnoreCase("OK")){

                                    JSONObject object = jsonObject.getJSONObject("result");

                                    int success = object.getInt("success");

                                    if (success == 1) {

                                        switch (requestID) {

                                            case REQ_TO_SIGNUP:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_PROFILE_UPADATE:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_FORGOT_PASSWORD:

                                                serverListener.onSuccess(object.getString("message"),requestID);

                                                break;


                                            case REQ_TO_CHANGE_PASSWORD:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_TO_BOOKTABLE:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_TO_CHANGE_EMAIL:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;
                                            case REQ_GET_STRIPE_KEY:

                                                serverListener.onSuccess(object.getString("publishkey"), requestID);
                                                break;

                                            case REQ_ADD_ADDRESS:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_EDIT_ADDRESS:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_DELETE_ADDRESS:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;

                                            case REQ_ADDRESS_STATUS:

                                                serverListener.onSuccess(object.getString("message"), requestID);

                                                break;
                                            case REQ_ADD_STRIPE_CARD:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;

                                            case REQ_TIME:

                                                serverListener.onSuccess(object.getString("timeList"), requestID);
                                                break;

                                            case REQ_GUEST_COUNT:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;

                                            case REQ_CHECK_DELIVERY_AREA:

                                                // serverListener.onSuccess(jsonObject.getString("message"), requestID);
                                                serverListener.onSuccess(object.getString("delivery_charge"), requestID);

                                                break;

                                            case REQ_TO_STARTPUSHER:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;

                                            case REQ_SEND_REVIEW:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;

                                            case REQ_STRIPE_CARD_DELETE:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;

                                            case REQ_STRIPE_PAYMENT:

                                                serverListener.onSuccess(object.getString("transaction_id"), requestID);
                                                break;

                                            case REQ_UNFAV_RESTAURANT:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;
                                            case REQ_DRIVERSIGNUP:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;
                                            case REQ_STORESIGNUP:

                                                serverListener.onSuccess(object.getString("message"), requestID);
                                                break;


                                            default:

                                                serverListener.onSuccess(getJsonModelType(response), requestID);

                                                break;
                                        }

                                    } else {

                                        if(object.has("message")){
                                            serverListener.onFailure(object.getString("message"),requestID);
                                        }else{
                                            serverListener.onFailure("Please try again!",requestID);
                                        }
                                    }
                                }else {
                                    serverListener.onFailure("Please try again!",requestID);

                                }

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }else{
                            serverListener.onFailure("Please try again!",requestID);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        serverListener.onFailure("Please try again!", requestID);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return param;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }

    //Get url based on requestID
    public String getURL()
    {
        String URL=null;

        switch (requestID)
        {

            /******************My Account Section**********************/

            case REQ_PROFILE_UPADATE:

                URL = Constens.MY_ACCOUNT;

                break;
            case REQ_GET_PROFILE:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_ORDER_HISTORY:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_TO_GET_STOREDETAILS:

                URL = Constens.SERVER_URL;

                break;

            case REQ_GET_PROMOTIONIMAGE:

                URL = Constens.SERVER_URL;

                break;

            case REQ_GET_STRIPE_KEY:
                URL = Constens.SERVER_URL;
                break;

            case REQ_TO_GET_DEALS:
                URL = Constens.SERVER_URL;
                break;


            case REQ_ADDRESSLIST:

                URL = Constens.MY_ACCOUNT;

                break;

                case REQ_ADDRESSLIST_CHECKOUT:

                URL = Constens.CHECKOUT;

                break;

            case REQ_TO_STARTPUSHER:
                URL = Constens.SERVER_URL;
                break;

            case REQ_ADD_STRIPE_CARD:
                URL = Constens.MY_ACCOUNT;
                break;

            case REQ_PRIMARY_ADD:

                URL = Constens.SERVER_URL;

                break;

            case REQ_STRIPE_CARD_DELETE:

                URL = Constens.MY_ACCOUNT;

                break;


            case REQ_ADD_ADDRESS:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_EDIT_ADDRESS:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_UPDATE_IMAGE:

                URL = Constens.SERVER_URL;

                break;

            case REQ_ORDER_TRECK:

                URL = Constens.SERVER_URL;

                break;

            case REQ_DELETE_ADDRESS:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_ADDRESS_STATUS:

                URL = Constens.MY_ACCOUNT;

                break;

            case REQ_TO_APPVIEW:

                URL = Constens.SERVER_URL;
                break;

            case REQ_TO_GET_CITYLIST:

                URL = Constens.SERVER_URL;
                break;

            case REQ_TO_GET_AREALIST:

                URL = Constens.SERVER_URL;
                break;

            case REQ_TO_LOGIN:

                URL = Constens.LOGIN;
                break;

            case REQ_SOCIAL_LOGIN:

                URL = Constens.SOCIAL_LOGIN;
                break;

            case REQ_FORGOT_PASSWORD:

                URL = Constens.FORGOTPASSWORD;
                break;

            case REQ_TO_SIGNUP:

                URL = Constens.SIGNUP;
                break;

            case REQ_TO_CHANGE_PASSWORD:

                URL = Constens.MY_ACCOUNT;
                break;


            case REQ_TO_BOOKTABLE:

                URL = Constens.BOOKATABLE;
                break;

            case REQ_TO_BOOKTABLE_HISTORY:

                URL = Constens.MY_ACCOUNT;
                break;

            case REQ_TO_CHANGE_EMAIL:

                URL = Constens.SERVER_URL;
                break;

            case REQ_GET_STRIPE_CARD:
                URL = Constens.MY_ACCOUNT;
                break;

            case REQ_REWARD_HISTORY:
                URL = Constens.REWARD_HISTORY;
                break;

            case REQ_ORDER_VIEW:
                URL = Constens.MY_ACCOUNT;
                break;

            /******************Home Section**********************/

            case REQ_TO_GET_STORELIST:

                URL = Constens.STORE_LIST;
                break;

            case REQ_TO_GET_STOREITEMS:

                URL = Constens.MENU_LIST;
                break;

            case REQ_TO_REVIEWS:

                URL = Constens.SERVER_URL;
                break;

            case REQ_TO_GET_STORESUB_ITEMS:

                URL = Constens.SERVER_URL;
                break;

            case REQ_ADDON:
                URL = Constens.PRODUCTDETAILS;
                break;

            case REQ_SUBADDON:
                URL = Constens.PRODUCTSUBADDON;
                break;

            case REQ_TIME:
                URL = Constens.CHECKOUT;
                break;
            case REQ_GUEST_COUNT:
                URL = Constens.SERVER_URL;
                break;

            case REQ_VOUCHER_CODE:

                URL = Constens.CHECKOUT;
                break;

            case REQ_CHECK_DELIVERY_AREA:

                URL = Constens.SERVER_URL;
                break;

            case REQ_GET_WALLETHISTORY:

                URL = Constens.MY_ACCOUNT;
                break;

            case REQ_ADD_WALLET:
                URL = Constens.MY_ACCOUNT;
                break;

             case REQ_ORDER_REGISTER:
                URL = Constens.PLACE_ORDER;
                break;

            case REQ_STRIPE_PAYMENT:
                URL = Constens.SERVER_URL;
                break;

            case REQ_GET_CURRENTADDRESS:
                URL = Constens.GET_LOCATION;
                break;
            case REQ_SEND_REVIEW:
                URL = Constens.MY_ACCOUNT;
                break;
            case REQ_SETTINGS:
                URL = Constens.SETTINGS;
                break;
            case REQ_SEARCH_OPTIONS:
                URL = Constens.SEARCH_OPTIONS;
                break;
            case REQ_SUB_DISTRIC:
                URL = Constens.SUB_DISTRIC;
                break;

            case REQ_CHANGE_FAV_STATUS:
                URL = Constens.FAV_STATUS;
                break;

            case REQ_CHANGE_FAV_LIST:
                URL = Constens.FAV_STATUS;
                break;

            case REQ_UNFAV_RESTAURANT:
                URL = Constens.FAV_STATUS;
                break;

            case REQ_GET_REWARD:
                URL = Constens.GET_REWARD;
                break;

            case REQ_REFER_HISTORY:
                URL = Constens.MY_ACCOUNT;
                break;

            case REQ_DRIVERSIGNUP:
                URL = Constens.DRIVER_SIGNUP;
                break;

            case REQ_STORESIGNUP:
                URL = Constens.STORE_SIGNUP;
                break;

            default:
                URL = Constens.SERVER_URL;
                break;
        }

        return URL;
    }


    /**
     * Method to give appropriate class to parse the JSON data
     *
     * @return Class based on request ID ser ver class edited s
     */
    @SuppressWarnings("rawtypes")
    private Class getModel()
    {
        Class model=null;
        switch (requestID) {

            /******************My Account Section**********************/

            case REQ_ORDER_HISTORY:

                model=OrderHistoryList.class;

                break;

            case REQ_ADDRESSLIST:

                model=AddressList.class;

                break;

            case REQ_ADDRESSLIST_CHECKOUT:

                model=AddressListCheckout.class;

                break;

            case REQ_TO_APPVIEW:

                model = String.class;
                break;

            case REQ_STRIPE_CARD_DELETE:

                model = String.class;
                break;

            case REQ_TO_GET_CITYLIST:

                model = CityList.class;
                break;

            case REQ_TO_GET_STOREDETAILS:

                model = RestaurantDetailsList.class;
                break;

            case REQ_TO_STARTPUSHER:
                model = String.class;
                break;

            case REQ_GET_STRIPE_KEY:
                model = String.class;
                break;

            case REQ_TO_GET_AREALIST:

                model = AreaList.class;
                break;

            case REQ_ORDER_TRECK:

                model = OrderTrackDetails.class;
                break;

            case REQ_TO_GET_DEALS:

                model = DealsManagement.class;
                break;

            case REQ_ADD_STRIPE_CARD:
                model = String.class;
                break;

            case REQ_TO_LOGIN:

                model = LoginDetails.class;
                break;

            case REQ_SOCIAL_LOGIN:

                model = LoginDetails.class;
                break;

            case REQ_GET_PROMOTIONIMAGE:

                model = BannerImageModel.class;
                break;

            case REQ_FORGOT_PASSWORD:

                model = String.class;
                break;

            case REQ_UPDATE_IMAGE:

                model = UserImage.class;
                break;

            case REQ_TO_SIGNUP:

                model = String.class;
                break;

            case REQ_PROFILE_UPADATE:

                model = String.class;
                break;

            case REQ_GET_PROFILE:

                model = CustomerDetail.class;
                break;

            case REQ_TO_GET_STORELIST:

                model = RestaurantListResponse.class;
                break;

            case REQ_TO_GET_STOREITEMS:

                model = RestaurantMenuListResponse.class;
                break;


            case REQ_TO_REVIEWS:

                model = ViewReviews.class;
                break;

            case REQ_GET_STRIPE_CARD:
                model = StripeCardList.class;
                break;

            case REQ_REWARD_HISTORY:
                model = RewardHistoryList.class;
                break;

            case REQ_ADD_WALLET:
                model = AddWalletBalance.class;
                break;


            case REQ_TO_GET_STORESUB_ITEMS:

                model = RestaurantsSubItems.class;
                break;



            case REQ_TO_CHANGE_PASSWORD:

                model = String.class;
                break;

            case REQ_TO_BOOKTABLE:

                model = String.class;
                break;

            case REQ_TO_BOOKTABLE_HISTORY:

                model = BookTableHistory.class;
                break;


            case REQ_ADDON:
                model=AddonList.class;
                break;


            case REQ_SUBADDON:
                model=SubAddonList.class;
                break;

            case REQ_TIME:
                model = String.class;
                break;

            case REQ_GUEST_COUNT:
                model = String.class;
                break;

            case REQ_VOUCHER_CODE:
                model = VoucherDetailsList.class;
                break;

            case REQ_CHECK_DELIVERY_AREA:
                model = String.class;
                break;

            case REQ_SEND_REVIEW:
                model = String.class;
                break;

            case REQ_GET_WALLETHISTORY:
                model = WalletHistoryList.class;
                break;

            case REQ_ORDER_VIEW:
                model=OrderViewResponse.class;
                break;

            case REQ_ORDER_REGISTER:
                model = OrderSuccess.class;
                break;

             case REQ_STRIPE_PAYMENT:
                model = String.class;
                break;

            case REQ_GET_CURRENTADDRESS:
                model = LocationModel.class;
                break;

            case REQ_SETTINGS:
                model = SettingsList.class;
                break;

            case REQ_SEARCH_OPTIONS:
                model = SearchOptionList.class;
                break;

            case REQ_SUB_DISTRIC:
                model = SubDistricList.class;
                break;

            case REQ_CHANGE_FAV_STATUS:
                model = FavouritModel.class;
                break;

            case REQ_CHANGE_FAV_LIST:
                model = MyFavoriteListModel.class;
                break;

            case REQ_GET_REWARD:
                model = RewardList.class;
                break;

            case REQ_REFER_HISTORY:
                model = ReferHistory.class;
                break;

            default:
                model = String.class;
                break;
        }
        return model;
    }


    private Object getJsonModelType(String data) {
        Object result = null;
        try {
            Gson gson = new Gson();
            result = gson.fromJson(data, getModel());
            Log.e("result from gson","result from gson"+result);

        } catch (Exception e) {
            e.printStackTrace();;
            Log.e("ServerRequestHandler ", "" + e);
        }
        return result;
    }


    /***********************************************************************************************/
    //SSL Certification creation method but not use
    /***********************************************************************************************/
    //HUrlStack
    HurlStack hurlStack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }
    };

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
        InputStream caInput = context.getResources().openRawResource(R.raw.random); // this cert file stored in \app\src\main\res\raw folder path

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
    /***********************************************************************************************/
    /***********************************************************************************************/


}
