package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 09-02-2017.
 */

public class RestaurantDetailsList implements Serializable {

    public RestaurantDetails storeDetails;

    public class RestaurantDetails {

        @SerializedName("id")
        String id;

        @SerializedName("collection")
        String collection;

        @SerializedName("delivery")
        String delivery;

        @SerializedName("store_name")
        String store_name;

        @SerializedName("status")
        String status;

        @SerializedName("contact_phone")
        String contact_phone;

        @SerializedName("payment_method")
        String payment_method;

        @SerializedName("todayStatus")
        String todayStatus;

        @SerializedName("minimum_order")
        String minimum_order;

        @SerializedName("delivery_charge")
        String delivery_charge;

        @SerializedName("offerRange")
        String offerRange;

        @SerializedName("offerPercentage")
        String offerPercentage;

        @SerializedName("cardFee")
        String cardFee;

        @SerializedName("offerType")
        String offerType;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getMinimum_order() {
            return minimum_order;
        }

        public void setMinimum_order(String minimum_order) {
            this.minimum_order = minimum_order;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getOfferRange() {
            return offerRange;
        }

        public void setOfferRange(String offerRange) {
            this.offerRange = offerRange;
        }

        public String getOfferPercentage() {
            return offerPercentage;
        }

        public void setOfferPercentage(String offerPercentage) {
            this.offerPercentage = offerPercentage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getTodayStatus() {
            return todayStatus;
        }

        public void setTodayStatus(String todayStatus) {
            this.todayStatus = todayStatus;
        }

        public String getCardFee() {
            return cardFee;
        }

        public void setCardFee(String cardFee) {
            this.cardFee = cardFee;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getOfferType() {
            return offerType;
        }

        public void setOfferType(String offerType) {
            this.offerType = offerType;
        }
    }
}
