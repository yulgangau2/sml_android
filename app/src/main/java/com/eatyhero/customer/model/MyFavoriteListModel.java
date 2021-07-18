package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 28-03-2018.
 */

public class MyFavoriteListModel {


    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("favMenuList")
        public ArrayList<FavMenuList> favMenuList;
        @SerializedName("favRstList")
        public ArrayList<FavRstList> favRstList;
    }

    public static class FavMenuList {
        @SerializedName("menus")
        public ArrayList<Menus> menus;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("restaurant_menu")
        public Restaurant_menu restaurant_menu;
        @SerializedName("restaurant")
        public Restaurant restaurant;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("menu_name")
        public String menu_name;
        @SerializedName("menu_id")
        public String menu_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
        @SerializedName("restaurant_details")
        public ArrayList<Restaurant_details> restaurant_details;
    }

    public static class Restaurant_details {
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
        @SerializedName("paypalStatus")
        public String paypalStatus;
        @SerializedName("stripeStatus")
        public String stripeStatus;
        @SerializedName("codStatus")
        public String codStatus;
        @SerializedName("currentStatus")
        public String currentStatus;
        @SerializedName("deliveryCharge")
        public String deliveryCharge;
        @SerializedName("restOffers")
        public ArrayList<RestOffers> restOffers;
        @SerializedName("first_user")
        public String first_user;
    }

    public static class RestOffers implements Serializable {
        @SerializedName("id")
        public String id;
        @SerializedName("resid")
        public String resid;
        @SerializedName("first_user")
        public String first_user;
        @SerializedName("normal")
        public String normal;
        @SerializedName("delivery_mode")
        public String delivery_mode;
        @SerializedName("free_percentage")
        public String free_percentage;
        @SerializedName("free_price")
        public String free_price;
        @SerializedName("normal_percentage")
        public String normal_percentage;
        @SerializedName("normal_price")
        public String normal_price;
        @SerializedName("offer_percentage")
        public String offer_percentage;
        @SerializedName("offer_price")
        public String offer_price;
        @SerializedName("offer_from")
        public String offer_from;
        @SerializedName("offer_to")
        public String offer_to;
        @SerializedName("status")
        public String status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class Menus {
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
        @SerializedName("menu_price")
        public String menu_price;
        @SerializedName("dealMenu")
        public String dealMenu;
    }

    public static class Restaurant_menu {
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

    public static class Restaurant {
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

    public static class FavRstList {
        @SerializedName("finalReview")
        public String finalReview;
        @SerializedName("totalRating")
        public String totalRating;
        @SerializedName("deliveryCharge")
        public String deliveryCharge;
        @SerializedName("to_distance")
        public String to_distance;
        @SerializedName("cuisineLists")
        public String cuisineLists;
        @SerializedName("Restaurants")
        public Restaurants Restaurants;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
    }

    public static class Restaurants {
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
}
