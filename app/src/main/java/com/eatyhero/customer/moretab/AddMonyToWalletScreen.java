package com.eatyhero.customer.moretab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.StripeCardList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 9/18/2015.
 */
public class AddMonyToWalletScreen extends BaseActivity implements ServerListener, Serializable {

    //Create Objects
    static Utility utility;
    static LoginSession loginSession;
    static ServerRequest serverRequest;

    //View Pager Object
    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;
    PageIndicator pageIndicator;

    static String getAmount = "";
    //Strip token
    static String stripeToken;
    static String cardHolderName;
    static String cardNumber;
    static String expiryMonth;
    static String expiryYear;
    static String cvvNumber;
    static String saveCardDetails = "";
    Boolean stripeCardDetails;

    //Create List
    private static List<StripeCardList.CardDetails> stripeCardDetailList = new ArrayList<>();
    private static List<StripeCardList.CardDetails> stripeCardDetailListView = new ArrayList<>();

    //Xml Objects
    Toolbar toolbar;
    RadioGroup  priceRadioGroup;
    TextView actionBarTitleTextView, walletPrice;
    ImageView addWalletMoney, backImageView;
    EditText amountEditText;
    Button addWalletButton;
    LinearLayout addWalletBalanceLayout;
    RelativeLayout stripeCardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wallet_screennew);

        //Initialize Xml id
        toolbar                = (Toolbar) findViewById(R.id.toolbar);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        addWalletMoney         = (ImageView) toolbar.findViewById(R.id.addWalletMoney);

        amountEditText        = (EditText) findViewById(R.id.amountEditText);
        addWalletButton       = (Button) findViewById(R.id.addWalletButton);
        walletPrice           = (TextView) toolbar.findViewById(R.id.walletBalanceTextView);
        backImageView         = (ImageView) toolbar.findViewById(R.id.backImageView);
        priceRadioGroup       = (RadioGroup) findViewById(R.id.amountRadioGroup);

        addWalletBalanceLayout = (LinearLayout) findViewById(R.id.addWalletBalaneLayout);
        stripeCardLayout       = (RelativeLayout) findViewById(R.id.stripCardLayout);

        //Initialize View Pager Objects
        fragmentPagerAdapter = new CustomCardSwipe(getSupportFragmentManager());
        viewPager            = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);
        pageIndicator        = (CirclePageIndicator) findViewById(R.id.circleindicator);
        pageIndicator.setViewPager(viewPager);

        //Initialize Objects
        utility       = Utility.getInstance(this);
        loginSession  = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);

        //ActionBar Title Set & Hide the ActionBar Wallet Menu
        actionBarTitleTextView.setText(getResources().getString(R.string.addWallet));
        addWalletMoney.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null) {

            walletPrice.setText(String.format("%.2f",Double.parseDouble(loginSession.getWalletAmount()))+ " " +loginSession.getCurrencyCode());
        }

        //Get Stripe card details
        if (utility.isConnectingToInternet()) {

            Map<String, String> params = new HashMap<>();
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "getStripeCard");

            utility.showProgressDialog();

            serverRequest.createRequest(AddMonyToWalletScreen.this, params, RequestID.REQ_GET_STRIPE_CARD);

        } else {

            utility.toast(getResources().getString(R.string.noInternet));
        }

        //Actionbar BackButton
        actionBarTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stripeCardDetails = false;
                Log.e("stripeCardDetails", "stripeCardDetails" + stripeCardDetails);
                finish();
            }
        });

        priceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                switch (id) {

                    case R.id.radio1:
                        amountEditText.setText("100"+ " " +loginSession.getCurrencyCode());

                        break;
                    case R.id.radio4:
                        amountEditText.setText("200"+ " " +loginSession.getCurrencyCode());

                        break;
                    case R.id.radio5:
                        amountEditText.setText("500"+ " " +loginSession.getCurrencyCode());

                        break;
                }
            }
        });

        //addWalletButton Click Event
        addWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buttonText = addWalletButton.getText().toString().trim();
                getAmount = amountEditText.getText().toString().replaceAll("[^\\d]", "");

                    if (getAmount.isEmpty()) {

                        utility.toast(getResources().getString(R.string.enterTheAmount));

                    } else if (Integer.parseInt(getAmount) == 0) {

                        utility.toast(getResources().getString(R.string.enterAmountGreater));

                    } else {

                        addWalletBalanceLayout.setVisibility(LinearLayout.GONE);
                        stripeCardLayout.setVisibility(LinearLayout.VISIBLE);
                    }
            }
        });
    }


    @Override
    public void onSuccess(Object result, RequestID requestID) {

        utility.hideProgressDialog();
        stripeCardDetailList.clear();

        switch (requestID) {

            case REQ_GET_STRIPE_CARD:

                Log.e("Success", "Success");

                StripeCardList stripeCardList = (StripeCardList) result;

                stripeCardDetailList = stripeCardList.result.cardDetails;
                stripeCardDetailListView = stripeCardList.result.cardDetails;

                if (!stripeCardDetailList.isEmpty()) {

                    StripeCardList.CardDetails stripeCardDetailsobj = new StripeCardList.CardDetails();

                    stripeCardDetailsobj.setNewCard(true);

                    stripeCardDetailList.add(stripeCardDetailsobj);

                    //Initialize View Pager Objects
                    fragmentPagerAdapter = new CustomCardSwipe(getSupportFragmentManager());

                    viewPager.setAdapter(fragmentPagerAdapter);

                    pageIndicator.setViewPager(viewPager);

                    pageIndicator.notifyDataSetChanged();
                }
                setResult(3);
                break;

        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        utility.hideProgressDialog();
        // utility.toast(error);
        switch (requestID) {

            case REQ_GET_STRIPE_CARD:

                stripeCardDetailList.clear();

                if (stripeCardDetailList.isEmpty()) {

                    StripeCardList.CardDetails stripeCardDetailsobj = new StripeCardList.CardDetails();
                    stripeCardDetailsobj.setNewCard(true);

                    if (stripeCardDetailsobj.isNewCard()) {

                        stripeCardDetailList.add(stripeCardDetailsobj);

                        //Initialize View Pager Objects
                        fragmentPagerAdapter = new CustomCardSwipe(getSupportFragmentManager());
                        viewPager.setAdapter(fragmentPagerAdapter);
                        pageIndicator.setViewPager(viewPager);
                        pageIndicator.notifyDataSetChanged();

                        break;

                    }

                }

                break;
        }

    }


    //Custom Card Swipe
    public static class CustomCardSwipe extends FragmentPagerAdapter {

        public CustomCardSwipe(FragmentManager supportFragmentManager) {

            super(supportFragmentManager);

        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new CustomCardFragment();
            try {

                Bundle bundle = new Bundle();
                StripeCardList.CardDetails stripeCardDetails = stripeCardDetailList.get(position);
                bundle.putSerializable("CardDetails", stripeCardDetails);
                fragment.setArguments(bundle);

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return stripeCardDetailList.size();
        }

    }

    /********************
     * Custom Card Fragment
     ****************/
    @SuppressLint("ValidFragment")
    public static class CustomCardFragment extends Fragment implements ServerListener {

        StripeCardList.CardDetails stripeCardDetails1;
        Bundle bundle = new Bundle();
        String stripeCustomerID = "";

        //xml resource
        ScrollView NewCardLayout;
        RelativeLayout SavedCardlayout;
        TextView cardNumberTextView;
        TextView validThruTextView;

        //New Card Layout XMl resource
        EditText cardHoldernameEditText, cardNumberEditText, CvvEditText;
        Spinner expiryMonthSpinner, expiryYearSpinner;
        CheckBox saveCardDetailsCheckBox;
        Button newCardPayButton;
        ImageView cardTypeImage;

        String a;
        int keyDel;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootview = inflater.inflate(R.layout.custom_checkout_card, container, false);

            NewCardLayout      = (ScrollView) rootview.findViewById(R.id.NewCardLayout);
            SavedCardlayout    = (RelativeLayout) rootview.findViewById(R.id.SavedCardLayout);
            cardNumberTextView = (TextView) rootview.findViewById(R.id.cardNumberTextView);
            validThruTextView  = (TextView) rootview.findViewById(R.id.validThruTextView);

            //initialize xml ids
            cardNumberEditText      = (EditText) rootview.findViewById(R.id.cardNumberEditText);
            cardHoldernameEditText  = (EditText) rootview.findViewById(R.id.cardHoldernameEditText);
            expiryMonthSpinner      = (Spinner) rootview.findViewById(R.id.expiryMonthSpinner);
            expiryYearSpinner       = (Spinner) rootview.findViewById(R.id.expiryYearSpinner);
            CvvEditText             = (EditText) rootview.findViewById(R.id.CvvEditText);
            saveCardDetailsCheckBox = (CheckBox) rootview.findViewById(R.id.saveCardDetailsCheckBox);
            newCardPayButton        = (Button) rootview.findViewById(R.id.newCardPayButton);

            cardNumberEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    boolean flag = true;
                    String eachBlock[] = cardNumberEditText.getText().toString().split("-");
                    for (int i = 0; i < eachBlock.length; i++) {
                        if (eachBlock[i].length() > 4) {
                            flag = false;
                        }
                    }
                    if (flag) {

                        cardNumberEditText.setOnKeyListener(new View.OnKeyListener() {

                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (keyCode == KeyEvent.KEYCODE_DEL)
                                    keyDel = 1;
                                return false;
                            }
                        });

                        if (keyDel == 0) {

                            if (((cardNumberEditText.getText().length() + 1) % 5) == 0) {

                                if (cardNumberEditText.getText().toString().split("-").length <= 3) {
                                    cardNumberEditText.setText(cardNumberEditText.getText() + "-");
                                    cardNumberEditText.setSelection(cardNumberEditText.getText().length());
                                }
                            }
                            a = cardNumberEditText.getText().toString();
                        } else {
                            a = cardNumberEditText.getText().toString();
                            keyDel = 0;
                        }

                    } else {
                        cardNumberEditText.setText(a);
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            bundle = getArguments();
            if (bundle != null) {

                stripeCardDetails1 = (StripeCardList.CardDetails) bundle.getSerializable("CardDetails");

                if (stripeCardDetails1.isNewCard()) {
                    NewCardLayout.setVisibility(LinearLayout.VISIBLE);
                    SavedCardlayout.setVisibility(RelativeLayout.GONE);

                } else {

                    NewCardLayout.setVisibility(LinearLayout.GONE);
                    SavedCardlayout.setVisibility(RelativeLayout.VISIBLE);
                    stripeCustomerID = stripeCardDetails1.id;
                    cardNumberTextView.setText("XXXX-XXXX-XXXX-" + stripeCardDetails1.card_number);
                    validThruTextView.setText(stripeCardDetails1.exp_month + "/" + stripeCardDetails1.exp_year);
                }

                SavedCardlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addWalletBalance();
                    }
                });

                //newCardPay Button Click Event
                newCardPayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String check = "yes";

                        //Validation Part
                        cardHolderName = cardHoldernameEditText.getText().toString();
                        cardNumber = cardNumberEditText.getText().toString().replace("-","");
                        expiryMonth = expiryMonthSpinner.getSelectedItem().toString();
                        expiryYear = expiryYearSpinner.getSelectedItem().toString();
                        cvvNumber = CvvEditText.getText().toString();

                        if (saveCardDetailsCheckBox.isChecked()) {

                            if (stripeCardDetailList.size() == 4) {

                                check = "no";
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setTitle(getResources().getString(R.string.alert));
                                alertDialog.setMessage(getResources().getString(R.string.cardsExisting));
                                alertDialog.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                        saveCardDetails = "No";

                                        saveCardDetailsCheckBox.setChecked(false);
                                    }
                                });

                                alertDialog.show();


                            } else {

                                check = "yes";
                                saveCardDetails = "Yes";
                            }

                        } else {

                            check = "yes";
                            saveCardDetails = "No";
                        }

                        //validation for card
                        if (cardNumber.isEmpty()) {

                            utility.toast(getResources().getString(R.string.PleaseEnterCardNumber));

                        } else if (cardHolderName.isEmpty()) {

                            utility.toast(getResources().getString(R.string.cardHolderName));

                        } else if (expiryMonth.equalsIgnoreCase("Month")) {

                            utility.toast(getResources().getString(R.string.expiryMonth));

                        } else if (expiryYear.equalsIgnoreCase("Year")) {

                            utility.toast(getResources().getString(R.string.expiryYear));

                        } else if (cvvNumber.isEmpty()) {

                            utility.toast(getResources().getString(R.string.cvvNumber));

                        } else {
                            if (check.equalsIgnoreCase("yes")) {
                                Card card = new Card(cardNumber, Integer.parseInt(expiryMonth), Integer.parseInt(expiryYear), cvvNumber);

                                boolean validation = card.validateCard();

                                if (validation) {

                                    utility.showProgressDialog();

                                    new Stripe(getContext()).createToken(
                                            card,
                                            loginSession.getPublishKey(),//Constens.PUBLISHABLE_KEY
                                            new TokenCallback() {
                                                public void onSuccess(Token token) {

                                                    Log.e("Token", "Token" + token.getCard().getLast4());
                                                    stripeToken = token.getId();

                                                    //Server request
                                                    if (utility.isConnectingToInternet()) {

                                                        Map<String, String> params = new HashMap<>();

                                                        params.put("action", "MyAccount");
                                                        params.put("page", "MyWalletAddMoney");
                                                        params.put("card_id", "");
                                                        params.put("stripe_cardnumber", token.getCard().getLast4());
                                                        params.put("cardHolderName", cardHolderName);
                                                        params.put("stripe_expmonth", expiryMonth);
                                                        params.put("stripe_expyear", expiryYear);
                                                        params.put("stripe_cvccode", cvvNumber);
                                                        params.put("saveCard", saveCardDetails);
                                                        params.put("stripe_token", stripeToken);
                                                        params.put("customer_id", loginSession.getUserId());
                                                        params.put("amount", getAmount);
                                                        Log.e("Values to Server", "" + params);

                                                        serverRequest.createRequest((ServerListener) CustomCardFragment.this, params, RequestID.REQ_ADD_WALLET);

                                                    } else {

                                                        utility.toast(getResources().getString(R.string.noInternet));
                                                    }

                                                }

                                                public void onError(Exception error) {

                                                }
                                            });

                                } else if (!card.validateNumber()) {

                                    utility.toast(getResources().getString(R.string.cardNumberInvalid));

                                } else if (!card.validateExpiryDate()) {

                                    utility.toast(getResources().getString(R.string.expirationdateInvalid));

                                } else if (!card.validateCVC()) {

                                    utility.toast(getResources().getString(R.string.CvvInvalid));

                                } else {

                                    utility.toast(getResources().getString(R.string.cardDetailsInvalid));
                                }
                            }
                        }

                    }

                });

            }

            return rootview;
        }

        public void addWalletBalance() {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(getResources().getString(R.string.selectedCard));
            alertDialog.setPositiveButton(getResources().getString(R.string.useThisToPay), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                    //Get Stripe card details
                    if (utility.isConnectingToInternet()) {

                        Map<String, String> params = new HashMap<>();

                        params.put("action", "MyAccount");
                        params.put("page", "MyWalletAddMoney");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("amount", getAmount);
                        params.put("card_id", stripeCustomerID);

                        Log.e("Values to Server", "" + params);
                        utility.showProgressDialog();

                        serverRequest.createRequest(CustomCardFragment.this, params, RequestID.REQ_ADD_WALLET);

                    } else {

                        utility.toast(getResources().getString(R.string.noInternet));
                    }
                }

            }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });

            alertDialog.setNeutralButton(getResources().getString(R.string.Delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (utility.isConnectingToInternet()) {

                        Map<String, String> params = new HashMap<>();

                        params.put("stripeId", stripeCustomerID);
                        params.put("action", "MyAccount");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("page", "SavedCardDelete");

                        utility.showProgressDialog();
                        serverRequest.createRequest(CustomCardFragment.this, params, RequestID.REQ_STRIPE_CARD_DELETE);

                    } else {

                        utility.noInternetAlertDialog();
                    }

                }
            });

            alertDialog.show();


        }

        @Override
        public void onSuccess(Object result, RequestID requestID) {

            stripeCardDetailList.clear();
            utility.hideProgressDialog();
            switch (requestID) {

                case REQ_STRIPE_CARD_DELETE:
                    utility.hideProgressDialog();
                    utility.toast(result.toString());
                    getActivity().finish();

                    break;

                case REQ_ADD_WALLET:

                    utility.toast(getResources().getString(R.string.moneyAddSuccess));
                    getActivity().setResult(3);
                    getActivity().finish();

                    break;}
        }

        @Override
        public void onFailure(String error, RequestID requestID) {

            utility.hideProgressDialog();
            utility.toast(error);
        }
    }
}
