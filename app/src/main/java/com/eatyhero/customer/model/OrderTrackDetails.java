package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 28-10-2016.
 */

public class OrderTrackDetails {


    @SerializedName("orderId")
    String orderId;

    @SerializedName("ref_number")
    String refnumber;


    @SerializedName("storeLatitude")
    String storeLatitude;

    @SerializedName("storeLongitude")
    String storeLongitude;


    @SerializedName("driverLatitude")
    String driverLatitude;

    @SerializedName("driverLongitude")
    String driverLongitude;


    @SerializedName("customerLatitude")
    String customerLatitude;

    @SerializedName("customerLongitude")
    String customerLongitude;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefnumber() {
        return refnumber;
    }

    public void setRefnumber(String refnumber) {
        this.refnumber = refnumber;
    }

    public String getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(String storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public String getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(String storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public String getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(String driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public String getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(String driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public String getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(String customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public String getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(String customerLongitude) {
        this.customerLongitude = customerLongitude;
    }
}
