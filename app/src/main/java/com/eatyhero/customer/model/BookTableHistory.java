package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 21-06-2017.
 */

public class BookTableHistory implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Restaurant {
        @SerializedName("id")
        public String id;
        @SerializedName("restaurant_name")
        public String restaurant_name;
    }

    public static class Bookings {
        @SerializedName("id")
        public String id;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("booking_id")
        public String booking_id;
        @SerializedName("customer_name")
        public String customer_name;
        @SerializedName("guest_count")
        public String guest_count;
        @SerializedName("booking_email")
        public String booking_email;
        @SerializedName("booking_phone")
        public String booking_phone;
        @SerializedName("booking_instruction")
        public String booking_instruction;
        @SerializedName("booking_date")
        public String booking_date;
        @SerializedName("booking_time")
        public String booking_time;
        @SerializedName("status")
        public String status;
        @SerializedName("cancel_reason")
        public String cancel_reason;
        @SerializedName("created")
        public String created;
        @SerializedName("updated")
        public String updated;
        @SerializedName("restaurant")
        public Restaurant restaurant;
        @SerializedName("store_name")
        public String store_name;
    }

    public static class Result {
        @SerializedName("success")
        public String success;
        @SerializedName("bookings")
        public ArrayList<Bookings> bookings;
    }
}
