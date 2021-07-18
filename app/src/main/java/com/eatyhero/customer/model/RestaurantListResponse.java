package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22-01-2018.
 */

public class RestaurantListResponse implements Serializable {


    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("promotions")
        public ArrayList<Promotions1> promotions1;
        @SerializedName("today")
        public String today;
        @SerializedName("resTypeList")
        public ArrayList<ResTypeList> resTypeList;
        @SerializedName("siteSettings")
        public SiteSettings siteSettings;
        @SerializedName("deliveryCount")
        public String deliveryCount;
        @SerializedName("pickupCount")
        public String pickupCount;
        @SerializedName("sideCuisines")
        public SideCuisines sideCuisines;
        @SerializedName("bannerLists")
        public ArrayList<BannerLists> bannerLists;
        @SerializedName("currentDay")
        public String currentDay;
        @SerializedName("allCuisinesLists")
        public ArrayList<AllCuisinesLists> allCuisinesLists;
        @SerializedName("restaurantLists")
        public ArrayList<RestaurantLists> restaurantLists;
    }

    public static class Promotions1 {
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
        @SerializedName("promo_image")
        public String promo_image;
    }

    public static class ResTypeList {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("sortorder")
        public String sortorder;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("restype_name")
        public String restype_name;
        @SerializedName("id")
        public String id;
    }

    public static class SiteSettings {

        @SerializedName("stripe_publishkeyTest")
        public String stripe_publishkeyTest;
        @SerializedName("stripe_secretkeyTest")
        public String stripe_secretkeyTest;
        @SerializedName("stripe_publishkey")
        public String stripe_publishkey;
        @SerializedName("stripe_secretkey")
        public String stripe_secretkey;
        @SerializedName("stripe_mode")
        public String stripe_mode;
        @SerializedName("site_currency")
        public String site_currency;
    }

    public static class SideCuisines {
        @SerializedName("American")
        public String American;
        @SerializedName("Italian")
        public String Italian;
        @SerializedName("Chinese")
        public String Chinese;
        @SerializedName("Indian")
        public String Indian;
    }

    public static class BannerLists {
        @SerializedName("banner_link")
        public String banner_link;
        @SerializedName("banner_image")
        public String banner_image;
        @SerializedName("id")
        public String id;
    }

    public static class AllCuisinesLists {
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public String id;
    }

    public static class RestaurantLists {
        @SerializedName("openclose")
        public String openclose;
        @SerializedName("currentStatus")
        public String currentStatus;
        @SerializedName("subdistrict_name")
        public String subdistrict_name;
        @SerializedName("district_name")
        public String district_name;
        @SerializedName("city_name")
        public String city_name;
        @SerializedName("finalReview")
        public String finalReview;
        @SerializedName("totalRating")
        public String totalRating;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("restOffers")
        public ArrayList<RestOffers> restOffers;
        @SerializedName("deliveryAreasLists")
        public String deliveryAreasLists;
        @SerializedName("cuisineRecord")
        public String cuisineRecord;
        @SerializedName("cuisineLists")
        public String cuisineLists;
        @SerializedName("to_distance")
        public String to_distance;
        @SerializedName("free_delivery_avail")
        public String free_delivery_avail;
        @SerializedName("favCount")
        public String favCount;
        @SerializedName("RstLikeCnt")
        public String RstLikeCnt;
        @SerializedName("restaurant_added_date")
        public String restaurant_added_date;
        @SerializedName("delivery_settings")
        public ArrayList<Delivery_settings> delivery_settings;
        @SerializedName("restaurant_menus")
        public ArrayList<Restaurant_menus> restaurant_menus;
        @SerializedName("reviews")
        public ArrayList<Reviews> reviews;
        @SerializedName("offerStatus")
        public String offers;
        @SerializedName("restaurant_payments")
        public ArrayList<Restaurant_payments> restaurant_payments;
        @SerializedName("promotions")
        public ArrayList<Promotions> promotions;
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
        @SerializedName("reward_option")
        public String reward_option;
        @SerializedName("deals")
        public String deals;
    }

    public static class RestOffers {
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

    public static class Delivery_settings {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("map_type")
        public String map_type;
        @SerializedName("radius_color")
        public String radius_color;
        @SerializedName("driver_chargehint")
        public String driver_chargehint;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("delivery_miles")
        public String delivery_miles;
        @SerializedName("subdistrict_id")
        public String subdistrict_id;
        @SerializedName("district_id")
        public String district_id;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
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

    public static class Reviews {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("message")
        public String message;
        @SerializedName("rating")
        public String rating;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("order_id")
        public String order_id;
        @SerializedName("id")
        public String id;
    }

    public static class Restaurant_payments {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("payment_id")
        public String payment_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
    }

    public static class Promotions {
        @SerializedName("to_distance")
        public String to_distance;
        @SerializedName("cuisineList")
        public String cuisineList;
        @SerializedName("promo_image_url")
        public String promo_image_url;
        @SerializedName("promo_image")
        public String promo_image;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
        @SerializedName("delivery_charge")
        public String delivery_charge;
    }
}
