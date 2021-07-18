package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 8/12/2015.
 */
public class MainAddon implements Serializable {


    @SerializedName("mainaddons_id")
    String mainAddonId;

    @SerializedName("mainaddons_name")
    String mainAddonsName;

    @SerializedName("mainaddons_mini_count")
    String mainaddonsMiniCount;

    @SerializedName("mainaddons_count")
    String mainAddonsCount;

    public String getMainAddonid() {
        return mainAddonId;
    }

    public String getMainAddonId() {
        return mainAddonId;
    }

    public void setMainAddonId(String mainAddonId) {
        this.mainAddonId = mainAddonId;
    }

    public String getMainAddonsName() {
        return mainAddonsName;
    }

    public void setMainAddonsName(String mainAddonsName) {
        this.mainAddonsName = mainAddonsName;
    }

    public String getMainAddonsCount() {
        return mainAddonsCount;
    }

    public void setMainAddonsCount(String mainAddonsCount) {
        this.mainAddonsCount = mainAddonsCount;
    }

    public String getMainaddonsMiniCount() {
        return mainaddonsMiniCount;
    }

    public void setMainaddonsMiniCount(String mainaddonsMiniCount) {
        this.mainaddonsMiniCount = mainaddonsMiniCount;
    }
}

