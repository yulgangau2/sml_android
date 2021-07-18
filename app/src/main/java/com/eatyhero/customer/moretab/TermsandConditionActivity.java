package com.eatyhero.customer.moretab;

import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.Constens;
import com.eatyhero.customer.common.Utility;

/**
 * Created by admin on 05-04-2017.
 */

public class TermsandConditionActivity extends BaseActivity {

    //Create Objects
    Utility utility;
    TextView actionBarTitle;
    ImageView backIconImageView;
    Constens constens;
    private CoordinatorLayout coordinatorLayout;

    /**************************
     * Create xml objects
     ************************************/
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarTitle = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);
        //coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        /************************** Initializing objects ************************************/
        webView = (WebView) findViewById(R.id.webview);
        webView.clearCache(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);

        utility = Utility.getInstance(this);
        constens = new Constens();



        if (utility.isConnectingToInternet()) {
            webView.loadUrl("https://www.foodorderingsystem.com/pages/terms-conditions");
        } else {
            utility.showAlertDialog(getResources().getString(R.string.noInternet));
        }

        //ActionBar Back Button Click Event
        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }



}

