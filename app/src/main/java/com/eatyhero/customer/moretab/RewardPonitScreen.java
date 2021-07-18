package com.eatyhero.customer.moretab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
import com.eatyhero.customer.model.RewardHistoryList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardPonitScreen extends BaseActivity implements ServerListener {

    Toolbar toolbar;
    TextView actionBarTitleTextView,totalPoints;
    ImageView backIconImageView;
    RewardHistoryList rewardHistoryList;
    RewardListAdapter rewardListAdapter;
    List<RewardHistoryList.CustomerPoints> rewardList1 = new ArrayList<>();

    ServerRequest serverRequest;
    LoginSession loginSession;

    @BindView(R.id.rewardHistoryListView)ListView rewardHistoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_ponit_screen);
        ButterKnife.bind(this);

        serverRequest    = ServerRequest.getInstance(this);
        loginSession = LoginSession.getInstance(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        totalPoints = (TextView) findViewById(R.id.totalPoints);

        actionBarTitleTextView.setText(getResources().getString(R.string.RewardPoints));

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("customer_id", loginSession.getUserId());
            serverRequest.createRequest(RewardPonitScreen.this, params, RequestID.REQ_REWARD_HISTORY);
            showProgressDialog();
        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        rewardHistoryList = (RewardHistoryList) result;
        rewardList1 = rewardHistoryList.result.customerPoints;
        totalPoints.setVisibility(View.VISIBLE);
        totalPoints.setText(getResources().getString(R.string.earnedPoints)+rewardHistoryList.result.totalPoints);
        rewardListAdapter = new RewardListAdapter(RewardPonitScreen.this, rewardList1, this);
        rewardHistoryListView.setAdapter(rewardListAdapter);


    }

    @Override
    public void onFailure(String error, RequestID requestID) {
hideProgressDialog();
    }
    public class RewardListAdapter extends BaseAdapter {

        //Create Objects
        Activity activity;
        RewardPonitScreen rewardHistory;

        LoginSession loginSession;
        ServerRequest serverRequest;

        //Create List
        List<RewardHistoryList.CustomerPoints> allRewards = new ArrayList<>();


        public RewardListAdapter(Activity activity, List<RewardHistoryList.CustomerPoints> allRewards, RewardPonitScreen fragment) {
            this.activity = activity;
            this.allRewards = allRewards;
            this.rewardHistory = fragment;
        }

        @Override
        public int getCount() {

            return allRewards.size();
        }

        @Override
        public Object getItem(int i) {
            return allRewards.get(i);
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
                view = inflater.inflate(R.layout.reward_history_content, null);

            TextView orderIdTextView       = (TextView) view.findViewById(R.id.orderId);
            TextView restaurantname   = (TextView) view.findViewById(R.id.restaurantname);
            TextView totalValue         = (TextView) view.findViewById(R.id.totalValue);
            TextView points = (TextView) view.findViewById(R.id.points);
            TextView dateTime   = (TextView) view.findViewById(R.id.dateTime);
            TextView type    = (TextView) view.findViewById(R.id.type);

            orderIdTextView.setText(allRewards.get(position).order.order_number);
            restaurantname.setText(allRewards.get(position).restaurant_name);
            totalValue.setText(String.format("%.2f",Double.parseDouble(allRewards.get(position).total))+ " " +loginSession.getCurrencyCode());
            points.setText(allRewards.get(position).points+getResources().getString(R.string.points));
            String a[] = allRewards.get(position).created.split("T");
            dateTime.setText(a[0]);
            if ((allRewards.get(position).type).equalsIgnoreCase("SPENT")) {
                type.setTextColor(getResources().getColor(R.color.red));
                type.setText(allRewards.get(position).type);
            } else if((allRewards.get(position).type).equalsIgnoreCase("EARNED")) {
                type.setTextColor(getResources().getColor(R.color.colorGreen));
                type.setText(allRewards.get(position).type);
            }

            return view;
        }

    }
}
