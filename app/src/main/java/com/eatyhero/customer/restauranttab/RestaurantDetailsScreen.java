package com.eatyhero.customer.restauranttab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginScreen;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.baskettab.NewCartDetailsScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.NonScrollExpandableListView;
import com.eatyhero.customer.common.NonScrollListView;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.CartDetails;
import com.eatyhero.customer.model.FavouritModel;
import com.eatyhero.customer.model.RestaurantMenuListResponse;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by admin on 22-01-2018.
 */

public class RestaurantDetailsScreen extends BaseActivity implements ServerListener {

    //Create Objects
    private static Dialog replaceCartAlert;
    private static LoginSession loginSession;
    private static ServerRequest serverRequest;
    private static DatabaseManager databaseManager;

    //Create xml file

    @BindView(R.id.backIconImageView)
    ImageView backIconImageView;
    @BindView(R.id.bookTableTV)
    TextView bookTableTV;
    @BindView(R.id.contentLayout)
    RelativeLayout contentLayout;
    @BindView(R.id.restaurantImageView)
    ImageView restaurantImageView;
    @BindView(R.id.actionBarTitleTextView)
    TextView actionBarTitleTextView;
    @BindView(R.id.restaurantNameTextView)
    TextView restaurantNameTextView;
    @BindView(R.id.restaurantItemsTextView)
    TextView restaurantItemsTextView;
    @BindView(R.id.mainRatingBar)
    RatingBar mainRatingBar;

    @BindView(R.id.miniorderTextView)
    TextView miniorderTextView;
    @BindView(R.id.distanceTextView)
    TextView distanceTextView;
    @BindView(R.id.deliveryFeesTextView)
    TextView deliveryFeesTextView;

    @BindView(R.id.restaurantMenuGroup)
    RadioGroup restaurantMenuGroup;
    @BindView(R.id.menuButton)
    RadioButton menuButton;
    @BindView(R.id.dealButton)
    RadioButton dealButton;
    @BindView(R.id.infoButton)
    RadioButton infoButton;
    @BindView(R.id.offerButton)
    RadioButton offerButton;
    @BindView(R.id.reviewButton)
    RadioButton reviewButton;
    @BindView(R.id.rewardButton)
    RadioButton rewardButton;

    @BindView(R.id.openCloseListView)
    ListView openCloseListView;
    @BindView(R.id.infoLinerLayout)
    LinearLayout infoLinerLayout;
    @BindView(R.id.dealsMenuListView)
    ExpandableListView dealsMenuListView;
    @BindView(R.id.offerLinerLayout)
    LinearLayout offerLinerLayout;
    @BindView(R.id.rewardLinerLayout)
    LinearLayout rewardLinerLayout;
    @BindView(R.id.offerTexView)
    TextView offerTexView;
    @BindView(R.id.offerTexView2)
    TextView offerTexView2;
    @BindView(R.id.offerTexView3)
    TextView offerTexView3;
    @BindView(R.id.offerImageView)
    ImageView offerImageView;
    @BindView(R.id.reviewLinerLayout)
    LinearLayout reviewLinerLayout;
    @BindView(R.id.firstUserOfferLayout)
    LinearLayout firstUserOfferLayout;
    @BindView(R.id.normalOfferLayout)
    LinearLayout normalOfferLayout;
    @BindView(R.id.deliveryLayoutOfferLayout)
    LinearLayout deliveryLayoutOfferLayout;
    @BindView(R.id.reviewListView)
    ListView reviewListView;

    @BindView(R.id.topLayout)
    RelativeLayout topLayout;

    private static RelativeLayout bottomCartLayout;
    private static TextView itemTextView;
    private static TextView amountTextView;


    @BindView(R.id.favoriteLatout)
    LinearLayout favoriteLatout;
    @BindView(R.id.likeImageview)
    ImageView likeImageview;
    @BindView(R.id.likeCount)
    TextView likeCount;

    @BindView(R.id.productLayout)
    LinearLayout productLayout;

    @BindView(R.id.rewardLayout)
    RelativeLayout rewardLayout;
    @BindView(R.id.rewardTextView)
    TextView rewardTextView;

    String cuisineNames = "";
    String deliveryCharge = "";
    String distance = "";
    String rating = "";
    static String restaurantName = "";
    static String resId;

    @BindView(R.id.searchIcon)
    ImageView searchIcon;
    public static EditText searchEditText;


    FragmentStatePagerAdapter adapter;
    ViewPager pager;
    TabLayout indicator;

    boolean lensOpen = false;


    StoreTiming storeTiming;
    ArrayList<StoreTiming> storeTimings = new ArrayList<>();
    RestaurantInfoAdapter restaurantInfoAdapter;
    DealCategoryAdapter dealCategoryAdapter;
    boolean liked;

    ArrayList<StoreTiming> restaurantOpenCloses = new ArrayList<>();

    ArrayList<String> displayMainCatName = new ArrayList<>();
    private static List<List<RestaurantMenuListResponse.Menu>> displaySubCateList = new ArrayList<>();
    private static List<RestaurantMenuListResponse.Menu> menuitems = new ArrayList<>();


    String FIRST_USER = "", FIRST_OFFER_PER = "", FIRST_OFFER_AMOUNT = "", NORMAL_USER = "", NORMAL_OFFER_PER = "", NORMAL_OFFER_AMOUNT = "";
    String codPayment, stripePayment, paypalPayment;

    static RestaurantMenuListResponse restaurantMenuListResponse;
    FavouritModel favouritModel;

    String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details_screen);

        //Initialize xml objects
        ButterKnife.bind(this);
        LayerDrawable stars = (LayerDrawable) mainRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        //Initialize class objects
        loginSession = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);
        databaseManager = DatabaseManager.getInstance(this);

        bottomCartLayout = (RelativeLayout) findViewById(R.id.bottomCartLayout);
        itemTextView = (TextView) findViewById(R.id.itemTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        searchEditText = findViewById(R.id.searchEditText);

        Intent intent = getIntent();
        if (intent != null) {
            getRestaurantMenuListResponse(intent.getStringExtra("res_id"));
            resId = intent.getStringExtra("res_id");
            cuisineNames = intent.getStringExtra("res_cuisineLists");
            deliveryCharge = intent.getStringExtra("res_deliveryCharge");
            distance = intent.getStringExtra("res_distance");
            rating = intent.getStringExtra("rating");

            loginSession.setcusinName(cuisineNames);
            loginSession.setdelivery(deliveryCharge);
            loginSession.setdistance(distance);
            loginSession.setrating(rating);

        }



        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lensOpen) {
                    searchEditText.setText("");
                    searchEditText.setVisibility(GONE);
                    lensOpen = false;
                } else {
                    searchEditText.setVisibility(View.VISIBLE);
                    lensOpen = true;
                }
            }
        });

        indicator = (TabLayout) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);

        adapter = new Swipetab(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setupWithViewPager(pager);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter = new Swipetab(getSupportFragmentManager());
                pager.setAdapter(adapter);
                indicator.setupWithViewPager(pager);
            }
        });

        currency = loginSession.getCurrencyCode();


        actionBarTitleTextView.setText(getResources().getString(R.string.categories));

        restaurantMenuGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {

                    case R.id.menuButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.BOLD);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.NORMAL);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.NORMAL);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);

                        reviewLinerLayout.setVisibility(GONE);
                        offerLinerLayout.setVisibility(GONE);
                        infoLinerLayout.setVisibility(GONE);
                        productLayout.setVisibility(View.VISIBLE);
                        dealsMenuListView.setVisibility(GONE);
                        rewardLinerLayout.setVisibility(GONE);

                        break;

                    case R.id.dealButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.BOLD);
                        dealButton.setTypeface(dealButton.getTypeface(), Typeface.BOLD);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.NORMAL);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.NORMAL);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);

                        reviewLinerLayout.setVisibility(GONE);
                        offerLinerLayout.setVisibility(GONE);
                        infoLinerLayout.setVisibility(GONE);
                        productLayout.setVisibility(GONE);
                        dealsMenuListView.setVisibility(View.VISIBLE);
                        rewardLinerLayout.setVisibility(GONE);

                        break;

                    case R.id.infoButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.NORMAL);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.BOLD);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.NORMAL);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);

                        reviewLinerLayout.setVisibility(GONE);
                        offerLinerLayout.setVisibility(GONE);
                        infoLinerLayout.setVisibility(View.VISIBLE);
                        productLayout.setVisibility(GONE);
                        dealsMenuListView.setVisibility(GONE);
                        rewardLinerLayout.setVisibility(GONE);

                        break;

                    case R.id.offerButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.NORMAL);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.NORMAL);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.BOLD);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);

                        reviewLinerLayout.setVisibility(GONE);
                        offerLinerLayout.setVisibility(View.VISIBLE);
                        infoLinerLayout.setVisibility(GONE);
                        productLayout.setVisibility(GONE);
                        dealsMenuListView.setVisibility(GONE);
                        rewardLinerLayout.setVisibility(GONE);

                        break;

                    case R.id.reviewButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.NORMAL);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.NORMAL);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.NORMAL);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.BOLD);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);

                        reviewLinerLayout.setVisibility(View.VISIBLE);
                        offerLinerLayout.setVisibility(GONE);
                        infoLinerLayout.setVisibility(GONE);
                        productLayout.setVisibility(GONE);
                        dealsMenuListView.setVisibility(GONE);
                        rewardLinerLayout.setVisibility(GONE);

                        break;

                    case R.id.rewardButton:

                        menuButton.setTypeface(menuButton.getTypeface(), Typeface.NORMAL);
                        infoButton.setTypeface(infoButton.getTypeface(), Typeface.NORMAL);
                        offerButton.setTypeface(offerButton.getTypeface(), Typeface.NORMAL);
                        reviewButton.setTypeface(reviewButton.getTypeface(), Typeface.NORMAL);
                        rewardButton.setTypeface(reviewButton.getTypeface(), Typeface.BOLD);

                        reviewLinerLayout.setVisibility(GONE);
                        offerLinerLayout.setVisibility(GONE);
                        infoLinerLayout.setVisibility(GONE);
                        productLayout.setVisibility(GONE);
                        dealsMenuListView.setVisibility(GONE);
                        rewardLinerLayout.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });

        bottomCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(RestaurantDetailsScreen.this, NewCartDetailsScreen.class);
                startActivity(intent1);
            }
        });

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        bookTableTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loginSession.isLoggedIn()) {

                    Intent bookatable = new Intent(RestaurantDetailsScreen.this, BookATableScreen.class);
                    bookatable.putExtra("openclose", (Serializable) restaurantOpenCloses);
                    startActivity(bookatable);

                } else {

                    Intent intent1 = new Intent(RestaurantDetailsScreen.this, LoginScreen.class);
                    startActivity(intent1);
                }


            }
        });


        /*scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                if (scrollY > 365) {

                    actionBarTitleTextView.setText(restaurantName);
                } else {

                    actionBarTitleTextView.setText(getResources().getString(R.string.categories));
                }

            }
        });*/

        likeImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (liked) {
                    likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_red_color));
                    liked = false;
                    getlike("0");
                    Intent intent = new Intent();
                    intent.putExtra("id",resId);
                    intent.putExtra("boolean",false);
                    setResult(200,intent);
                } else {
                    likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_full_red_color));
                    liked = true;
                    getlike("1");
                    Intent intent = new Intent();
                    intent.putExtra("id",resId);
                    intent.putExtra("boolean",true);
                    setResult(200,intent);
                }
            }
        });


    }

    private void getlike(String like) {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("chval", like);
            params.put("action", "changeFavStatus");
            params.put("page", "changeResFavStatus");
            params.put("resId", resId);
            params.put("userId", loginSession.getUserId());
            params.put("resName", restaurantName);
            showProgressDialog();
            serverRequest.createRequest(RestaurantDetailsScreen.this, params, RequestID.REQ_CHANGE_FAV_STATUS);

        } else {
            noInternetAlertDialog();
        }
    }


    private void getRestaurantMenuListResponse(String res_id) {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("resId", res_id);
            params.put("customer_id", loginSession.getUserId());
            showProgressDialog();
            serverRequest.createRequest(RestaurantDetailsScreen.this, params, RequestID.REQ_TO_GET_STOREITEMS);

        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID) {

            case REQ_TO_GET_STOREITEMS:

                displayMainCatName.clear();
                displaySubCateList.clear();
                menuitems.clear();

                restaurantMenuListResponse = (RestaurantMenuListResponse) result;
//                Log.e("YESNO", restaurantMenuListResponse.result.restDetails.reward_option);
                if (restaurantMenuListResponse.result.restDetails.reward_option != null) {

                    if (restaurantMenuListResponse.result.restDetails.reward_option.equalsIgnoreCase("Yes")) {

                        rewardButton.setVisibility(GONE);
                        rewardTextView.setText(restaurantMenuListResponse.result.restDetails.reward_text);

                    } else {

                        rewardButton.setVisibility(GONE);
                    }
                }

                if (getIntent().getStringExtra("res_distance") == null) {
                    cuisineNames = restaurantMenuListResponse.result.restDetails.cuisineLists;
                    deliveryCharge = restaurantMenuListResponse.result.restDetails.delivery_charge;
                    distance = restaurantMenuListResponse.result.restDetails.to_distance;
                    rating = restaurantMenuListResponse.result.restDetails.finalReview;

                    loginSession.setcusinName(cuisineNames);
                    loginSession.setdelivery(deliveryCharge);
                    loginSession.setdistance(distance);
                    loginSession.setrating(rating);
                }


                restaurantName = restaurantMenuListResponse.result.restDetails.restaurant_name;
                restaurantNameTextView.setText(restaurantMenuListResponse.result.restDetails.restaurant_name);
                restaurantItemsTextView.setText(cuisineNames.replace(",", ", "));

                if (rating.equals("") || rating.isEmpty()) {
                } else {
                    mainRatingBar.setRating(Float.parseFloat(rating) / 20f);
                }

                distanceTextView.setText(restaurantMenuListResponse.result.restDetails.estimate_time + " mins");
                deliveryFeesTextView.setText(deliveryCharge + " " + loginSession.getCurrencyCode());
                miniorderTextView.setText(String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.minimum_order)) + " " + loginSession.getCurrencyCode());
                if (!restaurantMenuListResponse.result.restDetails.restaurant_logo.isEmpty()) {
                    Picasso.with(this)
                            .load(restaurantMenuListResponse.result.restDetails.restaurant_logo)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(restaurantImageView);
                }

                if (restaurantMenuListResponse.result.restDetails.restaurant_booktable.equalsIgnoreCase("YES")) {
                    bookTableTV.setVisibility(View.VISIBLE);
                } else {
                    bookTableTV.setVisibility(GONE);
                }

                if (loginSession.isLoggedIn() == false && restaurantMenuListResponse.result.restDetails.favCount.equalsIgnoreCase("") && restaurantMenuListResponse.result.restDetails.likeCount.equalsIgnoreCase("")) {
                    favoriteLatout.setVisibility(GONE);
                } else {
                    if (!restaurantMenuListResponse.result.restDetails.likeCount.equalsIgnoreCase("")) {
                        likeCount.setText(getResources().getString(R.string.like) + " " + restaurantMenuListResponse.result.restDetails.likeCount);
                    }
                    if (restaurantMenuListResponse.result.restDetails.favCount.equalsIgnoreCase("0")) {
                        likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_red_color));
                        liked = false;
                    } else if (restaurantMenuListResponse.result.restDetails.favCount.equalsIgnoreCase("1")) {
                        likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_full_red_color));
                        liked = true;
                    }
                }

                //SetMenuListView
//                ArrayList<String> listDataHeader = new ArrayList<String>();
//                HashMap<String, ArrayList<RestaurantMenuListResponse.Menu>> listDataChild = new HashMap<>();
                displayMainCatName.add("All");
                displaySubCateList.add(restaurantMenuListResponse.result.restDetails.allMenus);

                for (RestaurantMenuListResponse.MenusDetails menusDetails : restaurantMenuListResponse.result.restDetails.menusDetails) {

                    if (menusDetails.status.equalsIgnoreCase("1") && menusDetails.delete_status.equalsIgnoreCase("N")) {

                        Log.e("good", "verygood");
//                        listDataHeader.add(menusDetails.category_name);
//                        listDataChild.put(menusDetails.category_name, menusDetails.menu);
                        displayMainCatName.add(menusDetails.category_name);
                        displaySubCateList.add(menusDetails.menu);
                    }

                }

                adapter.notifyDataSetChanged();
                pager.setOffscreenPageLimit(displayMainCatName.size());


                //SetMenuListView
                ArrayList<String> listDealHeader = new ArrayList<String>();
                HashMap<String, ArrayList<RestaurantMenuListResponse.DealProducts>> listDealChild = new HashMap<>();

                if (restaurantMenuListResponse.result.dealProducts != null &&
                        restaurantMenuListResponse.result.dealProducts.size() > 0) {

                    for (RestaurantMenuListResponse.DealProducts menusDetails : restaurantMenuListResponse.result.dealProducts) {

                        if (menusDetails.MainProduct.status.equalsIgnoreCase("1") && menusDetails.MainProduct.delete_status.equalsIgnoreCase("N")) {

                            listDealHeader.add(menusDetails.MainProduct.category.category_name);
                            listDealChild.put(menusDetails.MainProduct.category.category_name, restaurantMenuListResponse.result.dealProducts);
                        }
                        break;
                    }
                    dealCategoryAdapter = new DealCategoryAdapter(this, listDealHeader, listDealChild);
                    dealsMenuListView.setAdapter(dealCategoryAdapter);

                    for (int i = 0; i < dealCategoryAdapter.getGroupCount(); i++)
                        dealsMenuListView.expandGroup(i);

                } else {
                    dealButton.setVisibility(GONE);
                }

                //SetInfo Section
                //Monday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.monday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.monday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.monday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.monday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Monday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.monday_status);
                storeTimings.add(storeTiming);

                //Tuesday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.tuesday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.tuesday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.tuesday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.tuesday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Tuesday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.tuesday_status);
                storeTimings.add(storeTiming);

                //Wednesday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.wednesday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.wednesday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.wednesday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.wednesday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Wednesday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.wednesday_status);
                storeTimings.add(storeTiming);

                //Thursday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.thursday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.thursday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.thursday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.thursday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Thursday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.thursday_status);
                storeTimings.add(storeTiming);

                //Friday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.friday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.friday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.friday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.friday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Friday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.friday_status);
                storeTimings.add(storeTiming);

                //Saturday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.saturday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.saturday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.saturday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.saturday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Saturday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.saturday_status);
                storeTimings.add(storeTiming);

                //Sunday
                storeTiming = new StoreTiming();
                storeTiming.setFirstOpen(restaurantMenuListResponse.result.restDetails.sunday_first_opentime);
                storeTiming.setFirstClose(restaurantMenuListResponse.result.restDetails.sunday_first_closetime);
                storeTiming.setSecondOpen(restaurantMenuListResponse.result.restDetails.sunday_second_opentime);
                storeTiming.setSecondClose(restaurantMenuListResponse.result.restDetails.sunday_second_closetime);
                storeTiming.setDate(getResources().getString(R.string.Sunday));
                storeTiming.setStatus(restaurantMenuListResponse.result.restDetails.sunday_status);
                storeTimings.add(storeTiming);

                restaurantInfoAdapter = new RestaurantInfoAdapter(this, storeTimings);
                openCloseListView.setAdapter(restaurantInfoAdapter);


                //Custom Review
                Log.e("reviews", "" + String.valueOf(restaurantMenuListResponse.result.restDetails.reviews));
                if (restaurantMenuListResponse.result.restDetails.reviews.isEmpty()) {
                    reviewButton.setVisibility(GONE);
                } else {
                    reviewButton.setVisibility(View.VISIBLE);
                }
                CustomReviewclass customReviewclass = new CustomReviewclass(this, restaurantMenuListResponse.result.restDetails.reviews);
                reviewListView.setAdapter(customReviewclass);


                if (!restaurantMenuListResponse.result.restDetails.restOffers.isEmpty()) {

                    offerButton.setVisibility(View.VISIBLE);

                    if (restaurantMenuListResponse.result.restDetails.restOffers.size() > 0) {

                        if (restaurantMenuListResponse.result.restDetails.restOffers.get(0).first_user.equalsIgnoreCase("Y")) {
                            String value = restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_percentage;
                            if (!value.isEmpty() && !value.equals("0")) {
                                firstUserOfferLayout.setVisibility(View.VISIBLE);
                                offerTexView.setText(getResources().getString(R.string.flat) + " " + value + getResources().getString(R.string.orderAbove) + " " + String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_price)) + " " + loginSession.getCurrencyCode());

                                FIRST_USER = "YES";
                                FIRST_OFFER_PER = restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_percentage;
                                FIRST_OFFER_AMOUNT = restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_price;

                            } else {

                                firstUserOfferLayout.setVisibility(GONE);

                                FIRST_USER = "NO";
                                FIRST_OFFER_PER = restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_percentage;
                                FIRST_OFFER_AMOUNT = restaurantMenuListResponse.result.restDetails.restOffers.get(0).free_price;

                            }
                        } else {

                            firstUserOfferLayout.setVisibility(GONE);

                            FIRST_USER = "NO";
                            FIRST_OFFER_PER = "0";
                            FIRST_OFFER_AMOUNT = "0";
                        }

                        if (restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal.equalsIgnoreCase("Y")) {
                            String value = restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_percentage;
                            if (!value.isEmpty() && !value.equals("0")) {
                                normalOfferLayout.setVisibility(View.VISIBLE);
                                offerTexView2.setText(getResources().getString(R.string.flat) + " " + value + getResources().getString(R.string.normalOfferText) + " " + String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_price)) + " " + loginSession.getCurrencyCode());

                                NORMAL_USER = "YES";
                                NORMAL_OFFER_PER = restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_percentage;
                                NORMAL_OFFER_AMOUNT = restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_price;

                            } else {
                                normalOfferLayout.setVisibility(GONE);

                                NORMAL_USER = "NO";
                                NORMAL_OFFER_PER = restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_percentage;
                                NORMAL_OFFER_AMOUNT = restaurantMenuListResponse.result.restDetails.restOffers.get(0).normal_price;

                            }
                        } else {
                            normalOfferLayout.setVisibility(GONE);

                            NORMAL_USER = "NO";
                            NORMAL_OFFER_PER = "0";
                            NORMAL_OFFER_AMOUNT = "0";

                        }

                        if (!restaurantMenuListResponse.result.restDetails.free_delivery.isEmpty() && !restaurantMenuListResponse.result.restDetails.free_delivery.equalsIgnoreCase("0")) {

                            Log.e("enter", "1");
                            Log.e("entervalue", restaurantMenuListResponse.result.restDetails.free_delivery);
                            deliveryLayoutOfferLayout.setVisibility(View.VISIBLE);
                            offerTexView3.setText(getResources().getString(R.string.makeAnOrder) + " " + String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.free_delivery)) + " " + loginSession.getCurrencyCode() + " " + getResources().getString(R.string.freeDeliveryOffer));

                        } else {

                            deliveryLayoutOfferLayout.setVisibility(GONE);
//                            offerButton.setVisibility(View.GONE);
                        }

                    } else {

                        if (!restaurantMenuListResponse.result.restDetails.free_delivery.isEmpty() && !restaurantMenuListResponse.result.restDetails.free_delivery.equals("0")) {
                            Log.e("enter", "2");
                            deliveryLayoutOfferLayout.setVisibility(View.VISIBLE);
                            offerTexView3.setText(getResources().getString(R.string.makeAnOrder) + " " + String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.free_delivery)) + " " + loginSession.getCurrencyCode() + " " + getResources().getString(R.string.freeDeliveryOffer));

                            FIRST_USER = "NO";
                            NORMAL_USER = "NO";
                            FIRST_OFFER_PER = "0";
                            FIRST_OFFER_AMOUNT = "0";
                            NORMAL_OFFER_PER = "0";
                            NORMAL_OFFER_AMOUNT = "0";

                        } else {

                            deliveryLayoutOfferLayout.setVisibility(GONE);
                            offerButton.setVisibility(GONE);

                            FIRST_USER = "NO";
                            NORMAL_USER = "NO";
                            FIRST_OFFER_PER = "0";
                            FIRST_OFFER_AMOUNT = "0";
                            NORMAL_OFFER_PER = "0";
                            NORMAL_OFFER_AMOUNT = "0";

                        }
                    }

                } else {

                    if (!restaurantMenuListResponse.result.restDetails.free_delivery.isEmpty() && !restaurantMenuListResponse.result.restDetails.free_delivery.equals("0")) {
                        Log.e("enter", "3");
                        offerButton.setVisibility(View.VISIBLE);
                        deliveryLayoutOfferLayout.setVisibility(View.VISIBLE);
                        offerTexView3.setText(getResources().getString(R.string.makeAnOrder) + " " + String.format("%.2f", Double.parseDouble(restaurantMenuListResponse.result.restDetails.free_delivery)) + " " + loginSession.getCurrencyCode() + " " + getResources().getString(R.string.freeDeliveryOffer));

                        FIRST_USER = "NO";
                        NORMAL_USER = "NO";
                        FIRST_OFFER_PER = "0";
                        FIRST_OFFER_AMOUNT = "0";
                        NORMAL_OFFER_PER = "0";
                        NORMAL_OFFER_AMOUNT = "0";

                    } else {

                        deliveryLayoutOfferLayout.setVisibility(GONE);
                        offerButton.setVisibility(GONE);

                        FIRST_USER = "NO";
                        NORMAL_USER = "NO";
                        FIRST_OFFER_PER = "0";
                        FIRST_OFFER_AMOUNT = "0";
                        NORMAL_OFFER_PER = "0";
                        NORMAL_OFFER_AMOUNT = "0";
                    }

                }

                ArrayList<String> paymentnames = new ArrayList<>();
                paymentnames.clear();
                if (!restaurantMenuListResponse.result.restDetails.restaurant_payments.isEmpty()) {
                    for (RestaurantMenuListResponse.RestaurantPayments restaurantPayments : restaurantMenuListResponse.result.restDetails.restaurant_payments) {

                        paymentnames.add(restaurantPayments.payment_method.payment_method_name.toUpperCase());

                    }
                    if (paymentnames.contains("COD")) {
                        codPayment = "YES";
                    } else {
                        codPayment = "NO";
                    }
                    if (paymentnames.contains("STRIPE")) {
                        stripePayment = "YES";
                    } else {
                        stripePayment = "NO";
                    }
                    if (paymentnames.contains("PAYPAL")) {
                        paypalPayment = "YES";
                    } else {
                        paypalPayment = "NO";
                    }

                } else {

                    codPayment = "NO";
                    stripePayment = "NO";
                    paypalPayment = "NO";
                }


                //Save Restaurant Details
                loginSession.saveResDetails(
                        restaurantMenuListResponse.result.restDetails.id,
                        restaurantMenuListResponse.result.restDetails.restaurant_name,
                        restaurantMenuListResponse.result.restDetails.contact_address,
                        deliveryCharge,
                        restaurantMenuListResponse.result.restDetails.free_delivery,
                        restaurantMenuListResponse.result.restDetails.restaurant_tax,
                        restaurantMenuListResponse.result.restDetails.minimum_order,
                        restaurantMenuListResponse.result.restDetails.restaurant_delivery,
                        restaurantMenuListResponse.result.restDetails.restaurant_pickup,
                        FIRST_USER,
                        NORMAL_USER,
                        FIRST_OFFER_PER,
                        FIRST_OFFER_AMOUNT,
                        NORMAL_OFFER_PER,
                        NORMAL_OFFER_AMOUNT,
                        restaurantMenuListResponse.result.restDetails.currentStatus,
                        restaurantMenuListResponse.result.restDetails.first_user,
                        codPayment,
                        stripePayment,
                        paypalPayment);


                if (loginSession.isLoggedIn()) {
                    loginSession.setFirstUser(restaurantMenuListResponse.result.restDetails.first_user);
                }


                contentLayout.setVisibility(View.VISIBLE);
                topLayout.setVisibility(View.VISIBLE);
                restaurantNameTextView.requestFocus();

                break;

            case REQ_CHANGE_FAV_STATUS:

                favouritModel = (FavouritModel) result;
                toast(favouritModel.result.message);

                hideProgressDialog();
                finish();
                startActivity(getIntent());

        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
    }

//*****************************//////////++++++++++++++++++++++++++++++++++///////////*******************************************

    //code for list and swipe a category
    private class Swipetab extends FragmentStatePagerAdapter {

        public Swipetab(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new SwipeList();

            try {

//                Log.e("swipeTab","1st");
                //sending Category name to the listview through bundle
                Bundle bundle = new Bundle();

                if (searchEditText.getText().toString().isEmpty()) {

                    bundle.putSerializable("Menuitems", (Serializable) displaySubCateList.get(position));
                    fragment.setArguments(bundle);
                } else {
                    List<RestaurantMenuListResponse.Menu> filteredMenuList = new ArrayList<RestaurantMenuListResponse.Menu>();

                    for (int i = 0; i < displaySubCateList.get(position).size(); ++i) {
                        if (displaySubCateList.get(position).get(i).menu_name.toLowerCase().contains(searchEditText.getText().toString().toLowerCase())) {
                            filteredMenuList.add(displaySubCateList.get(position).get(i));
                        }
                    }

                    bundle.putSerializable("Menuitems", (Serializable) filteredMenuList);
                    fragment.setArguments(bundle);
                }


            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return displayMainCatName.get(position);
        }

        @Override
        public int getCount() {
            return displayMainCatName.size();
        }

    }

    public static class SwipeList extends Fragment {

        SwipeListAdapter swipeListAdapter;

        Bundle bundle = new Bundle();

        //        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = inflater.inflate(R.layout.add_to_cart_list_view, container, false);

            ListView listview = (ListView) rootview.findViewById(R.id.addToCartListview);
//            Log.e("SwipeList","2nd");
            //getting category name  to the swipelist class through bundle
            bundle = getArguments();
            menuitems = (List<RestaurantMenuListResponse.Menu>) bundle.getSerializable("Menuitems");

            swipeListAdapter = new SwipeListAdapter(getActivity(), menuitems);
            swipeListAdapter.notifyDataSetChanged();
            listview.setAdapter(swipeListAdapter);
            listview.deferNotifyDataSetChanged();
            //getTotalHeightofListView(listview);

            return rootview;
        }
    }

    private static class SwipeListAdapter extends BaseAdapter {

        FragmentActivity activity;
        List<RestaurantMenuListResponse.Menu> menuitems;
        ViewHolder viewHolder;

        //create String
        String getCurrentDate;
        Dialog progressdialog;

        //create calender instance
        Calendar calender;

        SwipeListAdapter(FragmentActivity activity, List<RestaurantMenuListResponse.Menu> menuitems) {
            this.activity = activity;
            this.menuitems = menuitems;
            calender = Calendar.getInstance();

            //get current date and time
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            getCurrentDate = sdf.format(calender.getTime());
        }

        @Override
        public int getCount() {
            return menuitems.size();
        }

        @Override
        public Object getItem(int i) {
            return menuitems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            viewHolder = new ViewHolder();
            final CartDetails cartItems = new CartDetails();

            if (view == null) {
                hideProgressDialog();
                LayoutInflater inflater = activity.getLayoutInflater();
                view = inflater.inflate(R.layout.custom_subcategories_list, null);

                viewHolder.menuNameTextView = (TextView) view.findViewById(R.id.menuNameTextView);
                viewHolder.menuDescriptionTextView = (TextView) view.findViewById(R.id.menuDescriptionTextView);
                viewHolder.menuPriceTextView = (TextView) view.findViewById(R.id.menuPriceTextView);
                viewHolder.menuType = (ImageView) view.findViewById(R.id.menuType);
                viewHolder.plusButton = (ImageView) view.findViewById(R.id.plusButton);
                viewHolder.favMenuButton = (ImageView) view.findViewById(R.id.favMenuButton);
                viewHolder.menuImage = (ImageView) view.findViewById(R.id.menuImage);
                viewHolder.menuContentLayout = (RelativeLayout) view.findViewById(R.id.menuContentLayout);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
//            Log.e("Entrerring", "wow");
            //Set product image
            if (menuitems.get(i).menu_image.isEmpty()) {
                viewHolder.menuImage.setImageResource(android.R.color.transparent);
            } else {
                Picasso.with(activity)
                        .load(restaurantMenuListResponse.result.menuImgUrl + menuitems.get(i).menu_image)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(viewHolder.menuImage);
            }

            //Set product name,description,price
            viewHolder.menuNameTextView.setText(menuitems.get(i).menu_name);

            try {
                if (menuitems.get(i).menu_details != null && !menuitems.get(i).menu_details.isEmpty()&& !menuitems.get(i).menu_details.get(0).orginal_price.isEmpty()){
                    viewHolder.menuPriceTextView.setText(String.format("%.2f", Double.parseDouble(menuitems.get(i).menu_details.get(0).orginal_price)) + " " + loginSession.getCurrencyCode());
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (menuitems.get(i).menu_description.isEmpty()) {
                viewHolder.menuDescriptionTextView.setText(menuitems.get(i).menu_description);
                viewHolder.menuDescriptionTextView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.menuDescriptionTextView.setVisibility(GONE);
            }

            //Set menuType ImageView
            if (menuitems.get(i).menu_type.equalsIgnoreCase("veg")) {
                viewHolder.menuType.setVisibility(View.VISIBLE);
                viewHolder.menuType.setImageDrawable(activity.getResources().getDrawable(R.drawable.veg));
            } else if (menuitems.get(i).menu_type.equalsIgnoreCase("nonveg")) {
                viewHolder.menuType.setVisibility(View.VISIBLE);
                viewHolder.menuType.setImageDrawable(activity.getResources().getDrawable(R.drawable.non_veg));
            } else {
                viewHolder.menuType.setVisibility(GONE);
            }



            if (loginSession.isLoggedIn() == false && menuitems.get(i).favMenuCount.equalsIgnoreCase("")) {
                viewHolder.favMenuButton.setVisibility(GONE);
            } else {
//                if (!menuitems.get(i).favourite.equalsIgnoreCase("")) {
////                    likeCount.setText(restaurantMenuListResponse.result.restDetails.likeCount);
//                }
                if (menuitems.get(i).favMenuCount.equalsIgnoreCase("0")) {
                    Log.e("like issue", String.valueOf(menuitems.get(i)) + " " + menuitems.get(i).menu_name + " " + menuitems.get(i).favMenuCount);
                    viewHolder.favMenuButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav_red_color));

                } else if (menuitems.get(i).favMenuCount.equalsIgnoreCase("1")) {
                    Log.e("like issue", String.valueOf(menuitems.get(i)) + " " + menuitems.get(i).menu_name + " " + menuitems.get(i).favMenuCount);
                    viewHolder.favMenuButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav_full_red_color));

                } else {
                    Log.e("like error issue", String.valueOf(menuitems.get(i)) + " " + menuitems.get(i).menu_name + " " + menuitems.get(i).favMenuCount);
                }
            }

            viewHolder.favMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (menuitems.get(i).favMenuCount.equalsIgnoreCase("1")) {
                        showProgressDialog();
//                        viewHolder.favMenuButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav_red_color));
                        getMenulike("0", menuitems.get(i).id, menuitems.get(i).menu_name);

                    } else {
                        showProgressDialog();
//                        viewHolder.favMenuButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.fav_full_red_color));
                        getMenulike("1", menuitems.get(i).id, menuitems.get(i).menu_name);

                    }
                }
            });

            viewHolder.menuContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean addOption = true;

                /*if (databaseManager.getCount() > 0) {

                    if (databaseManager.getResId().equalsIgnoreCase(menuitems.get(i).restaurant_id)) {

                        addOption = true;
                    } else {

                        addOption = false;
                    }
                } else {

                    addOption = true;
                }*/

                    if (addOption) {

                        String menuAddons = menuitems.get(i).menu_addon;
                        String restid = menuitems.get(i).restaurant_id;
                        String menuPriceOption = menuitems.get(i).price_option;
                        String menuPrice = menuitems.get(i).menu_details.get(0).orginal_price;
                        String menuId = menuitems.get(i).id;
                        String menuType = menuitems.get(i).menu_type;
                        String menuName;
                        if (menuitems.get(i).dealMenu.isEmpty() || menuitems.get(i).dealMenu.equalsIgnoreCase("null")) {
                            menuName = menuitems.get(i).menu_name;
                        } else {
                            menuName = menuitems.get(i).menu_name + "[Deal :: " + menuitems.get(i).dealMenu + "]";
                        }

                        //if (menuAddons.equalsIgnoreCase("No") && menuPriceOption.equalsIgnoreCase("single")) {
                        if (false) {

                            int oldQuantity = 0;

                            databaseManager.openDatabase();
                            try {
                                oldQuantity = Integer.parseInt(databaseManager.getQuantity(menuitems.get(i).id));
                            } catch (Exception e) {
                            }
                            databaseManager.closeDatabase();

                            if (oldQuantity < 25) {

                                CartDetails cartItems = new CartDetails();
                                cartItems.setMenuId(menuId);
                                cartItems.setResId(restid);
                                cartItems.setMenuName(menuName);
                                cartItems.setMenuType(menuType);
                                cartItems.setMenuSize("");
                                cartItems.setMenuPrice(menuPrice);
                                cartItems.setAddonName("");
                                cartItems.setAddonPrice("0");
                                cartItems.setQuantity("1");
                                cartItems.setTotalPrice(menuPrice);
                                cartItems.setInstruction("");

                                //values insert to database
                                databaseManager.openDatabase();
                                databaseManager.insert(cartItems);
                                databaseManager.closeDatabase();


                                Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.menuAddedSuccessfully), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                                bottomCartLayout.setVisibility(View.VISIBLE);
                                itemTextView.setText(databaseManager.getQuantityCount() + " " + activity.getResources().getString(R.string.item));
                                amountTextView.setText(String.format("%.2f", Double.parseDouble(databaseManager.getSubTotal())) + " " + loginSession.getCurrencyCode());

                                loginSession.setDbResName(restaurantName);

                            } else {

                                Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.maximumAvailableQuantity), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }

                        } else {

                            Intent intent = new Intent(activity, AddToCartScreen.class);
                            intent.putExtra("productid", menuitems.get(i).id);
                            intent.putExtra("productname", menuitems.get(i).menu_name);
                            intent.putExtra("restaurantName", restaurantName);
                            intent.putExtra("image", restaurantMenuListResponse.result.menuImgUrl + menuitems.get(i).menu_image);
//                            if (menuitems.get(i).dealMenu.isEmpty() || menuitems.get(i).dealMenu.equalsIgnoreCase("null")) {
                                intent.putExtra("dealmenu", "null");
//                            } else {
//                                intent.putExtra("dealmenu", menuitems.get(i).dealMenu);
//                            }

                            activity.startActivity(intent);

                        }
                    } else {

                        if (replaceCartAlert == null) {
                            replaceCartAlert = new Dialog(activity);
                            replaceCartAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            replaceCartAlert.setContentView(R.layout.dialogue_replace_cart);
                            replaceCartAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            replaceCartAlert.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }

                        TextView alertTextView = (TextView) replaceCartAlert.findViewById(R.id.alertTextView);
                        TextView yesButton = (TextView) replaceCartAlert.findViewById(R.id.yesButton);
                        TextView noButton = (TextView) replaceCartAlert.findViewById(R.id.noButton);

                        String preResName = loginSession.getDbResName();
                        String newResName = restaurantName;
                        String tag = activity.getResources().getString(R.string.cartContainsDishes) + preResName + activity.getResources().getString(R.string.doUWantToDiscard) + newResName;
                        alertTextView.setText(tag);

                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                databaseManager.clearTable();
                                loginSession.clear_restaurant();
                                replaceCartAlert.dismiss();
                                loginSession.saveCardCount(0);
                            }
                        });

                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                replaceCartAlert.dismiss();
                            }
                        });


                        replaceCartAlert.show();
                        replaceCartAlert.setCancelable(false);

                    }
                }
            });

            return view;
        }

        private void getMenulike(String like, String id, String menuName) {

            if (checkInternet()) {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("chval", like);
                params.put("action", "changeFavStatus");
                params.put("page", "changeFavMenuStatus");
                params.put("resId", resId);
                params.put("menuId", id);
                params.put("userId", loginSession.getUserId());
                params.put("menuName", menuName);
//                showProgressDialog();
                serverRequest.createRequest((ServerListener) activity, params, RequestID.REQ_CHANGE_FAV_STATUS);
            } else {
                noInternetAlertDialog();
            }
        }

        public boolean checkInternet() {
            ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)

                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
            return false;
        }

        public void noInternetAlertDialog() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

            alertDialog.setTitle(activity.getResources().getString(R.string.checkInternetConnection));

            alertDialog.setPositiveButton(activity.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }


        public void showProgressDialog() {
            if (progressdialog == null) {
                progressdialog = new Dialog(activity);
                progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressdialog.setContentView(R.layout.custom_progressbar);
                progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            progressdialog.setCancelable(false);
            if (!activity.isFinishing()) {
                progressdialog.show();
            }

        }

        public void hideProgressDialog() {
            try {

                if (progressdialog.isShowing()) {

                    progressdialog.dismiss();
                }

            } catch (Exception e) {

            }

        }


        private static class ViewHolder {

            TextView menuNameTextView;
            TextView menuDescriptionTextView, menuPriceTextView;
            RelativeLayout menuContentLayout;
            ImageView menuType, plusButton, favMenuButton, menuImage;
        }

        // show alert dialog when quty <25
        private void showAlertplus(String s) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

            alertDialog.setTitle(s);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }

        // show alert dialog when quty >0
        private void showAlert(String s) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

            alertDialog.setTitle(s);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }
    }

//    **********************************************//////////+++++++++++++++++++++++++++++///////////***************************


    private class RestaurantInfoAdapter extends BaseAdapter {

        Activity activity;
        ArrayList<StoreTiming> restaurantOpenClosesList = new ArrayList<>();

        public RestaurantInfoAdapter(Activity activity, ArrayList<StoreTiming> restaurantOpenCloses) {

            this.activity = activity;
            this.restaurantOpenClosesList = restaurantOpenCloses;

        }

        @Override
        public int getCount() {

            return restaurantOpenClosesList.size();
        }

        @Override
        public Object getItem(int i) {

            return restaurantOpenClosesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = activity.getLayoutInflater();

            view = layoutInflater.inflate(R.layout.open_close_list, null);

            TextView todayTextview = (TextView) view.findViewById(R.id.todayTextview);
            TextView firstOpenTextview = (TextView) view.findViewById(R.id.firstOpenTextview);
            TextView firstCloseTextview = (TextView) view.findViewById(R.id.firstCloseTextview);
            TextView secondOpenTextview = (TextView) view.findViewById(R.id.secondOpenTextview);
            TextView secondCloseTextview = (TextView) view.findViewById(R.id.secondCloseTextview);
            TextView closeTextview = (TextView) view.findViewById(R.id.closeTextview);
            TextView hifenTextview = (TextView) view.findViewById(R.id.hifenTextview);
            TextView underHivenTextview = (TextView) view.findViewById(R.id.underHivenTextview);

            if (restaurantOpenClosesList.get(i).getSecondClose().equalsIgnoreCase("0:00 AM")) {
                if (restaurantOpenClosesList.get(i).getStatus().equalsIgnoreCase("Open")) {
                    closeTextview.setVisibility(GONE);
                    todayTextview.setText(restaurantOpenClosesList.get(i).getDate());
                    firstOpenTextview.setText(restaurantOpenClosesList.get(i).getFirstOpen());
                    firstCloseTextview.setText(restaurantOpenClosesList.get(i).getFirstClose());
                    secondOpenTextview.setText(getResources().getString(R.string.Closed));
                    secondOpenTextview.setTextColor(getResources().getColor(R.color.colorPrimary));
                    secondCloseTextview.setVisibility(GONE);
                    underHivenTextview.setVisibility(GONE);
                    // secondCloseTextview.setText(restaurantOpenClosesList.get(i).getSecondclose());

                } else {

                    todayTextview.setText(restaurantOpenClosesList.get(i).getDate());
                    closeTextview.setVisibility(View.VISIBLE);
                    closeTextview.setText(getResources().getString(R.string.Closed));
                    firstOpenTextview.setVisibility(GONE);
                    firstCloseTextview.setVisibility(GONE);
                    secondOpenTextview.setVisibility(GONE);
                    secondCloseTextview.setVisibility(GONE);
                    hifenTextview.setVisibility(GONE);
                    underHivenTextview.setVisibility(GONE);
                }


            } else {
                if (restaurantOpenClosesList.get(i).getStatus().equalsIgnoreCase("")) {
                    closeTextview.setVisibility(GONE);
                    todayTextview.setText(restaurantOpenClosesList.get(i).getDate());
                    firstOpenTextview.setText(restaurantOpenClosesList.get(i).getFirstOpen());
                    firstCloseTextview.setText(restaurantOpenClosesList.get(i).getFirstClose());
                    secondOpenTextview.setText(restaurantOpenClosesList.get(i).getSecondOpen());
                    secondCloseTextview.setText(restaurantOpenClosesList.get(i).getSecondClose());

                } else {

                    todayTextview.setText(restaurantOpenClosesList.get(i).getDate());
                    closeTextview.setVisibility(View.VISIBLE);
                    closeTextview.setText(getResources().getString(R.string.Closed));
                    firstOpenTextview.setVisibility(GONE);
                    firstCloseTextview.setVisibility(GONE);
                    secondOpenTextview.setVisibility(GONE);
                    secondCloseTextview.setVisibility(GONE);
                    hifenTextview.setVisibility(GONE);
                    underHivenTextview.setVisibility(GONE);
                }
            }

            return view;
        }
    }

    private class CustomReviewclass extends BaseAdapter {

        ArrayList<RestaurantMenuListResponse.Reviews> reviews1;
        Activity activity;

        public CustomReviewclass(FragmentActivity activity, ArrayList<RestaurantMenuListResponse.Reviews> reviews) {
            this.activity = activity;
            this.reviews1 = reviews;
        }

        @Override
        public int getCount() {
            return reviews1.size();
        }

        @Override
        public Object getItem(int i) {
            return reviews1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            view = inflater.inflate(R.layout.custom_review_fragment_screen, null);

            TextView titleTV = (TextView) view.findViewById(R.id.titleTV);
            TextView descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
            TextView reviewDateTV = (TextView) view.findViewById(R.id.reviewDateTV);
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

            titleTV.setText(reviews1.get(i).user.first_name);
            descriptionTV.setText(reviews1.get(i).message);
            ratingBar.setRating(Float.parseFloat(reviews1.get(i).rating));

            String reviewDate = "";
            try {
                String date = reviews1.get(i).created;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyyy h:mm a");
                Date d = sdf.parse(date);
                Log.e("dateintial", d.toString());
                Log.e("date", sdf2.format(d));
                reviewDate = sdf2.format(d);
            } catch (Exception e) {
            }
            reviewDateTV.setText(reviewDate);

            return view;
        }
    }

    //Set store timing model
    private class StoreTiming implements Serializable {

        String firstOpen;
        String firstClose;
        String secondOpen;
        String secondClose;
        String date;
        String status;

        public String getFirstOpen() {
            return firstOpen;
        }

        public void setFirstOpen(String firstOpen) {
            this.firstOpen = firstOpen;
        }

        public String getFirstClose() {
            return firstClose;
        }

        public void setFirstClose(String firstClose) {
            this.firstClose = firstClose;
        }

        public String getSecondOpen() {
            return secondOpen;
        }

        public void setSecondOpen(String secondOpen) {
            this.secondOpen = secondOpen;
        }

        public String getSecondClose() {
            return secondClose;
        }

        public void setSecondClose(String secondClose) {
            this.secondClose = secondClose;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    private class DealCategoryAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private ArrayList<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, ArrayList<RestaurantMenuListResponse.DealProducts>> _listDataChild;

        public DealCategoryAdapter(Context context, ArrayList<String> listDataHeader,
                                   HashMap<String, ArrayList<RestaurantMenuListResponse.DealProducts>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.deal_menu_layout, null);
            }

            final String headerTitle = (String) getGroup(groupPosition);

            TextView menuNameTextView = (TextView) convertView.findViewById(R.id.menuNameTextView);
            TextView menuDescriptionTextView = (TextView) convertView.findViewById(R.id.menuDescriptionTextView);
            TextView menuPriceTextView = (TextView) convertView.findViewById(R.id.menuPriceTextView);
            ImageView menuType = (ImageView) convertView.findViewById(R.id.menuType);
            ImageView dealmenuImage = (ImageView) convertView.findViewById(R.id.dealmenuImage);
            ImageView plusButton = (ImageView) convertView.findViewById(R.id.plusButton);
            final ImageView menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
            RelativeLayout menuContentLayout = (RelativeLayout) convertView.findViewById(R.id.menuContentLayout);

            menuNameTextView.setText(_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_name + " + " + _listDataChild.get(headerTitle).get(childPosition).SubProduct.menu_name);
            menuPriceTextView.setText(String.format("%.2f", Double.parseDouble(_listDataChild.get(headerTitle).get(childPosition).price)) + " " + loginSession.getCurrencyCode());

            if (!_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_description.isEmpty()) {
                menuDescriptionTextView.setText(_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_description);
                menuDescriptionTextView.setVisibility(View.VISIBLE);
            } else {
                menuDescriptionTextView.setVisibility(GONE);
            }

            //Set menuType ImageView
            if (_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_type.equalsIgnoreCase("veg")) {
                menuType.setVisibility(View.VISIBLE);
                menuType.setImageDrawable(getResources().getDrawable(R.drawable.veg));
            } else if (_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_type.equalsIgnoreCase("nonveg")) {
                menuType.setVisibility(View.VISIBLE);
                menuType.setImageDrawable(getResources().getDrawable(R.drawable.non_veg));
            } else {
                menuType.setVisibility(GONE);
            }

            if (_listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_image.equalsIgnoreCase("")) {
                //menuImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                menuImage.setImageResource(android.R.color.transparent);
            } else {
                Picasso.with(getApplicationContext())
                        .load(restaurantMenuListResponse.result.menuImgUrl + _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_image)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(menuImage);
            }

            if (_listDataChild.get(headerTitle).get(childPosition).SubProduct.menu_image.equalsIgnoreCase("")) {
                //dealmenuImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
                dealmenuImage.setImageResource(android.R.color.transparent);

            } else {
                Picasso.with(getApplicationContext())
                        .load(restaurantMenuListResponse.result.menuImgUrl + _listDataChild.get(headerTitle).get(childPosition).SubProduct.menu_image)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(dealmenuImage);
            }

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean addOption = true;

                    if (databaseManager.getCount() > 0) {

                        if (databaseManager.getResId().equalsIgnoreCase(_listDataChild.get(headerTitle).get(childPosition).restaurant_id)) {

                            addOption = true;
                        } else {

                            addOption = false;
                        }
                    } else {

                        addOption = true;
                    }

                    if (addOption) {

                        String menuAddons = _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_addon;
                        String restid = _listDataChild.get(headerTitle).get(childPosition).MainProduct.restaurant_id;
                        String menuPriceOption = _listDataChild.get(headerTitle).get(childPosition).MainProduct.price_option;
                        String menuPrice = _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_detailss.get(0).orginal_price;
                        String menuId = _listDataChild.get(headerTitle).get(childPosition).id;
                        String menuType = _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_type;
                        String menuName = _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_name + "[Deal :: " + _listDataChild.get(headerTitle).get(childPosition).SubProduct.menu_name + "]";

                        if (menuAddons.equalsIgnoreCase("No") && menuPriceOption.equalsIgnoreCase("single")) {

                            int oldQuantity = 0;

                            databaseManager.openDatabase();
                            try {
                                oldQuantity = Integer.parseInt(databaseManager.getQuantity(_listDataChild.get(headerTitle).get(childPosition).id));
                            } catch (Exception e) {
                            }
                            databaseManager.closeDatabase();

                            if (oldQuantity < 25) {

                                CartDetails cartItems = new CartDetails();
                                cartItems.setMenuId(menuId);
                                cartItems.setResId(restid);
                                cartItems.setMenuName(menuName);
                                cartItems.setMenuType(menuType);
                                cartItems.setMenuSize("");
                                cartItems.setMenuPrice(menuPrice);
                                cartItems.setAddonName("");
                                cartItems.setAddonPrice("0");
                                cartItems.setQuantity("1");
                                cartItems.setTotalPrice(menuPrice);
                                cartItems.setInstruction("");

                                //values insert to database
                                databaseManager.openDatabase();
                                databaseManager.insert(cartItems);
                                databaseManager.closeDatabase();


                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.menuAddedSuccessfully), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                                bottomCartLayout.setVisibility(View.VISIBLE);
                                itemTextView.setText(String.valueOf(databaseManager.getQuantityCount()) + " " + getResources().getString(R.string.item));
                                amountTextView.setText(String.format("%.2f", Double.parseDouble(databaseManager.getSubTotal())) + " " + loginSession.getCurrencyCode());

                                loginSession.setDbResName(restaurantName);

                            } else {

                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.maximumAvailableQuantity), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }

                        } else {

                            Intent intent = new Intent(RestaurantDetailsScreen.this, AddToCartScreen.class);
                            intent.putExtra("productid", _listDataChild.get(headerTitle).get(childPosition).main_product);
                            intent.putExtra("productname", _listDataChild.get(headerTitle).get(childPosition).MainProduct.menu_name);
                            intent.putExtra("restaurantName", restaurantName);
                            intent.putExtra("dealmenu", _listDataChild.get(headerTitle).get(childPosition).SubProduct.menu_name);
                            startActivity(intent);

                        }
                    } else {

                        if (replaceCartAlert == null) {
                            replaceCartAlert = new Dialog(RestaurantDetailsScreen.this);
                            replaceCartAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            replaceCartAlert.setContentView(R.layout.dialogue_replace_cart);
                            replaceCartAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            replaceCartAlert.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }

                        TextView alertTextView = (TextView) replaceCartAlert.findViewById(R.id.alertTextView);
                        TextView yesButton = (TextView) replaceCartAlert.findViewById(R.id.yesButton);
                        TextView noButton = (TextView) replaceCartAlert.findViewById(R.id.noButton);

                        String preResName = loginSession.getDbResName();
                        String newResName = restaurantName;
                        String tag = getResources().getString(R.string.cartContainsDishes) + preResName + getResources().getString(R.string.doUWantToDiscard) + newResName;
                        alertTextView.setText(tag);

                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                databaseManager.clearTable();
                                loginSession.clear_restaurant();
                                replaceCartAlert.dismiss();
                                loginSession.saveCardCount(0);
                            }
                        });

                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                replaceCartAlert.dismiss();
                            }
                        });


                        replaceCartAlert.show();
                        replaceCartAlert.setCancelable(false);

                    }

                }

            });

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.menu_custom_maincategory, null);
            }
            TextView mainCategoryNameTextView = (TextView) convertView
                    .findViewById(R.id.mainCategoryNameTextView);
            LinearLayout mainLayout = (LinearLayout) convertView
                    .findViewById(R.id.mainLayout);
            mainCategoryNameTextView.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        try {

            DatabaseManager databaseManager = DatabaseManager.getInstance(this);

            if (databaseManager.getCount() > 0) {

                bottomCartLayout.setVisibility(View.VISIBLE);
                itemTextView.setText(String.valueOf(databaseManager.getQuantityCount()) + " " + getResources().getString(R.string.item));
                amountTextView.setText(String.format("%.2f", Double.parseDouble(databaseManager.getSubTotal())) + " " + loginSession.getCurrencyCode());


            } else {

                bottomCartLayout.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }


    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
