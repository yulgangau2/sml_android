package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 22-02-2018.
 */

public class RewardHistoryList implements Serializable {


    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("success")
        public String success;
        @SerializedName("customerPoints")
        public List<CustomerPoints> customerPoints;
        @SerializedName("totalPoints")
        public String totalPoints;
    }

    public static class CustomerPoints {
        @SerializedName("order")
        public Order order;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public String status;
        @SerializedName("type")
        public String type;
        @SerializedName("points")
        public String points;
        @SerializedName("total")
        public String total;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("order_id")
        public String order_id;
        @SerializedName("id")
        public String id;
    }

    public static class Order {
        @SerializedName("reward_offer")
        public String reward_offer;
        @SerializedName("order_number")
        public String order_number;
    }
}
