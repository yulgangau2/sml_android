package com.eatyhero.customer.model;

import java.util.ArrayList;

/**
 * Created by admin on 27-12-2016.
 */

public class RestaurantTypeList {

    private String type;
    private ArrayList<RestaurantNameList> restaurantNameLists = new ArrayList<RestaurantNameList>();

    public RestaurantTypeList(String type, ArrayList<RestaurantNameList> restaurantNameLists) {
        super();
        this.type = type;
        this.restaurantNameLists = restaurantNameLists;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<RestaurantNameList> getRestaurantNameLists() {
        return restaurantNameLists;
    }

    public void setRestaurantNameLists(ArrayList<RestaurantNameList> restaurantNameLists) {
        this.restaurantNameLists = restaurantNameLists;
    }
}
