package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 09-09-2016.
 */
public class WalletHistoryList implements Serializable {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class WalletHistory {
        @SerializedName("id")
        public String id;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("purpose")
        public String purpose;
        @SerializedName("transaction_type")
        public String transaction_type;
        @SerializedName("transaction_details")
        public String transaction_details;
        @SerializedName("amount")
        public String amount;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("walletHistory")
        public ArrayList<WalletHistory> walletHistory;
        @SerializedName("walletAmount")
        public String walletAmount;
    }
}
