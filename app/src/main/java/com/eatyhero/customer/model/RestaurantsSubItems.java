package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 07-09-2016.
 */
public class RestaurantsSubItems implements Serializable {

    public ArrayList<ProductList> productList;

    public class ProductList implements Serializable {

        @SerializedName("id")
        String id;

        @SerializedName("product_name")
        String productName;

        @SerializedName("product_description")
        String productDescription;

        @SerializedName("product_type")
        String productType;

        @SerializedName("product_image")
        String productImage;

        @SerializedName("popular_dish")
        String popularDish;

        @SerializedName("spicy_dish")
        String spicyDish;

        @SerializedName("price")
        String price;

        @SerializedName("price_option")
        String priceOption;

        @SerializedName("product_addons")
        String addons;

        public String getPriceOption() {
            return priceOption;
        }

        public void setPriceOption(String priceOption) {
            this.priceOption = priceOption;
        }

        public String getAddons() {
            return addons;
        }

        public void setAddons(String addons) {
            this.addons = addons;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getPopularDish() {
            return popularDish;
        }

        public void setPopularDish(String popularDish) {
            this.popularDish = popularDish;
        }

        public String getSpicyDish() {
            return spicyDish;
        }

        public void setSpicyDish(String spicyDish) {
            this.spicyDish = spicyDish;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }


}
