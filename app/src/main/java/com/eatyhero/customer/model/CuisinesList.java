package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 06-09-2016.
 */
public class CuisinesList implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("cuisine_name")
    String cuisineName;

    @SerializedName("cuisineCount")
    String cuisineCount;

    boolean checked;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getCuisineCount() {
        return cuisineCount;
    }

    public void setCuisineCount(String cuisineCount) {
        this.cuisineCount = cuisineCount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
