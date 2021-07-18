package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 07-10-2015.
 */
public class VoucherDetailsList {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class VoucherDetails {
        @SerializedName("id")
        public String id;
        @SerializedName("resid")
        public String resid;
        @SerializedName("voucher_code")
        public String voucher_code;
        @SerializedName("type_offer")
        public String type_offer;
        @SerializedName("offer_mode")
        public String offer_mode;
        @SerializedName("free_delivery")
        public String free_delivery;
        @SerializedName("offer_value")
        public String offer_value;
        @SerializedName("eligible_points")
        public String eligible_points;
        @SerializedName("voucher_from")
        public String voucher_from;
        @SerializedName("voucher_to")
        public String voucher_to;
        @SerializedName("status")
        public String status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("voucherDetails")
        public VoucherDetails voucherDetails;
    }
}
