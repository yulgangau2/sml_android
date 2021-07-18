package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 1/22/2016.
 */
public class LoginDetails implements Serializable {

    @SerializedName("status")
    public String status;
    @SerializedName("result")
    public Result result;

    public static class Result {
        @SerializedName("first_user")
        public String first_user;
        @SerializedName("success")
        public int success;
        @SerializedName("message")
        public String message;
        @SerializedName("customerId")
        public String customerId;
        @SerializedName("name")
        public String name;
        @SerializedName("firstName")
        public String firstName;
        @SerializedName("lastName")
        public String lastName;
        @SerializedName("email")
        public String email;
        @SerializedName("customerPhone")
        public String customerPhone;
    }
}
