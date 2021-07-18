package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 13-04-2018.
 */

public class SampleJava {

    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("orderLists")
        public List<OrderLists> orderLists;
        @SerializedName("success")
        public int success;
    }

    public static class OrderLists {
        @SerializedName("cuisine_list")
        public String cuisine_list;
        @SerializedName("rating")
        public int rating;
        @SerializedName("reviews")
        public List<String> reviews;
        @SerializedName("restaurant")
        public Restaurant restaurant;
        @SerializedName("destination_longitude")
        public String destination_longitude;
        @SerializedName("destination_latitude")
        public String destination_latitude;
        @SerializedName("source_longitude")
        public String source_longitude;
        @SerializedName("source_latitude")
        public String source_latitude;
        @SerializedName("status")
        public String status;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("order_grand_total")
        public double order_grand_total;
        @SerializedName("payment_method")
        public String payment_method;
        @SerializedName("distance")
        public String distance;
        @SerializedName("delivery_charge")
        public int delivery_charge;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
        @SerializedName("order_number")
        public String order_number;
    }

    public static class Restaurant {
        @SerializedName("restaurant_cuisine")
        public String restaurant_cuisine;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("id")
        public int id;
    }
}
