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

import androidx.annotation.Nullable;

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
import android.widget.TextView;

import com.eatyhero.customer.database.Database;
import com.eatyhero.customer.restauranttab.RestaurantDetailsScreen;
import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginScreen;
import com.eatyhero.customer.base.BaseFragment;
import com.eatyhero.customer.base.NewBaseHomeScreen;
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
 * Created by admin on 22-03-2017.
 */

public class CardDetailScreenFragment extends BaseFragment implements ServerListener {

    //Create Objects
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;
    SharedPreferences preferences;
    DatabaseManager databaseManager;
    Cursor cursor;
    CartItemsAdapter cartItemsAdapter;
    PlaceOrderData placeOrderData;
    NewBaseHomeScreen baseHomeScreen;

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
    RelativeLayout allContentLayout;
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
    String restaurantID, resAddress, restaurantName, restaurantDeliveryCharge = "", restaurantMinorder, restaurantTax;
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
    double subTotal;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cart_details_fragment, container, false);

        //Initialize xml file
        ButterKnife.bind(this, rootView);

        //Create Objects
        utility = Utility.getInstance(getActivity());
        serverRequest = ServerRequest.getInstance(getActivity());
        Cartitems = new ArrayList<>();
        databaseManager = DatabaseManager.getInstance(getActivity());
        loginSession = LoginSession.getInstance(getActivity());
        placeOrderData = new PlaceOrderData();
        baseHomeScreen = NewBaseHomeScreen.getInstance();

        //showStatusBar
        utility.showStatusBar();

        utility.CURRENT_SCREEN = "CARTDETAILS_SCREEN";

        /****************Get Restaurant Details from preferences******************/
        preferences = getActivity().getSharedPreferences("restaurant", getActivity().MODE_PRIVATE);
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


        loginSession.saveChecked("true");
        Log.e("getChecked", "getChecked" + loginSession.getChecked());

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

                deliveryChargeTextView.setText(String.format("%.2f", Double.parseDouble(restaurantDeliveryCharge)) + " " + loginSession.getCurrencyCode());
            } else {

                deliveryChargeTextView.setText(getResources().getString(R.string.free));
            }

            orderType = "delivery";
            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
            calculation();

        }

        backIconImageView.setVisibility(View.GONE);

        if (!restaurantDeliveryCharge.isEmpty() && Double.parseDouble(restaurantDeliveryCharge.replace("-", ".")) > 0) {

            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
            deliveryChargeTextView.setText(String.format("%.2f", Double.parseDouble(restaurantDeliveryCharge)) + " " + loginSession.getCurrencyCode());
        } else if (restaurantDeliveryCharge.isEmpty()) {
            deliveryChargLayout.setVisibility(RelativeLayout.GONE);
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

                            deliveryChargeTextView.setText(String.format("%.2f", Double.parseDouble(restaurantDeliveryCharge)) + " " + loginSession.getCurrencyCode());
                        } else {

                            deliveryChargeTextView.setText(getResources().getString(R.string.free));
                            restaurantDeliveryCharge = "Free";
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
                        if (voucherType.equalsIgnoreCase("free_delivery")) {

                            voucherDetailLayout.setVisibility(View.GONE);
                            voucherLayout.setVisibility(RelativeLayout.VISIBLE);
                            voucherCodeEditText.setText("");
                            removeFreeDeliveryButton.setSelected(true);

                        }

                        break;
                }
            }
        });

        addMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RestaurantDetailsScreen.class);
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
                cartDetails.setInstruction(cursor.getString(cursor.getColumnIndex(Database.INSTRUCTION)));
                totalPriceList.add(cursor.getString(10));
                getDbCartdetails.add(cartDetails);

            } while (cursor.moveToNext());
        }
        cursor.close();

        cartItemsAdapter = new CartItemsAdapter(getActivity(), getDbCartdetails);
        cartMenuListListView.setAdapter(cartItemsAdapter);
        cartItemsAdapter.notifyDataSetChanged();
        utility.setListViewHeightBasedOnChildren(cartMenuListListView);

        //Add Voucher code
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
                            InputMethodManager inputMethod = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethod.hideSoftInputFromWindow(voucherCodeEditText.getWindowToken(), 0);

                            if (isConnectingToInternet()) {
                                Map<String, String> params = new HashMap<>();
                                params.put("action", "VoucherAdded");
                                params.put("store_id", restaurantID);
                                params.put("customer_id", loginSession.getUserId());
                                params.put("voucherCode", vouchercode);
                                showProgressDialog();

                                serverRequest.createRequest(CardDetailScreenFragment.this, params, RequestID.REQ_VOUCHER_CODE);
                                voucherCodeEditText.setText("");
                            } else {

                                noInternetAlertDialog();
                            }

                        } else {

                            toast(getResources().getString(R.string.loginToUseVoucher));
                            Show_Dialog();
                        }

                    } else {
                        toast(getResources().getString(R.string.enterVoucherCode));
                    }
                }

            }
        });

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

                            utility.showAlertDialog(getResources().getString(R.string.restaurantMinimumOrder) + restaurantMinorder);
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
                    noInternetAlertDialog();
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

        return rootView;
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
                    // showAlertDialog();
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

                deliveryChargeTextView.setText(getResources().getString(R.string.free));
                voucherDetailLayout.setVisibility(RelativeLayout.VISIBLE);
                voucherLayout.setVisibility(RelativeLayout.GONE);
                totalPrice = totalPrice/* - deliveryCharge*/;
                removeFreeDeliveryButton.setVisibility(View.VISIBLE);
                restaurantDeliveryCharge = "Free";

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
                totalPrice = 0;
                grandTotalTextView.setText("0.00 " + loginSession.getCurrencyCode());
            }

        } catch (NumberFormatException e) {

            Log.e("eNumberFormat", "eNumberFormat" + e);

        }

    }

    private void callGetRewardsMethod() {

        if (isConnectingToInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("subTotal", String.valueOf(subTotal));
            params.put("grandTotal", String.valueOf(totalPrice));
            params.put("customer_id", loginSession.getUserId());
            params.put("resid", restaurantID);
            serverRequest.createRequest(CardDetailScreenFragment.this, params, RequestID.REQ_GET_REWARD);
            showProgressDialog();
        } else {
            noInternetAlertDialog();
        }
    }

    public void showAlertDialog(String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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

        Log.e("cartCount", "" + cartCount);

        if (cartCount > 0) {

            baseHomeScreen.cartCountTextView.setText(loginSession.getCartCount());
            Log.e("+loginSession", "+loginSession" + loginSession.getCartCount());

            allContentLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);

        } else if (cartCount == 0) {
            baseHomeScreen.cartCountTextView.setVisibility(View.GONE);
            emptyCartLayout.setVisibility(View.VISIBLE);
            allContentLayout.setVisibility(View.GONE);
        }
    }

    //refreshDataBase
    public void refreshDataBase() {

        try {

            cartCount = databaseManager.getCount();
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
                    getDbCartdetails.add(cartItems);

                } while (cursor.moveToNext());
            }
            cursor.close();
            cartItemsAdapter = new CartItemsAdapter(getActivity(), getDbCartdetails);
            cartMenuListListView.setAdapter(cartItemsAdapter);
            cartItemsAdapter.notifyDataSetChanged();
            utility.setListViewHeightBasedOnChildren(cartMenuListListView);
            calculation();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Login Checkout Dialog
    private void Show_Dialog() {

        Intent intent = new Intent(getActivity(), LoginScreen.class);
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

                // voucherCheckButton.setBackgroundResource(R.drawable.voucher_checked);

                if (orderType.equalsIgnoreCase("pickup") && voucherType.equalsIgnoreCase("free_delivery")) {
                    utility.toast(getResources().getString(R.string.voucherNotApplicable));
                    voucherType = "";
                    voucherPrice = "";
                    voucherCodeStatus = "";

                } else {
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

                    Log.e("totalPrice123", "" + totalPrice);

                    if (totalPrice >= 0) {
                        grandTotalTextView.setText(String.format("%.2f", totalPrice) + " " + loginSession.getCurrencyCode());
                    } else {
                        totalPrice = 0;
                        grandTotalTextView.setText("0.00 " + loginSession.getCurrencyCode());
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
        ArrayList<CartDetails> getCartitems;

        public CartItemsAdapter(Activity activity, ArrayList<CartDetails> getCartitems) {
            this.activity = activity;
            this.getCartitems = getCartitems;
        }

        @Override
        public int getCount() {
            try {
                return getCartitems.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            try {
                return getCartitems.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            try {

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
                            showAlertDialog(getResources().getString(R.string.maximumAvailableQuantity));
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
                            showAlertDialog(getResources().getString(R.string.minimumQuantity));
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
                                toast(getResources().getString(R.string.maximumQuantityOnly));
                            } else {
                                databaseManager.updateQuantity(ID, quantity);
                                refreshDataBase();

                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;

        }
    }

    //PASSING VALUES
    private void goCheckoutPage() {

//        if(String.valueOf(totalPrice).equalsIgnoreCase("0.0")){
//
//            toast("inva");
//
//        }else {

        Log.e("ins  da", instructionEditText.getText().toString());
        Intent intent = new Intent(getActivity(), CheckoutScreen.class);
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

//        }
    }

    private void openKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
