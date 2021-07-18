package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 09-09-2016.
 */
public class Shoppingcart implements Serializable {


    //cart Items
    @SerializedName("product_id")
    String menuId;

    @SerializedName("store_id")
    String resId;

    @SerializedName("product_name")
    String menuName;

    /* @SerializedName("menutype")
     String menuType;

     @SerializedName("pizza_size")
     String menuSize;
 */
    @SerializedName("product_quantity")
    String quantity;

    @SerializedName("subaddons_name")
    String addonName;

   /* @SerializedName("addonsprice")
    String addonPrice;*/

    @SerializedName("product_price")
    String menuPrice;

    @SerializedName("product_total_price")
    String totalPrice;

    @SerializedName("product_description")
    String instruction;


    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAddonName() {
        return addonName;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

}
