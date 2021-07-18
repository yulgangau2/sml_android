package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 8/10/2015.
 */
public class CartItemsList implements Serializable{

    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Restaurant_menu {
        @SerializedName("id")
        public String id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("category_id")
        public String category_id;
        @SerializedName("menu_name")
        public String menu_name;
        @SerializedName("price_option")
        public String price_option;
        @SerializedName("menu_description")
        public String menu_description;
        @SerializedName("menu_image")
        public String menu_image;
        @SerializedName("menu_type")
        public String menu_type;
        @SerializedName("menu_addon")
        public String menu_addon;
        @SerializedName("popular_dish")
        public String popular_dish;
        @SerializedName("spicy_dish")
        public String spicy_dish;
        @SerializedName("favourite")
        public String favourite;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class Carts {
        @SerializedName("id")
        public String id;
        @SerializedName("order_id")
        public String order_id;
        @SerializedName("temp_orderid")
        public String temp_orderid;
        @SerializedName("menu_id")
        public String menu_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("menu_name")
        public String menu_name;
        @SerializedName("subaddons_name")
        public String subaddons_name;
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("menu_price")
        public String menu_price;
        @SerializedName("offer_price")
        public String offer_price;
        @SerializedName("total_price")
        public String total_price;
        @SerializedName("quantity")
        public String quantity;
        @SerializedName("menu_description")
        public String menu_description;
        @SerializedName("menu_image")
        public String menu_image;
        @SerializedName("session_id")
        public String session_id;
        @SerializedName("created")
        public String created;
        @SerializedName("updated")
        public String updated;
        @SerializedName("restaurant_menu")
        public Restaurant_menu restaurant_menu;
    }

    public static class User {
        @SerializedName("id")
        public String id;
        @SerializedName("role_id")
        public String role_id;
        @SerializedName("username")
        public String username;
        @SerializedName("password")
        public String password;
        @SerializedName("permissions")
        public String permissions;
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("phone_number")
        public String phone_number;
        @SerializedName("wallet_amount")
        public String wallet_amount;
        @SerializedName("image")
        public String image;
        @SerializedName("newsletter")
        public String newsletter;
        @SerializedName("device_type")
        public String device_type;
        @SerializedName("device_id")
        public String device_id;
        @SerializedName("status")
        public String status;
        @SerializedName("deleted_status")
        public String deleted_status;
        @SerializedName("created")
        public String created;
        @SerializedName("updated")
        public String updated;
    }

    public static class Restaurant {
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("id")
        public String id;
        @SerializedName("restaurant_logo")
        public String restaurant_logo;
        @SerializedName("contact_address")
        public String contact_address;
        @SerializedName("contact_phone")
        public String contact_phone;
    }

    public static class OrderDetail {
        @SerializedName("id")
        public String id;
        @SerializedName("order_number")
        public String order_number;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("driver_id")
        public String driver_id;
        @SerializedName("ref_number")
        public String ref_number;
        @SerializedName("customer_name")
        public String customer_name;
        @SerializedName("customer_email")
        public String customer_email;
        @SerializedName("customer_phone")
        public String customer_phone;
        @SerializedName("source_latitude")
        public String source_latitude;
        @SerializedName("source_longitude")
        public String source_longitude;
        @SerializedName("destination_latitude")
        public String destination_latitude;
        @SerializedName("destination_longitude")
        public String destination_longitude;
        @SerializedName("flat_no")
        public String flat_no;
        @SerializedName("google_address")
        public String google_address;
        @SerializedName("address")
        public String address;
        @SerializedName("landmark")
        public String landmark;
        @SerializedName("state_name")
        public String state_name;
        @SerializedName("city_name")
        public String city_name;
        @SerializedName("location_name")
        public String location_name;
        @SerializedName("user_type")
        public String user_type;
        @SerializedName("order_type")
        public String order_type;
        @SerializedName("assoonas")
        public String assoonas;
        @SerializedName("order_description")
        public String order_description;
        @SerializedName("delivery_date")
        public String delivery_date;
        @SerializedName("delivery_time")
        public String delivery_time;
        @SerializedName("delivery_time_slot")
        public String delivery_time_slot;
        @SerializedName("delivered_time")
        public String delivered_time;
        @SerializedName("offer_percentage")
        public String offer_percentage;
        @SerializedName("offer_amount")
        public String offer_amount;
        @SerializedName("tax_percentage")
        public String tax_percentage;
        @SerializedName("tax_amount")
        public String tax_amount;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("voucher_code")
        public String voucher_code;
        @SerializedName("voucher_percentage")
        public String voucher_percentage;
        @SerializedName("voucher_amount")
        public String voucher_amount;
        @SerializedName("tip_percentage")
        public String tip_percentage;
        @SerializedName("tip_amount")
        public String tip_amount;
        @SerializedName("order_sub_total")
        public String order_sub_total;
        @SerializedName("order_grand_total")
        public String order_grand_total;
        @SerializedName("cardfee_percentage")
        public String cardfee_percentage;
        @SerializedName("cardfee_price")
        public String cardfee_price;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("payment_method")
        public String payment_method;
        @SerializedName("split_payment")
        public String split_payment;
        @SerializedName("transaction_id")
        public String transaction_id;
        @SerializedName("distance")
        public String distance;
        @SerializedName("driver_invoice")
        public String driver_invoice;
        @SerializedName("driver_invoice_number")
        public String driver_invoice_number;
        @SerializedName("driver_deliver_date")
        public String driver_deliver_date;
        @SerializedName("driver_charge")
        public int driver_charge;
        @SerializedName("failed_reason")
        public String failed_reason;
        @SerializedName("reward_offer")
        public String reward_offer;
        @SerializedName("reward_offer_percentage")
        public String reward_offer_percentage;
        @SerializedName("stripe_customerid")
        public String stripe_customerid;
        @SerializedName("payerID")
        public String payerID;
        @SerializedName("paymentToken")
        public String paymentToken;
        @SerializedName("paymentID")
        public String paymentID;
        @SerializedName("status")
        public String status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("completed_time")
        public String completed_time;
        @SerializedName("carts")
        public ArrayList<Carts> carts;
        @SerializedName("user")
        public User user;
        @SerializedName("restaurant")
        public Restaurant restaurant;
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("OrderDetail")
        public OrderDetail OrderDetail;
    }
}
