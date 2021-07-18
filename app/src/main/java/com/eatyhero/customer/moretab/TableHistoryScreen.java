package com.eatyhero.customer.moretab;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.NonScrollListView;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.BookTableHistory;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 21-06-2017.
 */

public class TableHistoryScreen extends BaseActivity implements ServerListener {

    //create xml objects
    NonScrollListView buktableListView;
    Toolbar toolbar;
    ImageView backIconImageView;
    TextView actionBarTitleTextView;
    RelativeLayout no_table_history;
    LinearLayout tableHistoryyes;
    ScrollView scrollView;

    //crete objects
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;

    //initialize arraylist
    ArrayList<BookTableHistory.Bookings> tableHistory = new ArrayList<BookTableHistory.Bookings>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_table_history_layout);

        //initialize toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        //initialize listview
        buktableListView = (NonScrollListView) findViewById(R.id.buktableListView);
        no_table_history = (RelativeLayout) findViewById(R.id.no_table_history);
        tableHistoryyes     = (LinearLayout) findViewById(R.id.tableHistory);

        //object initialization
        utility = Utility.getInstance(this);
        loginSession = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);


        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              finish();
            }
        });

        actionBarTitleTextView.setText(getResources().getString(R.string.bookTableHistory));

        if (utility.isConnectingToInternet()) {

            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("action", "MyAccount");
            params.put("page", "BookingHistory");
            params.put("customer_id",loginSession.getUserId());

            serverRequest.createRequest(TableHistoryScreen.this, params, RequestID.REQ_TO_BOOKTABLE_HISTORY);
            utility.showProgressDialog();

        } else {

            utility.noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {
        utility.hideProgressDialog();
        switch (requestID) {

            case REQ_TO_BOOKTABLE_HISTORY:

                tableHistoryyes.setVisibility(View.VISIBLE);

                BookTableHistory bookTableHistory = (BookTableHistory) result;
                tableHistory = bookTableHistory.result.bookings;

                CustomTableHistoryScreen customTableHistoryScreen = new CustomTableHistoryScreen(TableHistoryScreen.this, tableHistory);
                buktableListView.setAdapter(customTableHistoryScreen);
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        utility.hideProgressDialog();
        no_table_history.setVisibility(View.VISIBLE);
    }

    private class CustomTableHistoryScreen extends BaseAdapter {

        TableHistoryScreen activity;
        ArrayList<BookTableHistory.Bookings> tableHistory1;

        public CustomTableHistoryScreen(TableHistoryScreen tableHistoryScreen, ArrayList<BookTableHistory.Bookings> tableHistory) {

            this.activity = tableHistoryScreen;
            this.tableHistory1 = tableHistory;

        }


        @Override
        public int getCount() {
            return tableHistory1.size();
        }

        @Override
        public Object getItem(int i) {
            return tableHistory1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            view = inflater.inflate(R.layout.custom_table_history_screen, null);

            TextView bookingorderNoTV       = (TextView)view.findViewById(R.id.bookingorderNoTV);
            TextView customerNameTV         = (TextView)view.findViewById(R.id.customerNameTV);
            TextView GuestCountTextView     = (TextView)view.findViewById(R.id.GuestCountTextView);
            TextView bookingEmailTextView   = (TextView)view.findViewById(R.id.bookingEmailTextView);
            TextView phoneNoTextView        = (TextView)view.findViewById(R.id.phoneNoTextView);
            TextView storenameTextView      = (TextView)view.findViewById(R.id.storenameTextView);
            TextView bukingDateTimeTextView = (TextView)view.findViewById(R.id.bukingDateTimeTextView);
            TextView bukingStatus           = (TextView)view.findViewById(R.id.bukingStatus);

            bookingorderNoTV.setText(tableHistory1.get(i).id);
            customerNameTV.setText(tableHistory1.get(i).customer_name);
            GuestCountTextView.setText(tableHistory1.get(i).guest_count);
            bookingEmailTextView.setText(tableHistory1.get(i).booking_email);
            phoneNoTextView.setText(tableHistory1.get(i).booking_phone);
            bukingStatus.setText(tableHistory1.get(i).status);
            storenameTextView.setText(tableHistory1.get(i).store_name);
            bukingDateTimeTextView.setText(tableHistory1.get(i).booking_date+" / "+tableHistory1.get(i).booking_time);

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}