package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 08-10-2015.
 */
public class AddWalletBalance {

    @SerializedName("walletBalance")
    String walletBalance;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }
}
