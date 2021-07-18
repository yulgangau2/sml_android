package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 22-11-2017.
 */

public class LocationModel implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("address")
        public String address;
    }
}
