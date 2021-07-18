package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 8/10/2015.
 */
public class Addons implements Serializable {

    @SerializedName("store_id")
    String restaurantId;

    @SerializedName("product_name")
    String menuName;

    @SerializedName("sizeoption")
    String sizeOption;

    @SerializedName("product_type")
    String menuType;

    @SerializedName("product_addons")
    String menuAddons;

    @SerializedName("price_option")
    String price_option;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getSizeOption() {
        return sizeOption;
    }

    public void setSizeOption(String sizeOption) {
        this.sizeOption = sizeOption;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuAddons() {
        return menuAddons;
    }

    public void setMenuAddons(String menuAddons) {
        this.menuAddons = menuAddons;
    }

    public String getPriceOption() {
        return price_option;
    }

    public void setPriceOption(String price_option) {
        this.price_option = price_option;
    }
}
