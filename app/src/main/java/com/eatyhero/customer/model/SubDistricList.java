package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 23-03-2018.
 */

public class SubDistricList {

    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("distlist")
        public ArrayList<Distlist> distlist;
        @SerializedName("success")
        public int success;
    }

    public static class Distlist {
        @SerializedName("subdistricts")
        public ArrayList<Sub> subdistricts;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public String status;
        @SerializedName("district_name")
        public String district_name;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("country_id")
        public String country_id;
        @SerializedName("id")
        public String id;
    }

    public static class Sub {
        @SerializedName("subdistrict_name")
        public String subdistrict_name;
        @SerializedName("id")
        public String id;
        @SerializedName("restCount")
        public String rstCount;
    }
}
