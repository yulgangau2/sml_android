package com.eatyhero.customer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 8/8/2015.
 */
public class SubAddonList {

   @SerializedName("status")
   public String status;
   @SerializedName("result")
   public Result result;

   public static class ProductAddons {
      @SerializedName("id")
      public String id;
      @SerializedName("subaddons_name")
      public String subaddons_name;
      @SerializedName("subaddons_price")
      public String subaddons_price;

      boolean selected;

      public boolean isSelected() {
         return selected;
      }

      public void setSelected(boolean selected) {
         this.selected = selected;
      }
   }

   public static class Result {
      @SerializedName("success")
      public int success;
      @SerializedName("productAddons")
      public ArrayList<ProductAddons> productAddons;
   }
}
