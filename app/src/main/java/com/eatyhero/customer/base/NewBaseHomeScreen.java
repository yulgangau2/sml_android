package com.eatyhero.customer.base;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eatyhero.customer.restauranttab.FavoriteScreen;
import com.facebook.FacebookSdk;
import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginFragment;
import com.eatyhero.customer.baskettab.CardDetailScreenFragment;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.moretab.InfoScreen;
import com.eatyhero.customer.ordertab.NewOrderHistoryScreen;
import com.eatyhero.customer.restauranttab.RestaurantListScreen;

/**
 * Created by admin on 22-01-2018.
 */

public class NewBaseHomeScreen extends BaseActivity implements View.OnClickListener {

    //Create Objects
    private static NewBaseHomeScreen baseHomeScreen;
    Dialog alertDialog;
    LoginSession loginSession;

    //Create xml file
    FrameLayout frameLayout;
    public static CardView baseTabs;
    public static LinearLayout restaurantButton, tablehistoryButton, orderhistoryButton, infoButton,favoriteButton;
    public static TextView cartCountTextView;
    public static ImageView backIconImageView, restaurantImageview, basketImageView, ordersImageview, moreImageview,favoriteImageview;
    public static TextView restaurantTextview, basketTextview, ordersTextview, moreTextview,favoriteTextview;

    public static Utility utility;
    LoginSession loginsession;

    //Create class objects
    public static NewBaseHomeScreen getInstance() {
        baseHomeScreen = new NewBaseHomeScreen();
        return baseHomeScreen;
    }

    boolean locationBaseSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FacebookSdk.sdkInitialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_home_screen);

        //Initialize xml layout
        frameLayout         = (FrameLayout) findViewById(R.id.frameLayout);
        restaurantButton    = (LinearLayout) findViewById(R.id.restaurantButton);
        tablehistoryButton  = (LinearLayout) findViewById(R.id.tablehistoryButton);
        orderhistoryButton  = (LinearLayout) findViewById(R.id.orderhistoryButton);
        infoButton          = (LinearLayout) findViewById(R.id.infoButton);
        favoriteButton          = (LinearLayout) findViewById(R.id.favoriteButton);
        cartCountTextView   = (TextView) findViewById(R.id.cartCountTextView);
        restaurantImageview = (ImageView) findViewById(R.id.restaurantImageview);
        basketImageView     = (ImageView) findViewById(R.id.basketImageView);
        ordersImageview     = (ImageView) findViewById(R.id.ordersImageview);
        moreImageview       = (ImageView) findViewById(R.id.moreImageview);
        restaurantTextview  = (TextView) findViewById(R.id.restaurantTextview);
        basketTextview      = (TextView) findViewById(R.id.basketTextview);
        ordersTextview      = (TextView) findViewById(R.id.ordersTextview);
        moreTextview        = (TextView) findViewById(R.id.moreTextview);
        favoriteTextview        = (TextView) findViewById(R.id.favoriteTextview);
        favoriteImageview        = (ImageView) findViewById(R.id.favoriteImageview);

        //Initialize class objects
        utility      = Utility.getInstance(this);
        loginsession = LoginSession.getInstance(this);
        loginSession = LoginSession.getInstance(this);

        //Set click event
        restaurantButton.setOnClickListener(this);
        tablehistoryButton.setOnClickListener(this);
        orderhistoryButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);

        baseTabs = (CardView) findViewById(R.id.baseTabs);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) baseTabs.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        //Default fragment
        if(utility.APP_START_FRAGMENT.equalsIgnoreCase("tablehistoryButton")){
            locationBaseSearch = false;
            tablehistoryButton.performClick();
        }else if(utility.APP_START_FRAGMENT.equalsIgnoreCase("orderhistoryButton")){
            locationBaseSearch = false;
            orderhistoryButton.performClick();
        }else if(utility.APP_START_FRAGMENT.equalsIgnoreCase("infoButton")){
            locationBaseSearch = false;
            infoButton.performClick();
        } else if (utility.APP_START_FRAGMENT.equalsIgnoreCase("favoriteButton")) {
            locationBaseSearch = false;
            favoriteButton.performClick();
        } else if (utility.APP_START_FRAGMENT.equalsIgnoreCase("searchButton")){
            locationBaseSearch = true;
            if (frameLayout != null) {
                RestaurantListScreen restaurantListScreen = new RestaurantListScreen();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, restaurantListScreen);
                ft.commit();
            }
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.restaurantButton:

                if(locationBaseSearch){

                    restaurantImageview.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_color_icon));
                    restaurantTextview.setTextColor(getResources().getColor(R.color.colorPrimary));
                    ordersImageview.setImageDrawable(getResources().getDrawable(R.drawable.orders_icon));
                    ordersTextview.setTextColor(getResources().getColor(R.color.dimblack));
                    moreImageview.setImageDrawable(getResources().getDrawable(R.drawable.more_icon));
                    moreTextview.setTextColor(getResources().getColor(R.color.dimblack));
                    basketImageView.setImageDrawable(getResources().getDrawable(R.drawable.basket_icon));
                    basketTextview.setTextColor(getResources().getColor(R.color.dimblack));
                    favoriteImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
                    favoriteTextview.setTextColor(getResources().getColor(R.color.dimblack));

                    if(!utility.CURRENT_SCREEN.equalsIgnoreCase("RESTAURANT_LIST")){

                        RestaurantListScreen restaurantListScreen = new RestaurantListScreen();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout, restaurantListScreen);
                        ft.commit();
                    }
                }else{

                    Intent intent = new Intent(NewBaseHomeScreen.this,NewLocationScreen.class);
                    startActivity(intent);
                    this.finish();
                }

                break;

            case R.id.tablehistoryButton:

                restaurantImageview.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_icon));
                restaurantTextview.setTextColor(getResources().getColor(R.color.dimblack));
                ordersImageview.setImageDrawable(getResources().getDrawable(R.drawable.orders_icon));
                ordersTextview.setTextColor(getResources().getColor(R.color.dimblack));
                moreImageview.setImageDrawable(getResources().getDrawable(R.drawable.more_icon));
                moreTextview.setTextColor(getResources().getColor(R.color.dimblack));
                basketImageView.setImageDrawable(getResources().getDrawable(R.drawable.basket_color_icon));
                basketTextview.setTextColor(getResources().getColor(R.color.colorPrimary));
                favoriteImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
                favoriteTextview.setTextColor(getResources().getColor(R.color.dimblack));

                if(!utility.CURRENT_SCREEN.equalsIgnoreCase("CARTDETAILS_SCREEN")) {

                    CardDetailScreenFragment cardDetailScreen = new CardDetailScreenFragment();
                    FragmentManager fm1 = getSupportFragmentManager();
                    FragmentTransaction ft1 = fm1.beginTransaction();
                    fm1.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft1.replace(R.id.frameLayout, cardDetailScreen);
                    ft1.commit();
                }
                break;


            case R.id.orderhistoryButton:

                restaurantImageview.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_icon));
                restaurantTextview.setTextColor(getResources().getColor(R.color.dimblack));
                ordersImageview.setImageDrawable(getResources().getDrawable(R.drawable.orders_color_icon));
                ordersTextview.setTextColor(getResources().getColor(R.color.colorPrimary));
                moreImageview.setImageDrawable(getResources().getDrawable(R.drawable.more_icon));
                moreTextview.setTextColor(getResources().getColor(R.color.dimblack));
                basketImageView.setImageDrawable(getResources().getDrawable(R.drawable.basket_icon));
                basketTextview.setTextColor(getResources().getColor(R.color.dimblack));
                favoriteImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
                favoriteTextview.setTextColor(getResources().getColor(R.color.dimblack));

                if(!utility.CURRENT_SCREEN.equalsIgnoreCase("ORDER_HISTORY_SCREEN")) {

                    NewOrderHistoryScreen newOrderHistoryScreen = new NewOrderHistoryScreen();
                    FragmentManager fm2 = getSupportFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    ft2.replace(R.id.frameLayout, newOrderHistoryScreen);
                    ft2.commit();
                }


                break;

            case R.id.infoButton:

                restaurantImageview.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_icon));
                restaurantTextview.setTextColor(getResources().getColor(R.color.dimblack));
                basketImageView.setImageDrawable(getResources().getDrawable(R.drawable.basket_icon));
                basketTextview.setTextColor(getResources().getColor(R.color.dimblack));
                ordersImageview.setImageDrawable(getResources().getDrawable(R.drawable.orders_icon));
                ordersTextview.setTextColor(getResources().getColor(R.color.dimblack));
                moreImageview.setImageDrawable(getResources().getDrawable(R.drawable.more_color_icon));
                moreTextview.setTextColor(getResources().getColor(R.color.colorPrimary));
                favoriteImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border));
                favoriteTextview.setTextColor(getResources().getColor(R.color.dimblack));

                if(!utility.CURRENT_SCREEN.equalsIgnoreCase("INFO_SCREEN")) {

                    if (frameLayout != null) {

                        if (loginsession.isLoggedIn()) {

                            InfoScreen infoScreen = new InfoScreen();
                            FragmentManager fm3 = getSupportFragmentManager();
                            FragmentTransaction ft3 = fm3.beginTransaction();
                            ft3.replace(R.id.frameLayout, infoScreen);
                            ft3.commit();

                        } else {

                            LoginFragment newInfoScreen = new LoginFragment();
                            FragmentManager fm3 = getSupportFragmentManager();
                            FragmentTransaction ft3 = fm3.beginTransaction();
                            ft3.replace(R.id.frameLayout, newInfoScreen);
                            ft3.commit();

                        }

                    }
                }
                break;

            case R.id.favoriteButton:

                restaurantImageview.setImageDrawable(getResources().getDrawable(R.drawable.restaurant_icon));
                restaurantTextview.setTextColor(getResources().getColor(R.color.dimblack));
                ordersImageview.setImageDrawable(getResources().getDrawable(R.drawable.orders_icon));
                ordersTextview.setTextColor(getResources().getColor(R.color.dimblack));
                moreImageview.setImageDrawable(getResources().getDrawable(R.drawable.more_icon));
                moreTextview.setTextColor(getResources().getColor(R.color.dimblack));
                basketImageView.setImageDrawable(getResources().getDrawable(R.drawable.basket_icon));
                basketTextview.setTextColor(getResources().getColor(R.color.dimblack));
                favoriteImageview.setImageDrawable(getResources().getDrawable(R.drawable.favorite_color));
                favoriteTextview.setTextColor(getResources().getColor(R.color.colorPrimary));

                if(!utility.CURRENT_SCREEN.equalsIgnoreCase("FAVORITE_SCREEN")) {

                    FavoriteScreen favoriteScreen = new FavoriteScreen();
                    FragmentManager fm2 = getSupportFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    ft2.replace(R.id.frameLayout, favoriteScreen);
                    ft2.commit();
                }


                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(utility.filterDialogueopen.equalsIgnoreCase("YES")){

            RestaurantListScreen.closeFilterDialogue();

        }else{
            finish();
        }


    }

    private void showAlertDialog() {

        if (alertDialog == null) {

            alertDialog = new Dialog(this);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.dialog_for_alert);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        TextView noTextView = (TextView) alertDialog.findViewById(R.id.noTextView);
        TextView yesTextView = (TextView) alertDialog.findViewById(R.id.yesTextView);

        noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }
}
