package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22-01-2018.
 */

public class RestaurantMenuListResponse implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class RestOffers implements Serializable{
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

    public static class User implements Serializable{
        @SerializedName("first_name")
        public String first_name;
        @SerializedName("last_name")
        public String last_name;
    }

    public static class Reviews implements Serializable{
        @SerializedName("id")
        public String id;
        @SerializedName("order_id")
        public String order_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("customer_id")
        public String customer_id;
        @SerializedName("rating")
        public String rating;
        @SerializedName("message")
        public String message;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("user")
        public User user;
    }

    public static class Menu_details implements Serializable {
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

    public static class Menu implements Serializable{
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

         @SerializedName("favMenuCount")
        public String favMenuCount;
         @SerializedName("MenuLikeCnt")
        public String MenuLikeCnt;
         @SerializedName("dealMenu")
        public String dealMenu;

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

    public static class MenusDetails implements Serializable{
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("category_id")
        public String category_id;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("menu")
        public ArrayList<Menu> menu;
    }

    public static class RestDetails implements Serializable{
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("contact_name")
        public String contact_name;
        @SerializedName("contact_phone")
        public String contact_phone;
        @SerializedName("contact_email")
        public String contact_email;
        @SerializedName("contact_address")
        public String contact_address;
        @SerializedName("sourcelatitude")
        public String sourcelatitude;
        @SerializedName("sourcelongitude")
        public String sourcelongitude;
        @SerializedName("street_address")
        public String street_address;
        @SerializedName("state_id")
        public String state_id;
        @SerializedName("city_id")
        public String city_id;
        @SerializedName("location_id")
        public String location_id;
        @SerializedName("restaurant_name")
        public String restaurant_name;
        @SerializedName("seo_url")
        public String seo_url;
        @SerializedName("restaurant_phone")
        public String restaurant_phone;
        @SerializedName("monday_first_opentime")
        public String monday_first_opentime;
        @SerializedName("monday_first_closetime")
        public String monday_first_closetime;
        @SerializedName("monday_second_opentime")
        public String monday_second_opentime;
        @SerializedName("monday_second_closetime")
        public String monday_second_closetime;
        @SerializedName("tuesday_first_opentime")
        public String tuesday_first_opentime;
        @SerializedName("tuesday_first_closetime")
        public String tuesday_first_closetime;
        @SerializedName("tuesday_second_opentime")
        public String tuesday_second_opentime;
        @SerializedName("tuesday_second_closetime")
        public String tuesday_second_closetime;
        @SerializedName("wednesday_first_opentime")
        public String wednesday_first_opentime;
        @SerializedName("wednesday_first_closetime")
        public String wednesday_first_closetime;
        @SerializedName("wednesday_second_opentime")
        public String wednesday_second_opentime;
        @SerializedName("wednesday_second_closetime")
        public String wednesday_second_closetime;
        @SerializedName("thursday_first_opentime")
        public String thursday_first_opentime;
        @SerializedName("thursday_first_closetime")
        public String thursday_first_closetime;
        @SerializedName("thursday_second_opentime")
        public String thursday_second_opentime;
        @SerializedName("thursday_second_closetime")
        public String thursday_second_closetime;
        @SerializedName("friday_first_opentime")
        public String friday_first_opentime;
        @SerializedName("friday_first_closetime")
        public String friday_first_closetime;
        @SerializedName("friday_second_opentime")
        public String friday_second_opentime;
        @SerializedName("friday_second_closetime")
        public String friday_second_closetime;
        @SerializedName("saturday_first_opentime")
        public String saturday_first_opentime;
        @SerializedName("saturday_first_closetime")
        public String saturday_first_closetime;
        @SerializedName("saturday_second_opentime")
        public String saturday_second_opentime;
        @SerializedName("saturday_second_closetime")
        public String saturday_second_closetime;
        @SerializedName("sunday_first_opentime")
        public String sunday_first_opentime;
        @SerializedName("sunday_first_closetime")
        public String sunday_first_closetime;
        @SerializedName("sunday_second_opentime")
        public String sunday_second_opentime;
        @SerializedName("sunday_second_closetime")
        public String sunday_second_closetime;
        @SerializedName("monday_status")
        public String monday_status;
        @SerializedName("tuesday_status")
        public String tuesday_status;
        @SerializedName("wednesday_status")
        public String wednesday_status;
        @SerializedName("thursday_status")
        public String thursday_status;
        @SerializedName("friday_status")
        public String friday_status;
        @SerializedName("saturday_status")
        public String saturday_status;
        @SerializedName("sunday_status")
        public String sunday_status;
        @SerializedName("restaurant_tax")
        public String restaurant_tax;
        @SerializedName("restaurant_cuisine")
        public String restaurant_cuisine;
        @SerializedName("restaurant_visibility")
        public String restaurant_visibility;
        @SerializedName("restaurant_dispatch")
        public String restaurant_dispatch;
        @SerializedName("restaurant_pickup")
        public String restaurant_pickup;
        @SerializedName("restaurant_delivery")
        public String restaurant_delivery;
        @SerializedName("restaurant_booktable")
        public String restaurant_booktable;
        @SerializedName("restaurant_about")
        public String restaurant_about;
        @SerializedName("username")
        public String username;
        @SerializedName("estimate_time")
        public String estimate_time;
        @SerializedName("minimum_order")
        public String minimum_order;
        @SerializedName("free_delivery")
        public String free_delivery;
        @SerializedName("map_mode")
        public String map_mode;
        @SerializedName("email_order")
        public String email_order;
        @SerializedName("order_email")
        public String order_email;
        @SerializedName("sms_option")
        public String sms_option;
        @SerializedName("sms_phonenumber")
        public String sms_phonenumber;
        @SerializedName("restaurant_commission")
        public String restaurant_commission;
        @SerializedName("invoice_period")
        public String invoice_period;
        @SerializedName("meta_title")
        public String meta_title;
        @SerializedName("meta_keyword")
        public String meta_keyword;
        @SerializedName("meta_description")
        public String meta_description;
        @SerializedName("restaurant_logo")
        public String restaurant_logo;
        @SerializedName("logo_name")
        public String logo_name;
        @SerializedName("status")
        public String status;
        @SerializedName("delete_status")
        public String delete_status;
        @SerializedName("created")
        public String created;
        @SerializedName("cuisineLists")
        public String cuisineLists;
        @SerializedName("finalReview")
        public String finalReview;
        @SerializedName("delivery_charge")
        public String delivery_charge;
        @SerializedName("to_distance")
        public String to_distance;
        @SerializedName("modified")
        public String modified;
        @SerializedName("restaurant_payments")
        public ArrayList<RestaurantPayments> restaurant_payments;
        @SerializedName("restOffers")
        public ArrayList<RestOffers> restOffers;
        @SerializedName("reviews")
        public ArrayList<Reviews> reviews;
        @SerializedName("currentStatus")
        public String currentStatus;
        @SerializedName("menusDetails")
        public ArrayList<MenusDetails> menusDetails;
        @SerializedName("first_user")
        public String first_user;
        @SerializedName("reward_option")
        public String reward_option;
        @SerializedName("reward_text")
        public String reward_text;

        @SerializedName("RstLikeCnt")
        public String likeCount;
        @SerializedName("favCount")
        public String favCount;

        @SerializedName("allMenus")
        public ArrayList<Menu> allMenus;
    }

    public static class Result {
        @SerializedName("restDetails")
        public RestDetails restDetails;
        @SerializedName("success")
        public String success;
        @SerializedName("menuImgUrl")
        public String menuImgUrl;
        @SerializedName("dealProducts")
        public ArrayList<DealProducts> dealProducts;
    }

    public static class RestaurantPayments implements Serializable{

        @SerializedName("id")
        public int id;
        @SerializedName("restaurant_id")
        public int restaurant_id;
        @SerializedName("payment_id")
        public int payment_id;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
        @SerializedName("payment_method")
        public Payment_method payment_method;
    }

    public static class Payment_method {
        @SerializedName("id")
        public int id;
        @SerializedName("payment_method_name")
        public String payment_method_name;
        @SerializedName("payment_method_image")
        public String payment_method_image;
        @SerializedName("status")
        public int status;
        @SerializedName("created")
        public String created;
        @SerializedName("modified")
        public String modified;
    }

    public static class DealProducts {
        @SerializedName("MainProduct")
        public MainProduct MainProduct;
        @SerializedName("SubProduct")
        public SubProduct SubProduct;
        @SerializedName("created")
        public String created;
        @SerializedName("status")
        public String status;
        @SerializedName("sub_product")
        public String sub_product;
        @SerializedName("main_product")
        public String main_product;
        @SerializedName("deal_name")
        public String deal_name;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
        @SerializedName("price")
        public String price;
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
        public String category_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
    }
    public static class Menu_detailss {
        @SerializedName("orginal_price")
        public String orginal_price;
        @SerializedName("menu_id")
        public String menu_id;
        @SerializedName("sub_name")
        public String sub_name;
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
        public String category_id;
        @SerializedName("restaurant_id")
        public String restaurant_id;
        @SerializedName("id")
        public String id;
    }

    String favButton = "0";
}
