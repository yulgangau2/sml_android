package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 11-08-2015.
 */
public class CartDetails implements Serializable
{

    //cart Items
    @SerializedName("menuid")
    String menuId;

    @SerializedName("restaurantid")
    String resId;

    @SerializedName("menuname")
    String menuName;

    @SerializedName("menutype")
    String menuType;

    @SerializedName("pizza_size")
    String menuSize;

    @SerializedName("quantity")
    String quantity;

    @SerializedName("addonsname")
    String addonName;

    @SerializedName("addonsprice")
    String addonPrice;

    @SerializedName("menuprice")
    String menuPrice;

    @SerializedName("tot_menuprice")
    String totalPrice;

    @SerializedName("specialinstruction")
    String menu_instruction;


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

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(String menuSize) {
        this.menuSize = menuSize;
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

    public String getAddonPrice() {
        return addonPrice;
    }

    public void setAddonPrice(String addonPrice) {
        this.addonPrice = addonPrice;
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
        return menu_instruction;
    }

    public void setInstruction(String instruction) {
        this.menu_instruction = instruction;
    }
}
