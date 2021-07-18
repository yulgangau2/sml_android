package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 21-02-2018.
 */

public class RewardList implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Result {
        @SerializedName("rewardPoint")
        public String rewardPoint;
        @SerializedName("rewardPercentage")
        public String rewardPercentage;
        @SerializedName("earn_point")
        public String earn_point;
        @SerializedName("rewardAmount")
        public String rewardAmount;
        @SerializedName("rewardPoints")
        public String rewardPoints;
        @SerializedName("success")
        public int success;
    }
}
