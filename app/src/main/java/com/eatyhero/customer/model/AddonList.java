package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 01-08-2015.
 */
public class AddonList implements Serializable {


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Menu_details {
        @SerializedName("id")
        public String id;
        @SerializedName("menu_id")
        public String menu_id;
        @SerializedName("sub_name")
        public String sub_name;
        @SerializedName("orginal_price")
        public String orginal_price;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class Details {
        @SerializedName("id")
        public String id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("category_id")
        public String category_id;
        @SerializedName("menu_name")
        public String menu_name;
        @SerializedName("price_option")
        public String price_option;
        @SerializedName("menu_description")
        public String menu_description;
        @SerializedName("menu_image")
        public String menu_image;
        @SerializedName("menu_type")
        public String menu_type;
        @SerializedName("menu_addon")
        public String menu_addon;
        @SerializedName("popular_dish")
        public String popular_dish;
        @SerializedName("spicy_dish")
        public String spicy_dish;
        @SerializedName("favourite")
        public String favourite;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("menu_details")
        public ArrayList<Menu_details> menu_details;
    }

    public static class Variants {
        @SerializedName("id")
        public String id;
        @SerializedName("menu_id")
        public String menu_id;
        @SerializedName("sub_name")
        public String sub_name;
        @SerializedName("orginal_price")
        public String orginal_price;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class MainAddon {
        @SerializedName("mainaddons_id")
        public String mainaddons_id;
        @SerializedName("mainaddons_name")
        public String mainaddons_name;
        @SerializedName("mainaddons_mini_count")
        public String mainaddons_mini_count;
        @SerializedName("mainaddons_count")
        public String mainaddons_count;
    }

    public static class ProductDetails {
        @SerializedName("Details")
        public Details Details;
        @SerializedName("variants")
        public ArrayList<Variants> variants;
        @SerializedName("MainAddon")
        public ArrayList<MainAddon> MainAddon;
    }

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("productDetails")
        public ProductDetails productDetails;
    }
}
