package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 10/8/2015.
 */
public class StripeCardList implements Serializable {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class CardDetails implements Serializable {
        @SerializedName("id")
        public String id;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("customer_name")
        public String customer_name;
        @SerializedName("stripe_token_id")
        public String stripe_token_id;
        @SerializedName("stripe_customer_id")
        public String stripe_customer_id;
        @SerializedName("card_id")
        public String card_id;
        @SerializedName("card_number")
        public String card_number;
        @SerializedName("card_brand")
        public String card_brand;
        @SerializedName("card_type")
        public String card_type;
        @SerializedName("exp_month")
        public String exp_month;
        @SerializedName("exp_year")
        public String exp_year;
        @SerializedName("country")
        public String country;
        @SerializedName("client_ip")
        public String client_ip;
        @SerializedName("status")
        public String status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;

        boolean newCard = false;

        public boolean isNewCard() {
            return newCard;
        }

        public void setNewCard(boolean newCard) {
            this.newCard = newCard;
        }
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("cardDetails")
        public ArrayList<CardDetails> cardDetails;
    }
}


