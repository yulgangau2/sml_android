package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 23-03-2018.
 */

public class SearchOptionList implements Serializable {

    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("cuisineslist")
        public ArrayList<Cuisineslist> cuisineslist;
        @SerializedName("restaurantlist")
        public ArrayList<Restaurantlist> restaurantlist;
        @SerializedName("citylist")
        public ArrayList<Citylist> citylist;
        @SerializedName("success")
        public int success;
    }

    public static class Cuisineslist {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("cuisine_seo")
        public String cuisine_seo;
        @SerializedName("cuisine_name")
        public String cuisine_name;
        @SerializedName("id")
        public String id;
    }

    public static class Restaurantlist {
        @SerializedName("restaurant_menus")
        public ArrayList<Restaurant_menus> restaurant_menus;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("fb_page_id")
        public String fb_page_id;
        @SerializedName("device_id")
        public String device_id;
        @SerializedName("device_name")
        public String device_name;
        @SerializedName("is_logged")
        public String is_logged;
        @SerializedName("restaurant_photos5")
        public String restaurant_photos5;
        @SerializedName("restaurant_photos4")
        public String restaurant_photos4;
        @SerializedName("restaurant_photos3")
        public String restaurant_photos3;
        @SerializedName("restaurant_photos2")
        public String restaurant_photos2;
        @SerializedName("restaurant_photos1")
        public String restaurant_photos1;
        @SerializedName("logo_name")
        public String logo_name;
        @SerializedName("restaurant_logo")
        public String restaurant_logo;
        @SerializedName("meta_description")
        public String meta_description;
        @SerializedName("meta_keyword")
        public String meta_keyword;
        @SerializedName("meta_title")
        public String meta_title;
        @SerializedName("invoice_period")
        public String invoice_period;
        @SerializedName("restaurant_commission")
        public String restaurant_commission;
        @SerializedName("restaurant_income")
        public String restaurant_income;
        @SerializedName("sms_phonenumber")
        public String sms_phonenumber;
        @SerializedName("sms_option")
        public String sms_option;
        @SerializedName("order_email")
        public String order_email;
        @SerializedName("email_order")
        public String email_order;
        @SerializedName("map_mode")
        public String map_mode;
        @SerializedName("free_delivery")
        public String free_delivery;
        @SerializedName("minimum_order")
        public String minimum_order;
        @SerializedName("estimate_time")
        public String estimate_time;
        @SerializedName("username")
        public String username;
        @SerializedName("restaurant_about")
        public String restaurant_about;
        @SerializedName("restaurant_booktable")
        public String restaurant_booktable;
        @SerializedName("restaurant_delivery")
        public String restaurant_delivery;
        @SerializedName("restaurant_pickup")
        public String restaurant_pickup;
        @SerializedName("restaurant_dispatch")
        public String restaurant_dispatch;
        @SerializedName("restaurant_visibility")
        public String restaurant_visibility;
        @SerializedName("restaurant_cuisine")
        public String restaurant_cuisine;
        @SerializedName("restaurant_tax")
        public String restaurant_tax;
        @SerializedName("sunday_status")
        public String sunday_status;
        @SerializedName("saturday_status")
        public String saturday_status;
        @SerializedName("friday_status")
        public String friday_status;
        @SerializedName("thursday_status")
        public String thursday_status;
        @SerializedName("wednesday_status")
        public String wednesday_status;
        @SerializedName("tuesday_status")
        public String tuesday_status;
        @SerializedName("monday_status")
        public String monday_status;
        @SerializedName("sunday_second_closetime")
        public String sunday_second_closetime;
        @SerializedName("sunday_second_opentime")
        public String sunday_second_opentime;
        @SerializedName("sunday_first_closetime")
        public String sunday_first_closetime;
        @SerializedName("sunday_first_opentime")
        public String sunday_first_opentime;
        @SerializedName("saturday_second_closetime")
        public String saturday_second_closetime;
        @SerializedName("saturday_second_opentime")
        public String saturday_second_opentime;
        @SerializedName("saturday_first_closetime")
        public String saturday_first_closetime;
        @SerializedName("saturday_first_opentime")
        public String saturday_first_opentime;
        @SerializedName("friday_second_closetime")
        public String friday_second_closetime;
        @SerializedName("friday_second_opentime")
        public String friday_second_opentime;
        @SerializedName("friday_first_closetime")
        public String friday_first_closetime;
        @SerializedName("friday_first_opentime")
        public String friday_first_opentime;
        @SerializedName("thursday_second_closetime")
        public String thursday_second_closetime;
        @SerializedName("thursday_second_opentime")
        public String thursday_second_opentime;
        @SerializedName("thursday_first_closetime")
        public String thursday_first_closetime;
        @SerializedName("thursday_first_opentime")
        public String thursday_first_opentime;
        @SerializedName("wednesday_second_closetime")
        public String wednesday_second_closetime;
        @SerializedName("wednesday_second_opentime")
        public String wednesday_second_opentime;
        @SerializedName("wednesday_first_closetime")
        public String wednesday_first_closetime;
        @SerializedName("wednesday_first_opentime")
        public String wednesday_first_opentime;
        @SerializedName("tuesday_second_closetime")
        public String tuesday_second_closetime;
        @SerializedName("tuesday_second_opentime")
        public String tuesday_second_opentime;
        @SerializedName("tuesday_first_closetime")
        public String tuesday_first_closetime;
        @SerializedName("tuesday_first_opentime")
        public String tuesday_first_opentime;
        @SerializedName("monday_second_closetime")
        public String monday_second_closetime;
        @SerializedName("monday_second_opentime")
        public String monday_second_opentime;
        @SerializedName("monday_first_closetime")
        public String monday_first_closetime;
        @SerializedName("monday_first_opentime")
        public String monday_first_opentime;
        @SerializedName("restaurant_type")
        public String restaurant_type;
        @SerializedName("restaurant_phone")
        public String restaurant_phone;
        @SerializedName("seo_url")
        public String seo_url;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("subdistrict_id")
        public String subdistrict_id;
        @SerializedName("district_id")
        public String district_id;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("street_address")
        public String street_address;
        @SerializedName("sourcelongitude")
        public String sourcelongitude;
        @SerializedName("sourcelatitude")
        public String sourcelatitude;
        @SerializedName("contact_address")
        public String contact_address;
        @SerializedName("contact_email")
        public String contact_email;
        @SerializedName("contact_phone")
        public String contact_phone;
        @SerializedName("contact_name")
        public String contact_name;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("id")
        public String id;
    }

    public static class Restaurant_menus {
        @SerializedName("category")
        public Category category;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("favourite")
        public String favourite;
        @SerializedName("spicy_dish")
        public String spicy_dish;
        @SerializedName("popular_dish")
        public String popular_dish;
        @SerializedName("menu_addon")
        public String menu_addon;
        @SerializedName("menu_type")
        public String menu_type;
        @SerializedName("menu_image")
        public String menu_image;
        @SerializedName("menu_description")
        public String menu_description;
        @SerializedName("price_option")
        public String price_option;
        @SerializedName("menu_name")
        public String menu_name;
        @SerializedName("category_id")
        public String category_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
    }

    public static class Category {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("sortorder")
        public String sortorder;
        @SerializedName("category_seo")
        public String category_seo;
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("store_id")
        public String store_id;
        @SerializedName("parent_id")
        public String parent_id;
        @SerializedName("id")
        public String id;
    }

    public static class Citylist {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public String status;
        @SerializedName("state_code")
        public String state_code;
        @SerializedName("city_name")
        public String city_name;
        @SerializedName("country_id")
        public String country_id;
        @SerializedName("id")
        public String id;
    }
}
