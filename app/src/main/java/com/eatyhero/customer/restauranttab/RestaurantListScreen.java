package com.eatyhero.customer.restauranttab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseFragment;
import com.eatyhero.customer.base.NewBaseHomeScreen;
import com.eatyhero.customer.base.NewLocationScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.CuisionNames;
import com.eatyhero.customer.model.RestaurantListResponse;
import com.eatyhero.customer.model.RestaurantTypeName;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 22-01-2018.
 */

public class RestaurantListScreen extends BaseFragment implements ServerListener{

    //Create Objects
    private static Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;
    DatabaseManager databaseManager;
    private static NewBaseHomeScreen newBaseHomeScreen;

    //Create xml file
    LinearLayoutManager linearLayoutManager;
    private static Toolbar toolbar;
    private static NestedScrollView nestedScroolView;
    private static RelativeLayout detailsLayout;

    @BindView(R.id.searchLayout)RelativeLayout searchLayout;
    @BindView(R.id.locationImageView)ImageView locationImageView;
    @BindView(R.id.backImageView)ImageView backImageView;
    @BindView(R.id.searchAddressTextView)TextView searchAddressTextView;
    @BindView(R.id.searchImageView)ImageView searchImageView;
    @BindView(R.id.filterButton)ImageView filterButton;
    @BindView(R.id.filterRestaurant)EditText filterRestaurant;

    @BindView(R.id.viewpager)ViewPager viewpager;
    @BindView(R.id.restaurantListRecyclerView)RecyclerView restaurantListRecyclerView;

    private static RelativeLayout filterLinearLayout;

    @BindView(R.id.filterClearButton)ImageView filterClearButton;
    @BindView(R.id.filterresetButton)Button filterresetButton;
    @BindView(R.id.deliveryPickupRadioGroup)RadioGroup deliveryPickupRadioGroup;
    @BindView(R.id.pickupRadioButton)RadioButton pickupRadioButton;
    @BindView(R.id.deliveryRadioButton)RadioButton deliveryRadioButton;
    @BindView(R.id.allRadioButton)RadioButton allRadioButton;

    @BindView(R.id.promotionRadioGroup)RadioGroup promotionRadioGroup;
    @BindView(R.id.offerRadioButton)RadioButton offerRadioButton;
    @BindView(R.id.freeDeliveryRadioButton)RadioButton freeDeliveryRadioButton;
    @BindView(R.id.dealRadioButton)RadioButton dealRadioButton;

    @BindView(R.id.ratingbars)CheckBox ratingbars;
    @BindView(R.id.cuisinesListView)ListView cuisinesListView;
    @BindView(R.id.restaurantTypeListView)ListView restaurantTypeListView;
    @BindView(R.id.applyFilterButton)Button applyFilterButton;

    RestaurantListAdapter restaurantListAdapter;
    ImagePagerAdapter adapter;
    CusinListAdapter cusinListAdapter;
    RestaurantTypeListAdapter restaurantTypeListAdapter;
    ImageDataModel imageDataModel;

    boolean searchMenuOpen = true;
    static ArrayList<RestaurantListResponse.AllCuisinesLists> cuisinesList1 = new ArrayList<>();
    static ArrayList<RestaurantListResponse.ResTypeList> resTypeLists1 = new ArrayList<>();
    ArrayList<RestaurantListResponse.RestaurantLists> originalarrayList;
    ArrayList<RestaurantListResponse.RestaurantLists> resTypeArrayList = new ArrayList<>();

    //Filter objects
    String globalCusineName = "All cuisine",globalOrderType = "All",globalPromotionType = "",globalRestaurantType = "All Type";
    double globalAverage = 0;
    boolean poster;

    public static int ACTIVITY_REQUEST = 1001;

    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurant_list_screen, container, false);

        //Initialize xml layout
        ButterKnife.bind(this,rootView);

         toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
         nestedScroolView = (NestedScrollView)rootView.findViewById(R.id.nestedScroolView);
         detailsLayout = (RelativeLayout)rootView.findViewById(R.id.detailsLayout);
         filterLinearLayout = (RelativeLayout)rootView.findViewById(R.id.filterLinearLayout);

        //Initialize objects
        loginSession    = LoginSession.getInstance(getActivity());
        serverRequest   = ServerRequest.getInstance(getActivity());
        utility         = Utility.getInstance(getActivity());
        newBaseHomeScreen   = NewBaseHomeScreen.getInstance();
        databaseManager = DatabaseManager.getInstance(getActivity());

        //Iniitalize view pager
        viewpager.setPadding(65,20,65,20);
        viewpager.setClipToPadding(false);
        viewpager.setPageMargin(40);
        viewpager.setClipChildren(false);

        //Set address name
//        searchAddressTextView.setText(loginSession.getAreaName());

        utility.CURRENT_SCREEN = "RESTAURANT_LIST";

        //get RestaurantList
        getRestaurantList();

        //Lens open close
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchMenuOpen) {
                    searchLayout.setVisibility(View.VISIBLE);
                    detailsLayout.setVisibility(View.GONE);
                    searchMenuOpen = false;
                } else {
                    searchLayout.setVisibility(View.GONE);
                    detailsLayout.setVisibility(View.VISIBLE);
                    searchMenuOpen = true;
                    filterRestaurant.setText("");
                }
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchLayout.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                searchMenuOpen = true;
                filterRestaurant.setText("");

            }
        });

        //Open locationSet screen
        locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        searchAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationImageView.performClick();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.filterDialogueopen = "YES";
                newBaseHomeScreen.baseTabs.setVisibility(View.GONE);
                nestedScroolView.setVisibility(View.GONE);
                filterLinearLayout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                openFilterDialogue();

            }
        });

        //filter method
        filterRestaurant.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    String filter_text = filterRestaurant.getText().toString().trim().toLowerCase(Locale.getDefault());
                    restaurantListAdapter.searchFilter(filter_text);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }


    //Getting restaurant list
    private void getRestaurantList() {

//        if(utility.listResponse.equals("empty")){
//            if (isConnectingToInternet()) {
//                Map<String, String> params = new HashMap<>();
//                // the POST parameters:
//                params.put("searchLocation", loginSession.getAreaName());
//                params.put("customer_id", loginSession.getUserId());
//                serverRequest.createRequest(RestaurantListScreen.this, params, RequestID.REQ_TO_GET_STORELIST);
//                showProgressDialog();
//            } else {
//                noInternetAlertDialog();
//            }

            if (isConnectingToInternet()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", "searches");
                params.put("currentLocation", loginSession.getAreaName());
                params.put("city_id", loginSession.getCityid());
                params.put("subdist_id", loginSession.getSubDis());
                params.put("city_id1", loginSession.getCityidRes());
                params.put("city_id2", loginSession.getCityidCus());
                params.put("resName", loginSession.getResid());
                params.put("cuisines", loginSession.getcusid());
                params.put("Search_by", loginSession.getsearchType());
                params.put("customer_id", loginSession.getUserId());
                showProgressDialog();
                serverRequest.createRequest(RestaurantListScreen.this, params, RequestID.REQ_TO_GET_STORELIST);
            } else {
                noInternetAlertDialog();
            }

//        }else{
//            onSuccess(utility.listResponse,RequestID.REQ_TO_GET_STORELIST);
//        }
    }

    //openFilterDialogue
    private void openFilterDialogue() {

        /////////////Set cuisine list values //////////////////////////
        final ArrayList<CuisionNames> cuisionNamesList = new ArrayList<>();
        CuisionNames cuisionNames;
        for (RestaurantListResponse.AllCuisinesLists cuisinesList : cuisinesList1) {
            if (cuisinesList.name.equalsIgnoreCase(globalCusineName)) {
                cuisionNames = new CuisionNames(cuisinesList.name, true);
                cuisionNamesList.add(cuisionNames);
                Collections.sort(cuisionNamesList, new Comparator<CuisionNames>() {
                    public int compare(CuisionNames obj1, CuisionNames obj2) {
                        // TODO Auto-generated method stub
                        return obj1.getName().compareToIgnoreCase(obj2.getName());
                    }
                });

            } else {
                cuisionNames = new CuisionNames(cuisinesList.name, false);
                cuisionNamesList.add(cuisionNames);
                Collections.sort(cuisionNamesList, new Comparator<CuisionNames>() {
                    public int compare(CuisionNames obj1, CuisionNames obj2) {
                        // TODO Auto-generated method stub
                        return obj1.getName().compareToIgnoreCase(obj2.getName());
                    }
                });
            }
        }

        cusinListAdapter = new CusinListAdapter(getActivity(), cuisionNamesList);
        cuisinesListView.setAdapter(cusinListAdapter);
        cusinListAdapter.notifyDataSetChanged();
        getTotalHeightofListView(cuisinesListView);


        /////////////Set cuisine list values //////////////////////////
        final ArrayList<RestaurantTypeName> restaurantTypeNamesList = new ArrayList<>();
        RestaurantTypeName restaurantTypeName;
        for (RestaurantListResponse.ResTypeList resTypeList : resTypeLists1) {
            if (resTypeList.restype_name.equalsIgnoreCase(globalRestaurantType)) {
                restaurantTypeName = new RestaurantTypeName(resTypeList.restype_name, true);
                restaurantTypeNamesList.add(restaurantTypeName);
                Collections.sort(restaurantTypeNamesList, new Comparator<RestaurantTypeName>() {
                    public int compare(RestaurantTypeName obj1, RestaurantTypeName obj2) {
                        // TODO Auto-generated method stub
                        return obj1.getName().compareToIgnoreCase(obj2.getName());
                    }
                });

            } else {
                restaurantTypeName = new RestaurantTypeName(resTypeList.restype_name, false);
                restaurantTypeNamesList.add(restaurantTypeName);
                Collections.sort(restaurantTypeNamesList, new Comparator<RestaurantTypeName>() {
                    public int compare(RestaurantTypeName obj1, RestaurantTypeName obj2) {
                        // TODO Auto-generated method stub
                        return obj1.getName().compareToIgnoreCase(obj2.getName());
                    }
                });
            }
        }

        restaurantTypeListAdapter = new RestaurantTypeListAdapter(getActivity(), restaurantTypeNamesList);
        restaurantTypeListView.setAdapter(restaurantTypeListAdapter);
        restaurantTypeListAdapter.notifyDataSetChanged();
        getTotalHeightofListView(restaurantTypeListView);

        filterresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utility.filterDialogueopen = "NO";
                newBaseHomeScreen.baseTabs.setVisibility(View.VISIBLE);
                allRadioButton.setChecked(true);
                pickupRadioButton.setChecked(false);
                deliveryRadioButton.setChecked(false);
                offerRadioButton.setChecked(false);
                freeDeliveryRadioButton.setChecked(false);
                dealRadioButton.setChecked(false);
                ratingbars.setChecked(false);
                globalCusineName = "";
                globalRestaurantType = "All Type";
                filterLinearLayout.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                nestedScroolView.setVisibility(View.VISIBLE);
                restaurantListAdapter.ResetFilter();

            }
        });

        filterClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.filterDialogueopen = "NO";
                newBaseHomeScreen.baseTabs.setVisibility(View.VISIBLE);
                filterLinearLayout.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                nestedScroolView.setVisibility(View.VISIBLE);
            }


        });

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.filterDialogueopen = "NO";
                newBaseHomeScreen.baseTabs.setVisibility(View.VISIBLE);
                filterLinearLayout.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                nestedScroolView.setVisibility(View.VISIBLE);

                //Get OrderType
                if (pickupRadioButton.isChecked()) {

                    globalOrderType = "Pickup";

                } else if (deliveryRadioButton.isChecked()) {

                    globalOrderType = "Delivery";

                } else if (allRadioButton.isChecked()) {

                    globalOrderType = "All";
                }

                //Get promotionType
                if (offerRadioButton.isChecked()) {

                    globalPromotionType = "Offer";

                } else if (freeDeliveryRadioButton.isChecked()) {

                    globalPromotionType = "FreeDelivery";

                }else if (dealRadioButton.isChecked()) {

                    globalPromotionType = "Deal";

                }else {

                    globalPromotionType = "";

                }



                if (ratingbars.isChecked()) {

                    globalAverage = 50;

                } else {

                    globalAverage = 0;
                }

                restaurantListAdapter.ApplyFillter();

            }
        });

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {



        try {

            utility.listResponse = result;
            RestaurantListResponse restaurantListResponse = (RestaurantListResponse)result;
            loginSession.setCurrencyCode(restaurantListResponse.result.siteSettings.site_currency);

            Log.e("stripe_modestripe_mode",restaurantListResponse.result.siteSettings.stripe_mode);

            if(restaurantListResponse.result.siteSettings.stripe_mode.equalsIgnoreCase("test")){
                loginSession.setPusherKey(restaurantListResponse.result.siteSettings.stripe_publishkeyTest);
            }else{
                loginSession.setPusherKey(restaurantListResponse.result.siteSettings.stripe_publishkey);
            }

            //Set viewpager element

            ArrayList<ImageDataModel>imageDataModels = new ArrayList<>();
            for (RestaurantListResponse.RestaurantLists restaurantLists : restaurantListResponse.result.restaurantLists){

                if(restaurantLists.promotions != null && restaurantLists.promotions.size() > 0){

                    for(RestaurantListResponse.Promotions promotions : restaurantLists.promotions){

                        imageDataModel = new ImageDataModel();
                        imageDataModel.setCuisineList(promotions.cuisineList);
                        imageDataModel.setDelivery_charge(promotions.delivery_charge);
                        imageDataModel.setPromo_image_url(promotions.promo_image_url);
                        imageDataModel.setRestaurant_id(promotions.restaurant_id);
                        imageDataModel.setTo_distance(promotions.to_distance);
                        imageDataModels.add(imageDataModel);
                    }

                }
           }
            if(imageDataModels != null && imageDataModels.size() > 0) {
                adapter = new ImagePagerAdapter(getActivity(),imageDataModels);
                viewpager.setAdapter(adapter);
                viewpager.setCurrentItem(1);
                poster = true;
            }else{
                viewpager.setVisibility(View.GONE);
                poster=false;
            }


            //Set listview element
            linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
            restaurantListRecyclerView.setLayoutManager(linearLayoutManager);
            restaurantListRecyclerView.setItemAnimator(new DefaultItemAnimator());
            restaurantListRecyclerView.setHasFixedSize(true);
            restaurantListAdapter = new RestaurantListAdapter(getActivity(),restaurantListResponse.result.restaurantLists);
            restaurantListRecyclerView.setAdapter(restaurantListAdapter);

            //Set cusines response
            cuisinesList1 = restaurantListResponse.result.allCuisinesLists;
            resTypeLists1 = restaurantListResponse.result.resTypeList;

            filterButton.setVisibility(View.VISIBLE);

            hideProgressDialog();

        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
        filterButton.setVisibility(View.INVISIBLE);
    }

    public static void closeFilterDialogue() {

        utility.filterDialogueopen = "NO";
        newBaseHomeScreen.baseTabs.setVisibility(View.VISIBLE);
        filterLinearLayout.setVisibility(View.GONE);
        detailsLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        nestedScroolView.setVisibility(View.VISIBLE);
    }


    private class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder>{

        private Context context;
//        private ArrayList<RestaurantListResponse.RestaurantLists> originalarrayList;
        private ArrayList<RestaurantListResponse.RestaurantLists> dummyarrayList;
        private ArrayList<RestaurantListResponse.RestaurantLists> midarrayList;

        //Crete String objects
        String mSearchText;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView closedTextView,restaurantNameTextView,restaurantCuisinTextView,
                    ratingTextView,distanceTextView,deliveryFeeTextView,
                    restaurantOfferTextView, likeCount, deliveryTimeTextView;
            ImageView restaurantImageView,rewardImageView, likeImageview;
            RelativeLayout selectLayout;
            RelativeLayout closedImageView;
            LinearLayout favoriteLatout;
            RatingBar ratingBar;

            public MyViewHolder(View view) {
                super(view);
                closedTextView = (TextView)view.findViewById(R.id.closedTextView);
                restaurantNameTextView = (TextView)view.findViewById(R.id.restaurantNameTextView);
                restaurantCuisinTextView = (TextView)view.findViewById(R.id.restaurantCuisinTextView);
                restaurantOfferTextView = (TextView)view.findViewById(R.id.restaurantOfferTextView);
                ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
                distanceTextView = (TextView)view.findViewById(R.id.distanceTextView);
                deliveryFeeTextView = (TextView)view.findViewById(R.id.deliveryFeeTextView);
                restaurantImageView = (ImageView) view.findViewById(R.id.restaurantImageView);
                rewardImageView = (ImageView) view.findViewById(R.id.rewardImageView);
                selectLayout = (RelativeLayout) view.findViewById(R.id.selectLayout);
                closedImageView = (RelativeLayout) view.findViewById(R.id.closedImageView);
                favoriteLatout = (LinearLayout) view.findViewById(R.id.favoriteLatout);
                likeImageview = (ImageView) view.findViewById(R.id.likeImageview);
                likeCount = (TextView) view.findViewById(R.id.likeCount);
                deliveryTimeTextView = (TextView) view.findViewById(R.id.deliveryTimeTextView);
                ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            }
        }

        public RestaurantListAdapter(Context context, ArrayList<RestaurantListResponse.RestaurantLists> originalarrayList1) {
            this.context = context;
            originalarrayList = originalarrayList1;
            this.dummyarrayList = new ArrayList<RestaurantListResponse.RestaurantLists>();
            this.dummyarrayList.addAll(originalarrayList);
            this.midarrayList = new ArrayList<RestaurantListResponse.RestaurantLists>();
            this.midarrayList.addAll(originalarrayList);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_restaurant_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.restaurantNameTextView.setText(originalarrayList.get(position).restaurant_name);
            holder.restaurantCuisinTextView.setText(originalarrayList.get(position).cuisineLists.replace(",",", "));
            holder.ratingTextView.setText(originalarrayList.get(position).finalReview);
            holder.ratingBar.setRating(Float.parseFloat(originalarrayList.get(position).finalReview)/20f);
            holder.distanceTextView.setText(originalarrayList.get(position).minimum_order+loginSession.getCurrencyCode()+"\nMIN");
            holder.deliveryFeeTextView.setText(originalarrayList.get(position).delivery_charge+loginSession.getCurrencyCode()+"\nDEL FEE");
            holder.deliveryTimeTextView.setText(originalarrayList.get(position).estimate_time+"\nmin");

            if (!originalarrayList.get(position).restaurant_logo.isEmpty()) {
                Picasso.with(context)
                        .load(originalarrayList.get(position).restaurant_logo)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(context.getResources().getDrawable(R.drawable.no_image))
                        .into(holder.restaurantImageView);
            }

            if(originalarrayList.get(position).currentStatus.equalsIgnoreCase("Closed")){
                holder.closedImageView.setVisibility(View.VISIBLE);
                holder.closedTextView.setText(getResources().getString(R.string.preOrder));
            }else if(originalarrayList.get(position).currentStatus.equalsIgnoreCase("PreOrder")){
                holder.closedImageView.setVisibility(View.VISIBLE);
                holder.closedTextView.setText(getResources().getString(R.string.preOrder));
            }else{
                holder.closedImageView.setVisibility(View.GONE);
            }


            if (!loginSession.isLoggedIn() && originalarrayList.get(position).RstLikeCnt.equalsIgnoreCase("")&&originalarrayList.get(position).favCount.equalsIgnoreCase("")){
                holder.likeImageview.setVisibility(View.GONE);
                holder.likeCount.setVisibility(View.GONE);
            }else {
                if (!originalarrayList.get(position).RstLikeCnt.equalsIgnoreCase("")){
                    holder.likeCount.setText(context.getResources().getString(R.string.like)+" "+originalarrayList.get(position).RstLikeCnt);
                }
                if (originalarrayList.get(position).favCount.equalsIgnoreCase("0")){
                    holder.likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_red_color));
                }else if (originalarrayList.get(position).favCount.equalsIgnoreCase("1")){
                    holder.likeImageview.setImageDrawable(getResources().getDrawable(R.drawable.fav_full_red_color));
                }
            }

            if(originalarrayList.get(position).reward_option.equalsIgnoreCase("Yes")){
                holder.rewardImageView.setVisibility(View.VISIBLE);
            }else{
                holder.rewardImageView.setVisibility(View.GONE);
            }

            if (originalarrayList.get(position).restOffers != null && !originalarrayList.get(position).restOffers.isEmpty()) {

                if (!originalarrayList.get(position).restOffers.isEmpty()) {

                    if (originalarrayList.get(position).restOffers.size() > 0) {

                        if (originalarrayList.get(position).restOffers.get(0).first_user.equalsIgnoreCase("Y")) {

                            String value = originalarrayList.get(position).restOffers.get(0).free_percentage;
                            if (!value.isEmpty() && !value.equals("0")) {
                                Log.e("restOffers","YES");
                                holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                                holder.restaurantOfferTextView.setText(getResources().getString(R.string.flat) +" " + value +" " + getResources().getString(R.string.yourFirstOrder));
                            } else {

                                holder.restaurantOfferTextView.setVisibility(View.GONE);
                            }

                        } else {

                            String value = originalarrayList.get(position).restOffers.get(0).normal_percentage;
                            if (!value.isEmpty() && !value.equals("0")) {
                                holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                                holder.restaurantOfferTextView.setText(getResources().getString(R.string.flat) +" " + value +" " + getResources().getString(R.string.yourFirstOrder));
                            } else {
                                holder.restaurantOfferTextView.setVisibility(View.GONE);
                            }
                        }

                    }

                }else{

                    if (originalarrayList.get(position).free_delivery!=null
                            && !originalarrayList.get(position).free_delivery.isEmpty()
                            && Double.parseDouble(originalarrayList.get(position).free_delivery)>1) {

                        holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                        holder.restaurantOfferTextView.setText(getResources().getString(R.string.freeDeliveryOrderAbove)+" " +String.format("%.2f", Double.parseDouble(originalarrayList.get(position).free_delivery))+ " " +loginSession.getCurrencyCode());

                    }else if (originalarrayList.get(position).free_delivery!=null
                            && !originalarrayList.get(position).free_delivery.isEmpty()
                            && Double.parseDouble(originalarrayList.get(position).free_delivery)==1) {

                        holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                        holder.restaurantOfferTextView.setText(getResources().getString(R.string.freeDelivery));

                    } else {

                        holder.restaurantOfferTextView.setVisibility(View.GONE);
                    }
                }
            }else{

                if (originalarrayList.get(position).free_delivery!=null
                        && !originalarrayList.get(position).free_delivery.isEmpty()
                        && Double.parseDouble(originalarrayList.get(position).free_delivery)>1) {

                    holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                    holder.restaurantOfferTextView.setText(getResources().getString(R.string.freeDeliveryOrderAbove)+" " +String.format("%.2f", Double.parseDouble(originalarrayList.get(position).free_delivery))+ " " +loginSession.getCurrencyCode());

                }else if (originalarrayList.get(position).free_delivery!=null
                        && !originalarrayList.get(position).free_delivery.isEmpty()
                        && Double.parseDouble(originalarrayList.get(position).free_delivery)==1) {

                    holder.restaurantOfferTextView.setVisibility(View.VISIBLE);
                    holder.restaurantOfferTextView.setText(getResources().getString(R.string.freeDelivery));

                } else {

                    holder.restaurantOfferTextView.setVisibility(View.GONE);
                }

            }

            holder.selectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                    intent.putExtra("res_id",originalarrayList.get(position).id);
                    intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                    intent.putExtra("res_deliveryCharge",originalarrayList.get(position).delivery_charge);
                    intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                    intent.putExtra("rating",originalarrayList.get(position).finalReview);
                    startActivityForResult(intent,ACTIVITY_REQUEST);*/

                    if(databaseManager.getCount() > 0){

                        if(databaseManager.getResId().equalsIgnoreCase(originalarrayList.get(position).id)){

                            Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                            intent.putExtra("res_id",originalarrayList.get(position).id);
                            intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                            intent.putExtra("res_deliveryCharge",originalarrayList.get(position).delivery_charge);
                            intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                            intent.putExtra("rating",originalarrayList.get(position).finalReview);
                            startActivityForResult(intent,ACTIVITY_REQUEST);

                        }else{

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setIcon(R.drawable.caution);
                            alertDialog.setTitle(getResources().getString(R.string.note));
                            alertDialog.setMessage(getResources().getString(R.string.addedSomeItem));

                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    databaseManager.clearTable();
                                    loginSession.clear_restaurant();
                                    dialog.cancel();
                                    loginSession.saveCardCount(0);

                                    Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                                    intent.putExtra("res_id",originalarrayList.get(position).id);
                                    intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                                    intent.putExtra("res_deliveryCharge",originalarrayList.get(position).delivery_charge);
                                    intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                                    intent.putExtra("rating",originalarrayList.get(position).finalReview);
                                    startActivityForResult(intent,ACTIVITY_REQUEST);

                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }
                    }else{

                        Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                        intent.putExtra("res_id",originalarrayList.get(position).id);
                        intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                        intent.putExtra("res_deliveryCharge",originalarrayList.get(position).delivery_charge);
                        intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                        intent.putExtra("rating",originalarrayList.get(position).finalReview);
                        startActivityForResult(intent,ACTIVITY_REQUEST);
                    }

                }
            });

        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public int getItemCount() {
            return originalarrayList.size();
        }

        //Apply searchFilter method
        public void searchFilter(String filter_text) {

            mSearchText = filter_text;
            filter_text = filter_text.toLowerCase(Locale.getDefault());
            originalarrayList.clear();

            if (filter_text.length() == 0) {
                originalarrayList.addAll(dummyarrayList);

            } else {
                for (RestaurantListResponse.RestaurantLists restaurantss : dummyarrayList) {
                    if (restaurantss.restaurant_name.toLowerCase(Locale.getDefault()).contains(filter_text)) {
                        originalarrayList.add(restaurantss);
                    }
                }
                if (originalarrayList.size() == 0) {
                    utility.displayToast(getResources().getString(R.string.noRestaurantFound));
                }
            }

            notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }

        //Apply ResetFilter method
        public void ResetFilter() {

            originalarrayList.clear();

            for (RestaurantListResponse.RestaurantLists restaurantss : dummyarrayList) {
                    originalarrayList.add(restaurantss);
            }
            notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
        }

        //Apply filter method
        public void ApplyFillter() {

            midarrayList.clear();
            originalarrayList.clear();
            resTypeArrayList.clear();

            for (RestaurantListResponse.RestaurantLists restaurantss : dummyarrayList) {

                if (globalOrderType.equalsIgnoreCase("All")) {

                    if (restaurantss.cuisineLists.contains(globalCusineName)) {
                        midarrayList.add(restaurantss);
                    } else if (globalCusineName.equalsIgnoreCase("All cuisine")) {
                        midarrayList.add(restaurantss);
                    }
                } else if (globalOrderType.equalsIgnoreCase("Pickup")) {
                    if (restaurantss.restaurant_pickup.equalsIgnoreCase("yes")) {
                        if (restaurantss.cuisineLists.contains(globalCusineName)) {
                            midarrayList.add(restaurantss);
                        } else if (globalCusineName.equalsIgnoreCase("All cuisine")) {
                            midarrayList.add(restaurantss);

                        }
                    }
                } else if (globalOrderType.equalsIgnoreCase("Delivery")) {
                    if (restaurantss.restaurant_delivery.equalsIgnoreCase("yes")) {
                        if (restaurantss.cuisineLists.contains(globalCusineName)) {
                            midarrayList.add(restaurantss);
                        } else if (globalCusineName.equalsIgnoreCase("All cuisine")) {
                            midarrayList.add(restaurantss);

                        }
                    }
                }
            }

            if (!globalPromotionType.equalsIgnoreCase("")) {
                for (RestaurantListResponse.RestaurantLists restaurantss : midarrayList) {

                    if (globalPromotionType.equalsIgnoreCase("Offer")) {
                        if (restaurantss.offers.equalsIgnoreCase("Yes")) {
                            resTypeArrayList.add(restaurantss);
                        }
                    } else if (globalPromotionType.equalsIgnoreCase("FreeDelivery")) {
                        if (restaurantss.free_delivery_avail.equalsIgnoreCase("Yes")) {
                            resTypeArrayList.add(restaurantss);
                        }
                    }else if (globalPromotionType.equalsIgnoreCase("Deal")) {
                        if (restaurantss.deals.equalsIgnoreCase("Yes")) {
                            resTypeArrayList.add(restaurantss);
                        }
                    }
                }
            }else {
                resTypeArrayList.addAll(midarrayList);
            }
            String typeId="";
            if (!globalRestaurantType.equalsIgnoreCase("All Type")) {
                for (RestaurantListResponse.ResTypeList resTypeList : resTypeLists1){
                    if (globalRestaurantType.equalsIgnoreCase(resTypeList.restype_name)){
                        typeId = resTypeList.id;
                        break;
                    }
                }
            }

            if (!globalRestaurantType.equalsIgnoreCase("All Type")) {
                for (RestaurantListResponse.RestaurantLists restaurantss : resTypeArrayList) {
                    if (restaurantss.restaurant_type.equalsIgnoreCase(typeId)) {
                        originalarrayList.add(restaurantss);
                    }
                }
            }else {
                originalarrayList.addAll(resTypeArrayList);
            }


            if (originalarrayList.size() == 0) {
                utility.displayToast(getResources().getString(R.string.noRestaurantFound));
            }
            notifyDataSetChanged();
            if (poster) {
                adapter.notifyDataSetChanged();
            }


        }
    }


    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        Activity activity;
        ArrayList<ImageDataModel>arrayList;

        public ImagePagerAdapter(Activity activity, ArrayList<ImageDataModel> arrayList) {
            this.activity = activity;
            this.arrayList = arrayList;
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.viewpager_home, container, false);
            assert imageLayout != null;
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView_pager_home);
            RelativeLayout rl_homepager = (RelativeLayout) imageLayout.findViewById(R.id.rl_homepager);

            try{
                Picasso.with(getActivity())
                        .load(arrayList.get(position).getPromo_image_url())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.drawable.no_banner)
                        .into(imageView);
                imageView.setVisibility(View.VISIBLE);
                rl_homepager.setVisibility(View.VISIBLE);

            }
            catch (Exception e) {

            }



            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(databaseManager.getCount() > 0){

                        if(databaseManager.getResId().equalsIgnoreCase(arrayList.get(position).restaurant_id)){

                            Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                            intent.putExtra("res_id",arrayList.get(position).restaurant_id);
                            intent.putExtra("res_cuisineLists",arrayList.get(position).cuisineList);
                            intent.putExtra("res_deliveryCharge",arrayList.get(position).delivery_charge);
                            intent.putExtra("res_distance",arrayList.get(position).to_distance);
                            intent.putExtra("rating","0");
                            startActivityForResult(intent,ACTIVITY_REQUEST);

                        }else{

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setIcon(R.drawable.caution);
                            alertDialog.setTitle(getResources().getString(R.string.note));
                            alertDialog.setMessage(getResources().getString(R.string.addedSomeItem));

                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    databaseManager.clearTable();
                                    loginSession.clear_restaurant();
                                    dialog.cancel();
                                    loginSession.saveCardCount(0);

                                    Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                                    intent.putExtra("res_id",arrayList.get(position).restaurant_id);
                                    intent.putExtra("res_cuisineLists",arrayList.get(position).cuisineList);
                                    intent.putExtra("res_deliveryCharge",arrayList.get(position).delivery_charge);
                                    intent.putExtra("res_distance",arrayList.get(position).to_distance);
                                    intent.putExtra("rating","0");
                                    startActivityForResult(intent,ACTIVITY_REQUEST);

                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }
                    }else{

                        Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                        intent.putExtra("res_id",arrayList.get(position).restaurant_id);
                        intent.putExtra("res_cuisineLists",arrayList.get(position).cuisineList);
                        intent.putExtra("res_deliveryCharge",arrayList.get(position).delivery_charge);
                        intent.putExtra("res_distance",arrayList.get(position).to_distance);
                        intent.putExtra("rating","0");
                        startActivityForResult(intent,ACTIVITY_REQUEST);
                    }


                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ACTIVITY_REQUEST && resultCode==200) {
            for(RestaurantListResponse.RestaurantLists restaurant : originalarrayList) {
                if(data.getStringExtra("id").equals(restaurant.id)) {
                    restaurant.favCount = data.getBooleanExtra("boolean", false) ? "1" : "0";
                    restaurantListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private class CusinListAdapter extends BaseAdapter{

        Activity activity;
        List<CuisionNames> cuisinesLists;

        public CusinListAdapter(Activity activity, List<CuisionNames> cuisinesLists) {
            this.activity = activity;
            this.cuisinesLists = cuisinesLists;
        }

        @Override
        public int getCount() {
            return cuisinesLists.size();
        }

        @Override
        public Object getItem(int i) {
            return cuisinesLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.custom_cuisines_list, null);

            RadioButton cuisinesCheckBox = (RadioButton) view.findViewById(R.id.cuisinesCheckBox);

            cuisinesCheckBox.setText(cuisinesLists.get(position).getName().substring(0, 1).toUpperCase() + cuisinesLists.get(position).getName().substring(1));

            boolean checked = cuisinesLists.get(position).isSelected();
            if (checked) {

                cuisinesCheckBox.setChecked(checked);
                globalCusineName = cuisinesLists.get(position).getName();

            } else {

                cuisinesCheckBox.setChecked(false);
            }

            cuisinesCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < cuisinesLists.size(); i++) {

                        if (i == position) {

                            cuisinesLists.get(i).setSelected(true);
                            globalCusineName = cuisinesLists.get(position).getName();

                        } else {

                            cuisinesLists.get(i).setSelected(false);
                        }
                    }

                    notifyDataSetChanged();
                }


            });

            return view;
        }
    }

    private class RestaurantTypeListAdapter extends BaseAdapter{

        Activity activity;
        List<RestaurantTypeName> typeList;

        public RestaurantTypeListAdapter(Activity activity, List<RestaurantTypeName> typeList) {
            this.activity = activity;
            this.typeList = typeList;
        }

        @Override
        public int getCount() {
            return typeList.size();
        }

        @Override
        public Object getItem(int i) {
            return typeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.custom_restype_list, null);

            RadioButton typeCheckBox = (RadioButton) view.findViewById(R.id.typeCheckBox);

            typeCheckBox.setText(typeList.get(position).getName().substring(0, 1).toUpperCase() + typeList.get(position).getName().substring(1));

            boolean checked = typeList.get(position).isSelected();
            if (checked) {

                typeCheckBox.setChecked(checked);
                globalRestaurantType = typeList.get(position).getName();

            } else {

                typeCheckBox.setChecked(false);
            }

            typeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < typeList.size(); i++) {

                        if (i == position) {

                            typeList.get(i).setSelected(true);
                            globalRestaurantType = typeList.get(position).getName();

                        } else {

                            typeList.get(i).setSelected(false);
                        }
                    }

                    notifyDataSetChanged();
                }


            });

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getRestaurantList();
        int cartCount = databaseManager.getQuantityCount();
        loginSession.saveCardCount(cartCount);
        if (cartCount > 0) {
            newBaseHomeScreen.cartCountTextView.setText(loginSession.getCartCount());
            newBaseHomeScreen.cartCountTextView.setVisibility(View.VISIBLE);
        } else if (cartCount == 0) {
            newBaseHomeScreen.cartCountTextView.setVisibility(View.GONE);
        }

    }

    private class ImageDataModel implements Serializable{

        String restaurant_id;
        String promo_image_url;
        String cuisineList;
        String to_distance;
        String delivery_charge;

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getPromo_image_url() {
            return promo_image_url;
        }

        public void setPromo_image_url(String promo_image_url) {
            this.promo_image_url = promo_image_url;
        }

        public String getCuisineList() {
            return cuisineList;
        }

        public void setCuisineList(String cuisineList) {
            this.cuisineList = cuisineList;
        }

        public String getTo_distance() {
            return to_distance;
        }

        public void setTo_distance(String to_distance) {
            this.to_distance = to_distance;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }
    }
}
