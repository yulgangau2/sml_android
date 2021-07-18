package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 06-03-2018.
 */

public class ReferHistory implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Referrals {
        @SerializedName("id")
        public String id;
        @SerializedName("referral_option")
        public String referral_option;
        @SerializedName("invite_amount")
        public String invite_amount;
        @SerializedName("receive_amount")
        public String receive_amount;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class ReferredList {
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
        @SerializedName("username")
        public String username;
        @SerializedName("created")
        public String created;
    }

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
        @SerializedName("address")
        public String address;
        @SerializedName("referral_code")
        public String referral_code;
        @SerializedName("referred_by")
        public String referred_by;
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
        @SerializedName("modified")
        public String modified;
        @SerializedName("referral_url")
        public String referral_url;
    }

    public static class Result {
        @SerializedName("referrals")
        public Referrals referrals;
        @SerializedName("referredList")
        public ArrayList<ReferredList> referredList;
        @SerializedName("customerDetails")
        public CustomerDetails customerDetails;
        @SerializedName("success")
        public int success;
    }
}
