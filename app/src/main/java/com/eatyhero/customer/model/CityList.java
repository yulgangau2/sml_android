package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 29-06-2016.
 */
public class CityList implements Serializable {

    public ArrayList<Cities> cityList;


    public class Cities implements Serializable  {

        @SerializedName("id")
        String id;

        @SerializedName("city_name")
        String cityName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
