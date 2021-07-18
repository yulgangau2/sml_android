package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 9/28/2015.
 */
public class OrderHistoryList implements Serializable{

    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class OrderLists {
        @SerializedName("order_number")
        public String order_number;
        @SerializedName("id")
        public String id;
        @SerializedName("payment_method")
        public String payment_method;
        @SerializedName("order_grand_total")
        public String order_grand_total;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("status")
        public String status;
        @SerializedName("source_latitude")
        public String source_latitude;
        @SerializedName("source_longitude")
        public String source_longitude;
        @SerializedName("destination_latitude")
        public String destination_latitude;
        @SerializedName("destination_longitude")
        public String destination_longitude;
        @SerializedName("rating")
        public String rating;
        @SerializedName("distance")
        public String distance;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("cuisine_list")
        public String cuisine_list;
        @SerializedName("order_type")
        public String order_type;
        @SerializedName("restaurant")
        public Restaurant restaurant;
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("orderLists")
        public ArrayList<OrderLists> orderLists;
    }
    public static class Restaurant {
        @SerializedName("restaurant_cuisine")
        public String restaurant_cuisine;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("id")
        public String id;
    }
}
