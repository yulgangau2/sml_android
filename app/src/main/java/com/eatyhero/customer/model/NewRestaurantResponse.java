package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 02-05-2018.
 */

public class NewRestaurantResponse {


    @SerializedName("result")
    public Result result;
    @SerializedName("status")
    public String status;

    public static class Result {
        @SerializedName("success")
        public int success;
        @SerializedName("menuImgUrl")
        public String menuImgUrl;
        @SerializedName("dealProducts")
        public List<DealProducts> dealProducts;
        @SerializedName("restDetails")
        public RestDetails restDetails;
        @SerializedName("offerPercentage")
        public String offerPercentage;
        @SerializedName("normalOffer")
        public String normalOffer;
        @SerializedName("offerAmount")
        public String offerAmount;
    }

    public static class DealProducts {
        @SerializedName("MainProduct")
        public MainProduct MainProduct;
        @SerializedName("SubProduct")
        public SubProduct SubProduct;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public int status;
        @SerializedName("sub_product")
        public int sub_product;
        @SerializedName("main_product")
        public int main_product;
        @SerializedName("deal_name")
        public String deal_name;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class MainProduct {
        @SerializedName("menu_details")
        public List<Menu_detailss> menu_detailss;
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
        public int category_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class Menu_detailss {
        @SerializedName("orginal_price")
        public int orginal_price;
        @SerializedName("menu_id")
        public int menu_id;
        @SerializedName("sub_name")
        public String sub_name;
        @SerializedName("id")
        public int id;
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
        public int sortorder;
        @SerializedName("category_seo")
        public String category_seo;
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("store_id")
        public int store_id;
        @SerializedName("parent_id")
        public int parent_id;
        @SerializedName("id")
        public int id;
    }

    public static class SubProduct {
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
        public int category_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class RestDetails {
        @SerializedName("menusDetails")
        public List<MenusDetails> menusDetails;
        @SerializedName("finalReview")
        public int finalReview;
        @SerializedName("totalRating")
        public int totalRating;
        @SerializedName("restOffers")
        public List<String> restOffers;
        @SerializedName("deliveryAreasLists")
        public String deliveryAreasLists;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("distance")
        public String distance;
        @SerializedName("currentStatus")
        public String currentStatus;
        @SerializedName("cuisineLists")
        public String cuisineLists;
        @SerializedName("RstLikeCnt")
        public int RstLikeCnt;
        @SerializedName("favCount")
        public int favCount;
        @SerializedName("first_user")
        public String first_user;
        @SerializedName("reward_text")
        public String reward_text;
        @SerializedName("restaurant_menus")
        public List<Restaurant_menus> restaurant_menus;
        @SerializedName("delivery_settings")
        public List<Delivery_settings> delivery_settings;
        @SerializedName("reviews")
        public List<String> reviews;
        @SerializedName("offers")
        public List<Offers> offers;
        @SerializedName("restaurant_payments")
        public List<Restaurant_payments> restaurant_payments;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("reward_option")
        public String reward_option;
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
        public int restaurant_commission;
        @SerializedName("restaurant_income")
        public int restaurant_income;
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
        public int free_delivery;
        @SerializedName("minimum_order")
        public int minimum_order;
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
        public int restaurant_tax;
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
        public int subdistrict_id;
        @SerializedName("district_id")
        public int district_id;
        @SerializedName("city_id")
        public int city_id;
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
        public int user_id;
        @SerializedName("id")
        public int id;
    }

    public static class MenusDetails {
        @SerializedName("menu")
        public List<Menu> menu;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("status")
        public String status;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("category_name")
        public String category_name;
    }

    public static class Menu {
        @SerializedName("dealMenu")
        public String dealMenu;
        @SerializedName("MenuLikeCnt")
        public int MenuLikeCnt;
        @SerializedName("favMenuCount")
        public int favMenuCount;
        @SerializedName("menu_details")
        public List<MenuDetails> menu_details;
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
        public int category_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class MenuDetails {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("orginal_price")
        public int orginal_price;
        @SerializedName("sub_name")
        public String sub_name;
        @SerializedName("menu_id")
        public int menu_id;
        @SerializedName("id")
        public int id;
    }

    public static class Restaurant_menus {
        @SerializedName("menu_details")
        public List<Menu_details> menu_details;
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
        public int category_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class Menu_details {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("orginal_price")
        public int orginal_price;
        @SerializedName("sub_name")
        public String sub_name;
        @SerializedName("menu_id")
        public int menu_id;
        @SerializedName("id")
        public int id;
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
        public int delivery_charge;
        @SerializedName("delivery_miles")
        public int delivery_miles;
        @SerializedName("subdistrict_id")
        public int subdistrict_id;
        @SerializedName("district_id")
        public int district_id;
        @SerializedName("city_id")
        public int city_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class Offers {
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public String status;
        @SerializedName("offer_to")
        public String offer_to;
        @SerializedName("offer_from")
        public String offer_from;
        @SerializedName("offer_price")
        public String offer_price;
        @SerializedName("offer_percentage")
        public String offer_percentage;
        @SerializedName("normal_price")
        public int normal_price;
        @SerializedName("normal_percentage")
        public int normal_percentage;
        @SerializedName("free_price")
        public int free_price;
        @SerializedName("free_percentage")
        public int free_percentage;
        @SerializedName("delivery_mode")
        public String delivery_mode;
        @SerializedName("normal")
        public String normal;
        @SerializedName("first_user")
        public String first_user;
        @SerializedName("resid")
        public int resid;
        @SerializedName("id")
        public int id;
    }

    public static class Restaurant_payments {
        @SerializedName("payment_method")
        public Payment_method payment_method;
        @SerializedName("modified")
        public String modified;
        @SerializedName("created")
        public String created;
        @SerializedName("payment_status")
        public String payment_status;
        @SerializedName("payment_id")
        public int payment_id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("id")
        public int id;
    }

    public static class Payment_method {
        @SerializedName("status")
        public int status;
        @SerializedName("payment_method_image")
        public String payment_method_image;
        @SerializedName("payment_method_name")
        public String payment_method_name;
        @SerializedName("id")
        public int id;
    }
}
