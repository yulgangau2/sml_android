package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 10-09-2016.
 */
public class OrderSuccess implements Serializable {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("message")
        public String message;
        @SerializedName("order_id")
        public String order_id;
    }
}
