package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 28-03-2017.
 */
public class RestaurantOpenClose implements Serializable{

    @SerializedName("firstopen")
    String firstopen;

    @SerializedName("today")
    String today;

    @SerializedName("firstclose")
    String firstclose;

    @SerializedName("secondopen")
    String secondopen;

    @SerializedName("secondclose")
    String secondclose;

    @SerializedName("status")
    String status;


    public String getFirstopen() {
        return firstopen;
    }

    public String getToday() {
        return today;
    }

    public String getFirstclose() {
        return firstclose;
    }

    public String getSecondopen() {
        return secondopen;
    }

    public String getSecondclose() {
        return secondclose;
    }

    public String getStatus() {
        return status;
    }

    public void setFirstopen(String firstopen) {
        this.firstopen = firstopen;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public void setFirstclose(String firstclose) {
        this.firstclose = firstclose;
    }

    public void setSecondopen(String secondopen) {
        this.secondopen = secondopen;
    }

    public void setSecondclose(String secondclose) {
        this.secondclose = secondclose;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
