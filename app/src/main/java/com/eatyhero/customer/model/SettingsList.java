package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 07-02-2018.
 */

public class SettingsList implements Serializable{


    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Sitesettings {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("site_name")
        public String site_name;
        @SerializedName("site_logo")
        public String site_logo;
        @SerializedName("site_fav")
        public String site_fav;
        @SerializedName("address_mode")
        public String address_mode;
        @SerializedName("search_by")
        public String search_by;
        @SerializedName("admin_name")
        public String admin_name;
        @SerializedName("admin_email")
        public String admin_email;
        @SerializedName("contact_us_email")
        public String contact_us_email;
        @SerializedName("order_email")
        public String order_email;
        @SerializedName("invoice_email")
        public String invoice_email;
        @SerializedName("contact_phone")
        public String contact_phone;
        @SerializedName("site_address")
        public String site_address;
        @SerializedName("site_country")
        public String site_country;
        @SerializedName("site_state")
        public String site_state;
        @SerializedName("site_city")
        public String site_city;
        @SerializedName("site_zip")
        public String site_zip;
        @SerializedName("site_currency")
        public String site_currency;
        @SerializedName("site_timezone")
        public String site_timezone;
        @SerializedName("stripe_mode")
        public String stripe_mode;
        @SerializedName("stripe_secretkey")
        public String stripe_secretkey;
        @SerializedName("stripe_publishkey")
        public String stripe_publishkey;
        @SerializedName("stripe_secretkeyTest")
        public String stripe_secretkeyTest;
        @SerializedName("stripe_publishkeyTest")
        public String stripe_publishkeyTest;
        @SerializedName("paypal_mode")
        public String paypal_mode;
        @SerializedName("test_clientid")
        public String test_clientid;
        @SerializedName("live_clientid")
        public String live_clientid;
        @SerializedName("google_analytics")
        public String google_analytics;
        @SerializedName("woopra_analytics")
        public String woopra_analytics;
        @SerializedName("zoopim_code")
        public String zoopim_code;
        @SerializedName("mail_option")
        public String mail_option;
        @SerializedName("smtp_host")
        public String smtp_host;
        @SerializedName("smtp_port")
        public String smtp_port;
        @SerializedName("smtp_username")
        public String smtp_username;
        @SerializedName("smtp_password")
        public String smtp_password;
        @SerializedName("vat_no")
        public String vat_no;
        @SerializedName("vat_percent")
        public String vat_percent;
        @SerializedName("card_fee")
        public String card_fee;
        @SerializedName("invoice_duration")
        public String invoice_duration;
        @SerializedName("offline_status")
        public String offline_status;
        @SerializedName("offline_notes")
        public String offline_notes;
        @SerializedName("sms_token")
        public String sms_token;
        @SerializedName("sms_id")
        public String sms_id;
        @SerializedName("sms_source_number")
        public String sms_source_number;
        @SerializedName("meta_title")
        public String meta_title;
        @SerializedName("meta_keywords")
        public String meta_keywords;
        @SerializedName("meta_description")
        public String meta_description;
        @SerializedName("multiple_language")
        public String multiple_language;
        @SerializedName("default_language")
        public String default_language;
        @SerializedName("other_language")
        public String other_language;
        @SerializedName("facebook_api_id")
        public String facebook_api_id;
        @SerializedName("facebook_secret_key")
        public String facebook_secret_key;
        @SerializedName("google_api_id")
        public String google_api_id;
        @SerializedName("google_secret_key")
        public String google_secret_key;
        @SerializedName("google_key1")
        public String google_key1;
        @SerializedName("google_key2")
        public String google_key2;
        @SerializedName("google_key3")
        public String google_key3;
        @SerializedName("google_key4")
        public String google_key4;
        @SerializedName("google_key5")
        public String google_key5;
        @SerializedName("mailchimp_key")
        public String mailchimp_key;
        @SerializedName("mailchimp_list_id")
        public String mailchimp_list_id;
        @SerializedName("pusher_key")
        public String pusher_key;
        @SerializedName("pusher_secret")
        public String pusher_secret;
        @SerializedName("pusher_id")
        public String pusher_id;
        @SerializedName("assign_status")
        public String assign_status;
        @SerializedName("assign_miles")
        public String assign_miles;
        @SerializedName("created")
        public String created;
        @SerializedName("updated")
        public String updated;
    }

    public static class Result {
        @SerializedName("success")
        public String success;
        @SerializedName("sitesettings")
        public Sitesettings sitesettings;
    }
}
