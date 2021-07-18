package com.eatyhero.customer.restauranttab;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginScreen;
import com.eatyhero.customer.base.BaseFragment;
import com.eatyhero.customer.base.NewBaseHomeScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.CartDetails;
import com.eatyhero.customer.model.MyFavoriteListModel;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 27-03-2018.
 */

public class FavoriteScreen extends BaseFragment implements ServerListener {

    @BindView(R.id.favRestaurantList)RecyclerView favRestaurantList;
    @BindView(R.id.favMenuList)ExpandableListView favMenuList;
    @BindView(R.id.loginButton)Button loginButton;
    @BindView(R.id.loginLayout)RelativeLayout loginLayout;
    @BindView(R.id.favoriteMenuTab)TextView favoriteMenuTab;
    @BindView(R.id.favoriteResTab)TextView favoriteResTab;
    @BindView(R.id.AlernateText)TextView AlernateText;
    @BindView(R.id.menuView)View menuView;
    @BindView(R.id.restaurantView)View restaurantView;

    FavoriteRestaurantAdapter favoriteRestaurantAdapter;
//    FavoriteMenuAdapter favoriteMenuAdapter;
    LoginSession loginSession;
    Utility utility;
    ServerRequest serverRequest;
    DatabaseManager databaseManager;

    MyFavoriteListModel myFavoriteListModel;
    MainCategoryAdapter mainCategoryAdapter;

    LinearLayoutManager linearLayoutManager;
    NewBaseHomeScreen newBaseHomeScreen;

    boolean resEmpty = false, menuEmpty = false;

    ArrayList<MyFavoriteListModel.Restaurant_details> resDetails = new ArrayList<>() ;

    Dialog replaceCartAlert;
    String restaurantName="";
    int list = 0;

    String FIRST_USER="",FIRST_OFFER_PER="",FIRST_OFFER_AMOUNT="",NORMAL_USER="",NORMAL_OFFER_PER="",NORMAL_OFFER_AMOUNT="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_screen, container, false);

        //Initialize xml layout
        ButterKnife.bind(this, rootView);

        utility.CURRENT_SCREEN = "FAVORITE_SCREEN";
        loginSession    = LoginSession.getInstance(getActivity());
        utility         = Utility.getInstance(getActivity());
        serverRequest = ServerRequest.getInstance(getActivity());
        databaseManager = DatabaseManager.getInstance(getActivity());

        if (loginSession.isLoggedIn()) {

            getFavList();
            loginLayout.setVisibility(View.GONE);
            favRestaurantList.setVisibility(View.GONE);
            favMenuList.setVisibility(View.VISIBLE);
            menuView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            restaurantView.setBackgroundColor(getResources().getColor(R.color.white));

        } else {

            loginLayout.setVisibility(View.VISIBLE);
            favMenuList.setVisibility(View.GONE);
            favRestaurantList.setVisibility(View.GONE);

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), LoginScreen.class);
                getActivity().startActivity(intent);
            }
        });

        favoriteMenuTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (menuEmpty){
                    favMenuList.setVisibility(View.GONE);
                    AlernateText.setText(getResources().getString(R.string.noFavouriteMenu));
                    AlernateText.setVisibility(View.VISIBLE);
                }else {
                    favMenuList.setVisibility(View.VISIBLE);
                    AlernateText.setVisibility(View.GONE);
                }
                favRestaurantList.setVisibility(View.GONE);
                menuView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                restaurantView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        favoriteResTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resEmpty){
                    favRestaurantList.setVisibility(View.GONE);
                    AlernateText.setText(getResources().getString(R.string.noFavouriteRestaurant));
                    AlernateText.setVisibility(View.VISIBLE);
                }else {
                    favRestaurantList.setVisibility(View.VISIBLE);
                    AlernateText.setVisibility(View.GONE);
                }

                favRestaurantList.setVisibility(View.VISIBLE);
                favMenuList.setVisibility(View.GONE);
                restaurantView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                menuView.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        return rootView;
    }

    public void getFavList() {

        if (isConnectingToInternet()) {
            Map<String, String> params = new HashMap<>();
            params.put("action", "changeFavStatus");
            params.put("page", "getCustomerFavourites");
            params.put("customer_id", loginSession.getUserId());
            showProgressDialog();
            serverRequest.createRequest(FavoriteScreen.this, params, RequestID.REQ_CHANGE_FAV_LIST);
        } else {
            noInternetAlertDialog();
        }
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID) {

            case REQ_CHANGE_FAV_LIST:


                myFavoriteListModel = (MyFavoriteListModel) result;
//                resDetails = myFavoriteListModel.result.favMenuList.get(0).restaurant_details;
                //Set listview element
                linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                favRestaurantList.setLayoutManager(linearLayoutManager);
                favRestaurantList.setItemAnimator(new DefaultItemAnimator());
                favRestaurantList.setHasFixedSize(true);
                favoriteRestaurantAdapter = new FavoriteRestaurantAdapter(getActivity(), myFavoriteListModel.result.favRstList);
                favRestaurantList.setAdapter(favoriteRestaurantAdapter);
                list=1;

//                if (myFavoriteListModel.result.favRstList.isEmpty()){
//                    favRestaurantList.setVisibility(View.GONE);
//                    AlernateText.setText("You have no Favorite Restaurants");
//                    AlernateText.setVisibility(View.VISIBLE);
//                    resEmpty = true;
//                }else {
//                    favRestaurantList.setVisibility(View.VISIBLE);
//                    AlernateText.setVisibility(View.GONE);
//                    resEmpty = false;
//                }
//
//                if (myFavoriteListModel.result.favMenuList.isEmpty()){
//                    favMenuList.setVisibility(View.GONE);
//                    AlernateText.setText("You have no Favorite Menus");
//                    AlernateText.setVisibility(View.VISIBLE);
//                    menuEmpty = true;
//                }else {
//                    favMenuList.setVisibility(View.VISIBLE);
//                    AlernateText.setVisibility(View.GONE);
//                    menuEmpty = false;
//                }

                //SetMenuListView
                ArrayList<String> listDataHeader = new ArrayList<String>();
                HashMap<String, ArrayList<MyFavoriteListModel.Menus>> listDataChild = new HashMap<>();

                for (MyFavoriteListModel.FavMenuList favMenu: myFavoriteListModel.result.favMenuList) {

                        listDataHeader.add(favMenu.restaurant.restaurant_name);
                        listDataChild.put(favMenu.restaurant.restaurant_name, favMenu.menus);
                        resDetails.addAll(favMenu.restaurant_details);
                }
                mainCategoryAdapter = new MainCategoryAdapter(getActivity(), listDataHeader, listDataChild);
                favMenuList.setAdapter(mainCategoryAdapter);

                for (int i = 0; i < mainCategoryAdapter.getGroupCount(); i++)
                    favMenuList.expandGroup(i);


                break;

            case REQ_UNFAV_RESTAURANT:
                toast(String.valueOf(result));
                getFavList();
                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        hideProgressDialog();
        toast(error);
        if (error.equalsIgnoreCase("Your favourites list is empty") && (list == 1)){
            Log.e("issue","gotit");
            mainCategoryAdapter.notifyDataSetChanged();
            favMenuList.setAdapter(mainCategoryAdapter);
            favoriteRestaurantAdapter.notifyDataSetChanged();
            favRestaurantList.setAdapter(favoriteRestaurantAdapter);
        }

    }

    private class FavoriteRestaurantAdapter extends RecyclerView.Adapter<FavoriteRestaurantAdapter.MyViewHolder>{

        private Context context;
        private ArrayList<MyFavoriteListModel.FavRstList> originalarrayList;
        private ArrayList<MyFavoriteListModel.FavRstList> dummyarrayList;

        //Crete String objects
        String mSearchText;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView favRestaurantName;
            ImageView deleteRestaurant;
            LinearLayout favoriteLatout;

            public MyViewHolder(View view) {
                super(view);
                favRestaurantName = (TextView)view.findViewById(R.id.favRestaurantName);
                deleteRestaurant = (ImageView) view.findViewById(R.id.deleteRestaurant);

            }
        }

        public FavoriteRestaurantAdapter(Context context, ArrayList<MyFavoriteListModel.FavRstList> originalarrayList) {
            this.context = context;
            this.originalarrayList = originalarrayList;
            this.dummyarrayList = new ArrayList<>();
            this.dummyarrayList.addAll(originalarrayList);

        }

        @Override
        public FavoriteRestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_favorite_restaurant_list, parent, false);

            return new FavoriteRestaurantAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FavoriteRestaurantAdapter.MyViewHolder holder, final int position) {

            holder.favRestaurantName.setText(originalarrayList.get(position).restaurant_name);


            holder.favRestaurantName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                    intent.putExtra("res_id",originalarrayList.get(position).id);
                    intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                    intent.putExtra("res_deliveryCharge",originalarrayList.get(position).delivery_charge);
                    intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                    intent.putExtra("rating",originalarrayList.get(position).finalReview);
                    startActivity(intent);*/

                    if(databaseManager.getCount() > 0){

                        if(databaseManager.getResId().equalsIgnoreCase(originalarrayList.get(position).restaurant_id)){

                            Intent intent = new Intent(getActivity(),RestaurantDetailsScreen.class);
                            intent.putExtra("res_id",originalarrayList.get(position).restaurant_id);
                            intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                            intent.putExtra("res_deliveryCharge",originalarrayList.get(position).deliveryCharge);
                            intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                            intent.putExtra("rating",originalarrayList.get(position).finalReview);
                            startActivity(intent);

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
                                    intent.putExtra("res_id",originalarrayList.get(position).restaurant_id);
                                    intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                                    intent.putExtra("res_deliveryCharge",originalarrayList.get(position).deliveryCharge);
                                    intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                                    intent.putExtra("rating",originalarrayList.get(position).finalReview);
                                    startActivity(intent);

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
                        intent.putExtra("res_id",originalarrayList.get(position).restaurant_id);
                        intent.putExtra("res_cuisineLists",originalarrayList.get(position).cuisineLists);
                        intent.putExtra("res_deliveryCharge",originalarrayList.get(position).deliveryCharge);
                        intent.putExtra("res_distance",originalarrayList.get(position).to_distance);
                        intent.putExtra("rating",originalarrayList.get(position).finalReview);
                        startActivity(intent);
                    }

                }
            });

            holder.deleteRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectingToInternet()) {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "changeFavStatus");
                        params.put("page", "unFavouriteRst");
                        params.put("resId", originalarrayList.get(position).restaurant_id);
                        params.put("userId", loginSession.getUserId());
                        showProgressDialog();
                        serverRequest.createRequest(FavoriteScreen.this, params, RequestID.REQ_UNFAV_RESTAURANT);
                    } else {
                        noInternetAlertDialog();
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

    }

    private class MainCategoryAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private ArrayList<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, ArrayList<MyFavoriteListModel.Menus>> _listDataChild;

        public MainCategoryAdapter(Context context, ArrayList<String> listDataHeader,
                                   HashMap<String, ArrayList<MyFavoriteListModel.Menus>> listChildData) {
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
                convertView = infalInflater.inflate(R.layout.custom_fav_submenu_list, null);
            }

            final String headerTitle = (String) getGroup(groupPosition);

            TextView menuNameTextView = (TextView) convertView.findViewById(R.id.menuNameTextView);
            TextView menuDescriptionTextView = (TextView) convertView.findViewById(R.id.menuDescriptionTextView);
            TextView menuPriceTextView = (TextView) convertView.findViewById(R.id.menuPriceTextView);
            ImageView menuType = (ImageView) convertView.findViewById(R.id.menuType);
            ImageView plusButton = (ImageView) convertView.findViewById(R.id.plusButton);
            ImageView favMenuDelete = (ImageView) convertView.findViewById(R.id.favMenuDelete);
            RelativeLayout menuContentLayout = (RelativeLayout) convertView.findViewById(R.id.menuContentLayout);

            menuNameTextView.setText(_listDataChild.get(headerTitle).get(childPosition).menu_name);
            menuPriceTextView.setText(loginSession.getCurrencyCode()+" "+String.format("%.2f",Double.parseDouble(_listDataChild.get(headerTitle).get(childPosition).menu_price)));

            if (!_listDataChild.get(headerTitle).get(childPosition).menu_description.isEmpty()) {
                menuDescriptionTextView.setText(_listDataChild.get(headerTitle).get(childPosition).menu_description);
                menuDescriptionTextView.setVisibility(View.VISIBLE);
            } else {
                menuDescriptionTextView.setVisibility(View.GONE);
            }

            //Set menuType ImageView
            if(_listDataChild.get(headerTitle).get(childPosition).menu_type.equalsIgnoreCase("veg")){
                menuType.setVisibility(View.VISIBLE);
                menuType.setImageDrawable(getResources().getDrawable(R.drawable.veg));
            }else if(_listDataChild.get(headerTitle).get(childPosition).menu_type.equalsIgnoreCase("nonveg")){
                menuType.setVisibility(View.VISIBLE);
                menuType.setImageDrawable(getResources().getDrawable(R.drawable.non_veg));
            }else{
                menuType.setVisibility(View.GONE);
            }

            favMenuDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectingToInternet()) {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "changeFavStatus");
                        params.put("page", "unFavouriteMenu");
                        params.put("resId", _listDataChild.get(headerTitle).get(childPosition).restaurant_id);
                        params.put("userId", loginSession.getUserId());
                        params.put("menuId", _listDataChild.get(headerTitle).get(childPosition).id);
                        showProgressDialog();
                        serverRequest.createRequest(FavoriteScreen.this, params, RequestID.REQ_UNFAV_RESTAURANT);
                    } else {
                        noInternetAlertDialog();
                    }
                }
            });

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean addOption = true;

                    if(databaseManager.getCount() > 0){

                        if(databaseManager.getResId().equalsIgnoreCase(_listDataChild.get(headerTitle).get(childPosition).restaurant_id)){

                            addOption = true;

                        }else{

                            addOption = false;
                        }
                    }else{

                        addOption = true;
                    }

                    if(addOption){

                        String menuAddons      = _listDataChild.get(headerTitle).get(childPosition).menu_addon;
                        String restid          = _listDataChild.get(headerTitle).get(childPosition).restaurant_id;
                        String menuPriceOption = _listDataChild.get(headerTitle).get(childPosition).price_option;
                        String menuPrice       = _listDataChild.get(headerTitle).get(childPosition).menu_price;
                        String menuId          =_listDataChild.get(headerTitle).get(childPosition).id;
                        String menuType        = _listDataChild.get(headerTitle).get(childPosition).menu_type;
                        String menuName        = _listDataChild.get(headerTitle).get(childPosition).menu_name;
                        restaurantName         = headerTitle;
                        Log.e("added restaurant",restaurantName);


                        for (MyFavoriteListModel.Restaurant_details details : resDetails){
                            Log.e("id for res",details.id);
                            Log.e("id for child",_listDataChild.get(headerTitle).get(childPosition).restaurant_id);
                            if (details.id.equalsIgnoreCase(_listDataChild.get(headerTitle).get(childPosition).restaurant_id)){

                                Log.e("id for success res",details.id);

                                if (!details.restOffers.isEmpty()) {


                                    if (details.restOffers.size() > 0) {

                                        if (details.restOffers.get(0).first_user.equalsIgnoreCase("Y")) {
                                            String value = details.restOffers.get(0).free_percentage;
                                            if (!value.isEmpty() && !value.equals("0")) {

                                                FIRST_USER = "YES";
                                                FIRST_OFFER_PER = details.restOffers.get(0).free_percentage;
                                                FIRST_OFFER_AMOUNT = details.restOffers.get(0).free_price;

                                            } else {


                                                FIRST_USER = "NO";
                                                FIRST_OFFER_PER = details.restOffers.get(0).free_percentage;
                                                FIRST_OFFER_AMOUNT = details.restOffers.get(0).free_price;

                                            }
                                        } else {

                                            FIRST_USER = "NO";
                                            FIRST_OFFER_PER = "0";
                                            FIRST_OFFER_AMOUNT = "0";
                                        }

                                        if (details.restOffers.get(0).normal.equalsIgnoreCase("Y")) {
                                            String value = details.restOffers.get(0).normal_percentage;
                                            if (!value.isEmpty() && !value.equals("0")) {

                                                NORMAL_USER = "YES";
                                                NORMAL_OFFER_PER = details.restOffers.get(0).normal_percentage;
                                                NORMAL_OFFER_AMOUNT = details.restOffers.get(0).normal_price;

                                            } else {

                                                NORMAL_USER = "NO";
                                                NORMAL_OFFER_PER = details.restOffers.get(0).normal_percentage;
                                                NORMAL_OFFER_AMOUNT = details.restOffers.get(0).normal_price;

                                            }
                                        } else {

                                            NORMAL_USER = "NO";
                                            NORMAL_OFFER_PER = "0";
                                            NORMAL_OFFER_AMOUNT = "0";

                                        }


                                    } else {

                                        if (!details.free_delivery.isEmpty() || !details.free_delivery.equals("0")) {

                                            FIRST_USER = "NO";
                                            NORMAL_USER = "NO";
                                            FIRST_OFFER_PER = "0";
                                            FIRST_OFFER_AMOUNT = "0";
                                            NORMAL_OFFER_PER = "0";
                                            NORMAL_OFFER_AMOUNT = "0";

                                        } else {
                                            FIRST_USER = "NO";
                                            NORMAL_USER = "NO";
                                            FIRST_OFFER_PER = "0";
                                            FIRST_OFFER_AMOUNT = "0";
                                            NORMAL_OFFER_PER = "0";
                                            NORMAL_OFFER_AMOUNT = "0";

                                        }
                                    }

                                } else {

                                    if (!details.free_delivery.isEmpty() || !details.free_delivery.equals("0")) {

                                        FIRST_USER = "NO";
                                        NORMAL_USER = "NO";
                                        FIRST_OFFER_PER = "0";
                                        FIRST_OFFER_AMOUNT = "0";
                                        NORMAL_OFFER_PER = "0";
                                        NORMAL_OFFER_AMOUNT = "0";

                                    } else {

                                        FIRST_USER = "NO";
                                        NORMAL_USER = "NO";
                                        FIRST_OFFER_PER = "0";
                                        FIRST_OFFER_AMOUNT = "0";
                                        NORMAL_OFFER_PER = "0";
                                        NORMAL_OFFER_AMOUNT = "0";
                                    }

                                }

                                Log.e("paymrnt details",details.codStatus+ details.stripeStatus+details.paypalStatus);
//                                Save Restaurant Details
                                loginSession.saveResDetails(
                                        details.id,
                                        details.restaurant_name,
                                        details.contact_address,
                                        details.deliveryCharge,
                                        details.free_delivery,
                                        details.restaurant_tax,
                                        details.minimum_order,
                                        details.restaurant_delivery,
                                        details.restaurant_pickup,
                                        details.first_user,
                                        NORMAL_USER,
                                        FIRST_OFFER_PER,
                                        FIRST_OFFER_AMOUNT,
                                        NORMAL_OFFER_PER,
                                        NORMAL_OFFER_AMOUNT,
                                        details.currentStatus,
                                        details.first_user,
                                        details.codStatus,
                                        details.stripeStatus,
                                        details.paypalStatus);


                            }
                        }

                        if (menuAddons.equalsIgnoreCase("No") && menuPriceOption.equalsIgnoreCase("single")) {

                            int oldQuantity = 0;

                            databaseManager.openDatabase();
                            try {
                                oldQuantity = Integer.parseInt(databaseManager.getQuantity(_listDataChild.get(headerTitle).get(childPosition).id));
                            }catch (Exception e){}
                            databaseManager.closeDatabase();

                            if(oldQuantity < 25) {

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


                                Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.menuAddedSuccessfully), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

//                                bottomCartLayout.setVisibility(View.VISIBLE);
//                                itemTextView.setText(String.valueOf(databaseManager.getCount()) + " item");
//                                amountTextView.setText(loginSession.getCurrencyCode() + " " + String.format("%.2f", Double.parseDouble(databaseManager.getSubTotal())));

                                loginSession.setDbResName(restaurantName);

                            }else{

                                Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.maximumAvailableQuantity), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }

                        } else {

                            Intent intent = new Intent(getActivity(),AddToCartScreen.class);
                            intent.putExtra("productid", _listDataChild.get(headerTitle).get(childPosition).id);
                            intent.putExtra("productname", _listDataChild.get(headerTitle).get(childPosition).menu_name);
                            intent.putExtra("restaurantName",restaurantName);
                            if (_listDataChild.get(headerTitle).get(childPosition).dealMenu.isEmpty()) {
                                intent.putExtra("dealmenu","null");
                            }else {
                                intent.putExtra("dealmenu", _listDataChild.get(headerTitle).get(childPosition).dealMenu);
                            }

                            startActivity(intent);

                        }
                    }else{

                        if (replaceCartAlert == null) {
                            replaceCartAlert = new Dialog(getActivity());
                            replaceCartAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            replaceCartAlert.setContentView(R.layout.dialogue_replace_cart);
                            replaceCartAlert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            replaceCartAlert.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }

                        TextView alertTextView = (TextView)replaceCartAlert.findViewById(R.id.alertTextView);
                        TextView yesButton = (TextView)replaceCartAlert.findViewById(R.id.yesButton);
                        TextView noButton = (TextView)replaceCartAlert.findViewById(R.id.noButton);

                        String preResName = loginSession.getDbResName();
                        String newResName = restaurantName;
                        String tag = getResources().getString(R.string.cartContainsDishes)+ preResName + getResources().getString(R.string.doUWantToDiscard)+newResName;
                        alertTextView.setText(tag);

                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                databaseManager.clearTable();
                                loginSession.clear_restaurant();
                                replaceCartAlert.dismiss();
                                loginSession.saveCardCount(0);

                                int cartCount = databaseManager.getQuantityCount();
                                loginSession.saveCardCount(cartCount);
                                if (cartCount > 0) {
                                    newBaseHomeScreen.cartCountTextView.setText(loginSession.getCartCount());
                                    newBaseHomeScreen.cartCountTextView.setVisibility(View.VISIBLE);
                                } else if (cartCount == 0) {
                                    newBaseHomeScreen.cartCountTextView.setVisibility(View.GONE);
                                }

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

                    int cartCount = databaseManager.getQuantityCount();
                    loginSession.saveCardCount(cartCount);
                    if (cartCount > 0) {
                        newBaseHomeScreen.cartCountTextView.setText(loginSession.getCartCount());
                        newBaseHomeScreen.cartCountTextView.setVisibility(View.VISIBLE);
                    } else if (cartCount == 0) {
                        newBaseHomeScreen.cartCountTextView.setVisibility(View.GONE);
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
                convertView = infalInflater.inflate(R.layout.custom_favorite_menu_list, null);
            }
            TextView menuRestaurantName = (TextView) convertView
                    .findViewById(R.id.menuRestaurantName);
            LinearLayout mainLayout = (LinearLayout) convertView
                    .findViewById(R.id.mainLayout);
            menuRestaurantName.setText(headerTitle);

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

        if (loginSession.isLoggedIn()) {

            getFavList();
            loginLayout.setVisibility(View.GONE);
            favRestaurantList.setVisibility(View.GONE);
            favMenuList.setVisibility(View.VISIBLE);
            menuView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            restaurantView.setBackgroundColor(getResources().getColor(R.color.white));

        } else {

            loginLayout.setVisibility(View.VISIBLE);
            favRestaurantList.setVisibility(View.GONE);

        }
        int cartCount = databaseManager.getQuantityCount();
        loginSession.saveCardCount(cartCount);
        if (cartCount > 0) {
            newBaseHomeScreen.cartCountTextView.setText(loginSession.getCartCount());
            newBaseHomeScreen.cartCountTextView.setVisibility(View.VISIBLE);
        } else if (cartCount == 0) {
            newBaseHomeScreen.cartCountTextView.setVisibility(View.GONE);
        }
    }
}
