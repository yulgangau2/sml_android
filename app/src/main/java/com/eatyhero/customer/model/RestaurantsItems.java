package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 07-09-2016.
 */
public class RestaurantsItems implements Serializable {

    String deal;

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public ArrayList<CategoryList> categoryList;

    public ArrayList<RestaurantOpenClose>  storeTiming;

    public StoreDetails storeDetails;

    public class CategoryList implements Serializable {

        @SerializedName("id")
        String id;

        @SerializedName("category_name")
        String categoryName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    public class StoreDetails implements Serializable {

        @SerializedName("id")
        String id;

        @SerializedName("collection")
        String collection;

        @SerializedName("delivery")
        String delivery;

        @SerializedName("delivery_option")
        String deliveryOption;

        @SerializedName("book_a_table")
        String book_a_table;

        @SerializedName("address")
        String address;

        @SerializedName("store_name")
        String storeName;

        @SerializedName("store_logo")
        String storeLogo;

        @SerializedName("tax")
        String tax;

        @SerializedName("minimum_order")
        String minimumOrder;

        @SerializedName("delivery_charge")
        String deliveryCharge;

        @SerializedName("estimate_time")
        String estimateTime;

        @SerializedName("delivery_distance")
        String deliveryDistance;

        @SerializedName("status")
        String status;

        @SerializedName("offer")
        String offer;

        @SerializedName("offerRange")
        String offerRange;

        @SerializedName("offerPercentage")
        String offerPercentage;

        @SerializedName("rating")
        String rating;

        @SerializedName("cuisines")
        String cuisines;

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

        public String getDeliveryOption() {
            return deliveryOption;
        }

        public void setDeliveryOption(String deliveryOption) {
            this.deliveryOption = deliveryOption;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreLogo() {
            return storeLogo;
        }

        public void setStoreLogo(String storeLogo) {
            this.storeLogo = storeLogo;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getMinimumOrder() {
            return minimumOrder;
        }

        public void setMinimumOrder(String minimumOrder) {
            this.minimumOrder = minimumOrder;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getEstimateTime() {
            return estimateTime;
        }

        public void setEstimateTime(String estimateTime) {
            this.estimateTime = estimateTime;
        }

        public String getDeliveryDistance() {
            return deliveryDistance;
        }

        public void setDeliveryDistance(String deliveryDistance) {
            this.deliveryDistance = deliveryDistance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getCuisines() {
            return cuisines;
        }

        public void setCuisines(String cuisines) {
            this.cuisines = cuisines;
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

        public String getBook_a_table() {
            return book_a_table;
        }

        public void setBook_a_table(String book_a_table) {
            this.book_a_table = book_a_table;
        }
    }
}
