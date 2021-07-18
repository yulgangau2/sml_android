package com.eatyhero.customer.model;

/**
 * Created by admin on 27-12-2016.
 */

public class RestaurantNameList {

    String id;
    String storeImage;
    String storeName;
    String storeCusine;
    String estimationTime;
    String rating;
    String offer;
    String status;
    String ratingCount;
    String collection;
    String delivery;
    String distance;
    String minOrder;
    String delCharge;
    String offerPercent;


    public RestaurantNameList(String id, String storeImage,
                              String storeName, String storeCusine, String estimationTime,
                              String rating, String offer, String status, String ratingCount, String collection, String delivery,
                              String distance,String minOrder,String delCharge,String offerPercent) {
        this.id = id;
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeCusine = storeCusine;
        this.estimationTime = estimationTime;
        this.rating = rating;
        this.offer = offer;
        this.status = status;
        this.ratingCount = ratingCount;
        this.collection = collection;
        this.delivery = delivery;
        this.distance = distance;
        this.minOrder = minOrder;
        this.delCharge = delCharge;
        this.offerPercent = offerPercent;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCusine() {
        return storeCusine;
    }

    public void setStoreCusine(String storeCusine) {
        this.storeCusine = storeCusine;
    }

    public String getEstimationTime() {
        return estimationTime;
    }

    public void setEstimationTime(String estimationTime) {
        this.estimationTime = estimationTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
    }

    public String getDelCharge() {
        return delCharge;
    }

    public void setDelCharge(String delCharge) {
        this.delCharge = delCharge;
    }

    public String getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(String offerPercent) {
        this.offerPercent = offerPercent;
    }
}

