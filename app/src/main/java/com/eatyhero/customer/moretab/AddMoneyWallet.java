package com.eatyhero.customer.moretab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.model.StripeCardList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 30-01-2018.
 */

public class AddMoneyWallet extends BaseActivity implements ServerListener, Serializable {

    //Create Objects
     LoginSession loginSession;
     ServerRequest serverRequest;

    //Xml Objects
    @BindView(R.id.backImageView)ImageView backImageView;
    @BindView(R.id.actionBarTitleTextView)TextView actionBarTitleTextView;
    @BindView(R.id.amountEditText)EditText amountEditText;
    @BindView(R.id.walletBalanceTextView)TextView walletBalanceTextView;
    @BindView(R.id.amountRadioGroup)RadioGroup amountRadioGroup;
    @BindView(R.id.cardListView)ListView cardListView;
    @BindView(R.id.addWalletButton)Button addWalletButton;

    ArrayList<StripeCardList.CardDetails> stripeCardDetailList = new ArrayList<>();
    String getAmount = "",CARD_ID="";
    int selected_position = -1;
    StripeCardListAdapter stripeCardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_to_wallet);

        ButterKnife.bind(this);

        //Initialize Objects
        loginSession  = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);

       //Get Intent values
        Intent intent = getIntent();
        if (intent != null) {
            walletBalanceTextView.setText(String.format("%.2f",Double.parseDouble(loginSession.getWalletAmount()))+ " " +loginSession.getCurrencyCode() );
        }

        //Get Stripe card details
        if (checkInternet()) {

            Map<String, String> params = new HashMap<>();
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "getStripeCard");
            showProgressDialog();
            serverRequest.createRequest(AddMoneyWallet.this, params, RequestID.REQ_GET_STRIPE_CARD);
        } else {
            noInternetAlertDialog();
        }

        amountRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        addWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String buttonText = addWalletButton.getText().toString().trim();
                getAmount = amountEditText.getText().toString().replaceAll("[^\\d]", "");

                if (getAmount.isEmpty()) {

                    toast(getResources().getString(R.string.enterTheAmount));

                } else if (Integer.parseInt(getAmount) == 0) {

                    toast(getResources().getString(R.string.enterAmountGreater));

                } else if(CARD_ID.isEmpty() || CARD_ID.equals("")){

                    toast(getResources().getString(R.string.selectCard));

                }else{
                        if (checkInternet()) {
                            Map<String, String> params = new HashMap<>();
                            params.put("action", "MyAccount");
                            params.put("page", "MyWalletAddMoney");
                            params.put("customer_id", loginSession.getUserId());
                            params.put("amount", getAmount);
                            params.put("card_id", CARD_ID);
                            showProgressDialog();
                            serverRequest.createRequest(AddMoneyWallet.this, params, RequestID.REQ_ADD_WALLET);
                        } else {
                            noInternetAlertDialog();
                        }

                }
            }
        });

        //backImageView click event
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID){

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

            case REQ_ADD_WALLET:

                toast(getResources().getString(R.string.moneyAddSuccess));
                setResult(3);
                finish();

                break;


        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
    }

    private class StripeCardListAdapter extends BaseAdapter {

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

            if (cardDetailsArrayList.get(position).card_brand.equalsIgnoreCase("visa")){
                onlineImage.setImageResource(R.drawable.debit_card);
            }else {
                onlineImage.setImageResource(R.drawable.debit_card);
            }

            onlineButton.setChecked(position == selected_position);

            onlineButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b) {
                            selected_position = position;
                            CARD_ID = cardDetailsArrayList.get(position).id;
                        } else {
                            CARD_ID = "";
                            selected_position = -1;
                        }

                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

    }
}
