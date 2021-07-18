package com.eatyhero.customer.moretab;

/**
 * Created by admin on 22-02-2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.StripeCardList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Admin on 19-08-2016.
 */
public class CardManagementScreen extends BaseActivity implements ServerListener {

    //Create class objects
    Utility utility;
    ServerRequest serverRequest;
    LoginSession loginSession;

    //Create xml objects
    Toolbar toolbar;
    ListView paymentListView;
    ImageView backIconImageView;
    TextView actionBarTitleTextView;
    ImageView addAddressImageView;
    RelativeLayout no_card_layout;

    //Create List
    List<StripeCardList.CardDetails> stripeCardDetailList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_management);

        //Initialize xml objects

        paymentListView = (ListView) findViewById(R.id.paymentListView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        addAddressImageView = (ImageView) toolbar.findViewById(R.id.addAddressImageView);
        no_card_layout = (RelativeLayout) findViewById(R.id.no_card_layout);

        //Initialize class objects
        utility = Utility.getInstance(this);
        loginSession = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);

        //Setup toolbar and actionbar name
        addAddressImageView.setVisibility(View.VISIBLE);
        actionBarTitleTextView.setText(getResources().getString(R.string.cardManagement));

        addAddressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("stripeCardDetailList", "stripeCardDetailList" + stripeCardDetailList);

                if (stripeCardDetailList.size() == 3) {
                    utility.showAlertDialog(getResources().getString(R.string.limitIs3));
                } else {
                    Intent intent = new Intent(CardManagementScreen.this, AddCardScreen.class);
                    startActivity(intent);
                }

            }
        });

        //Back Icon button click event
        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void getStripeCardList() {

        if (utility.isConnectingToInternet()) {

            Map<String, String> params = new HashMap<>();
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "getStripeCard");

            utility.showProgressDialog();
            serverRequest.createRequest(CardManagementScreen.this, params, RequestID.REQ_GET_STRIPE_CARD);

        } else {

            utility.noInternetAlertDialog();
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        utility.hideProgressDialog();

        switch (requestID) {

            case REQ_GET_STRIPE_CARD:

                paymentListView.setVisibility(View.VISIBLE);
                no_card_layout.setVisibility(View.GONE);
                StripeCardList stripeCardList = (StripeCardList) result;
                stripeCardDetailList = stripeCardList.result.cardDetails;
                Customlistview custom = new Customlistview(CardManagementScreen.this, stripeCardDetailList, this);
                paymentListView.setAdapter(custom);


                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        utility.hideProgressDialog();
        paymentListView.setVisibility(View.GONE);
        no_card_layout.setVisibility(View.VISIBLE);
        utility.toast(getResources().getString(R.string.noCards));
        paymentListView.setAdapter(null);

    }

    @Override
    public void onResume() {
        super.onResume();
        getStripeCardList();
    }

    public class Customlistview extends BaseAdapter implements ServerListener{

        List<StripeCardList.CardDetails> stripeCardDetailses;
        Activity activity;
        Utility utility;
        LoginSession loginSession;
        ServerRequest serverRequest;
        CardManagementScreen paymentCardScreen;

        public Customlistview(Activity activity, List<StripeCardList.CardDetails> stripeCardDetailses, CardManagementScreen paymentCardScreen) {
            this.activity = activity;
            this.stripeCardDetailses = stripeCardDetailses;
            utility = Utility.getInstance(activity);
            loginSession = LoginSession.getInstance(activity);
            serverRequest = ServerRequest.getInstance(activity);
            this.paymentCardScreen = paymentCardScreen;
        }

        @Override
        public int getCount() {
            return stripeCardDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return stripeCardDetailses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final LayoutInflater inflator=activity.getLayoutInflater();
            convertView=inflator.inflate(R.layout.custom_payment_card,null);

            TextView cardnumtext=(TextView)convertView.findViewById(R.id.cardNumberTextView);
            TextView validthrutextview=(TextView)convertView.findViewById(R.id.validThruTextView);
            TextView menuDeleteButton=(TextView)convertView.findViewById(R.id.menuDeleteButton);
            ImageView cardtype=(ImageView) convertView.findViewById(R.id.cardTypeImage);



            //Set cardBrand
            if (stripeCardDetailses.get(position).card_brand.equalsIgnoreCase("visa")){
                cardtype.setImageResource(R.drawable.debit_card);
            }else {
                cardtype.setImageResource(R.drawable.debit_card);
            }

            //Set card number
            cardnumtext.setText("xxxx-xxxx-xxxx-"+stripeCardDetailses.get(position).card_number);
            validthrutextview.setText(getResources().getString(R.string.validTill)+stripeCardDetailses.get(position).exp_month+"/"+stripeCardDetailses.get(position).exp_year);

            //Set menu button click event
            menuDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                            alertDialog.setTitle(getResources().getString(R.string.deleteCard));
                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                    //Get Stripe card list
                                    if (utility.isConnectingToInternet()) {

                                        Map<String, String> params = new HashMap<>();
                                        params.put("action", "MyAccount");
                                        params.put("customer_id", loginSession.getUserId());
                                        params.put("page", "SavedCardDelete");
                                        params.put("stripeId",stripeCardDetailses.get(position).id);

                                        utility.showProgressDialog();
                                        serverRequest.createRequest(Customlistview.this, params, RequestID.REQ_STRIPE_CARD_DELETE);

                                    } else {

                                        utility.noInternetAlertDialog();
                                    }


                                }
                            });

                            alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            alertDialog.show();

                        }
                    });

            return convertView;
        }

        @Override
        public void onSuccess(Object result, RequestID requestID) {

            switch (requestID) {
                case REQ_STRIPE_CARD_DELETE:
                    utility.hideProgressDialog();
                    utility.toast(result.toString());
                    paymentCardScreen.getStripeCardList();
                    break;
            }
        }

        @Override
        public void onFailure(String error, RequestID requestID) {

            utility.hideProgressDialog();
            utility.toast(error);

        }
    }
}