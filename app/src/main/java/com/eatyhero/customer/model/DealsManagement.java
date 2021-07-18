package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 28-06-2017.
 */

public class DealsManagement  implements Serializable {

public     ArrayList<DealsManagementValues> dealProducts = new ArrayList<>();

    public class DealsManagementValues implements Serializable {


        @SerializedName("productName")
        String productName;

        @SerializedName("addons")
        String addons;

        @SerializedName("spicyDish")
        String spicyDish;

        @SerializedName("productType")
        String productType;

        @SerializedName("popularDish")
        String popularDish;


        @SerializedName("orginalPrice")
        String orginalPrice;

        @SerializedName("subProductImage")
        String subProductImage;

        @SerializedName("mainProductImage")
        String mainProductImage;

        @SerializedName("productDescription")
        String productDescription;

        @SerializedName("price_option")
        String price_option;


        @SerializedName("productId")
        String productId;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getAddons() {
            return addons;
        }

        public void setAddons(String addons) {
            this.addons = addons;
        }

        public String getSpicyDish() {
            return spicyDish;
        }

        public void setSpicyDish(String spicyDish) {
            this.spicyDish = spicyDish;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getPopularDish() {
            return popularDish;
        }

        public void setPopularDish(String popularDish) {
            this.popularDish = popularDish;
        }

        public String getOrginalPrice() {
            return orginalPrice;
        }

        public void setOrginalPrice(String orginalPrice) {
            this.orginalPrice = orginalPrice;
        }

        public String getSubProductImage() {
            return subProductImage;
        }

        public void setSubProductImage(String subProductImage) {
            this.subProductImage = subProductImage;
        }

        public String getMainProductImage() {
            return mainProductImage;
        }

        public void setMainProductImage(String mainProductImage) {
            this.mainProductImage = mainProductImage;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public String getPrice_option() {
            return price_option;
        }

        public void setPrice_option(String price_option) {
            this.price_option = price_option;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }
}
