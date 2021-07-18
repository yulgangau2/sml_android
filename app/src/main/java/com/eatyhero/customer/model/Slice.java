package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 8/12/2015.
 */
public class Slice implements Serializable {

    @SerializedName("sub_name")
    String sliceName;

    @SerializedName("orginal_price")
    String slicePrice;

    @SerializedName("id")
    String sliceId;

    public String getSliceName() {
        return sliceName;
    }

    public void setSliceName(String sliceName) {
        this.sliceName = sliceName;
    }

    public String getSlicePrice() {
        return slicePrice;
    }

    public void setSlicePrice(String slicePrice) {
        this.slicePrice = slicePrice;
    }

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }
}
