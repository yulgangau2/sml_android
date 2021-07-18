package com.eatyhero.customer.ordertab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginScreen;
import com.eatyhero.customer.base.BaseFragment;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.fcm.Config;
import com.eatyhero.customer.model.OrderHistoryList;
import com.eatyhero.customer.restauranttab.OrderViewScreen;
import com.eatyhero.customer.restauranttab.RestaurantDetailsScreen;
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

public class NewOrderHistoryScreen extends BaseFragment implements ServerListener {

    //create objects
    OrderHistoryList orderHistoryList;
    OrderListAdapter orderListAdapter;
    List<OrderHistoryList.OrderLists> orderList1 = new ArrayList<>();
    ServerRequest serverRequestHandler;
    LoginSession loginSession;
    Utility utility;
    BroadcastReceiver broadcastReceiver;
    DatabaseManager databaseManager;

    //XMl objects
    @BindView(R.id.orderHistoryListView)ListView orderHistoryListView;
    @BindView(R.id.loginLayout)RelativeLayout loginLayout;
    @BindView(R.id.loginButton)Button loginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_history_screen, container, false);

        //Initialize the xml id
        ButterKnife.bind(this,rootView);

        //Initialize the object
        loginSession         = LoginSession.getInstance(getActivity());
        serverRequestHandler = ServerRequest.getInstance(getActivity());
        utility              = Utility.getInstance(getActivity());
        databaseManager = DatabaseManager.getInstance(getActivity());
        //showStatusBar
        utility.showStatusBar();

        utility.CURRENT_SCREEN = "ORDER_HISTORY_SCREEN";

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                boolean check_update = intent.getBooleanExtra("update", false);
                if (check_update) {
                    getOrderList();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), LoginScreen.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

    public void getOrderList() {

        if (utility.isConnectingToInternet()) {
            Map<String, String> params = new HashMap<>();
            // POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "OrderList");
            utility.showProgressDialog();
            serverRequestHandler.createRequest(this, params, RequestID.REQ_ORDER_HISTORY);
        } else {
            utility.noInternetAlertDialog();
        }
    }

    public void onResume() {
        super.onResume();
        Utility.update_check = true;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        if (loginSession.isLoggedIn()) {

            getOrderList();
            loginLayout.setVisibility(View.GONE);
            orderHistoryListView.setVisibility(View.VISIBLE);

        } else {

            loginLayout.setVisibility(View.VISIBLE);
            orderHistoryListView.setVisibility(View.GONE);

        }

        if (orderList1.size() > 0) {
            orderListAdapter.notifyDataSetChanged();
            orderHistoryListView.refreshDrawableState();
        }

    }

    public void onStop() {
        super.onStop();
        Utility.update_check = false;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    public void onDestroy() {
        super.onDestroy();
        Utility.update_check = false;
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        utility.hideProgressDialog();

        switch (requestID) {

            case REQ_ORDER_HISTORY:

                orderHistoryList = (OrderHistoryList) result;
                try {
                    orderList1 = orderHistoryList.result.orderLists;
                    orderListAdapter = new OrderListAdapter(getActivity(),orderList1,this);
                    orderHistoryListView.setAdapter(orderListAdapter);
                    getTotalHeightofListView(orderHistoryListView);
                } catch (NullPointerException e) {
                }

                break;

            case REQ_SEND_REVIEW:

                utility.toast(result.toString());
                getOrderList();
                break;

            case REQ_TO_GET_STOREITEMS:

                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        utility.toast(error);
        utility.hideProgressDialog();
        loginLayout.setVisibility(View.GONE);
        orderHistoryListView.setVisibility(View.GONE);
    }

    public class OrderListAdapter extends BaseAdapter {

        //Create Objects
        Activity activity;
        NewOrderHistoryScreen orderHistory;
        Dialog dialog;
        String reviewRatingMessage = "", orderID = "", getRatingValue = "";

        LoginSession loginSession;
        ServerRequest serverRequest;

        //Create List
        List<OrderHistoryList.OrderLists> allOrders = new ArrayList<>();


        public OrderListAdapter(Activity activity, List<OrderHistoryList.OrderLists> allOrders, NewOrderHistoryScreen fragment) {
            this.activity = activity;
            this.allOrders = allOrders;
            this.orderHistory = fragment;
        }

        @Override
        public int getCount() {

            return allOrders.size();
        }

        @Override
        public Object getItem(int i) {
            return allOrders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            if (activity != null) {

                loginSession = LoginSession.getInstance(activity);
                serverRequest = ServerRequest.getInstance(activity);
            }

            LayoutInflater inflater = activity.getLayoutInflater();
            if (view == null)
                view = inflater.inflate(R.layout.order_history_custom, null);

            TextView orderIdTextView       = (TextView) view.findViewById(R.id.orderIdTextView);
            TextView orderStatusTextView   = (TextView) view.findViewById(R.id.orderStatusTextView);
            TextView priceTextView         = (TextView) view.findViewById(R.id.priceTextView);
            TextView paymentStatusTextView = (TextView) view.findViewById(R.id.paymentStatusTextView);
            TextView paymentTypeTextView   = (TextView) view.findViewById(R.id.paymentTypeTextView);
            TextView trackOrderTextView    = (TextView) view.findViewById(R.id.trackOrderTextView);
            ImageView paymentTypeImageView = (ImageView) view.findViewById(R.id.paymentTypeImageView);
            TextView reviewRatingTextView  = (TextView) view.findViewById(R.id.reviewTextView);
            TextView reOrderTextView       = (TextView) view.findViewById(R.id.reOrderTextView);
            TextView restaurantTextView       = (TextView) view.findViewById(R.id.restaurantTextView);
            TextView deliveryTypeTextView       = (TextView) view.findViewById(R.id.deliveryTypeTextView);
            RatingBar ratingBarValue       = (RatingBar) view.findViewById(R.id.ratingBar);
            LayerDrawable stars            = (LayerDrawable) ratingBarValue.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

            orderIdTextView.setText(allOrders.get(position).order_number);
            priceTextView.setText(String.format("%.2f",Double.parseDouble(allOrders.get(position).order_grand_total))+ " " +loginSession.getCurrencyCode());
//            paymentTypeTextView.setText(allOrders.get(position).payment_method);
            try {
                restaurantTextView.setText(allOrders.get(position).restaurant.restaurant_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (allOrders.get(position).order_type.equalsIgnoreCase("delivery")){
                deliveryTypeTextView.setText(getResources().getString(R.string.delivery));
            }else {
                deliveryTypeTextView.setText(getResources().getString(R.string.pickUp));
            }


            if (allOrders.get(position).payment_status.equalsIgnoreCase("p")) {
                paymentStatusTextView.setText(getResources().getString(R.string.paid));
                paymentStatusTextView.setTextColor(activity.getResources().getColor(R.color.greenbutton));
            } else {
                paymentStatusTextView.setText(getResources().getString(R.string.unpaid));
                paymentStatusTextView.setTextColor(activity.getResources().getColor(R.color.red));
            }

            if (allOrders.get(position).payment_method.equalsIgnoreCase("stripe")) {

                paymentTypeTextView.setText(allOrders.get(position).payment_method);
                paymentTypeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.creditcard_icon));

            } else if (allOrders.get(position).payment_method.equalsIgnoreCase("cod")) {
                paymentTypeTextView.setText(getResources().getString(R.string.cod));
                paymentTypeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.currency_icon));

            } else if (allOrders.get(position).payment_method.equalsIgnoreCase("wallet")) {
                paymentTypeTextView.setText(allOrders.get(position).payment_method);
                paymentTypeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.nav_wallet_icon));

            }else if (allOrders.get(position).payment_method.equalsIgnoreCase("paypal")) {
                paymentTypeTextView.setText(getResources().getString(R.string.paypal));
                paymentTypeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.paypal));
            }

            if (allOrders.get(position).status.equalsIgnoreCase("Delivered")) {
                orderStatusTextView.setTextColor(Color.parseColor("#F26722"));
                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.delivered));

                String ratings = allOrders.get(position).rating;
                if (!ratings.isEmpty() && !ratings.equals("0")) {
                    reviewRatingTextView.setVisibility(View.GONE);
                    ratingBarValue.setVisibility(View.VISIBLE);
                    float ratingvalue = Float.parseFloat(ratings);
                    ratingBarValue.setRating(ratingvalue);


                } else {
                    reviewRatingTextView.setVisibility(View.VISIBLE);
                    ratingBarValue.setVisibility(View.GONE);
                }

            } else {

                reviewRatingTextView.setVisibility(View.GONE);
                ratingBarValue.setVisibility(View.GONE);
            }



            if ((allOrders.get(position).status.equalsIgnoreCase("Collected"))) {

                trackOrderTextView.setVisibility(View.VISIBLE);
                orderStatusTextView.setText(getResources().getString(R.string.collected));
                orderStatusTextView.setTextColor(Color.parseColor("#5aa30d"));

            } else if (allOrders.get(position).status.equalsIgnoreCase("Pending")) {

                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.pending));
                orderStatusTextView.setTextColor(Color.parseColor("#FF0000"));

            } else if (allOrders.get(position).status.equalsIgnoreCase("Driver Accepted")) {

                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.driverAccepted));
                orderStatusTextView.setTextColor(Color.parseColor("#5aa30d"));

            } else if (allOrders.get(position).status.equalsIgnoreCase("Accepted")) {

                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.accepted));
                orderStatusTextView.setTextColor(Color.parseColor("#5aa30d"));

            } else if (allOrders.get(position).status.equalsIgnoreCase("Failed")) {

                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.failed));
                orderStatusTextView.setTextColor(Color.parseColor("#FF0000"));

            } else if (allOrders.get(position).status.equalsIgnoreCase("Waiting")) {

                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.waiting));
                orderStatusTextView.setTextColor(Color.parseColor("#F26722"));

            }else if (allOrders.get(position).status.equalsIgnoreCase("Delivered")) {
                orderStatusTextView.setTextColor(Color.parseColor("#5aa30d"));
                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setText(getResources().getString(R.string.delivered));
            } else {

                orderStatusTextView.setText(allOrders.get(position).status);
                trackOrderTextView.setVisibility(View.GONE);
                orderStatusTextView.setTextColor(Color.parseColor("#5aa30d"));

            }

            reOrderTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    getRestaurantMenuListResponse(allOrders.get(position).restaurant_id);
                    if(databaseManager.getCount() > 0){

                        if(databaseManager.getResId().equalsIgnoreCase(allOrders.get(position).restaurant_id)){

                            Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                            intent.putExtra("res_id",allOrders.get(position).restaurant_id);
                            intent.putExtra("res_cuisineLists",allOrders.get(position).cuisine_list);
                            intent.putExtra("res_deliveryCharge",allOrders.get(position).delivery_charge);
                            intent.putExtra("res_distance",allOrders.get(position).distance);
                            intent.putExtra("rating",allOrders.get(position).rating);
                            startActivity(intent);

                        }else{

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setIcon(R.drawable.caution);
                            alertDialog.setTitle(getResources().getString(R.string.note));
                            alertDialog.setMessage(getResources().getString(R.string.addedSomeItem));

                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    databaseManager.clearTable();
                                    loginSession.clear_restaurant();
                                    dialog.cancel();
                                    loginSession.saveCardCount(0);

                                    Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                                    intent.putExtra("res_id",allOrders.get(position).restaurant_id);
                                    intent.putExtra("res_cuisineLists",allOrders.get(position).cuisine_list);
                                    intent.putExtra("res_deliveryCharge",allOrders.get(position).delivery_charge);
                                    intent.putExtra("res_distance",allOrders.get(position).distance);
                                    intent.putExtra("rating",allOrders.get(position).rating);
                                    startActivity(intent);

                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }
                    }else{

                        Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                        intent.putExtra("res_id",allOrders.get(position).restaurant_id);
                        intent.putExtra("res_cuisineLists",allOrders.get(position).cuisine_list);
                        intent.putExtra("res_deliveryCharge",allOrders.get(position).delivery_charge);
                        intent.putExtra("res_distance",allOrders.get(position).distance);
                        intent.putExtra("rating",allOrders.get(position).rating);
                        startActivity(intent);
                    }

                }
            });


            reviewRatingTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showRatingDialog();
                    orderID = allOrders.get(position).id;
                }
            });

            trackOrderTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, OrderTracking.class);
                    intent.putExtra("orderId", allOrders.get(position).id);
                    intent.putExtra("ref_number", allOrders.get(position).order_number);
                    intent.putExtra("status", allOrders.get(position).status);
                    intent.putExtra("source_latitude", allOrders.get(position).source_latitude);
                    intent.putExtra("source_longitude", allOrders.get(position).source_longitude);
                    intent.putExtra("destination_latitude", allOrders.get(position).destination_latitude);
                    intent.putExtra("destination_longitude", allOrders.get(position).destination_longitude);
                    startActivity(intent);


                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(),OrderViewScreen.class);
                    intent.putExtra("orderid",allOrders.get(position).id);
                    intent.putExtra("actionBarTitle","OrderView");
                    startActivity(intent);

                   /* Bundle bundle = new Bundle();
                    Fragment fragment = new NewOrderViewScreen();
                    bundle.putString("orderid", allOrders.get(position).id);
                    loginSession.saveOrderId(allOrders.get(position).id);
                    bundle.putString("actionBarTitle", "Order View");
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

                }
            });

            return view;
        }

        public void showRatingDialog() {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.rating_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            final RatingBar ratingBarValue = (RatingBar) dialog.findViewById(R.id.ratingBar);
            final EditText ratingMessage   = (EditText) dialog.findViewById(R.id.mesageEditText);
            TextView cancel                  = (TextView) dialog.findViewById(R.id.cancelButton);
            TextView ok                      = (TextView) dialog.findViewById(R.id.rateButton);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    notifyDataSetChanged();

                    reviewRatingMessage = ratingMessage.getText().toString();
                    getRatingValue = String.valueOf(ratingBarValue.getRating());

                    Log.e("Rating 1223456", "" + getRatingValue);

                    if (!getRatingValue.isEmpty() && Double.parseDouble(getRatingValue) > 0.0) {

                        if (utility.isConnectingToInternet()) {

                            Map<String, String> params = new HashMap<>();
                            // the POST parameters:
                            params.put("action", "MyAccount");
                            params.put("customer_id", loginSession.getUserId());
                            params.put("page", "OrderReview");
                            params.put("order_id", orderID);
                            params.put("rating", getRatingValue);
                            params.put("message", reviewRatingMessage);
                            serverRequest.createRequest(NewOrderHistoryScreen.this, params, RequestID.REQ_SEND_REVIEW);
                            dialog.dismiss();

                            Log.e("Rating To SerVer", "" + params);
                            utility.showProgressDialog();

                        } else {

                            utility.noInternetAlertDialog();
                        }
                    } else {
                        utility.toast(getResources().getString(R.string.selectRating));
                    }
                }
            });
        }

    }

    private void getRestaurantMenuListResponse(String res_id) {

        if (isConnectingToInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("resId", res_id);
            params.put("customer_id", loginSession.getUserId());
            showProgressDialog();
            serverRequestHandler.createRequest(NewOrderHistoryScreen.this, params, RequestID.REQ_TO_GET_STOREITEMS);

        } else {
            noInternetAlertDialog();
        }
    }
}
