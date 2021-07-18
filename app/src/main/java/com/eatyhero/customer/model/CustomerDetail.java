package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 01-04-2017.
 */

public class CustomerDetail implements Serializable {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class CustomerDetails {
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

    public static class Result {
        @SerializedName("success")
        public String success;
        @SerializedName("customerId")
        public String customerId;
        @SerializedName("firstName")
        public String firstName;
        @SerializedName("lastName")
        public String lastName;
        @SerializedName("email")
        public String email;
        @SerializedName("customerPhone")
        public String customerPhone;
        @SerializedName("customerDetails")
        public CustomerDetails customerDetails;
    }
}
