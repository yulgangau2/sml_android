package com.eatyhero.customer.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LoginSession {

    Activity activity;
    Utility utility;

    SharedPreferences deviceIdPreferences;
    SharedPreferences.Editor deviceIdEditor;

    SharedPreferences setAddressPreferences;
    SharedPreferences.Editor setAddressEditor;

    SharedPreferences checkedPreference;
    SharedPreferences.Editor checkEditor;

    SharedPreferences walletPreference;
    SharedPreferences.Editor walletEditor;

    SharedPreferences saveCardPreference;
    SharedPreferences.Editor saveCardEditor;

    SharedPreferences locationPreferences;
    SharedPreferences.Editor locationEditor;

    SharedPreferences gcmPreferences;
    SharedPreferences.Editor gcmEditor;

    SharedPreferences subPreferences;
    SharedPreferences.Editor subEditor;

    SharedPreferences fromPreferences;
    SharedPreferences.Editor fromEditor;

    SharedPreferences addtocartIdPreferences;
    SharedPreferences.Editor addtocartIdEditor;

    SharedPreferences resPreferences;
    SharedPreferences.Editor resEditor;

    SharedPreferences LocationPreferences;
    SharedPreferences.Editor LocationEditor;

    SharedPreferences dealPreferences;
    SharedPreferences.Editor dealEditor;

    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginEditor;

    SharedPreferences pusherPreferences;
    SharedPreferences.Editor pusherEditor;

    SharedPreferences catPreferences;
    SharedPreferences.Editor catEditor;

    SharedPreferences cartPreferences;
    SharedPreferences.Editor cartEditor;

    SharedPreferences rememberPreferences;
    SharedPreferences.Editor rememberEditor;

    SharedPreferences subMenuScreenPreferences;
    SharedPreferences.Editor subMenuScreenEditor;

    SharedPreferences storeId;
    SharedPreferences.Editor storeIdEditor;

    SharedPreferences catName;
    SharedPreferences.Editor catNameEditor;

    SharedPreferences getIns;
    SharedPreferences.Editor getInsEditor;

    SharedPreferences orderIDPref;
    SharedPreferences.Editor orderIDEditor;

    SharedPreferences currencyPref;
    SharedPreferences.Editor currencyEditor;

    SharedPreferences dbResNamePref;
    SharedPreferences.Editor dbResNameEditor;


    SharedPreferences cityidPref;
    SharedPreferences.Editor cityidEditor;

    SharedPreferences cityidresPref;
    SharedPreferences.Editor cityidresEditor;

    SharedPreferences cityidcusPref;
    SharedPreferences.Editor cityidcusEditor;

    SharedPreferences subdisidPref;
    SharedPreferences.Editor subdisidEditor;

    SharedPreferences residPref;
    SharedPreferences.Editor residEditor;

    SharedPreferences cusidPref;
    SharedPreferences.Editor cusidEditor;

    SharedPreferences currlatPref;
    SharedPreferences.Editor currlatEditor;

    SharedPreferences currlngPref;
    SharedPreferences.Editor currlngEditor;


    SharedPreferences cusinNamePref;
    SharedPreferences.Editor cusinNameEditor;

    SharedPreferences deliveryPref;
    SharedPreferences.Editor deliveryEditor;

    SharedPreferences distancePref;
    SharedPreferences.Editor distanceEditor;

    SharedPreferences ratingPref;
    SharedPreferences.Editor ratingEditor;

    SharedPreferences languagePref;
    SharedPreferences.Editor languageEditor;

    SharedPreferences searchTypePref;
    SharedPreferences.Editor searchTypeEditor;

    SharedPreferences appStatusPref;
    SharedPreferences.Editor appStatusEditor;


    public static LoginSession loginSession = null;
    public Object getPlace;

    //Constructor
    public static LoginSession getInstance(Activity activity) {
        if (loginSession == null) {
            loginSession = new LoginSession(activity);
        }
        return loginSession;
    }

    public LoginSession(Activity activity) {
        super();
        this.activity = activity;

        dbResNamePref = this.activity.getSharedPreferences("DBRESTAURANT", Context.MODE_PRIVATE);
        dbResNameEditor = dbResNamePref.edit();

        orderIDPref = this.activity.getSharedPreferences("orderid", Context.MODE_PRIVATE);
        orderIDEditor = orderIDPref.edit();

        resPreferences = this.activity.getSharedPreferences("restaurant", Context.MODE_PRIVATE);
        resEditor = resPreferences.edit();

        deviceIdPreferences = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        deviceIdEditor = deviceIdPreferences.edit();

        setAddressPreferences = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        setAddressEditor = setAddressPreferences.edit();


        saveCardPreference = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        saveCardEditor = saveCardPreference.edit();

        walletPreference = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        walletEditor = walletPreference.edit();

        storeId = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        storeIdEditor = storeId.edit();

        catName = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        catNameEditor = catName.edit();

        dealPreferences = this.activity.getSharedPreferences("deviceId", Context.MODE_PRIVATE);
        dealEditor = dealPreferences.edit();

        checkedPreference = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        checkEditor = checkedPreference.edit();

        locationPreferences = this.activity.getSharedPreferences("location", Context.MODE_PRIVATE);
        locationEditor = locationPreferences.edit();

        getIns = this.activity.getSharedPreferences("location", Context.MODE_PRIVATE);
        getInsEditor = getIns.edit();

        loginPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        loginEditor = loginPreferences.edit();

        pusherPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        pusherEditor = pusherPreferences.edit();

        catPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        catEditor = catPreferences.edit();

        cartPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        cartEditor = cartPreferences.edit();

        subPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        subEditor = subPreferences.edit();

        fromPreferences = this.activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        fromEditor = fromPreferences.edit();

        rememberPreferences = this.activity.getSharedPreferences("remember", Context.MODE_PRIVATE);
        rememberEditor = rememberPreferences.edit();

        addtocartIdPreferences = this.activity.getSharedPreferences("ADDTOCART", Context.MODE_PRIVATE);
        addtocartIdEditor = addtocartIdPreferences.edit();

        subMenuScreenPreferences = this.activity.getSharedPreferences("SUBMENU", Context.MODE_PRIVATE);
        subMenuScreenEditor = subMenuScreenPreferences.edit();

        gcmPreferences = this.activity.getSharedPreferences("FCM", Context.MODE_PRIVATE);
        gcmEditor = gcmPreferences.edit();

        LocationPreferences = this.activity.getSharedPreferences("LATLANGLocation", Context.MODE_PRIVATE);
        LocationEditor = LocationPreferences.edit();

        currencyPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        currencyEditor = currencyPref.edit();


        cityidPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        cityidEditor = cityidPref.edit();

        cityidresPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        cityidresEditor = cityidresPref.edit();

        cityidcusPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        cityidcusEditor = cityidcusPref.edit();

        subdisidPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        subdisidEditor = subdisidPref.edit();

        residPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        residEditor = residPref.edit();

        cusidPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        cusidEditor = cusidPref.edit();

        currlatPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        currlatEditor = currlatPref.edit();

        currlngPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        currlngEditor = currlngPref.edit();

        cusinNamePref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        cusinNameEditor = cusinNamePref.edit();

        deliveryPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        deliveryEditor = deliveryPref.edit();

        distancePref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        distanceEditor = distancePref.edit();

        ratingPref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        ratingEditor = ratingPref.edit();

        languagePref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        languageEditor = languagePref.edit();

        searchTypePref = this.activity.getSharedPreferences("Currency", Context.MODE_PRIVATE);
        searchTypeEditor = searchTypePref.edit();

        appStatusPref = this.activity.getSharedPreferences("AppStatus", Context.MODE_PRIVATE);
        appStatusEditor = appStatusPref.edit();


    }

    // login store
    public void saveDeviceId(String deviceId) {
        deviceIdEditor.putString("deviceId", deviceId);
        deviceIdEditor.commit();
    }

    public void saveFcmId(String fcmid) {
        gcmEditor.putString("fcmid", fcmid);
        gcmEditor.commit();
    }

    public String getFcmId() {
        String gcmid = gcmPreferences.getString("fcmid", "");
        return gcmid;
    }

    // login store
    public void saveLocation(String areaName) {
        locationEditor.putString("areaName", areaName);
        locationEditor.commit();
    }

    //get user email
    public String getAreaName() {
        String areaName = locationPreferences.getString("areaName", "");
        return areaName;
    }


    // login store
    public void saveLogInStore(String userID, String first_user, String userName, String lastName, String userEmail, String userPhone, String userImage) {
        loginEditor.putBoolean("boolean", true);
        loginEditor.putString("userID", userID);
        loginEditor.putString("first_user", first_user);
        loginEditor.putString("userName", userName);
        loginEditor.putString("lastName", lastName);
        loginEditor.putString("userEmail", userEmail);
        loginEditor.putString("userPhone", userPhone);
        loginEditor.putString("userImage", userImage);
        loginEditor.commit();
    }

    //login check
    public boolean isLoggedIn() {
        return loginPreferences.getBoolean("boolean", false);
    }

    //logout
    public void logout() {

        loginEditor.clear();
        loginEditor.commit();
        utility = Utility.getInstance(activity);
        utility.toast("Logout Successfully");

    }

    //get user id

    public void setFirstUser(String first_user) {

        loginEditor.putString("first_user", first_user);
        loginEditor.commit();
    }

    public String getFirstUser() {
        String firstUser = loginPreferences.getString("first_user", "");
        return firstUser;
    }

    //get user id
    public String getUserId() {
        String userID = loginPreferences.getString("userID", "");
        return userID;
    }

    //get user email
    public String getUserPhone() {
        String userPhone = loginPreferences.getString("UserPhone", "");
        return userPhone;
    }

    public void saveUserPhone(String UserPhone) {
        loginEditor.putString("UserPhone", UserPhone);
        loginEditor.commit();
    }

    //get user image
    public String getUserImage() {
        String userImage = loginPreferences.getString("userImage", "");
        return userImage;
    }

    // login store
    public void saveDriverImage(String driverImage) {
        loginEditor.putString("userImage", driverImage);
        loginEditor.commit();
    }


    // login store
    public void saveRemember(String rememberEmail, String rememberPassword) {

        Log.e("Values Come", rememberEmail + rememberPassword);
        rememberEditor.putBoolean("boolean", true);
        rememberEditor.putString("rememberEmail", rememberEmail);
        rememberEditor.putString("rememberPassword", rememberPassword);
        rememberEditor.commit();
    }

    public boolean isRemember() {
        return rememberPreferences.getBoolean("boolean", false);
    }

    //get user email
    public String getRememberEmail() {
        String rememberEmail = rememberPreferences.getString("rememberEmail", "");
        return rememberEmail;
    }

    //get user image
    public String getRememberPassword() {
        String rememberPassword = rememberPreferences.getString("rememberPassword", "");
        return rememberPassword;
    }

    //store restaurant tax and delivery charge
    public void saveResDetails(String resID, String resName, String resAddress, String delivery, String free_delivery, String tax,
                               String minOrder,
                               String resDelivery, String resPickup, String firstUser, String normalUser,
                               String firstOfferPercentage, String firstOfferAmount,
                               String normalOfferPercent, String normalOfferRange, String status, String loginUserStatus,
                               String codPayment, String stripePayment, String payPalPayment) {
        resEditor.putString("resid", resID);
        resEditor.putString("resName", resName);
        resEditor.putString("resAddress", resAddress);
        resEditor.putString("delivery", delivery);
        resEditor.putString("free_delivery", free_delivery);
        resEditor.putString("minorder", minOrder);
        resEditor.putString("tax", tax);
        resEditor.putString("ResDelivery", resDelivery);
        resEditor.putString("ResPickup", resPickup);
        resEditor.putString("firstUser", firstUser);
        resEditor.putString("normalUser", normalUser);
        resEditor.putString("firstOfferPercentage", firstOfferPercentage);
        resEditor.putString("firstOfferAmount", firstOfferAmount);
        resEditor.putString("normalOfferPercent", normalOfferPercent);
        resEditor.putString("normalOfferRange", normalOfferRange);
        resEditor.putString("status", status);
        resEditor.putString("loginUserStatus", loginUserStatus);
        resEditor.putString("codPayment", codPayment);
        resEditor.putString("stripePayment", stripePayment);
        resEditor.putString("payPalPayment", payPalPayment);
        resEditor.commit();


    }

    public String getCodPayment() {
        String codPayment = resPreferences.getString("codPayment", "");
        return codPayment;
    }

    public String getStripePayment() {
        String stripePayment = resPreferences.getString("stripePayment", "");
        return stripePayment;
    }

    public String getPayPalPayment() {
        String payPalPayment = resPreferences.getString("payPalPayment", "");
        return payPalPayment;
    }

    public String getFreeDelivery() {
        String freeDelivery = resPreferences.getString("free_delivery", "");
        return freeDelivery;
    }

    public String getStatus() {
        String status = resPreferences.getString("status", "");
        return status;
    }

    //clear restaurant details
    public void clear_restaurant() {
        resEditor.clear();
        resEditor.commit();
    }

    public void saveLoginUserStatus(String firstUserStatus) {

        resEditor.putString("firstUserStatus", firstUserStatus);
        resEditor.commit();


    }

    public void saveCardCount(int cartCount) {

        cartEditor.putString("cartCount", String.valueOf(cartCount));
        cartEditor.commit();

    }

    public String getCartCount() {
        String cartCount = catPreferences.getString("cartCount", "");
        return cartCount;
    }


    public void saveMenuName(String categoryName) {

        catNameEditor.putString("categoryName", String.valueOf(categoryName));
        catNameEditor.commit();


    }

    public String getcatName() {
        String catoName = catName.getString("categoryName", "");
        return catoName;
    }

    public void saveInstruction(String getInstruction) {

        getInsEditor.putString("userImage", getInstruction);
        getInsEditor.commit();
    }

    public String getInstruction() {
        String Ins = getIns.getString("userImage", "");
        return Ins;
    }


    //Pusher keys from lunch screen
    public void setPusherKey(String publishkey) {
        pusherEditor.putString("publishkey", publishkey);
        pusherEditor.commit();
    }

    public String getPublishKey() {
        String secretkey = pusherPreferences.getString("publishkey", "");
        return secretkey;
    }

    public void saveChecked(String aTrue) {

        checkEditor.putString("check", aTrue);
        checkEditor.commit();
    }

    public String getChecked() {
        String checkKey = checkedPreference.getString("check", "");
        return checkKey;
    }


    // login store
    public void saveTrackLocation(String lat, String lng) {
        LocationEditor.putString("lat", lat);
        LocationEditor.putString("lng", lng);
        LocationEditor.commit();
    }

    public void setWalletrAmount(String walletPriceAmount) {

        walletEditor.putString("walletPriceAmount", walletPriceAmount);


        walletEditor.commit();
    }

    public String getWalletAmount() {
        String walletAmt = walletPreference.getString("walletPriceAmount", "");
        return walletAmt;
    }

    public void setCurrencyCode(String currencyCode) {

        currencyEditor.putString("currencyCode", currencyCode);
        currencyEditor.commit();
    }

    public String getCurrencyCode() {

        String currencyCode = currencyPref.getString("currencyCode", "");
        return currencyCode;
    }

    public void setDbResName(String DbresName) {

        dbResNameEditor.putString("DbresName", DbresName);
        dbResNameEditor.commit();
    }

    public void setDbResDetails(String DbresDetails) {

        dbResNameEditor.putString("DbresDetails", DbresDetails);
        dbResNameEditor.commit();
    }

    public String getDbResName() {

        String DbresName = dbResNamePref.getString("DbresName", "");
        return DbresName;
    }

    public String getDbresDetails() {

        String DbresDetails = dbResNamePref.getString("DbresDetails", "");
        return DbresDetails;
    }

    public void setCityid(String cityid) {

        cityidEditor.putString("cityid", cityid);
        cityidEditor.commit();
    }

    public String getCityid() {

        String DbresDetails = cityidPref.getString("cityid", "");
        return DbresDetails;
    }

    public void setCityidRes(String cityidres) {

        cityidresEditor.putString("cityidres", cityidres);
        cityidresEditor.commit();
    }

    public String getCityidRes() {

        String DbresDetails = cityidresPref.getString("cityidres", "");
        return DbresDetails;
    }

    public void setCityidCus(String cityidcus) {

        cityidcusEditor.putString("cityidcus", cityidcus);
        cityidcusEditor.commit();
    }

    public String getCityidCus() {

        String DbresDetails = cityidcusPref.getString("cityidcus", "");
        return DbresDetails;
    }


    public void setSubDis(String subdis) {

        subdisidEditor.putString("subdis", subdis);
        subdisidEditor.commit();
    }

    public String getSubDis() {

        String DbresDetails = subdisidPref.getString("subdis", "");
        return DbresDetails;
    }

    public void setResid(String resid) {

        residEditor.putString("resid", resid);
        residEditor.commit();
    }

    public String getResid() {

        String DbresDetails = residPref.getString("resid", "");
        return DbresDetails;
    }

    public void setcusid(String cusid) {

        cusidEditor.putString("cusid", cusid);
        cusidEditor.commit();
    }

    public String getcusid() {

        String DbresDetails = cusidPref.getString("cusid", "");
        return DbresDetails;
    }

    public void setcurrlat(String currlat) {

        currlatEditor.putString("currlat", currlat);
        currlatEditor.commit();
    }

    public String getcurrlat() {

        String DbresDetails = currlatPref.getString("currlat", "");
        return DbresDetails;
    }

    public void setcurrlng(String currlng) {

        currlngEditor.putString("currlng", currlng);
        currlngEditor.commit();
    }

    public String getcurrlng() {

        String DbresDetails = currlngPref.getString("currlng", "");
        return DbresDetails;
    }

    public void setcusinName(String cusinName) {

        cusinNameEditor.putString("cusinName", cusinName);
        cusinNameEditor.commit();
    }

    public String getcusinName() {

        String DbresDetails = cusinNamePref.getString("cusinName", "");
        return DbresDetails;
    }

    public void setdelivery(String delivery) {

        deliveryEditor.putString("delivery", delivery);
        deliveryEditor.commit();
    }

    public String getdelivery() {

        String DbresDetails = deliveryPref.getString("delivery", "");
        return DbresDetails;
    }

    public void setdistance(String distance) {

        distanceEditor.putString("distance", distance);
        distanceEditor.commit();
    }

    public String getdistance() {

        String DbresDetails = distancePref.getString("distance", "");
        return DbresDetails;
    }

    public void setrating(String rating) {

        ratingEditor.putString("rating", rating);
        ratingEditor.commit();
    }

    public String getrating() {

        String DbresDetails = ratingPref.getString("rating", "");
        return DbresDetails;
    }

    public void setlanguage(String language) {

        languageEditor.putString("language", language);
        languageEditor.commit();
    }

    public String getlanguage() {

        String language = languagePref.getString("language", "");
        return language;
    }

    public void setsearchType(String searchType) {

        searchTypeEditor.putString("searchType", searchType);
        searchTypeEditor.commit();
    }

    public String getsearchType() {

        String searchType = searchTypePref.getString("searchType", "");
        return searchType;
    }

    public void setAppStatus(String appStatus) {

        appStatusEditor.putString("appStatus", appStatus);
        appStatusEditor.commit();
    }

    public String getAppStatus() {

        return appStatusPref.getString("appStatus", "");
    }


}
