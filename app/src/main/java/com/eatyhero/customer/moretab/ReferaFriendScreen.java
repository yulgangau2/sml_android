package com.eatyhero.customer.moretab;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Shareable;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.ReferHistory;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 06-03-2018.
 */

public class ReferaFriendScreen extends BaseActivity implements ServerListener, View.OnClickListener {

    //Create class objects
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;

    //Create xml files
    @BindView(R.id.actionBarTitleTextView)TextView actionBarTitleTextView;
    @BindView(R.id.backIconImageView)ImageView backIconImageView;
    @BindView(R.id.referHistoryListView)ListView referHistoryListView;
    @BindView(R.id.bannerTextView)TextView bannerTextView;
    @BindView(R.id.referCodeTextView)TextView referCodeTextView;
    @BindView(R.id.copyButton)Button copyButton;
    @BindView(R.id.referCodeUrlEditText)EditText referCodeUrlEditText;
    @BindView(R.id.referLayout)LinearLayout referLayout;
    @BindView(R.id.referHistoryLayout)LinearLayout referHistoryLayout;
    @BindView(R.id.shareRadioButton)RadioButton shareRadioButton;
    @BindView(R.id.viewRadioButton)RadioButton viewRadioButton;
    @BindView(R.id.fb_ShareButton)ShareButton fb_ShareButton;

    @BindView(R.id.referButtonsLayout)LinearLayout referButtonsLayout;
    @BindView(R.id.whatsupShareButton)ImageView whatsupShareButton;
    @BindView(R.id.fbShareButton)ImageView fbShareButton;
    @BindView(R.id.twitterShareButton)ImageView twitterShareButton;
    @BindView(R.id.smsShareButton)ImageView smsShareButton;
    @BindView(R.id.mailShareButton)ImageView mailShareButton;


    String referCode = "";
    String referAmount = "";
    String message= "";
    String appUrl="https://www.foodorderingsystem.com/users/apppage";

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.refera_friend_screen);

        //Initialize xml files
        ButterKnife.bind(this);

        //setClick linstner
        whatsupShareButton.setOnClickListener(this);
        fbShareButton.setOnClickListener(this);
        twitterShareButton.setOnClickListener(this);
        smsShareButton.setOnClickListener(this);
        mailShareButton.setOnClickListener(this);

        //Initialize class objects
        loginSession  = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);
        utility       = Utility.getInstance(this);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //Set actionbar title
        actionBarTitleTextView.setText("Refer a Friend");

        //Get customer info
        getCustomerInfo();

        fb_ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(appUrl))
                            .setContentTitle(message)
                            .build();
                    shareDialog.show(linkContent);
                    fb_ShareButton.setShareContent(linkContent);
                }

            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", referCodeUrlEditText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        shareRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    referLayout.setVisibility(View.VISIBLE);
                    referHistoryLayout.setVisibility(View.GONE);
                    referButtonsLayout.setVisibility(View.VISIBLE);
                }else{

                    referLayout.setVisibility(View.GONE);
                    referHistoryLayout.setVisibility(View.VISIBLE);
                    referButtonsLayout.setVisibility(View.GONE);
                }
            }
        });


        viewRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    referLayout.setVisibility(View.GONE);
                    referHistoryLayout.setVisibility(View.VISIBLE);
                    referButtonsLayout.setVisibility(View.GONE);

                }else{

                    referLayout.setVisibility(View.VISIBLE);
                    referHistoryLayout.setVisibility(View.GONE);
                    referButtonsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void getCustomerInfo() {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("action", "MyAccount");
            params.put("page", "ReferralList");
            params.put("customer_id", loginSession.getUserId());
            serverRequest.createRequest(ReferaFriendScreen.this, params, RequestID.REQ_REFER_HISTORY);
            showProgressDialog();
        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        ReferHistory referHistory = (ReferHistory)result;

        //setBanner TextView
        String text1 = "Invite your friend to Eatyhero, you get "+String.format("%.2f", Double.parseDouble(referHistory.result.referrals.invite_amount))+ " " +loginSession.getCurrencyCode()+".";
        String text2 = "after your friend registered with us.";
        String text3 = "Your friend will also get "+referHistory.result.referrals.receive_amount+ " " +loginSession.getCurrencyCode();
        bannerTextView.setText(Html.fromHtml(text1+"<Br>"+text2+"<Br>"+text3));

        //set refer code
        referAmount = referHistory.result.referrals.receive_amount;
        referCode = referHistory.result.customerDetails.referral_code;
        referCodeTextView.setText(referHistory.result.customerDetails.referral_code);
        referCodeUrlEditText.setText(referHistory.result.customerDetails.referral_url);

        message = "Enter this CODE "+referCode+" when you register with us and get "+referAmount+ " " +loginSession.getCurrencyCode();

        //setreferHistory
        if(!referHistory.result.referredList.isEmpty()){
            viewRadioButton.setVisibility(View.VISIBLE);
            ReferHistoryAdapter referHistoryAdapter = new ReferHistoryAdapter(this,referHistory.result.referredList);
            referHistoryListView.setAdapter(referHistoryAdapter);
        }else{
            viewRadioButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.whatsupShareButton:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String mm = "Enter this CODE *"+referCode+"* when you register with us and get "+referAmount+ " " +loginSession.getCurrencyCode();
                sendIntent.putExtra(Intent.EXTRA_TEXT, mm+" "+appUrl);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);

                break;

            case R.id.fbShareButton:

                fb_ShareButton.performClick();

                break;

            case R.id.twitterShareButton:

                Shareable shareInstance = new Shareable.Builder(this)
                        .message(message)
                        .socialChannel(Shareable.Builder.TWITTER)
                        .url(appUrl)
                        .build();
                shareInstance.share();

                break;

            case R.id.smsShareButton:

                Shareable shareInstanceSMS = new Shareable.Builder(this)
                        .message(message)
                        .socialChannel(Shareable.Builder.MESSAGES)
                        .url(appUrl)
                        .build();
                shareInstanceSMS.share();

                break;

            case R.id.mailShareButton:

                Shareable shareInstanceMAIL = new Shareable.Builder(this)
                        .message(message)
                        .socialChannel(Shareable.Builder.EMAIL)
                        .url(appUrl)
                        .build();
                shareInstanceMAIL.share();

                break;
        }
    }

    //ReferHistoryAdapter class
    private class ReferHistoryAdapter extends BaseAdapter{

        Activity activity;
        ArrayList<ReferHistory.ReferredList> referredLists;

        public ReferHistoryAdapter(Activity activity, ArrayList<ReferHistory.ReferredList> referredLists) {
            this.activity = activity;
            this.referredLists = referredLists;
        }

        @Override
        public int getCount() {
            return referredLists.size();
        }

        @Override
        public Object getItem(int position) {
            return referredLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_referd_history, null);

            TextView referralNameTextView = (TextView)convertView.findViewById(R.id.referralNameTextView);
            TextView statusTextView = (TextView)convertView.findViewById(R.id.statusTextView);
            TextView emailTextView = (TextView)convertView.findViewById(R.id.emailTextView);
            TextView dateTime = (TextView)convertView.findViewById(R.id.dateTime);


            referralNameTextView.setText(referredLists.get(position).first_name+" "+referredLists.get(position).last_name);
            emailTextView.setText(referredLists.get(position).username);

            String returnDate = "";
            try {
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date parsed = sourceFormat.parse(referredLists.get(position).created);
                TimeZone tz = TimeZone.getTimeZone("UTC");
                SimpleDateFormat destFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                destFormat.setTimeZone(tz);
                returnDate = destFormat.format(parsed);

            }catch (Exception e){}

            dateTime.setText(returnDate);

            return convertView;
        }
    }
    public static void shareFacebook(Activity activity, String text, String url) {
        boolean facebookAppFound = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));

        PackageManager pm = activity.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.packageName).contains("com.facebook.katana")) {
                final ActivityInfo activityInfo = app.activityInfo;
                final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setComponent(name);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
            shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        activity.startActivity(shareIntent);
    }

}
