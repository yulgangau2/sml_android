package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 24-01-2018.
 */

public class AddressListCheckout implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class AddressList {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("title")
        public String title;
        @SerializedName("flat_no")
        public String flat_no;
        @SerializedName("address")
        public String address;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("state_id")
        public String state_id;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("location_id")
        public String location_id;
        @SerializedName("address_phone")
        public String address_phone;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("to_distance")
        public String to_distance;
        @SerializedName("deliveryCharge")
        public String deliveryCharge;
    }

    public static class OutOfDelivery {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("title")
        public String title;
        @SerializedName("flat_no")
        public String flat_no;
        @SerializedName("address")
        public String address;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("state_id")
        public String state_id;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("location_id")
        public String location_id;
        @SerializedName("address_phone")
        public String address_phone;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("to_distance")
        public String to_distance;
    }

    public static class Result {
        @SerializedName("success")
        public String success;
        @SerializedName("addressList")
        public ArrayList<AddressList> addressList;
        @SerializedName("outOfDelivery")
        public ArrayList<OutOfDelivery> outOfDelivery;
    }
}
