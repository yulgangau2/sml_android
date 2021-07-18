package com.eatyhero.customer.model;

import java.io.Serializable;

/**
 * Created by user on 10/7/2015.
 */
public class PlaceOrderData   implements Serializable{

    //Cart Details
    String orderType;
    String restaurantID;
    String restaurantStatus;
    String subTotal;
    String deliveryCharge;
    String minimumcharge;
    String taxAmount;
    String taxPercentage;
    String offerAmount;
    String offerPercentage;
    String grandTotal;
    String voucherCodePercent;
    String voucherCodePrice;
    String voucherCodeStatus;
    String earnPoints;
    String rewardPercentage;
    String redeemAmount;

    //Guest user details
    String firstName;
    String lastName;
    String mobileNumber;
    String email;
    String password;
    String confirmPassword;

    //Address Info
    String addressId;
    String addressTitle;
    String googleAddress;
    String addressPhone;

    //Delivery info
    String foodAsSoonAs;
    String date;
    String time;
    String instruction;

   //payment info
    String paymentinfo;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantStatus() {
        return restaurantStatus;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getMinimumcharge() {
        return minimumcharge;
    }

    public void setMinimumcharge(String minimumcharge) {
        this.minimumcharge = minimumcharge;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getOfferPercentage() {
        return offerPercentage;
    }

    public void setOfferPercentage(String offerPercentage) {
        this.offerPercentage = offerPercentage;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getVoucherCodePercent() {
        return voucherCodePercent;
    }

    public void setVoucherCodePercent(String voucherCodePercent) {
        this.voucherCodePercent = voucherCodePercent;
    }

    public String getVoucherCodePrice() {
        return voucherCodePrice;
    }

    public void setVoucherCodePrice(String voucherCodePrice) {
        this.voucherCodePrice = voucherCodePrice;
    }

    public String getVoucherCodeStatus() {
        return voucherCodeStatus;
    }

    public void setVoucherCodeStatus(String voucherCodeStatus) {
        this.voucherCodeStatus = voucherCodeStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getFoodAsSoonAs() {
        return foodAsSoonAs;
    }

    public void setFoodAsSoonAs(String foodAsSoonAs) {
        this.foodAsSoonAs = foodAsSoonAs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPaymentinfo() {
        return paymentinfo;
    }

    public void setPaymentinfo(String paymentinfo) {
        this.paymentinfo = paymentinfo;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getEarnPoints() {
        return earnPoints;
    }

    public void setEarnPoints(String earnPoints) {
        this.earnPoints = earnPoints;
    }

    public String getRewardPercentage() {
        return rewardPercentage;
    }

    public void setRewardPercentage(String rewardPercentage) {
        this.rewardPercentage = rewardPercentage;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }
}
