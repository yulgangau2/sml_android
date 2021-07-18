package com.eatyhero.customer.moretab;

import android.app.Activity;
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
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.model.WalletHistoryList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 19-08-2016.
 */
public class WalletHistoryScreen extends BaseActivity implements ServerListener {

    //create objects
    ServerRequest serverRequest;
    LoginSession loginSession;
    WalletListAdapter walletListAdapter;

    //XMl objects
    ListView mywalletListView;
    ImageView backImageView;
    Toolbar toolbar;
    ImageView addWalletMoney;
    TextView actionBarTitleTextView,walletPrice;

    String walletPriceAmount = "0.00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_screen);

        //Register xml id
        mywalletListView        = (ListView) findViewById(R.id.mywalletListView);
        toolbar                 = (Toolbar)findViewById(R.id.toolbar);
        actionBarTitleTextView  = (TextView)toolbar.findViewById(R.id.actionBarTitleTextView);
        addWalletMoney          = (ImageView)toolbar.findViewById(R.id.addWalletMoney);
        walletPrice             = (TextView)toolbar.findViewById(R.id.walletBalanceTextView);
        backImageView           = (ImageView) toolbar.findViewById(R.id.backImageView);

        //Initialize objects
        loginSession         = LoginSession.getInstance(this);
        serverRequest        = ServerRequest.getInstance(this);

        actionBarTitleTextView.setText(getResources().getString(R.string.walletHistory));

        //Actionbar BackButton
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        addWalletMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WalletHistoryScreen.this, AddMoneyWallet.class);
                intent.putExtra("walletPriceAmount",walletPriceAmount);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getHistory();
    }

    public void getHistory() {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page","MyWallet");
            showProgressDialog();
            serverRequest.createRequest(WalletHistoryScreen.this, params, RequestID.REQ_GET_WALLETHISTORY);
        } else {
           noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID) {

            case REQ_GET_WALLETHISTORY :

                WalletHistoryList walletHistoryList=(WalletHistoryList)result;

                if(Double.parseDouble(walletHistoryList.result.walletAmount) > 0){

                    walletPriceAmount = walletHistoryList.result.walletAmount;
                    loginSession.setWalletrAmount(walletPriceAmount);

                }else{

                    walletPriceAmount = "0.00";
                    loginSession.setWalletrAmount(walletPriceAmount);

                }

                walletPrice.setText(String.format("%.2f",Double.parseDouble(walletHistoryList.result.walletAmount))+ " " +loginSession.getCurrencyCode());
                ArrayList<WalletHistoryList.WalletHistory>walletHistories =walletHistoryList.result.walletHistory;
                walletListAdapter = new WalletListAdapter(WalletHistoryScreen.this, walletHistories);
                mywalletListView.setAdapter(walletListAdapter);
                setResult(3);

                break;
        }


    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);

    }

    private class WalletListAdapter extends BaseAdapter {

        Activity activity;
        ArrayList<WalletHistoryList.WalletHistory>walletHistories;

        public WalletListAdapter(Activity activity, ArrayList<WalletHistoryList.WalletHistory> walletHistories) {
            this.activity = activity;
            this.walletHistories = walletHistories;
        }

        @Override
        public int getCount() {
            return walletHistories.size();
        }

        @Override
        public Object getItem(int position) {
            return walletHistories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)

                convertView = inflater.inflate(R.layout.my_wallet_custom, null);

            TextView purposeTextView              = (TextView) convertView.findViewById(R.id.purposeTextView);
            TextView dateTextView                 = (TextView) convertView.findViewById(R.id.dateTextView);
            TextView amountTextView               = (TextView)convertView.findViewById(R.id.amountTextView);
            TextView transactionTypeTextView      = (TextView)convertView.findViewById(R.id.transactionTypeTextView);
            TextView transactionDetailsTextView   = (TextView)convertView.findViewById(R.id.transactionDetailsTextView);

            purposeTextView.setText(walletHistories.get(position).purpose);
            amountTextView.setText(String.format("%.2f",Double.parseDouble(walletHistories.get(position).amount))+" "+loginSession.getCurrencyCode());
            transactionTypeTextView.setText(walletHistories.get(position).transaction_type);
            transactionDetailsTextView.setText(walletHistories.get(position).transaction_details);

            String reviewDate = "";
            try {
                String date = walletHistories.get(position).created;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                Date d = sdf.parse(date);
                Log.e("dateintial", d.toString());
                Log.e("date", sdf2.format(d));
                reviewDate = sdf2.format(d);
            }catch (Exception e){}
            dateTextView.setText(reviewDate);

            return convertView  ;
        }
    }
}

