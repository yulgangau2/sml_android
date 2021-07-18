package com.eatyhero.customer.baskettab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.AddressListCheckout;
import com.eatyhero.customer.model.OrderSuccess;
import com.eatyhero.customer.model.PlaceOrderData;
import com.eatyhero.customer.model.StripeCardList;
import com.eatyhero.customer.model.WalletHistoryList;
import com.eatyhero.customer.moretab.AddAddressScreen;
import com.eatyhero.customer.moretab.AddCardScreen;
import com.eatyhero.customer.moretab.AddMonyToWalletScreen;
import com.eatyhero.customer.restauranttab.OrderViewScreen;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by admin on 23-03-2017.
 */

public class CheckoutScreen extends BaseActivity implements ServerListener {

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequestHandler;
    DatabaseManager databaseManager;
    SharedPreferences preferences;
    PlaceOrderData placeOrderData;

    //Create Xml objects without static
    Toolbar toolbar;
    Button proceedButton, paymentCheckoutButton;
    EditText cardholderNameEditText, cardnumberEditText, postalCodeEditText, cvvEditText;
    RadioGroup cardSelectRadioGroup;
    RadioButton newCardRadioButton, savedCardRadioButton;
    LinearLayout newcardLayout;
    AutoCompleteTextView cardAddressEditText;
    CheckBox savedCardsCheckBox;
    Spinner monthSpinner, yearSpinner;
    TextView codPaymentBanner,paypalLayoutBanner,creditCardBanner;
    Dialog changeNumberDialog;
    String phoneNumber="";

    //view pager for card
    ViewPager viewPager;

    //Create Xml objects with static
    LinearLayout addressInfoDelivery,addButtonCardlayout,addAddressButtonLayou,addMoneyToWalletLayout;
    ImageView addAddressButton, addButtonCard,addMoneyToWallet, backIcon;
    ListView addressListView, cardListView,outofAreaAddressListView;
    EditText dateEdittext;
    TextView addressTagTV,errorText,totalBalanceTextView, walletAmountTextView;
    Spinner timeSelectSpinner;
    RadioGroup deliverytimeRadioGroup;
    RadioButton asapRadioButton, laterRadioButton;
    RelativeLayout savedCardLayout, datetimeLayout,creditCardAddLayout;
   // RadioGroup radioGroup;
    RadioButton codButton,paypalButton;
    CheckBox walletButton;
    RelativeLayout codLayout,walletLayout,paypalLayout,loadMoneyLayout;

    //Create String objects with static
    String getDate = "";
    String getFoodAsSoonAs = "Now";
    String restaurantID;
    String customerSelectedAddressID = "";
    String walletPriceAmount = "0.00";
    String tips = "0.00";
    String ORIGINAL_TOTAL = "";

    //create boolean objects with static
    Boolean dateTimeCheck = false, dateEditTextclicked = false;//false

    //create strings objects with static
    String getTime = "";
    String systemDate = "";
    String systemMonth = "";
    String systemYear = "";
    String curTime, localTime;

    //create int and double objects
    int cartCount;
    int dates, month, year;
    int selected_position = -1,addressSelected_position;

    //initialize list
    List<String> resOpeningTimes = new ArrayList<String>();
    ArrayList<StripeCardList.CardDetails> stripeCardDetailList = new ArrayList<>();

    //Paypalobjetcs
    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_CLIENT_ID = "AbescpvrHO2wsxZx_kgf7CQDXeAadmvH8i3XSW7VZ705CvumI3NeCmURiukRAY9UIhFTzAzAvNvBy4Da";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    StripeCardListAdapter stripeCardListAdapter;
    AddressListAdapter addressListAdapter;
    OutOffAddressListAdapter outOffAddressListAdapter;

    int REQ_ADD_STRIPE_CARD = 2;
    String ADDRESS_SELECT_OPTION = "DONT_GET",GET_WALLET_BALANCE="DONT_GET";

    String SELECT_PAYMENT="YES";
    String CARD_ID="",PAID_FULL="NO";
    String getWallBanalanceOnActivityResult = "NO";

    double remaining;
    boolean walletChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_checkout_screen);
        //showStatusBar
        showStatusBar();

        //Initialize the xml toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Initialize the xml imageView
        backIcon           = (ImageView) toolbar.findViewById(R.id.backIcon);
        addAddressButton   = (ImageView) findViewById(R.id.addAddressButton);
        addButtonCard      = (ImageView) findViewById(R.id.addButtonCard);

        //Initialize the xml TextView
        totalBalanceTextView    = (TextView) toolbar.findViewById(R.id.totalBalanceTextView);
        walletAmountTextView    = (TextView) findViewById(R.id.walletAmountTextView);
        errorText               = (TextView) findViewById(R.id.errorText);
        addressTagTV            = (TextView) findViewById(R.id.addressTagTV);

        //Initialize the xml Button
        proceedButton         = (Button) findViewById(R.id.proceedButton);
        paymentCheckoutButton = (Button) findViewById(R.id.paymentCheckoutButton);

        //Initialize the xml Linear Layout
        addMoneyToWalletLayout    = (LinearLayout) findViewById(R.id.addMoneyToWalletLayout);
        addMoneyToWallet          = (ImageView) findViewById(R.id.addMoneyToWallet);

        addressInfoDelivery       = (LinearLayout) findViewById(R.id.addressInfoDelivery);
        newcardLayout             = (LinearLayout) findViewById(R.id.newcardLayout);
        addButtonCardlayout       = (LinearLayout) findViewById(R.id.addButtonCardlayout);
        addAddressButtonLayou       = (LinearLayout) findViewById(R.id.addAddressButtonLayou);

        //Initialize the xml ListView
        addressListView = (ListView) findViewById(R.id.addressListView);
        outofAreaAddressListView = (ListView) findViewById(R.id.outofAreaAddressListView);
        cardListView    = (ListView) findViewById(R.id.cardListView);

        //Initialize the xml EditText
        dateEdittext           = (EditText) findViewById(R.id.dateEdittext);
        cardholderNameEditText = (EditText) findViewById(R.id.cardholderNameEditText);
        cardnumberEditText     = (EditText) findViewById(R.id.cardnumberEditText);
        cvvEditText            = (EditText) findViewById(R.id.cvvEditText);
        postalCodeEditText     = (EditText) findViewById(R.id.postalCodeEditText);

        //Initialize the xml Spinner
        timeSelectSpinner = (Spinner) findViewById(R.id.timeSelectSpinner);
        monthSpinner      = (Spinner) findViewById(R.id.monthSpinner);
        yearSpinner       = (Spinner) findViewById(R.id.yearSpinner);

        //Initialize the xml RadioGroup/RadioButton
        deliverytimeRadioGroup = (RadioGroup) findViewById(R.id.deliverytimeRadioGroup);
        asapRadioButton        = (RadioButton) findViewById(R.id.asapRadioButton);
        laterRadioButton       = (RadioButton) findViewById(R.id.laterRadioButton);
        codButton              = (RadioButton) findViewById(R.id.codButton);
        walletButton           = (CheckBox) findViewById(R.id.walletButton);
        paypalButton           = (RadioButton) findViewById(R.id.paypalButton);
        cardSelectRadioGroup   = (RadioGroup) findViewById(R.id.cardSelectRadioGroup);
        newCardRadioButton     = (RadioButton) findViewById(R.id.newCardRadioButton);
        savedCardRadioButton   = (RadioButton) findViewById(R.id.savedCardRadioButton);

        //Initialize the xml CheckBox
        savedCardsCheckBox = (CheckBox) findViewById(R.id.savedCardsCheckBox);

        //Initialize the xml RelativeLayout
        datetimeLayout     = (RelativeLayout) findViewById(R.id.datetimeLayout);
        savedCardLayout    = (RelativeLayout) findViewById(R.id.savedCardLayout);
        loadMoneyLayout    = (RelativeLayout) findViewById(R.id.loadMoneyLayout);
        creditCardAddLayout    = (RelativeLayout) findViewById(R.id.creditCardAddLayout);

        codLayout = (RelativeLayout)findViewById(R.id.codLayout);
        walletLayout = (RelativeLayout)findViewById(R.id.walletLayout);
        paypalLayout = (RelativeLayout)findViewById(R.id.paypalLayout);

        //Initialize the xml Viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Initialize the xml AutoCompleteTextView
        cardAddressEditText = (AutoCompleteTextView) findViewById(R.id.cardAddressEditText);

        codPaymentBanner   = (TextView) findViewById(R.id.codPaymentBanner);
        paypalLayoutBanner = (TextView) findViewById(R.id.paypalLayoutBanner);
        creditCardBanner   = (TextView) findViewById(R.id.creditCardBanner);

        //Initialize the objects
        loginSession         = LoginSession.getInstance(this);
        serverRequestHandler = ServerRequest.getInstance(this);
        placeOrderData       = new PlaceOrderData();
        databaseManager      = DatabaseManager.getInstance(this);

        getWallBanalanceOnActivityResult = "NO";

        Intent intent = getIntent();
        if(intent!=null){
            placeOrderData = (PlaceOrderData) intent.getSerializableExtra("placeOrderData");
        }

        if (placeOrderData != null) {

            totalBalanceTextView.setText(String.format("%.2f", Double.parseDouble(placeOrderData.getGrandTotal()))+ " " + loginSession.getCurrencyCode());

        }


        //initent to get paypal service
        Intent paypalintent = new Intent(this, PayPalService.class);
        paypalintent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(paypalintent);

        //Get Preference value
        preferences = this.getSharedPreferences("restaurant", MODE_PRIVATE);
        restaurantID = preferences.getString("resid", "");

        ORIGINAL_TOTAL = placeOrderData.getGrandTotal();
        //get current date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setLenient(false);
        Date curDate = new Date();
        curTime = formatter.format(curDate);
        long millis = curDate.getTime();
        getDate = curTime;

        //check pickup or delivery to visible and invisible the addressInfo
        if (placeOrderData.getOrderType().equalsIgnoreCase("Pickup")) {
            addressInfoDelivery.setVisibility(View.GONE);
            GET_WALLET_BALANCE = "GET_IT";
            GetTimemethod();
        } else {
            addressInfoDelivery.setVisibility(View.VISIBLE);
            ADDRESS_SELECT_OPTION = "GET_IT";
            getAddressList();
        }


        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginSession.getUserPhone().isEmpty()&&phoneNumber.isEmpty()){

                    changeNumberDialog = new Dialog(CheckoutScreen.this);
                    changeNumberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    changeNumberDialog.setContentView(R.layout.add_number_screen);
                    changeNumberDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

                    //email edit text
                    final EditText numberEditText      = (EditText) changeNumberDialog.findViewById(R.id.numberEditText);
                    Button submit            = (Button) changeNumberDialog.findViewById(R.id.submitButton);
                    ImageView closeImageView = (ImageView) changeNumberDialog.findViewById(R.id.closeImageView);

                    //submit button onclick
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                           String newNumber = numberEditText.getText().toString();

//                            /Validation Part
                            if (newNumber.isEmpty()) {

                                numberEditText.setError(getResources().getString(R.string.pleaseEnterPhoneNumber));

                            } else{

                                //Server request listener
                                phoneNumber = newNumber;
                                loginSession.saveUserPhone(phoneNumber);

                                changeNumberDialog.dismiss();
                            }

                        }
                    });
                    changeNumberDialog.show();


                    closeImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            changeNumberDialog.dismiss();

                        }
                    });

                }else {

                    if (phoneNumber.isEmpty()){
                        phoneNumber= loginSession.getUserPhone();
                    }

                    if (laterRadioButton.isChecked()) {
                        try {
                            if (timeSelectSpinner.isShown()) {
                                getTime = timeSelectSpinner.getSelectedItem().toString();
                            }

                        } catch (NullPointerException e) {

                    }

                    if (dateEdittext.getText().toString().isEmpty()) {

                        toast(getResources().getString(R.string.enterDate));

                    } else if (timeSelectSpinner.getSelectedItem().toString().isEmpty()) {

                        toast(getResources().getString(R.string.selecttime));

                    } else if (timeSelectSpinner.getSelectedItem().toString().equalsIgnoreCase("closed")) {

                        toast(getResources().getString(R.string.selectFuture));

                    } else {

                        if(placeOrderData.getOrderType().equalsIgnoreCase("Pickup")){

                            OrderMethod();

                        } else if(customerSelectedAddressID.equals("")){

                            toast(getResources().getString(R.string.selectYourAddress));

                        }else{

                            OrderMethod();
                        }
                    }

                } else {

                        if(placeOrderData.getOrderType().equalsIgnoreCase("Pickup")){

                            OrderMethod();

                        } else if(customerSelectedAddressID.equals("")){

                            toast(getResources().getString(R.string.selectYourAddress));

                        }else{

                            OrderMethod();
                        }

                }
                }


            }
        });

        //ActionVar Back Button CLick Event
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        //split system date
        String splitdate = curTime;
        String[] systemDates = splitdate.split("-");
        systemDate = systemDates[0];
        systemMonth = systemDates[1];
        systemYear = systemDates[2];
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        localTime = date.format(currentLocalTime);


        codLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codButton.performClick();
            }
        });

        codButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(SELECT_PAYMENT.equalsIgnoreCase("YES")) {
                    if (isChecked) {
                        codButton.setChecked(true);
                        paypalButton.setChecked(false);

                        selected_position=-1;
                        CARD_ID = "";
                        try {

                            selected_position = -1;
                            stripeCardListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    codButton.setChecked(false);
                    paypalButton.setChecked(false);

                    selected_position=-1;
                    CARD_ID = "";
                    try{

                        selected_position=-1;
                        stripeCardListAdapter.notifyDataSetChanged();}catch (Exception e){e.printStackTrace();}
                }
            }
        });

        paypalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (Double.parseDouble(placeOrderData.getGrandTotal())>0){
                   paypalButton.performClick();
               } else {
                   toast(getResources().getString(R.string.anotherPaymentType));
               }


            }
        });

        paypalButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Double.parseDouble(placeOrderData.getGrandTotal()) > 0) {
                    if (SELECT_PAYMENT.equalsIgnoreCase("YES")) {
                        if (isChecked) {
                            codButton.setChecked(false);
                            paypalButton.setChecked(true);

                            selected_position = -1;
                            CARD_ID = "";
                            try {
                                selected_position = -1;
                                stripeCardListAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        codButton.setChecked(false);
                        paypalButton.setChecked(false);

                        selected_position = -1;
                        CARD_ID = "";
                        try {
                            selected_position = -1;
                            stripeCardListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    paypalButton.setChecked(false);
                    toast(getResources().getString(R.string.anotherPaymentType));
                }
            }
        });

        walletButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    walletChecked = true;
                    if(Double.parseDouble(walletPriceAmount) > 0){

                        double totalAmount = Double.parseDouble(placeOrderData.getGrandTotal());
                        double walletAmount = Double.parseDouble(walletPriceAmount);

                        if(walletAmount > totalAmount){

                            remaining = walletAmount - totalAmount;
                            walletAmountTextView.setText(String.format("%.2f",remaining)+" "+loginSession.getCurrencyCode());
                            SELECT_PAYMENT = "NO";
                            proceedButton.setText(getResources().getString(R.string.Pay)+ " 0.00"+ loginSession.getCurrencyCode()+")");
                            PAID_FULL = "YES";

                            codButton.setChecked(false);
                            paypalButton.setChecked(false);
                            selected_position=-1;
                            CARD_ID = "";
                            try {

                                selected_position = -1;
                                stripeCardListAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else{

                            remaining = totalAmount - walletAmount;
                            walletAmountTextView.setText("0.00 "+ loginSession.getCurrencyCode());
                            proceedButton.setText("PAY ( "+String.format("%.2f",remaining)+ loginSession.getCurrencyCode() + " )");
                            SELECT_PAYMENT = "YES";
                            PAID_FULL = "NO";
                        }


                    }else{
                        toast(getResources().getString(R.string.insufficientWalletAmount));
                        SELECT_PAYMENT = "YES";
                        walletButton.setChecked(false);
                        PAID_FULL = "NO";
                    }

                }else{
                    walletChecked = false;
                    proceedButton.setText(getResources().getString(R.string.Proceed));
                    SELECT_PAYMENT = "YES";
                    walletAmountTextView.setText(String.format("%.2f",Double.parseDouble(walletPriceAmount))+" "+loginSession.getCurrencyCode());
                    PAID_FULL = "NO";
               }

            }
        });


        //order is pre order
        Log.e("Stats",loginSession.getStatus());
        if (loginSession.getStatus().equalsIgnoreCase("Closed") || loginSession.getStatus().equalsIgnoreCase("PreOrder")) {

            asapRadioButton.setVisibility(View.GONE);
            laterRadioButton.setChecked(true);
            getFoodAsSoonAs = "Later";
            datetimeLayout.setVisibility(View.VISIBLE);
            dateEdittext.setVisibility(View.VISIBLE);
            timeSelectSpinner.setVisibility(View.VISIBLE);
        }else{
            asapRadioButton.setVisibility(View.VISIBLE);
        }

        //DatePicker  Button
        dateEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutScreen.this, new datePickerDialog(), year, month, dates);
                DatePicker datePicker = datePickerDialog.getDatePicker();
                Calendar calendar = Calendar.getInstance();
                datePicker.setMinDate(calendar.getTimeInMillis() - 1000);
                calendar.add(Calendar.DATE, 15);
                datePickerDialog.show();
                dateEditTextclicked = true;
            }
        });


        dateEdittext.setText(getDate);
        //Date Edit Text Text watcher
        dateEdittext.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
                getDate = dateEdittext.getText().toString();
                if (dateEditTextclicked) {

                    Log.e("dateEditTextclicked", "true");
                    if (!(getDate.equals(""))) {
                        if (checkInternet()) {
                            Map<String, String> param = new HashMap<>();
                            // POST parameters:
                            param.put("date", getDate);
                            param.put("resid", restaurantID);
                            param.put("action", "restaurantTiming");
                            showProgressDialog();
                            GET_WALLET_BALANCE = "DONT_GET";
                            serverRequestHandler.createRequest(CheckoutScreen.this, param, RequestID.REQ_TIME);
                        } else {
                            noInternetAlertDialog();
                        }
                    }
                } else {

                }
            }
        });


        getDate = dateEdittext.getText().toString();

        deliverytimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {

                    case R.id.asapRadioButton:
                        datetimeLayout.setVisibility(View.GONE);
                        dateEdittext.setVisibility(View.GONE);
                        timeSelectSpinner.setVisibility(View.GONE);
                        dateTimeCheck = true;
                        getFoodAsSoonAs = "Now";
                        break;

                    case R.id.laterRadioButton:
                        datetimeLayout.setVisibility(View.VISIBLE);
                        dateEdittext.setVisibility(View.VISIBLE);
                        timeSelectSpinner.setVisibility(View.VISIBLE);
                        dateTimeCheck = false;
                        getFoodAsSoonAs = "Later";
                        break;
                }
            }
        });

        addAddressButtonLayou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddressButton.performClick();
            }
        });
        //Add Address Button Clivk event
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(CheckoutScreen.this, AddAddressScreen.class);
                i.putExtra("addressId", "");
                i.putExtra("screen", "Add Address");
                i.putExtra("title", "");
                i.putExtra("phone", "");
                i.putExtra("address", "");
                startActivityForResult(i, 1);
            }
        });

        addButtonCardlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButtonCard.performClick();
            }
        });
        //for add cards
        addButtonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stripeCardDetailList.size() >=3) {
                    showAlertDialog(getResources().getString(R.string.limitIs3));
                } else {
                    Intent addCard = new Intent(CheckoutScreen.this, AddCardScreen.class);
                    startActivityForResult(addCard, REQ_ADD_STRIPE_CARD);
                }
            }
        });

        loadMoneyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addCard = new Intent(CheckoutScreen.this, AddMonyToWalletScreen.class);
                startActivityForResult(addCard, 3);
            }
        });


        //Payment Set options

        if(loginSession.getCodPayment().equalsIgnoreCase("YES")||loginSession.getCodPayment().equalsIgnoreCase("Y")){
            codPaymentBanner.setVisibility(View.VISIBLE);
            codLayout.setVisibility(View.VISIBLE);
            Log.e("cod","s");
        }else{
            codPaymentBanner.setVisibility(View.GONE);
            codLayout.setVisibility(View.GONE);
            Log.e("cod","no");
        }

        if(loginSession.getStripePayment().equalsIgnoreCase("YES")||loginSession.getStripePayment().equalsIgnoreCase("Y")){
            creditCardBanner.setVisibility(View.VISIBLE);
            cardListView.setVisibility(View.VISIBLE);
            creditCardAddLayout.setVisibility(View.VISIBLE);
            Log.e("strip","s");
        }else{
            creditCardBanner.setVisibility(View.GONE);
            cardListView.setVisibility(View.GONE);
            creditCardAddLayout.setVisibility(View.GONE);
            Log.e("strip","no");
        }

        if(loginSession.getPayPalPayment().equalsIgnoreCase("YES")||loginSession.getPayPalPayment().equalsIgnoreCase("Y")){
            paypalLayoutBanner.setVisibility(View.VISIBLE);
            paypalLayout.setVisibility(View.VISIBLE);
            Log.e("paypal","s");
        }else{
            paypalLayoutBanner.setVisibility(View.GONE);
            paypalLayout.setVisibility(View.GONE);
            Log.e("paypal","no");
        }

    }

    //paypal payment intent
    private PayPalPayment getThingToBuy(String paymentIntent) {
        double totalToPayPal;
        if (walletChecked){
            Log.e("paypalAmount", String.valueOf(remaining));
            totalToPayPal = remaining;
        }else {
            totalToPayPal = Double.parseDouble(placeOrderData.getGrandTotal());
        }
        Log.e("paypalAmount", String.valueOf(totalToPayPal));
        return new PayPalPayment(new BigDecimal(totalToPayPal),"THB", "Order Total",
                paymentIntent);//payedAmount
    }


    private void OrderMethod() {


        if(walletButton.isChecked()){

            if(PAID_FULL.equalsIgnoreCase("YES")){

                placeOrderMethod("cod", "0", "");

            }else{

                if (codButton.isChecked()) {

                    placeOrderMethod("cod", "0", "");

                } else if (!CARD_ID.isEmpty()) {

                    placeOrderMethod("stripe", tips, "");

                } else if (paypalButton.isChecked()) {

                    PayPalPayment thingToBuy = getThingToBuy(com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(CheckoutScreen.this, PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);

                } else {

                    toast(getResources().getString(R.string.selectAnyOnePayment));
                }

            }

        }else if (codButton.isChecked()) {

            placeOrderMethod("cod","0","");

        } else if (!CARD_ID.isEmpty()) {

            placeOrderMethod("stripe",tips,"");

        } else if(paypalButton.isChecked()){

            PayPalPayment thingToBuy = getThingToBuy(com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(CheckoutScreen.this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);

        }else{

            toast(getResources().getString(R.string.selectAnyOnePayment));
        }
    }

    public void getAddressList() {
        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // POST parameters:
            params.put("customer_id", loginSession.getUserId());
            params.put("action", "addressBookList");
            params.put("resid", placeOrderData.getRestaurantID());
            showProgressDialog();
            serverRequestHandler.createRequest(CheckoutScreen.this, params, RequestID.REQ_ADDRESSLIST_CHECKOUT);
        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID) {

            case REQ_ADDRESSLIST_CHECKOUT:

                try {

                    AddressListCheckout addressList = (AddressListCheckout) result;
                    addressListAdapter = new AddressListAdapter(this, addressList.result.addressList);
                    addressListView.setAdapter(addressListAdapter);
                    setListViewHeightBasedOnChildren(addressListView);

                    if(addressList.result.addressList.size() > 0){

                        customerSelectedAddressID = addressList.result.addressList.get(0).id;

                        Log.e("FEEEEEEE",loginSession.getFreeDelivery());

                        Log.e("getVoucherCodeStatus",placeOrderData.getVoucherCodeStatus());
                        Log.e("getVoucherCodePercent",placeOrderData.getVoucherCodePercent());
                        Log.e("getVoucherCodePrice",placeOrderData.getVoucherCodePrice());

                        if(!placeOrderData.getVoucherCodeStatus().isEmpty()){

                            if(placeOrderData.getVoucherCodePercent().equals("0") && placeOrderData.getVoucherCodePrice().equals("0")){

                                addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                placeOrderData.setDeliveryCharge("0");

                            }else{

                                if(Double.parseDouble(loginSession.getFreeDelivery())>0 && Double.parseDouble(databaseManager.getSubTotal()) >= Double.parseDouble(loginSession.getFreeDelivery())){

                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                    placeOrderData.setDeliveryCharge("0");

                                }else{

                                    placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                    double DeliveryCharge = Double.parseDouble(addressList.result.addressList.get(0).deliveryCharge);
                                    double addDelivery = Double.parseDouble(ORIGINAL_TOTAL) + DeliveryCharge;

                                    if (DeliveryCharge > 0) {

                                        Log.e("del chg", String.valueOf(DeliveryCharge));
                                        placeOrderData.setDeliveryCharge(String.valueOf(DeliveryCharge));
                                        placeOrderData.setGrandTotal(String.valueOf(addDelivery));
                                        if (addDelivery >=0) {
                                            totalBalanceTextView.setText(String.format("%.2f", addDelivery)+" "+loginSession.getCurrencyCode());
                                        } else {
                                            totalBalanceTextView.setText("0.00 "+loginSession.getCurrencyCode());
                                        }
                                        addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+String.format("%.2f", DeliveryCharge)+" "+loginSession.getCurrencyCode()+" )");
                                    } else {

                                        placeOrderData.setDeliveryCharge("0.0");
                                        placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                        if (Double.parseDouble(placeOrderData.getGrandTotal()) >= 0) {

                                            totalBalanceTextView.setText(String.format("%.2f", Double.parseDouble(placeOrderData.getGrandTotal()))+" "+loginSession.getCurrencyCode() );
                                        } else {
                                            totalBalanceTextView.setText("0.00 "+loginSession.getCurrencyCode());
                                        }
                                        addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                    }

                                }
                            }

                        }else{

                            if(Double.parseDouble(loginSession.getFreeDelivery())>0 && Double.parseDouble(databaseManager.getSubTotal()) >= Double.parseDouble(loginSession.getFreeDelivery())){

                                addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                placeOrderData.setDeliveryCharge("0");

                            }else{

                                placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                double DeliveryCharge = Double.parseDouble(addressList.result.addressList.get(0).deliveryCharge);
                                double addDelivery = Double.parseDouble(ORIGINAL_TOTAL) + DeliveryCharge;

                                if (DeliveryCharge > 0) {

                                    placeOrderData.setDeliveryCharge(String.valueOf(DeliveryCharge));
                                    placeOrderData.setGrandTotal(String.valueOf(addDelivery));
                                    if (addDelivery >= 0) {
                                        totalBalanceTextView.setText( String.format("%.2f", addDelivery)+" "+loginSession.getCurrencyCode());
                                    } else {
                                        totalBalanceTextView.setText("0.00 "+loginSession.getCurrencyCode());
                                    }
                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+String.format("%.2f", DeliveryCharge)+" "+loginSession.getCurrencyCode() +" )");
                                } else {

                                    placeOrderData.setDeliveryCharge("0.0");
                                    placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                    if (Double.parseDouble(placeOrderData.getGrandTotal()) >= 0) {

                                        totalBalanceTextView.setText(String.format("%.2f", Double.parseDouble(placeOrderData.getGrandTotal()))+" "+loginSession.getCurrencyCode());
                                    } else {
                                        totalBalanceTextView.setText("0.00 "+loginSession.getCurrencyCode());
                                    }
                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                }

                            }
                        }

                    }

                    if(ADDRESS_SELECT_OPTION.equalsIgnoreCase("GET_IT")){
                        GET_WALLET_BALANCE = "GET_IT";
                        GetTimemethod();
                    }

                    if(!addressList.result.outOfDelivery.isEmpty()){
                        outOffAddressListAdapter = new OutOffAddressListAdapter(this, addressList.result.outOfDelivery);
                        outofAreaAddressListView.setAdapter(outOffAddressListAdapter);
                        setListViewHeightBasedOnChildren(outofAreaAddressListView);
                        outofAreaAddressListView.setVisibility(View.VISIBLE);
                    }else{
                        outofAreaAddressListView.setVisibility(View.GONE);
                    }

                }catch (Exception e){e.printStackTrace();}

                break;

            case REQ_TIME:

                Log.e("Get REQ_DATE Response", "" + result.toString());

                String restaurantOpenningTime = result.toString();
                String array[] = restaurantOpenningTime.split(",");
                resOpeningTimes = Arrays.asList(array);
                ArrayAdapter<String> RestaurantTimingAdapter = new ArrayAdapter<String>(CheckoutScreen.this, R.layout.custom_spinner_textview, resOpeningTimes);
                RestaurantTimingAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                timeSelectSpinner.setAdapter(RestaurantTimingAdapter);

                if(GET_WALLET_BALANCE.equalsIgnoreCase("GET_IT")){
                    GetWalletBalance();
                }

                break;

            case REQ_ORDER_REGISTER:

                databaseManager.clearTable();
                cartCount = 0;
                loginSession.saveCardCount(cartCount);
                OrderSuccess orderSuccess = (OrderSuccess) result;
                toast(getResources().getString(R.string.orderPlacedSuccessfully));
                Intent intent = new Intent(CheckoutScreen.this, OrderViewScreen.class);
                intent.putExtra("orderid", orderSuccess.result.order_id);
                intent.putExtra("actionBarTitle", "CheckoutScreen");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;

            case REQ_GET_WALLETHISTORY:

                WalletHistoryList walletHistoryList = (WalletHistoryList) result;

                if (Double.parseDouble(walletHistoryList.result.walletAmount) > 0) {

                    walletPriceAmount = walletHistoryList.result.walletAmount;
                    loginSession.setWalletrAmount(walletPriceAmount);
                    walletAmountTextView.setText(String.format("%.2f",Double.parseDouble(walletHistoryList.result.walletAmount))+ " " +loginSession.getCurrencyCode());

                } else {

                    walletPriceAmount = "0.00";
                    loginSession.setWalletrAmount(walletPriceAmount);
                    walletAmountTextView.setText("0.00"+ " " +loginSession.getCurrencyCode());
                }

                if(getWallBanalanceOnActivityResult.equalsIgnoreCase("YES")){

                    if(!proceedButton.getText().toString().trim().equalsIgnoreCase("Proceed")){

                        if(Double.parseDouble(walletPriceAmount) > 0){

                            double totalAmount = Double.parseDouble(placeOrderData.getGrandTotal());
                            double walletAmount = Double.parseDouble(walletPriceAmount);

                            if(walletAmount > totalAmount){

                                remaining = walletAmount - totalAmount;
                                walletAmountTextView.setText(String.format("%.2f",remaining)+" "+loginSession.getCurrencyCode());
                                SELECT_PAYMENT = "NO";
                                proceedButton.setText(getResources().getString(R.string.Pay)+ "0.00 "+loginSession.getCurrencyCode() +" )");
                                PAID_FULL = "YES";

                                codButton.setChecked(false);
                                paypalButton.setChecked(false);
                                selected_position=-1;
                                CARD_ID = "";
                                try{
                                    selected_position=-1;
                                    stripeCardListAdapter.notifyDataSetChanged();}catch (Exception e){e.printStackTrace();}

                            }else{

                                remaining = totalAmount - walletAmount;
                                walletAmountTextView.setText("0.00 "+loginSession.getCurrencyCode());
                                proceedButton.setText("PAY ( "+ String.format("%.2f",remaining)+loginSession.getCurrencyCode() +" )");
                                SELECT_PAYMENT = "YES";
                                PAID_FULL = "NO";
                            }


                        }else{
                            toast(getResources().getString(R.string.insufficientWalletAmount));
                            SELECT_PAYMENT = "YES";
                            walletButton.setChecked(false);
                            PAID_FULL = "NO";
                        }

                    }


                }else{

                    getStripeCardDetails();
                }


                break;

            case REQ_GET_STRIPE_CARD:

                StripeCardList stripeCardList = (StripeCardList) result;
                stripeCardDetailList = stripeCardList.result.cardDetails;
                if (stripeCardDetailList.size() == 0) {
                } else {
                    stripeCardListAdapter = new StripeCardListAdapter(this,stripeCardList.result.cardDetails);
                    cardListView.setAdapter(stripeCardListAdapter);
                    setListViewHeightBasedOnChildren(cardListView);
                }

                break;

        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        hideProgressDialog();
        switch (requestID) {

            case REQ_ADDRESSLIST_CHECKOUT:
                toast(error);

                if(ADDRESS_SELECT_OPTION.equalsIgnoreCase("GET_IT")){
                    GET_WALLET_BALANCE = "GET_IT";
                    GetTimemethod();
                }

                customerSelectedAddressID="";

                break;

            case REQ_TIME:

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Closed");
                ArrayAdapter<String> RestaurantTimingAdapter = new ArrayAdapter<String>(CheckoutScreen.this, R.layout.custom_spinner_textview, arrayList);
                RestaurantTimingAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                timeSelectSpinner.setAdapter(RestaurantTimingAdapter);

                if(GET_WALLET_BALANCE.equalsIgnoreCase("GET_IT")){
                    GetWalletBalance();
                }

                break;

            case REQ_ORDER_REGISTER:
                toast(error);
                break;

            case REQ_GET_WALLETHISTORY:
                toast(error);
                if(!getWallBanalanceOnActivityResult.equalsIgnoreCase("YES")){
                    getStripeCardDetails();
                }

                break;

            case REQ_GET_STRIPE_CARD:
                toast(error);
                break;

        }
    }


    //datePickerDialog
    class datePickerDialog implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateEdittext.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

        }

    }

    //On Activity result for Refreshing customer Address
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            ADDRESS_SELECT_OPTION = "DONT_GET";
            getAddressList();

        } else if (resultCode == REQ_ADD_STRIPE_CARD) {

            getStripeCardDetails();

        } else if (resultCode == 3) {
            GET_WALLET_BALANCE = "DONT_GET";
            GetWalletBalance();
            getWallBanalanceOnActivityResult = "YES";

        }

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));


                        JSONObject jsonObject1 = new JSONObject(confirm.toJSONObject().toString(4));

                        JSONObject jsonObject12 = jsonObject1.getJSONObject("response");

                        String transID = jsonObject12.getString("id");

                        placeOrderMethod("paypal",transID,"0");

                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        toast(getResources().getString(R.string.anotherPaymentType));
                     //   new orderRegister().execute();


                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }

    //getTime method
    public void GetTimemethod() {

        if (checkInternet()) {
            Map<String, String> param = new HashMap<>();
            // POST parameters:
            param.put("date", getDate);
            param.put("resid", restaurantID);
            param.put("action", "restaurantTiming");
            showProgressDialog();
            serverRequestHandler.createRequest(CheckoutScreen.this, param, RequestID.REQ_TIME);
        } else {
            noInternetAlertDialog();
        }

    }

    public void GetWalletBalance(){

        if (checkInternet()) {

            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "MyWallet");

            showProgressDialog();

            serverRequestHandler.createRequest(CheckoutScreen.this, params, RequestID.REQ_GET_WALLETHISTORY);


        } else {

            noInternetAlertDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopService(new Intent(this, PayPalService.class));

    }

    private void getStripeCardDetails() {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "getStripeCard");
            showProgressDialog();
            serverRequestHandler.createRequest(CheckoutScreen.this, params, RequestID.REQ_GET_STRIPE_CARD);
        } else {
            noInternetAlertDialog();
        }

    }

    private class StripeCardListAdapter extends BaseAdapter{

        Activity activity;
        ArrayList<StripeCardList.CardDetails>cardDetailsArrayList;

        public StripeCardListAdapter(Activity activity, ArrayList<StripeCardList.CardDetails> cardDetailsArrayList) {
            this.activity = activity;
            this.cardDetailsArrayList = cardDetailsArrayList;
        }

        RadioButton onlineButton;
        ImageView onlineImage;

        @Override
        public int getCount() {
            return cardDetailsArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return cardDetailsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater infalInflater = activity.getLayoutInflater();
            convertView = infalInflater.inflate(R.layout.checkout_stripcard_list, null);

             onlineButton       = (RadioButton)convertView.findViewById(R.id.onlineButton);
             onlineImage        = (ImageView)convertView.findViewById(R.id.onlineImage);
             onlineButton.setText("xxxx-xxxx-xxxx-"+cardDetailsArrayList.get(position).card_number);
            onlineImage.setImageResource(R.drawable.debit_card);

            onlineButton.setChecked(position == selected_position);


                onlineButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (Double.parseDouble(placeOrderData.getGrandTotal()) > 0) {
                            if (SELECT_PAYMENT.equalsIgnoreCase("YES")) {

                                if (b) {
                                    selected_position = position;
                                    CARD_ID = cardDetailsArrayList.get(position).id;
                                    codButton.setChecked(false);
                                    paypalButton.setChecked(false);
                                } else {
                                    CARD_ID = "";
                                    selected_position = -1;
                                }

                            } else {

                            }
                        } else {

                            toast(getResources().getString(R.string.anotherPaymentType));
                        }

                        notifyDataSetChanged();
                    }
                });




            return convertView;
        }

    }

    //changeAddress Adapter class
    private class AddressListAdapter extends BaseAdapter {

        Activity activity;
        List<AddressListCheckout.AddressList> addressArrayList;

        public AddressListAdapter(FragmentActivity activity, List<AddressListCheckout.AddressList> addressArrayList) {
            this.activity = activity;
            this.addressArrayList = addressArrayList;
        }

        @Override
        public int getCount() {
            return addressArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return addressArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View addressView, ViewGroup viewGroup) {

            LayoutInflater infalInflater = activity.getLayoutInflater();
            addressView = infalInflater.inflate(R.layout.checkout_address_list, null);
            RadioButton checkAddressButton       = (RadioButton)addressView.findViewById(R.id.checkAddressButton);
            checkAddressButton.setText(addressArrayList.get(i).title);
           // checkAddressButton.setText(addressArrayList.get(i).title+" ( Delivery Charge"+ loginSession.getCurrencyCode()+" "+String.format("%.2f",Double.parseDouble(addressArrayList.get(i).deliveryCharge)) +" )");
            checkAddressButton.setChecked(i == addressSelected_position);

            checkAddressButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                    if (b) {

                        addressSelected_position = i;
                        customerSelectedAddressID = addressArrayList.get(i).id;

                        if(!placeOrderData.getVoucherCodeStatus().isEmpty()){

                            if(placeOrderData.getVoucherCodePercent().equals("0") && placeOrderData.getVoucherCodePrice().equals("0")){

                                addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                placeOrderData.setDeliveryCharge("0");

                            }else{

                                if(Double.parseDouble(loginSession.getFreeDelivery())>0 && Double.parseDouble(databaseManager.getSubTotal()) >= Double.parseDouble(loginSession.getFreeDelivery())){

                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                    placeOrderData.setDeliveryCharge("0");

                                }else{

                                    placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                    double DeliveryCharge = Double.parseDouble(addressArrayList.get(i).deliveryCharge);
                                    double addDelivery = Double.parseDouble(ORIGINAL_TOTAL) + DeliveryCharge;

                                    if (DeliveryCharge > 0) {

                                        placeOrderData.setDeliveryCharge(String.valueOf(DeliveryCharge));
                                        placeOrderData.setGrandTotal(String.valueOf(addDelivery));

                                        if (addDelivery >=0) {
                                            totalBalanceTextView.setText(loginSession.getCurrencyCode()+" "+ String.format(Locale.ENGLISH,"%.2f", addDelivery));
                                        } else {
                                            totalBalanceTextView.setText(loginSession.getCurrencyCode()+" 0.00");
                                        }
                                        addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+loginSession.getCurrencyCode()+" "+ String.format(Locale.ENGLISH,"%.2f", DeliveryCharge)+" )");
                                    } else {

                                        placeOrderData.setDeliveryCharge("0.0");
                                        placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                        if (Double.parseDouble(placeOrderData.getGrandTotal()) >= 0) {

                                            totalBalanceTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", Double.parseDouble(placeOrderData.getGrandTotal())));
                                        } else {
                                            totalBalanceTextView.setText(loginSession.getCurrencyCode()+" 0.00");
                                        }
                                        addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                    }

                                }
                            }

                        }else{

                            if(Double.parseDouble(loginSession.getFreeDelivery())>0 && Double.parseDouble(databaseManager.getSubTotal()) >= Double.parseDouble(loginSession.getFreeDelivery())){

                                addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                placeOrderData.setDeliveryCharge("0");

                            }else{

                                placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                double DeliveryCharge = Double.parseDouble(addressArrayList.get(i).deliveryCharge);
                                double addDelivery = Double.parseDouble(ORIGINAL_TOTAL) + DeliveryCharge;

                                if (DeliveryCharge > 0) {

                                    placeOrderData.setDeliveryCharge(String.valueOf(DeliveryCharge));
                                    placeOrderData.setGrandTotal(String.valueOf(addDelivery));
                                    if (addDelivery >=0) {
                                        totalBalanceTextView.setText(loginSession.getCurrencyCode()+" "+ String.format(Locale.ENGLISH,"%.2f", addDelivery));
                                    } else {
                                        totalBalanceTextView.setText(loginSession.getCurrencyCode()+" 0.00");
                                    }
                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+loginSession.getCurrencyCode()+" "+ String.format(Locale.ENGLISH,"%.2f", DeliveryCharge)+" )");
                                } else {

                                    placeOrderData.setDeliveryCharge("0.0");
                                    placeOrderData.setGrandTotal(placeOrderData.getGrandTotal());
                                    if (Double.parseDouble(placeOrderData.getGrandTotal()) >= 0) {

                                        totalBalanceTextView.setText(loginSession.getCurrencyCode()+" "+ String.format("%.2f", Double.parseDouble(placeOrderData.getGrandTotal())));
                                    } else {
                                        totalBalanceTextView.setText(loginSession.getCurrencyCode()+" 0.00");
                                    }
                                    addressTagTV.setText(getResources().getString(R.string.addressInfoFree)+getResources().getString(R.string.free)+")");
                                }

                            }
                        }

                    } else {

                        addressSelected_position = -1;
                    }
                    notifyDataSetChanged();
                }
            });

            return addressView;
        }
    }

    public void placeOrderMethod(String paymentType,String tipAmount,String transactionId){

        String PAYMENT_TYPE   = paymentType;
        String TRANSACTION_ID = transactionId;
        String TIP_AMOUNT     = tipAmount;

        if (checkInternet()) {

            Map<String, String> params = new HashMap<>();
            params.put("action", "CheckOut");
            params.put("page", "ConformOrder");
            params.put("customer_id", loginSession.getUserId());
            params.put("order_description", placeOrderData.getInstruction());
            params.put("rewardPercentage", placeOrderData.getRewardPercentage());
            params.put("rewardPoint", placeOrderData.getRedeemAmount());

            if(walletButton.isChecked()){
                params.put("payment_name", "Yes");

                if(PAID_FULL.equalsIgnoreCase("YES")){
                    params.put("paidFull", "yes");
                }else{
                    params.put("paidFull", "No");
                }

                if(SELECT_PAYMENT.equalsIgnoreCase("YES")){

                    params.put("payment_method", PAYMENT_TYPE);

                }else{

                    params.put("payment_method", "cod");
                }


            }else{
                params.put("payment_name", "");
                params.put("paidFull", "No");
                params.put("payment_method", PAYMENT_TYPE);
            }


            if(PAYMENT_TYPE.equalsIgnoreCase("stripe")){

                params.put("credit_card_choose",CARD_ID);
            }

            if (placeOrderData.getOrderType().equalsIgnoreCase("Pickup")) {
                params.put("order_type", "pickup");
            } else {
                params.put("order_type", "delivery");
            }

            if (getFoodAsSoonAs.equalsIgnoreCase("Now")) {
                params.put("delivery_date", curTime);
                params.put("delivery_time", localTime);
            } else {
                params.put("delivery_date", getDate);
                params.put("delivery_time", getTime);
            }


            params.put("tipamount", TIP_AMOUNT);
            params.put("voucher_code", placeOrderData.getVoucherCodeStatus());
            params.put("delivery_id", customerSelectedAddressID);
            params.put("assoonas", getFoodAsSoonAs);
            params.put("resid", placeOrderData.getRestaurantID());
            params.put("cartdetails", "" + databaseManager.getCart());

            if (Double.parseDouble(tips) > 0) {
                Double total = Double.parseDouble(placeOrderData.getGrandTotal()) + Double.parseDouble(tips);

                if (total > 0) {
                    params.put("ordertotalprice", String.valueOf(total));
                } else {
                    params.put("ordertotalprice", "0");
                }

            } else {

                if (Double.parseDouble(placeOrderData.getGrandTotal())>0) {
                    params.put("ordertotalprice", placeOrderData.getGrandTotal());
                } else {
                    params.put("ordertotalprice", "0");
                }

            }

            params.put("order_sub_total", placeOrderData.getSubTotal());
            params.put("customer_phone", loginSession.getUserPhone());
            params.put("tax_amount", placeOrderData.getTaxAmount());
            params.put("tax_percentage", placeOrderData.getTaxPercentage());
            params.put("voucher_amount", placeOrderData.getVoucherCodePrice());
            params.put("voucher_percentage", placeOrderData.getVoucherCodePercent());

            if(Double.parseDouble(loginSession.getFreeDelivery())>0 && Double.parseDouble(databaseManager.getSubTotal()) >= Double.parseDouble(loginSession.getFreeDelivery())){

                params.put("delivery_charge", "0");
            }else{
                params.put("delivery_charge", placeOrderData.getDeliveryCharge());
            }

            params.put("offer_amount", placeOrderData.getOfferAmount());
            params.put("offer_percentage", placeOrderData.getOfferPercentage());
            params.put("transaction_id", TRANSACTION_ID);
            params.put("phone_number", phoneNumber);
            params.put("order_from ","Android");
            Log.e("Check out passing", String.valueOf(params));
            showProgressDialog();
            serverRequestHandler.createRequest(CheckoutScreen.this, params, RequestID.REQ_ORDER_REGISTER);

        } else {

            noInternetAlertDialog();
        }

    }

    private class OutOffAddressListAdapter extends BaseAdapter{

        Activity activity;
        List<AddressListCheckout.OutOfDelivery> outOfDeliveries;

        public OutOffAddressListAdapter(FragmentActivity activity, List<AddressListCheckout.OutOfDelivery> addressArrayList) {
            this.activity = activity;
            this.outOfDeliveries = addressArrayList;
        }

        @Override
        public int getCount() {
            return outOfDeliveries.size();
        }

        @Override
        public Object getItem(int i) {
            return outOfDeliveries.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater infalInflater = activity.getLayoutInflater();
            convertView = infalInflater.inflate(R.layout.checkout_out_address_list, null);
            TextView checkAddressTextView       = (TextView)convertView.findViewById(R.id.checkAddressTextView);
            checkAddressTextView.setText(outOfDeliveries.get(i).title+getResources().getString(R.string.outOfDelivery));

            return convertView;
        }
    }

    public void startZoomOutAnimation() {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_animation);
        totalBalanceTextView.startAnimation(zoomOutAnimation);
    }
}

