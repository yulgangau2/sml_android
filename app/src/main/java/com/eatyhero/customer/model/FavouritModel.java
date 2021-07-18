package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 27-03-2018.
 */

public class FavouritModel {

    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("RstLikesCnt")
        public String RstLikesCnt;
        @SerializedName("message")
        public String message;
        @SerializedName("success")
        public int success;
    }
}
