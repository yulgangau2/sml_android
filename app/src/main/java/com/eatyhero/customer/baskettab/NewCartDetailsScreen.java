package com.eatyhero.customer.baskettab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eatyhero.customer.database.Database;
import com.eatyhero.customer.restauranttab.RestaurantDetailsScreen;
import com.facebook.CallbackManager;
import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginScreen;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.CartDetails;
import com.eatyhero.customer.model.PlaceOrderData;
import com.eatyhero.customer.model.RewardList;
import com.eatyhero.customer.model.VoucherDetailsList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 23-01-2018.
 */

public class NewCartDetailsScreen extends BaseActivity implements ServerListener {

    //Create Objects
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;
    SharedPreferences preferences;
    DatabaseManager databaseManager;
    Cursor cursor;
    CartItemsAdapter cartItemsAdapter;
    PlaceOrderData placeOrderData;

    //Xml Resource
    @BindView(R.id.backIconImageView)
    ImageView backIconImageView;
    @BindView(R.id.actionBarTitleTextView)
    TextView actionBarTitleTextView;
    @BindView(R.id.totalPriceTextView)
    TextView totalPriceTextView;

    @BindView(R.id.emptyCartLayout)
    LinearLayout emptyCartLayout;

    @BindView(R.id.allContentLayout)
    ScrollView allContentLayout;
    @BindView(R.id.deliveryPickupRadioGroup)
    RadioGroup deliveryPickupRadioGroup;
    @BindView(R.id.pickupRadioButton)
    RadioButton pickupRadioButton;
    @BindView(R.id.deliveryRadioButton)
    RadioButton deliveryRadioButton;
    @BindView(R.id.cartMenuListListView)
    ListView cartMenuListListView;
    @BindView(R.id.instructionEditText)
    EditText instructionEditText;

    @BindView(R.id.restaurantNameTextView)
    TextView restaurantNameTextView;
    @BindView(R.id.restaurantAddressTextView)
    TextView restaurantAddressTextView;

    @BindView(R.id.subTotalTextView)
    TextView subTotalTextView;
    @BindView(R.id.taxLayout)
    RelativeLayout taxLayout;
    @BindView(R.id.taxPercentageTextView)
    TextView taxPercentageTextView;
    @BindView(R.id.taxAmountTextView)
    TextView taxAmountTextView;
    @BindView(R.id.deliveryChargLayout)
    RelativeLayout deliveryChargLayout;
    @BindView(R.id.deliveryChargeTextView)
    TextView deliveryChargeTextView;
    @BindView(R.id.removeFreeDeliveryButton)
    ImageView removeFreeDeliveryButton;
    @BindView(R.id.offerLayout)
    RelativeLayout offerLayout;
    @BindView(R.id.offerpercentTextView)
    TextView offerpercentTextView;
    @BindView(R.id.offerPriceTextView)
    TextView offerPriceTextView;
    @BindView(R.id.voucherDetailLayout)
    RelativeLayout voucherDetailLayout;
    @BindView(R.id.voucherTextView)
    TextView voucherTextView;
    @BindView(R.id.voucehrPriceTextView)
    TextView voucehrPriceTextView;
    @BindView(R.id.removeVoucherButton)
    ImageView removeVoucherButton;
    @BindView(R.id.grandTotalTextView)
    TextView grandTotalTextView;
    @BindView(R.id.voucherLayout)
    RelativeLayout voucherLayout;
    @BindView(R.id.couponImageView)
    ImageView couponImageView;
    @BindView(R.id.vocherText)
    TextView vocherText;
    @BindView(R.id.voucherCodeEditText)
    EditText voucherCodeEditText;
    @BindView(R.id.voucherCheckButton)
    TextView voucherCheckButton;
    @BindView(R.id.checkoutButton)
    Button checkoutButton;

    @BindView(R.id.earnPointsTextView)
    TextView earnPointsTextView;
    @BindView(R.id.earnPointsLayout)
    RelativeLayout earnPointsLayout;
    @BindView(R.id.redeemLayout)
    RelativeLayout redeemLayout;
    @BindView(R.id.redeemPercentageTextView)
    TextView redeemPercentageTextView;
    @BindView(R.id.redeemAmountTextView)
    TextView redeemAmountTextView;
    @BindView(R.id.addMoreMenu)
    TextView addMoreMenu;


    //String onject
    String restaurantDeliveryOption, restaurantPickupOption;
    String restaurantID, resAddress, restaurantName, restaurantDeliveryCharge = "", restaurantMinorder = "0", restaurantTax;
    String firstUser = "", normalUser = "", firstOfferPercentage, firstOfferAmount, normalOfferPercent, normalOfferRange;
    String restaurantStatus;
    String getOfferPercent = "";
    String voucherType = "", voucherPrice = "";
    String voucherCodePrice = "0", voucherCodePercentage = "0";
    String voucherCodeStatus = "";
    String orderType = "delivery";

    String earnPoints = "0";

    //Create double
    double minimunOrderPrice;
    double deliveryCharge;
    double tax, calculatedTax;
    double subTotal = 0.00;
    double orderDiscountPrice;
    double appDiscountPrice;
    double totalPrice;
    double vouchertotal = 0.00;

    //Create int
    int cartCount;

    //create Arraylist
    ArrayList<CartDetails> getDbCartdetails = new ArrayList<CartDetails>();
    List<String> totalPriceList = new ArrayList<String>();
    ArrayList<CartDetails> Cartitems = null;

    ImageView plusButton, minusButton;


    boolean callReward = true;
    //    boolean callReward = false;
    double rewardAmount = 0;
    double rewardPoints = 0;
    double rewardPercentage = 0;
    double redeemAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_details_screen);

        ButterKnife.bind(this);

        //Create Objects
        utility = Utility.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);
        Cartitems = new ArrayList<>();
        databaseManager = DatabaseManager.getInstance(this);
        loginSession = LoginSession.getInstance(this);
        placeOrderData = new PlaceOrderData();

        //showStatusBar
        utility.showStatusBar();


        /****************Get Restaurant Details from preferences******************/
        preferences = getSharedPreferences("restaurant", this.MODE_PRIVATE);
        restaurantID = preferences.getString("resid", "");
        restaurantName = preferences.getString("resName", "");
        resAddress = preferences.getString("resAddress", "");
//        restaurantDeliveryCharge = preferences.getString("delivery", "");
        restaurantDeliveryCharge = "0";
        restaurantMinorder = preferences.getString("minorder", "");
        restaurantTax = preferences.getString("tax", "");
        restaurantDeliveryOption = preferences.getString("ResDelivery", "");
        restaurantPickupOption = preferences.getString("ResPickup", "");
        firstUser = preferences.getString("firstUser", "");
        normalUser = preferences.getString("normalUser", "");
        firstOfferPercentage = preferences.getString("firstOfferPercentage", "");
        firstOfferAmount = preferences.getString("firstOfferAmount", "");
        normalOfferPercent = preferences.getString("normalOfferPercent", "");
        normalOfferRange = preferences.getString("normalOfferRange", "");
        restaurantStatus = preferences.getString("status", "");

        Log.e("error case", restaurantMinorder);

        loginSession.saveChecked("true");
        restaurantNameTextView.setText(restaurantName);
        restaurantAddressTextView.setText(resAddress);


        //Tax Layout show hide method
        if (!restaurantTax.isEmpty() && Double.parseDouble(restaurantTax) > 0) {

            taxLayout.setVisibility(LinearLayout.VISIBLE);

            taxPercentageTextView.setText(getResources().getString(R.string.Tax) + restaurantTax + "%)");
        }

        //Call Empty Cart Visibility method
        emptycartVisibility();

        //RadioButton Show and Hide option
        if (restaurantDeliveryOption.equalsIgnoreCase("No")) {

            deliveryRadioButton.setVisibility(RadioButton.GONE);
            pickupRadioButton.setChecked(true);
            orderType = "pickup";
            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
            calculation();
        }

        if (restaurantPickupOption.equalsIgnoreCase("No")) {

            pickupRadioButton.setVisibility(RadioButton.GONE);
            deliveryRadioButton.setChecked(true);

            if (Double.parseDouble(restaurantDeliveryCharge) > 0) {
                deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                deliveryChargeTextView.setText(restaurantDeliveryCharge + " " + loginSession.getCurrencyCode());
            } else {
                deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                deliveryChargeTextView.setText(getResources().getString(R.string.free));
            }

            orderType = "delivery";
            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
            calculation();

        }

        if (!restaurantDeliveryCharge.isEmpty() && Double.parseDouble(restaurantDeliveryCharge) > 0) {

            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
            deliveryChargeTextView.setText(restaurantDeliveryCharge + " " + loginSession.getCurrencyCode());
        } else if (restaurantDeliveryCharge.isEmpty()) {
            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
//            deliveryChargLayout.setVisibility(RelativeLayout.VISIBLE);
            deliveryChargeTextView.setText(getResources().getString(R.string.free));
            restaurantDeliveryCharge = "0.00";
        }

        /*************deliveryPickupRadioGroup Click Event****************/
        deliveryPickupRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.deliveryRadioButton:

                        restaurantDeliveryCharge = "0.00";
                        if (Double.parseDouble(restaurantDeliveryCharge) > 0) {
                            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                            deliveryChargeTextView.setText(restaurantDeliveryCharge + " " + loginSession.getCurrencyCode());
                        } else {
                            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                            deliveryChargeTextView.setText(getResources().getString(R.string.free));
                            restaurantDeliveryCharge = "0";
                        }

                        orderType = "delivery";
                        deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                        calculation();

                        break;

                    case R.id.pickupRadioButton:

                        deliveryCharge = 0.00;
                        orderType = "pickup";
                        deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                        calculation();

                        if (restaurantDeliveryOption.equalsIgnoreCase("No")) {

                            deliveryRadioButton.setVisibility(RadioButton.GONE);
                            pickupRadioButton.setChecked(true);
                            orderType = "pickup";
                            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                            calculation();
                        }
                        /*if (voucherType.equalsIgnoreCase("free_delivery")) {

                            voucherDetailLayout.setVisibility(View.GONE);
                            voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                            voucherCodeEditText.setText("");
                            removeFreeDeliveryButton.setSelected(true);

                        }*///edited

                        break;
                }
            }
        });

        addMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewCartDetailsScreen.this, RestaurantDetailsScreen.class);
                intent.putExtra("res_id", restaurantID);
                intent.putExtra("res_cuisineLists", loginSession.getcusinName());
                intent.putExtra("res_deliveryCharge", loginSession.getdelivery());
                intent.putExtra("res_distance", loginSession.getdistance());
                intent.putExtra("rating", loginSession.getrating());
                startActivity(intent);
            }
        });

        /*************Get Cart items from database****************/
        cursor = databaseManager.getAll();
        if (cursor.moveToFirst()) {
            do {

                CartDetails cartDetails = new CartDetails();
                cartDetails.setMenuId(cursor.getString(0));//menu id
                cartDetails.setMenuName(cursor.getString(3));//menu name
                cartDetails.setMenuSize(cursor.getString(5));//getMenuSizeFromList
                cartDetails.setMenuPrice(cursor.getString(6));//getMenuPriceFromList
                cartDetails.setAddonName(cursor.getString(7));//mainAndSubaddons
                cartDetails.setAddonPrice(cursor.getString(8));//totalOfSubaddons
                cartDetails.setQuantity(cursor.getString(9));//quantity
                cartDetails.setTotalPrice(cursor.getString(10));//totalPrice
                totalPriceList.add(cursor.getString(10));
                cartDetails.setInstruction(cursor.getString(cursor.getColumnIndex(Database.INSTRUCTION)));
                getDbCartdetails.add(cartDetails);

            } while (cursor.moveToNext());
        }
        cursor.close();

        cartItemsAdapter = new CartItemsAdapter(this, getDbCartdetails);
        cartMenuListListView.setAdapter(cartItemsAdapter);
        cartItemsAdapter.notifyDataSetChanged();
        utility.setListViewHeightBasedOnChildren(cartMenuListListView);

        //Remove Voucher code
        removeVoucherButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                voucherType = "";
                voucherPrice = "";
                voucherCodeStatus = "";
                voucherDetailLayout.setVisibility(LinearLayout.GONE);
                voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                voucherCodeEditText.setText("");
                voucherCodePrice = "0";
                calculation();
            }
        });

        removeFreeDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voucherType = "";
                voucherPrice = "";
                voucherCodeStatus = "";
                removeFreeDeliveryButton.setVisibility(View.GONE);
                voucherLayout.setVisibility(View.VISIBLE);
                voucherCodeEditText.setText("");
                voucherCodePrice = "0";

                if (orderType.equalsIgnoreCase("pickup")) {

                    deliveryCharge = 0.00;

                } else {
                    restaurantDeliveryCharge = preferences.getString("delivery", "");
                    restaurantDeliveryCharge = "0";
                    deliveryCharge = Double.parseDouble(restaurantDeliveryCharge);
                    deliveryChargeTextView.setText(String.format("%.2f", Double.parseDouble(restaurantDeliveryCharge)) + " " + loginSession.getCurrencyCode());

                }
                calculation();
            }
        });

        //Price Calculation Method
        calculation();

        //Checkout Button
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (utility.isConnectingToInternet()) {

                    if (orderType.equalsIgnoreCase("delivery")) {

                        //check restaurant minimum order price
                        if (subTotal >= Double.parseDouble(restaurantMinorder)) {

                            //check user is logged in or not
                            if (loginSession.isLoggedIn()) {

                                goCheckoutPage();

                            } else {

                                Show_Dialog();

                            }

                        } else {

                            utility.showAlertDialog("Restaurant minimum order is " + restaurantMinorder);
                        }

                    } else {

                        //check user is logged in or not
                        if (loginSession.isLoggedIn()) {

                            goCheckoutPage();

                        } else {

                            Show_Dialog();
                        }
                    }

                } else {
                    utility.showAlertDialog("No Internet Connection!!!");
                }
            }

        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Double.parseDouble(databaseManager.getSubTotal()) > 0) {

                    plusButton.performClick();
                    minusButton.performClick();
                }

            }
        }, 200);


        voucherCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (voucherCheckButton.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.apply))) {

                    vocherText.setVisibility(View.GONE);
                    voucherCodeEditText.setVisibility(View.VISIBLE);
                    voucherCheckButton.setText(getResources().getString(R.string.submit));

                    openKeyBoard();
                    voucherCodeEditText.requestFocus();

                } else {

                    voucherCheckButton.setText(getResources().getString(R.string.apply));


                    vocherText.setVisibility(View.VISIBLE);
                    voucherCodeEditText.setVisibility(View.GONE);

                    String vouchercode = voucherCodeEditText.getText().toString();

                    if (!vouchercode.equals("")) {
                        if (loginSession.isLoggedIn()) {
                            InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethod.hideSoftInputFromWindow(voucherCodeEditText.getWindowToken(), 0);

                            if (checkInternet()) {
                                Map<String, String> params = new HashMap<>();
                                params.put("action", "VoucherAdded");
                                params.put("store_id", restaurantID);
                                params.put("customer_id", loginSession.getUserId());
                                params.put("voucherCode", vouchercode);
                                showProgressDialog();

                                serverRequest.createRequest(NewCartDetailsScreen.this, params, RequestID.REQ_VOUCHER_CODE);

                            } else {

                                noInternetAlertDialog();
                            }

                        } else {

                            Show_Dialog();
                        }

                    } else {
                        toast(getResources().getString(R.string.enterVoucherCode));
                    }
                }
            }
        });

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    //Calculation method
    private void calculation() {

        try {
            subTotal = 0.00;
            orderDiscountPrice = 0.00;
            appDiscountPrice = 0.00;

            for (int i = 0; i < totalPriceList.size(); i++) {
                double subtotal = Double.parseDouble(totalPriceList.get(i));

                subTotal += subtotal;

            }

            subTotalTextView.setText(String.format("%.2f", subTotal) + " " + loginSession.getCurrencyCode());

            if (orderType.equalsIgnoreCase("pickup")) {

                deliveryCharge = 0.00;
                restaurantDeliveryCharge = "0.00";

            } else {

                deliveryCharge = Double.parseDouble(restaurantDeliveryCharge);

            }

            minimunOrderPrice = Double.parseDouble(restaurantMinorder);
            tax = Double.parseDouble(restaurantTax);
            calculatedTax = subTotal * (tax / 100);
            taxAmountTextView.setText(String.format("%.2f", calculatedTax) + " " + loginSession.getCurrencyCode());

            if (loginSession.isLoggedIn()) {

                if (loginSession.getFirstUser().equalsIgnoreCase("YES") && firstUser.equalsIgnoreCase("YES")) {

                    if ((!firstOfferPercentage.isEmpty()) && (subTotal >= Double.parseDouble(firstOfferAmount) && !firstOfferAmount.equals("0"))) {

                        offerLayout.setVisibility(LinearLayout.VISIBLE);
                        offerpercentTextView.setText(getResources().getString(R.string.Offer) + firstOfferPercentage + "%)");
                        orderDiscountPrice = subTotal * (Double.parseDouble(firstOfferPercentage) / 100);
                        offerPriceTextView.setText("-" + String.format("%.2f", orderDiscountPrice) + " " + loginSession.getCurrencyCode());
                        double orderDiscountPriceTotal = subTotal - orderDiscountPrice;

                        totalPrice = orderDiscountPriceTotal /*+ deliveryCharge */ + calculatedTax;

                        Log.e("Offer minues totalPrice", "" + totalPrice);

                        getOfferPercent = firstOfferPercentage;

                    } else {
                        offerLayout.setVisibility(LinearLayout.GONE);
                        totalPrice = subTotal /*+ deliveryCharge*/ + calculatedTax;
                        Log.e("Offer no totalPrice", "" + totalPrice);
                    }

                } else {

                    if (normalUser.equalsIgnoreCase("YES")) {

                        if ((!normalOfferPercent.isEmpty()) && (subTotal >= Double.parseDouble(normalOfferRange) && !normalOfferRange.equals("0"))) {

                            offerLayout.setVisibility(LinearLayout.VISIBLE);
                            offerpercentTextView.setText(getResources().getString(R.string.Offer) + normalOfferPercent + "%)");
                            orderDiscountPrice = subTotal * (Double.parseDouble(normalOfferPercent) / 100);
                            offerPriceTextView.setText("-" + String.format("%.2f", orderDiscountPrice) + " " + loginSession.getCurrencyCode());
                            double orderDiscountPriceTotal = subTotal - orderDiscountPrice;

                            totalPrice = orderDiscountPriceTotal /*+ deliveryCharge */ + calculatedTax;

                            Log.e("Offer minues totalPrice", "" + totalPrice);

                            getOfferPercent = normalOfferPercent;

                        } else {
                            offerLayout.setVisibility(LinearLayout.GONE);
                            totalPrice = subTotal /*+ deliveryCharge*/ + calculatedTax;
                            Log.e("Offer no totalPrice", "" + totalPrice);
                        }
                    } else {
                        offerLayout.setVisibility(LinearLayout.GONE);
                        totalPrice = subTotal /*+ deliveryCharge*/ + calculatedTax;
                        Log.e("Offer no totalPrice", "" + totalPrice);
                    }
                }


            } else {

                offerLayout.setVisibility(LinearLayout.GONE);
                totalPrice = subTotal /*+ deliveryCharge*/ + calculatedTax;
            }


            //Vocher code
            Log.e("voucherPrice", voucherPrice);
            Log.e("voucherType", voucherType);
            if (voucherType.equalsIgnoreCase("price")) {

                if (subTotal > Double.parseDouble(voucherPrice)) {

                    totalPrice = totalPrice - Double.parseDouble(voucherPrice);

                    voucherDetailLayout.setVisibility(RelativeLayout.VISIBLE);
                    voucherLayout.setVisibility(RelativeLayout.GONE);
                    removeFreeDeliveryButton.setVisibility(View.GONE);
                    voucherTextView.setText(getResources().getString(R.string.voucher));
                    voucehrPriceTextView.setText("-" + String.format("%.2f", Double.parseDouble(voucherPrice)) + " " + loginSession.getCurrencyCode());

                    //send voucherCodePrice to server
                    voucherCodePrice = voucherPrice;
                    voucherCodePercentage = "0";

                } else {
                    utility.toast(getResources().getString(R.string.subTotalNotEnough));
                    voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                }

            } else if (voucherType.equalsIgnoreCase("percentage")) {

                vouchertotal = subTotal * (Double.parseDouble(voucherPrice) / 100);

                if (subTotal > vouchertotal) {
                    totalPrice = totalPrice - vouchertotal;
                    voucherDetailLayout.setVisibility(RelativeLayout.VISIBLE);
                    voucherLayout.setVisibility(RelativeLayout.GONE);
                    voucherTextView.setText(getResources().getString(R.string.Voucher) + voucherPrice + "%)");
                    voucehrPriceTextView.setText("-" + String.format("%.2f", vouchertotal) + " " + loginSession.getCurrencyCode());
                    removeFreeDeliveryButton.setVisibility(View.GONE);
                    //send voucherCodePrice to server
                    voucherCodePrice = String.format("%.2f", vouchertotal);
                    voucherCodePercentage = voucherPrice;
                    //  Log.e("voucher minues totalPrice",""+totalPrice);
                } else {

                    utility.toast(getResources().getString(R.string.subTotalNotEnough));
                    voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                }

            } else if (voucherType.equalsIgnoreCase("free_delivery")) {
                deliveryChargLayout.setVisibility(RelativeLayout.GONE);
                deliveryChargeTextView.setText(getResources().getString(R.string.free));
                voucherDetailLayout.setVisibility(RelativeLayout.VISIBLE);
                voucherLayout.setVisibility(RelativeLayout.GONE);
                removeFreeDeliveryButton.setVisibility(View.VISIBLE);
                restaurantDeliveryCharge = "0";

            }

            Log.e("final totalPrice", "" + totalPrice);


            if (callReward) {

                callGetRewardsMethod();
                callReward = false;

            } else {

                if (rewardAmount > 0) {

                    double earnPointsValue = (subTotal / rewardAmount) * rewardPoints;
                    earnPoints = String.valueOf(earnPointsValue).replace(".0", "");
                    earnPointsTextView.setText(getResources().getString(R.string.youWillEarn) + " " + earnPoints + " " + getResources().getString(R.string.points));
                    earnPointsLayout.setVisibility(View.VISIBLE);
                } else {

                    earnPointsLayout.setVisibility(View.GONE);
                }

                if (rewardPercentage > 0) {

                    redeemAmount = (rewardPercentage / 100) * subTotal;

                    redeemLayout.setVisibility(View.VISIBLE);
                    redeemPercentageTextView.setText(getResources().getString(R.string.RedeemAmount) + rewardPercentage + " % )");
                    redeemAmountTextView.setText("-" + String.format("%.2f", redeemAmount) + " " + loginSession.getCurrencyCode());

                    totalPrice = totalPrice - redeemAmount;

                } else {

                    redeemLayout.setVisibility(View.GONE);
                }

            }

            if (totalPrice >= 0) {
                grandTotalTextView.setText(String.format("%.2f", totalPrice) + " " + loginSession.getCurrencyCode());
            } else {
                grandTotalTextView.setText("0.00 " + loginSession.getCurrencyCode());
                totalPrice = 0;
            }


        } catch (NumberFormatException e) {

            Log.e("eNumberFormat", "eNumberFormat" + e);

        }

    }

    private void callGetRewardsMethod() {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("subTotal", String.valueOf(subTotal));
            params.put("grandTotal", String.valueOf(totalPrice));
            params.put("customer_id", loginSession.getUserId());
            params.put("resid", restaurantID);
            serverRequest.createRequest(NewCartDetailsScreen.this, params, RequestID.REQ_GET_REWARD);
            showProgressDialog();
        } else {
            noInternetAlertDialog();
        }
    }

    public void showAlertDialog(String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(title);

        //alertDialog.setMessage(message);

        //  alertDialog.setIcon(R.drawable.caution);

        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                voucherType = "";
                voucherPrice = "";
                voucherCodeStatus = "";
                voucherDetailLayout.setVisibility(LinearLayout.GONE);
                voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                voucherCodeEditText.setText("");
                voucherCodePrice = "0";
                calculation();


                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    //EMptyCart Visibility Method
    private void emptycartVisibility() {

        cartCount = databaseManager.getQuantityCount();
        loginSession.saveCardCount(cartCount);

        if (cartCount > 0) {
            Log.e("+loginSession", "+loginSession" + loginSession.getCartCount());
            allContentLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        } else if (cartCount == 0) {
            emptyCartLayout.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
            allContentLayout.setVisibility(View.GONE);
        }
    }

    //refreshDataBase
    public void refreshDataBase() {

        cartCount = databaseManager.getCount();

        Log.e("cartCount", "" + cartCount);
        Log.e("refreshDataBase", "Method call");

        getDbCartdetails.clear();
        totalPriceList.clear();
        emptycartVisibility();

        cursor = databaseManager.getAll();

        if (cursor.moveToFirst()) {

            do {
                CartDetails cartItems = new CartDetails();
                cartItems.setMenuId(cursor.getString(0));
                cartItems.setMenuName(cursor.getString(3));
                cartItems.setMenuSize(cursor.getString(5));
                cartItems.setMenuPrice(cursor.getString(6));
                cartItems.setAddonName(cursor.getString(7));
                cartItems.setAddonPrice(cursor.getString(8));
                cartItems.setQuantity(cursor.getString(9));
                cartItems.setTotalPrice(cursor.getString(10));
                totalPriceList.add(cursor.getString(10));
                cartItems.setInstruction(cursor.getString(cursor.getColumnIndex(Database.INSTRUCTION)));

                Log.e("cursor123456", "cursor123456" + cursor.getString(0) + "cursor3" + cursor.getString(3) + "cursor5" + cursor.getString(5) + "cursor6" + cursor.getString(6) + "cursor7" + cursor.getString(7) + "cursor8" + cursor.getString(8) + "cursor9" + cursor.getString(9) + "cursor10" + cursor.getString(10));
                Log.e("databasevalues", "Total price" + totalPriceList);
                Log.e("Menuprice", "" + cursor.getString(6));
                getDbCartdetails.add(cartItems);


            } while (cursor.moveToNext());
        }
        cursor.close();

        cartItemsAdapter = new CartItemsAdapter(this, getDbCartdetails);
        cartMenuListListView.setAdapter(cartItemsAdapter);
        cartItemsAdapter.notifyDataSetChanged();
        utility.setListViewHeightBasedOnChildren(cartMenuListListView);
        calculation();

    }

    //Login Checkout Dialog
    private void Show_Dialog() {

        Intent intent = new Intent(NewCartDetailsScreen.this, LoginScreen.class);
        startActivityForResult(intent, 11);

    }


    //Activity Result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 11) {
            calculation();
        }
    }


    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID) {

            case REQ_VOUCHER_CODE:

                Log.e("Return Response", "" + result);

                voucherCodeStatus = voucherCodeEditText.getText().toString().trim();
                VoucherDetailsList voucherDetails = (VoucherDetailsList) result;
                VoucherDetailsList.VoucherDetails voucherDetails1 = voucherDetails.result.voucherDetails;
                voucherType = voucherDetails1.offer_mode;
                voucherPrice = voucherDetails1.offer_value;

                Log.e("", voucherType + "  :  " + voucherPrice);
                if (orderType.equalsIgnoreCase("pickup") && voucherType.equalsIgnoreCase("free_delivery")) {
                    utility.toast(getResources().getString(R.string.voucherNotApplicable));
                    voucherType = "";
                    voucherPrice = "";
                    voucherCodeStatus = "";

                } else {
                    voucherType = voucherDetails1.offer_mode;
                    voucherPrice = voucherDetails1.offer_value;
                    voucherLayout.setVisibility(RelativeLayout.GONE);
                    calculation();
                }

                break;

            case REQ_GET_REWARD:

                RewardList rewardList = (RewardList) result;

                if (!rewardList.result.earn_point.isEmpty()) {
                    earnPoints = rewardList.result.earn_point;
                    earnPointsLayout.setVisibility(View.VISIBLE);
                    earnPointsTextView.setText(getResources().getString(R.string.youWillEarn) + rewardList.result.earn_point.replace("0.", "").replace(".0", "") + getResources().getString(R.string.points));
                } else {
                    earnPoints = "0";
                    earnPointsLayout.setVisibility(View.GONE);
                }


                if (!rewardList.result.rewardAmount.isEmpty()) {
                    rewardAmount = Double.parseDouble(rewardList.result.rewardAmount);
                } else {
                    rewardAmount = 0;
                }

                if (!rewardList.result.rewardPoints.isEmpty()) {
                    rewardPoints = Double.parseDouble(rewardList.result.rewardPoints);
                } else {
                    rewardPoints = 0;
                }


                if (!rewardList.result.rewardPercentage.isEmpty()) {

                    rewardPercentage = Double.parseDouble(rewardList.result.rewardPercentage);

                } else {
                    rewardPercentage = 0;
                }

                if (!rewardList.result.rewardPoint.isEmpty()) {

                    redeemAmount = Double.parseDouble(rewardList.result.rewardPoint);

                } else {
                    redeemAmount = 0;
                }

                if (rewardPercentage > 0) {

                    redeemLayout.setVisibility(View.VISIBLE);
                    redeemPercentageTextView.setText(getResources().getString(R.string.RedeemAmount) + rewardPercentage + " % )");
                    redeemAmountTextView.setText("-" + String.format("%.2f", redeemAmount) + " " + loginSession.getCurrencyCode());

                    totalPrice = totalPrice - redeemAmount;

                    if (totalPrice >= 0) {

                        grandTotalTextView.setText(String.format("%.2f", totalPrice) + " " + loginSession.getCurrencyCode());
                    } else {
                        grandTotalTextView.setText("0.00 " + loginSession.getCurrencyCode());
                        totalPrice = 0;
                    }


                } else {
                    redeemLayout.setVisibility(View.GONE);
                }


                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);

    }

    //CartItem Adapter Class
    public class CartItemsAdapter extends BaseAdapter {

        //Create objects
        Activity activity;
        Utility utility;

        //create list
        ArrayList<CartDetails> getCartitems = null;

        public CartItemsAdapter(Activity activity, ArrayList<CartDetails> getCartitems) {
            this.activity = activity;
            this.getCartitems = getCartitems;
            this.utility = Utility.getInstance(activity);

        }

        @Override
        public int getCount() {
            return getCartitems.size();
        }

        @Override
        public Object getItem(int position) {
            return getCartitems.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();
            if (convertView == null)
                convertView = inflater.inflate(R.layout.custom_cart_items, null);

            minusButton = (ImageView) convertView.findViewById(R.id.minusButton);
            plusButton = (ImageView) convertView.findViewById(R.id.plusButton);
            TextView menuNameTextView = (TextView) convertView.findViewById(R.id.menuNameTextView);
            TextView subAddonTextView = (TextView) convertView.findViewById(R.id.subAddonTextView);
            TextView menuInstruction = (TextView) convertView.findViewById(R.id.menuInstruction);
            final TextView menuQtyEditText = (TextView) convertView.findViewById(R.id.menuQtyEditText);
            TextView menuPriceTextView = (TextView) convertView.findViewById(R.id.menuPriceTextView);
            ImageView menuDeleteButton = (ImageView) convertView.findViewById(R.id.menuDeleteButton);

            String Menusizename = getCartitems.get(position).getMenuSize();

            if (Menusizename == null) {
                Menusizename = "";
            } else if (Menusizename.isEmpty()) {
                menuNameTextView.setText(getCartitems.get(position).getMenuName() + "(" + String.format("%.2f", Double.parseDouble(getCartitems.get(position).getMenuPrice())) + " " + loginSession.getCurrencyCode() + ")");
            } else {
                menuNameTextView.setText(getCartitems.get(position).getMenuName() + " [" + getCartitems.get(position).getMenuSize() + "(" + String.format("%.2f", Double.parseDouble(getCartitems.get(position).getMenuPrice())) + " " + loginSession.getCurrencyCode() + ")]");
            }

            if (!getCartitems.get(position).getAddonName().isEmpty()) {

                subAddonTextView.setVisibility(View.VISIBLE);
                subAddonTextView.setText(getCartitems.get(position).getAddonName());
            }

            if (getCartitems.get(position).getInstruction() != null
                    && !getCartitems.get(position).getInstruction().isEmpty()) {

                menuInstruction.setVisibility(View.VISIBLE);
                menuInstruction.setText(getCartitems.get(position).getInstruction());
            } else {
                menuInstruction.setVisibility(View.GONE);
            }

            menuQtyEditText.setText(getCartitems.get(position).getQuantity());

            menuPriceTextView.setText(String.format("%.2f", Double.parseDouble(getCartitems.get(position).getTotalPrice())) + " " + loginSession.getCurrencyCode());

            /**************plues minus button**/

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String qty = menuQtyEditText.getText().toString().trim();

                    int increase = Integer.parseInt(qty);
                    Log.e("Count values is", "" + increase);

                    if (increase >= 25) {
                        utility.showAlertDialog(getResources().getString(R.string.maximumAvailableQuantity));
                    } else {
                        increase += 1;

                        menuQtyEditText.setText(String.valueOf(increase));
                    }


                }
            });
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String qty1 = menuQtyEditText.getText().toString().trim();

                    int decrease = Integer.parseInt(qty1);
                    Log.e("Count values is", "" + decrease);

                    if (decrease == 1) {
                        utility.showAlertDialog(getResources().getString(R.string.minimumQuantity));
                    } else {
                        decrease -= 1;
                        menuQtyEditText.setText(String.valueOf(decrease));
                    }

                }
            });

            /*****************Delete Button*******************/
            menuDeleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String ID = getCartitems.get(position).getMenuId();
                    Log.e("ID", "ID" + ID);
                    //Log.e("menuid", ID);
                    databaseManager.deleteMenu(ID);
                    refreshDataBase();
                }
            });

            //Quantity Edit Text Text watcher
            menuQtyEditText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void afterTextChanged(Editable s) {

                    String ID = getCartitems.get(position).getMenuId();
                    String quantity = menuQtyEditText.getText().toString().trim();
                    if (quantity.length() > 0) {
                        if (Integer.parseInt(quantity) > 25) {
                            utility.toast(getResources().getString(R.string.maximumQuantityOnly));
                        } else {
                            databaseManager.updateQuantity(ID, quantity);
                            refreshDataBase();
                        }
                    }
                }
            });

            return convertView;

        }
    }

    //PASSING VALUES
    private void goCheckoutPage() {

        Log.e("ins  da", instructionEditText.getText().toString());
        Intent intent = new Intent(NewCartDetailsScreen.this, CheckoutScreen.class);
        placeOrderData.setOrderType(orderType);
        placeOrderData.setRestaurantID(restaurantID);
        placeOrderData.setRestaurantStatus(restaurantStatus);
        placeOrderData.setSubTotal(String.valueOf(subTotal));
        placeOrderData.setDeliveryCharge(restaurantDeliveryCharge);
        placeOrderData.setMinimumcharge(restaurantMinorder);
        placeOrderData.setTaxAmount(utility.formatString(String.valueOf(calculatedTax)));
        placeOrderData.setTaxPercentage(utility.formatString(String.valueOf(tax)));
        placeOrderData.setOfferAmount(utility.formatString(String.valueOf(orderDiscountPrice)));
        placeOrderData.setOfferPercentage(getOfferPercent);
        placeOrderData.setGrandTotal(String.format("%.2f", totalPrice));
        placeOrderData.setVoucherCodePrice(voucherCodePrice);
        placeOrderData.setVoucherCodePercent(voucherCodePercentage);
        placeOrderData.setVoucherCodeStatus(voucherCodeStatus);
        placeOrderData.setInstruction(instructionEditText.getText().toString());
        placeOrderData.setEarnPoints(earnPoints);
        placeOrderData.setRewardPercentage(utility.formatString(String.valueOf(rewardPercentage)));
        placeOrderData.setRedeemAmount(utility.formatString(String.valueOf(redeemAmount)));
        intent.putExtra("placeOrderData", placeOrderData);
        startActivity(intent);

    }

    private void openKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
