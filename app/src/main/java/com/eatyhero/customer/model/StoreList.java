package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 30-06-2016.
 */
public class StoreList implements Serializable {


    public ArrayList<ReviewList>  Review;

    @SerializedName("id")
    String id;

    @SerializedName("store_name")
    String storeName;

    @SerializedName("store_logo")
    String storeLogo;

    @SerializedName("delivery")
    String delivery;

    @SerializedName("collection")
    String collection;

    @SerializedName("minimum_order")
    String minimumOrder;

    @SerializedName("estimate_time")
    String estimateTime;

    @SerializedName("delivery_distance")
    String deliveryDistance;

    @SerializedName("delivery_charge")
    String deliveryCharge;

    @SerializedName("distance")
    String distance;

    @SerializedName("status")
    String status;

    @SerializedName("rating")
    String rating;

    @SerializedName("ratingCount")
    String ratingCount;

    @SerializedName("offer")
    String offer;

    @SerializedName("store_cuisines")
    String storeCuisines;

    @SerializedName("offer_percentage")
    String offer_percentage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(String minimumOrder) {
        this.minimumOrder = minimumOrder;
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

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getStoreCuisines() {
        return storeCuisines;
    }

    public void setStoreCuisines(String storeCuisines) {
        this.storeCuisines = storeCuisines;
    }

    public String getOffer_percentage() {
        return offer_percentage;
    }

    public void setOffer_percentage(String offer_percentage) {
        this.offer_percentage = offer_percentage;
    }
}
