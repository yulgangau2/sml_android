package com.eatyhero.customer.model;

import java.io.Serializable;

/**
 * Created by admin on 22-06-2017.
 */

public class PrimaryAddress implements Serializable{

    String address;
    String success;
    String message;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
