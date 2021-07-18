package com.eatyhero.customer.restauranttab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.base.NewBaseHomeScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.moretab.OrderViewResponse;
import com.eatyhero.customer.ordertab.OrderRoute;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by user on 9/28/2015.
 */
public class OrderViewScreen extends BaseActivity implements ServerListener {


    //Create objects
    Utility utility;
    LoginSession loginSession;
    OrderViewListAdapter orderViewListAdapter;
    ServerRequest serverRequestHandler;

    //create String objects
    String orderId = "",deliveryType;
    String taxPercentage,taxAmount,deliveryCharge,totalPrices;
    String actionTitle="";

    // XML Objects
    Toolbar toolbar;
    ScrollView allContentLayout;
    TextView actionBarTitle;
    ImageView backIconImageView;
    TextView resNameTextView,restaurantAddressTextView,restaurantMobileTextView,orderTextView,paymentTypeTextView,deliveryTypeTextView,deliveryDate,deliveryTime,deliveryDateTextView,deliveryTimeTextView,redeemTitle,redeemAmountTextView,rewardTextView;
    TextView ordeStatusTextView,customerNameTextView,customerAddressTextView,customerMobileTextView;
    TextView subtotalTextView,taxPercentTextView,taxTextView,cardFeeText,cardFeeTextView,deliveryChargeTextView,tipTextView,totalTextView;
    TextView successMessageTextView,instructionInfo,instructionInfoTextview,paymentStatusTextView,customerEmailTextView,orderDateTextView,orderTimeTextView;
    TextView colanDeliveryDate,colanDeliveryTime,colanredeemtitle;

    RelativeLayout deliveryChargLayout,customerAddressLayout,tipsChargLayout,cardFeeLayout,offerChargLayout,voucherChargLayout,earnpointsLayout;
    TextView customerInfo,offerText,OfferTextView,voucherText,voucherTextView,orderTrack,failedReasonTextView,failedReasonText;
    ListView listViewMenuList;
    RelativeLayout customerInfoCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_view_screen);

        //Register id values
        toolbar                   = (Toolbar)findViewById(R.id.toolbar);
        actionBarTitle            = (TextView)findViewById(R.id.actionBarTitleTextView);
        backIconImageView         = (ImageView)findViewById(R.id.backIconImageView);

        allContentLayout          = (ScrollView)findViewById(R.id.allContentLayout);
        //Success message title
        successMessageTextView    = (TextView)findViewById(R.id.successMessageTextView);

        //order information
        resNameTextView           = (TextView)findViewById(R.id.restaurantNameTextView);
        restaurantAddressTextView = (TextView)findViewById(R.id.restaurantAddressTextView);
        restaurantMobileTextView  = (TextView)findViewById(R.id.restaurantMobileTextView);
        orderTextView             = (TextView)findViewById(R.id.orderNoTextView);
        paymentTypeTextView       = (TextView)findViewById(R.id.paymentTypeTextView);
        deliveryTypeTextView      = (TextView)findViewById(R.id.deliveryTypeTextView);
        deliveryDate              = (TextView)findViewById(R.id.deliveryDate);
        deliveryTime              = (TextView)findViewById(R.id.deliveryTime);
        orderDateTextView         = (TextView)findViewById(R.id.orderDateTextView);
        colanDeliveryDate         = (TextView)findViewById(R.id.colanDeliveryDate);
        orderTimeTextView         = (TextView)findViewById(R.id.orderTimeTextView);
        colanDeliveryTime         = (TextView)findViewById(R.id.colanDeliveryTime);
        deliveryDateTextView      = (TextView)findViewById(R.id.deliveryDateTextView);
        deliveryTimeTextView      = (TextView)findViewById(R.id.deliveryTimeTextView);
        ordeStatusTextView        = (TextView)findViewById(R.id.ordeStatusTextView);
        orderTrack                = (TextView)findViewById(R.id.orderTrack);
        redeemTitle               = (TextView)findViewById(R.id.redeemTitle);
        redeemAmountTextView      = (TextView)findViewById(R.id.redeemAmountTextView);
        rewardTextView                = (TextView)findViewById(R.id.rewardTextView);
        colanredeemtitle          = (TextView)findViewById(R.id.colanredeemtitle);
        failedReasonTextView          = (TextView)findViewById(R.id.failedReasonTextView);
        failedReasonText          = (TextView)findViewById(R.id.failedReasonText);


        //Customer information
        customerAddressLayout     = (RelativeLayout)findViewById(R.id.customerAddressLayout);
        cardFeeLayout             = (RelativeLayout)findViewById(R.id.cardFeeLayout);
        customerNameTextView      = (TextView)findViewById(R.id.customerNameTextView);
        customerAddressTextView   = (TextView)findViewById(R.id.customerAddressTextView);
        customerMobileTextView    = (TextView)findViewById(R.id.customerMobileNoTextView);
        customerEmailTextView     = (TextView)findViewById(R.id.customerEmailTextView);

        //order details View
        listViewMenuList          = (ListView)findViewById(R.id.orderMenuListListView);
        subtotalTextView          = (TextView)findViewById(R.id.subTotalTextView);
        taxPercentTextView        = (TextView)findViewById(R.id.taxPercentTextView);
        taxTextView               = (TextView)findViewById(R.id.taxAmountTextView);
        deliveryChargLayout       = (RelativeLayout)findViewById(R.id.deliveryChargLayout);
        deliveryChargeTextView    = (TextView)findViewById(R.id.deliveryChargeTextView);
        cardFeeText               = (TextView)findViewById(R.id.cardFeeText);
        cardFeeTextView           = (TextView)findViewById(R.id.cardFeeTextView);
        tipsChargLayout           = (RelativeLayout)findViewById(R.id.tipsChargLayout);
        tipTextView               = (TextView)findViewById(R.id.tipTextView);
        totalTextView             = (TextView)findViewById(R.id.grandTotalTextView);
        instructionInfoTextview       = (TextView)findViewById(R.id.instructionInfoTextview);
        instructionInfo       = (TextView)findViewById(R.id.instructionInfo);
        paymentStatusTextView     = (TextView)findViewById(R.id.paymentStatusTextView);

        offerChargLayout          = (RelativeLayout)findViewById(R.id.offerChargLayout);
        voucherChargLayout        = (RelativeLayout)findViewById(R.id.voucherChargLayout);
        earnpointsLayout        = (RelativeLayout)findViewById(R.id.earnpointsLayout);
        offerText                 = (TextView)findViewById(R.id.offerText);
        OfferTextView             = (TextView)findViewById(R.id.OfferTextView);
        voucherText               = (TextView)findViewById(R.id.voucherText);
        voucherTextView           = (TextView)findViewById(R.id.voucherTextView);

        customerInfoCardView           = (RelativeLayout) findViewById(R.id.customerInfoCardView);
        customerInfo             = (TextView)findViewById(R.id.customerInfo);

        //Initialize the objects
        utility              = Utility.getInstance(this);
        loginSession         = LoginSession.getInstance(this);
        serverRequestHandler = ServerRequest.getInstance(this);

        //showStatusBar
        utility.showStatusBar();

        //get intent values
        Intent intent=getIntent();

        if(intent != null) {

            orderId = intent.getStringExtra("orderid");
            Log.e("orderId","orderId"+orderId);
            actionTitle   = intent.getStringExtra("actionBarTitle");
            Log.e("actionTitle","actionTitle"+actionTitle);

            //set ActionBarTitle
            if (actionTitle.equalsIgnoreCase("CheckoutScreen")) {

                actionBarTitle.setText("Checkout Success");
                orderTrack.setVisibility(View.GONE);
                successMessageTextView.setVisibility(View.VISIBLE);

            } else {

                //Set Actionbar Title
                actionBarTitle.setText(getResources().getString(R.string.orderview));
                successMessageTextView.setVisibility(View.GONE);
                //orderTrack.setVisibility(View.VISIBLE);
            }

        }
        orderTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(OrderViewScreen.this,OrderRoute.class);
                intent1.putExtra("orderid",orderId);
                startActivity(intent1);
            }
        });

        //server call
        if (utility.isConnectingToInternet()) {

            Map<String, String> params = new HashMap<>();
            // POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id",loginSession.getUserId());
            params.put("page","OrderDetail");
            params.put("orderId", orderId);

            utility.showProgressDialog();

            serverRequestHandler.createRequest(this, params, RequestID.REQ_ORDER_VIEW);

        } else {

           noInternetAlertDialog();
        }

        //ActionBar Back Button Click Event
        actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               onBackPressed();
            }


        });

    }

    //server class listener event
    @Override
    public void onSuccess(Object result, RequestID requestId) {

        utility.hideProgressDialog();
        switch (requestId){

            case REQ_ORDER_VIEW:

                try{

                    OrderViewResponse orderViewResponse = (OrderViewResponse)result;

                    allContentLayout.setVisibility(View.VISIBLE);
                    //order information
                    successMessageTextView.setText(getResources().getString(R.string.thankUNote)+orderViewResponse.result.OrderDetail.restaurant.restaurant_name);
                    resNameTextView.setText(orderViewResponse.result.OrderDetail.restaurant.restaurant_name);
                    restaurantAddressTextView.setText(orderViewResponse.result.OrderDetail.restaurant.contact_address);
                    restaurantMobileTextView.setText(orderViewResponse.result.OrderDetail.restaurant.contact_phone);
                    orderTextView.setText(orderViewResponse.result.OrderDetail.order_number);

                    if(orderViewResponse.result.OrderDetail.reward_offer.equalsIgnoreCase("0")) {

                        redeemTitle.setVisibility(View.GONE);
                        redeemAmountTextView.setVisibility(View.GONE);
                        colanredeemtitle.setVisibility(View.GONE);
                    }else {
                        redeemTitle.setText(getResources().getString(R.string.RedeemAmount)+ orderViewResponse.result.OrderDetail.reward_offer_percentage + "%)");
                        redeemAmountTextView.setText("-"+loginSession.getCurrencyCode()+" "+orderViewResponse.result.OrderDetail.reward_offer);

                    }

                    try{
                        if(Double.parseDouble(orderViewResponse.result.OrderDetail.orderPoint) > 0){
                            earnpointsLayout.setVisibility(View.VISIBLE);
                        }else{
                            earnpointsLayout.setVisibility(View.GONE);
                        }
                    }catch (Exception e){}

                    rewardTextView.setText(getResources().getString(R.string.youEarned)+orderViewResponse.result.OrderDetail.orderPoint +getResources().getString(R.string.points));

//                    paymentTypeTextView.setText(orderViewResponse.result.OrderDetail.payment_method);

                    if (orderViewResponse.result.OrderDetail.payment_method.equalsIgnoreCase("stripe")) {

                        paymentTypeTextView.setText(orderViewResponse.result.OrderDetail.payment_method);

                    } else if (orderViewResponse.result.OrderDetail.payment_method.equalsIgnoreCase("cod")) {
                        paymentTypeTextView.setText(getResources().getString(R.string.cod));

                    } else if (orderViewResponse.result.OrderDetail.payment_method.equalsIgnoreCase("wallet")) {

                        paymentTypeTextView.setText(orderViewResponse.result.OrderDetail.payment_method);
                    }else if (orderViewResponse.result.OrderDetail.payment_method.equalsIgnoreCase("paypal")) {
                        paymentTypeTextView.setText(getResources().getString(R.string.paypal));
                    }

                    if(orderViewResponse.result.OrderDetail.assoonas.equalsIgnoreCase("Now")){

                        deliveryDate.setVisibility(View.GONE);
                        deliveryDateTextView.setVisibility(View.GONE);
                        colanDeliveryDate.setVisibility(View.GONE);

                        deliveryTime.setVisibility(View.GONE);
                        deliveryTimeTextView.setVisibility(View.GONE);
                        colanDeliveryTime.setVisibility(View.GONE);
                    }

                    if(orderViewResponse.result.OrderDetail.order_type.equalsIgnoreCase("Delivery")){
                        deliveryTypeTextView.setText(getResources().getString(R.string.delivery));
                        customerInfoCardView.setVisibility(View.VISIBLE);
                        customerInfo.setVisibility(View.VISIBLE);
                    }else{
                        customerInfoCardView.setVisibility(View.GONE);
                        customerInfo.setVisibility(View.GONE);
                        deliveryTypeTextView.setText(getResources().getString(R.string.Collection));
                    }


                    String splitDate[] = orderViewResponse.result.OrderDetail.delivery_date.split("T");

                    deliveryDateTextView.setText(splitDate[0]);
                    deliveryTimeTextView.setText(orderViewResponse.result.OrderDetail.delivery_time);


                    if(orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Collected")){

                        ordeStatusTextView.setText(getResources().getString(R.string.collected));
                        orderTrack.setVisibility(View.VISIBLE);
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);

                    }  else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Pending")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.pending));
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);

                    } else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Driver Accepted")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.driverAccepted));
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);

                    } else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Accepted")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.accepted));
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);
                    } else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Failed")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.failed));
                        failedReasonTextView.setVisibility(View.VISIBLE);
                        failedReasonText.setVisibility(View.VISIBLE);
                        failedReasonTextView.setText(orderViewResponse.result.OrderDetail.failed_reason);

                    } else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Waiting")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.waiting));
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);

                    }else if (orderViewResponse.result.OrderDetail.status.equalsIgnoreCase("Delivered")) {

                        ordeStatusTextView.setText(getResources().getString(R.string.delivered));
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);
                    }else{

                        ordeStatusTextView.setText(orderViewResponse.result.OrderDetail.status.toUpperCase());
                        failedReasonTextView.setVisibility(View.GONE);
                        failedReasonText.setVisibility(View.GONE);
                    }

                    if(orderViewResponse.result.OrderDetail.order_type.equalsIgnoreCase("Pickup")||orderViewResponse.result.OrderDetail.order_type.equalsIgnoreCase("Collection")){
                        deliveryDate.setText(getResources().getString(R.string.pickupDate));
                        deliveryTime.setText(getResources().getString(R.string.pickupTime));
                    }else{
                        deliveryDate.setText(getResources().getString(R.string.deliverydate));
                        deliveryTime.setText(getResources().getString(R.string.deliveryTime));
                    }


                    String returnDate = "";
                    try {
                        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
                        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date parsed = sourceFormat.parse(orderViewResponse.result.OrderDetail.created);
                        SimpleDateFormat destFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                        returnDate = destFormat.format(parsed);

                        Log.e("returnDate",returnDate);
                    }catch (Exception e){}

                  //  orderDateTextView.setText(cartItemsList1.getOrderDate());
                  //  orderTimeTextView.setText(cartItemsList1.getOrderTime());


                    //Paymrnt Status
                    if(orderViewResponse.result.OrderDetail.payment_status.equalsIgnoreCase("P")){

                        paymentStatusTextView.setText(getResources().getString(R.string.paid));

                    }else{

                        paymentStatusTextView.setText(getResources().getString(R.string.notpaid));
                    }

                    //customer information
                    customerNameTextView.setText(orderViewResponse.result.OrderDetail.customer_name);

                    if(orderViewResponse.result.OrderDetail.order_type.equalsIgnoreCase("Pickup")){

                        customerAddressLayout.setVisibility(RelativeLayout.GONE);

                    }else{

                        customerAddressTextView.setText(orderViewResponse.result.OrderDetail.flat_no+", "+orderViewResponse.result.OrderDetail.address);
                    }

                    customerMobileTextView.setText(orderViewResponse.result.OrderDetail.customer_phone);
                    try{
                        if(orderViewResponse.result.OrderDetail.customer_phone.isEmpty()){
                            customerMobileTextView.setText(getResources().getString(R.string.noData));
                        }else{
                            customerMobileTextView.setText(orderViewResponse.result.OrderDetail.customer_phone);
                        }
                    }catch (Exception e){e.printStackTrace();}


                    customerEmailTextView.setText(orderViewResponse.result.OrderDetail.customer_email);

                    //instruction show and hide

                   /* if(orderViewResponse.result.OrderDetail.order_description.isEmpty()){

                        instructionInfoTextview.setVisibility(TextView.GONE);
                        instructionInfo.setVisibility(TextView.GONE);

                    }else{

                        instructionInfoTextview.setVisibility(TextView.VISIBLE);
                        instructionInfo.setVisibility(TextView.VISIBLE);
                        instructionInfoTextview.setText(orderViewResponse.result.OrderDetail.order_description);
                    }
*/
                    //menu price information

                    taxPercentage   = orderViewResponse.result.OrderDetail.tax_percentage;
                    taxAmount       = orderViewResponse.result.OrderDetail.tax_amount;
                    deliveryCharge  = orderViewResponse.result.OrderDetail.delivery_charge;
                    totalPrices     = orderViewResponse.result.OrderDetail.order_grand_total;
                    deliveryType    = orderViewResponse.result.OrderDetail.order_type;

                    //Change string to double
                    double subtotal = Double.parseDouble(orderViewResponse.result.OrderDetail.order_sub_total);
                    Log.d("Converted values", "Converted values is : " + subtotal);
                    double taxpercent = Double.parseDouble(taxPercentage);
                    double deliverycharge = Double.parseDouble(deliveryCharge);
                    double totalprice = Double.parseDouble(totalPrices);
                    double taxamount = Double.parseDouble(taxAmount);

                    //menu price information
                    subtotalTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", subtotal));
                    taxPercentTextView.setText(getResources().getString(R.string.Tax) + String.format("%.2f", taxpercent) + "%)");
                    taxTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", taxamount));

                    if(Double.parseDouble(orderViewResponse.result.OrderDetail.cardfee_price) > 0){

                        //cardFeeText.setText("CardFee ( "+orderInfo.getCardper()+"%"+"+"+orderInfo.getAdminper()+"% )");
                        cardFeeText.setText(getResources().getString(R.string.cardFee));
                        cardFeeTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f",Double.parseDouble(orderViewResponse.result.OrderDetail.cardfee_price)));
                        cardFeeLayout.setVisibility(RelativeLayout.GONE);
                    }else{

                        cardFeeLayout.setVisibility(RelativeLayout.GONE);

                    }


                    if(Double.parseDouble(orderViewResponse.result.OrderDetail.offer_amount) > 0){

                        //cardFeeText.setText("CardFee ( "+orderInfo.getCardper()+"%"+"+"+orderInfo.getAdminper()+"% )");

                        if(Double.parseDouble(orderViewResponse.result.OrderDetail.offer_percentage) > 0){

                            offerText.setText(getResources().getString(R.string.Offer)+orderViewResponse.result.OrderDetail.offer_percentage+" %)");
                        }else{

                            offerText.setText(getResources().getString(R.string.offer));
                        }

                        OfferTextView.setText("-"+loginSession.getCurrencyCode()+" "+ String.format("%.2f",Double.parseDouble(orderViewResponse.result.OrderDetail.offer_amount)));
                        offerChargLayout.setVisibility(RelativeLayout.VISIBLE);

                    }else{

                        offerChargLayout.setVisibility(RelativeLayout.GONE);

                    }

                    if (Double.parseDouble(orderViewResponse.result.OrderDetail.voucher_amount) > 0) {

                        //cardFeeText.setText("CardFee ( "+orderInfo.getCardper()+"%"+"+"+orderInfo.getAdminper()+"% )");
                        if (Double.parseDouble(orderViewResponse.result.OrderDetail.voucher_percentage) > 0) {
                            voucherText.setText(getResources().getString(R.string.Voucher) + orderViewResponse.result.OrderDetail.voucher_percentage + " % )");
                        } else {
                            voucherText.setText(getResources().getString(R.string.voucher));
                        }
                        voucherTextView.setText(loginSession.getCurrencyCode() + " " + String.format("%.2f", Double.parseDouble(orderViewResponse.result.OrderDetail.voucher_amount)));
                        voucherChargLayout.setVisibility(RelativeLayout.VISIBLE);

                    } else {

                        if(!orderViewResponse.result.OrderDetail.voucher_code.isEmpty() && !orderViewResponse.result.OrderDetail.voucher_code.equals("")){
                            if (orderViewResponse.result.OrderDetail.voucher_amount.equals("0") &&
                                    orderViewResponse.result.OrderDetail.voucher_percentage.equals("0")) {
                                voucherText.setText(getResources().getString(R.string.voucher));
                                voucherTextView.setText(getResources().getString(R.string.freeDelivery));
                                voucherChargLayout.setVisibility(RelativeLayout.VISIBLE);
                            }

                        }else{
                            voucherChargLayout.setVisibility(RelativeLayout.GONE);
                        }

                    }

                    //deliverycharge Layout Show and Hide
                    if (Double.parseDouble(orderViewResponse.result.OrderDetail.tip_amount) > 0)
                    {
                        tipsChargLayout.setVisibility(RelativeLayout.VISIBLE);
                        tipTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", Double.parseDouble(orderViewResponse.result.OrderDetail.tax_amount)));
                    }else{

                        tipsChargLayout.setVisibility(RelativeLayout.GONE);
                    }

                    //deliverycharge Layout Show and Hide
                    if(deliverycharge > 0){
                        deliveryChargeTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", deliverycharge));
                    }else{
                        deliveryChargeTextView.setText(getResources().getString(R.string.free));
                    }
                    if (deliveryType.equalsIgnoreCase("Pickup"))
                    {
                        deliveryChargLayout.setVisibility(View.GONE);
                    }else{
                        deliveryChargLayout.setVisibility(View.VISIBLE);
                    }

                    totalTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", totalprice));

                    orderViewListAdapter = new OrderViewListAdapter(OrderViewScreen.this, orderViewResponse.result.OrderDetail.carts, this);
                    listViewMenuList.setAdapter(orderViewListAdapter);
                    Utility.setListViewHeightBasedOnChildren(listViewMenuList);

                }catch(Exception e){

                    e.printStackTrace();
                }

                break;
        }

        restaurantMobileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ((TextView) view).getText().toString().trim()));
                startActivity(callIntent );
            }
        });

        customerMobileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + ((TextView) view).getText().toString().trim()));
                startActivity(callIntent );
            }
        });


    }

    @Override
    public void onFailure(String error, RequestID requestId) {

        utility.hideProgressDialog();

        switch (requestId){

            case REQ_ORDER_VIEW:

                utility.toast(error);

                break;
        }

    }

    //On Backpressed
    @Override
    public void onBackPressed() {

        if (actionTitle.equalsIgnoreCase("CheckoutScreen"))
        {
            utility.APP_START_FRAGMENT = "searchButton";
            utility.CURRENT_SCREEN = "";
            Intent intent=new Intent(OrderViewScreen.this,NewBaseHomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            finish();
        }
    }

    public class OrderViewListAdapter extends BaseAdapter {

        Activity activity;
        List<OrderViewResponse.Carts> cartItems;
         OrderViewScreen orderView;


        public OrderViewListAdapter(Activity activity, List<OrderViewResponse.Carts> cartItems, OrderViewScreen fragment) {
            this.activity = activity;
            this.cartItems = cartItems;
            this.orderView=fragment;
        }

        @Override
        public int getCount() {
            return cartItems.size();
        }

        @Override
        public Object getItem(int i) {
            return cartItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (view == null)

                view = inflater.inflate(R.layout.custom_ordermenu_details, null);

            TextView item = (TextView) view.findViewById(R.id.menuNameTextView);

            //String menusizename = cartItems.get(position).getMenuSize();

            item.setText(Html.fromHtml(cartItems.get(position).menu_name)+ " [" + "(" + loginSession.getCurrencyCode() +" "+ cartItems.get(position).menu_price + ")]");

       /* if (menusizename.isEmpty())
        {
            item.setText(Html.fromHtml(cartItems.get(position).getMenuName()));
        }
        else
        {
            item.setText(Html.fromHtml(cartItems.get(position).getMenuName())+ " [" + "(" + activity.getResources().getString(R.string.CurrencySymbol) +" "+ cartItems.get(position).getMenuPrice() + ")]");
        }*/

            TextView sub_add = (TextView) view.findViewById(R.id.subAddonTextView);
            sub_add.setText(Html.fromHtml(cartItems.get(position).subaddons_name));

            TextView pricetv = (TextView) view.findViewById(R.id.menuPriceTextView);

            pricetv.setText(loginSession.getCurrencyCode()+" " + String.format("%.2f",Double.parseDouble(cartItems.get(position).menu_price)));

        /*if(cartItems.get(position).getAddonPrice().equalsIgnoreCase("0.00"))
        {

        }
        else
        {

            pricetv.setText(activity.getResources().getString(R.string.CurrencySymbol)+" " + cartItems.get(position).getMenuPrice()+"("+ cartItems.get(position).getAddonPrice()+")");
        }
*/

            TextView qty = (TextView) view.findViewById(R.id.menuQuantityTextView);
            qty.setText(cartItems.get(position).quantity);

            TextView totaltv = (TextView) view.findViewById(R.id.totalPriceTextView);
            totaltv.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f",Double.parseDouble(cartItems.get(position).total_price)));

            TextView instructionTextView = (TextView) view.findViewById(R.id.instructionTextView);

            if(!cartItems.get(position).menu_description.isEmpty()){

                instructionTextView.setVisibility(View.VISIBLE);
                instructionTextView.setText(cartItems.get(position).menu_description);
            }

            return view;
        }
    }

}
