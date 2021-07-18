package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 29-06-2016.
 */
public class AreaList implements Serializable {

    public ArrayList<Areas> locations;


    public class Areas implements Serializable  {

        @SerializedName("id")
        String id;

        @SerializedName("location_name")
        String locationName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }
    }
}
