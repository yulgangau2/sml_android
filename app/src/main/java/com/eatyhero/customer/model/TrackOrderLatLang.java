package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 10-02-2017.
 */
public class TrackOrderLatLang {

   /* public String latitude;
    public String longitude;


    public TrackOrderLatLang(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }*/


    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
