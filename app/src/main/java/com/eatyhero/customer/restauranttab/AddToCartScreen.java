package com.eatyhero.customer.restauranttab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.CircleTransform;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;
import com.eatyhero.customer.model.AddonList;
import com.eatyhero.customer.model.CartDetails;
import com.eatyhero.customer.model.MenuSizeOptionList;
import com.eatyhero.customer.model.SubAddonList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 20-03-2017.
 */
public class AddToCartScreen extends BaseActivity implements ServerListener, View.OnClickListener {

    //Create objects
    DatabaseManager databaseManager;
    SubAddonList.ProductAddons subAddons;
    MenuSizeOptionList menuSizeOptionList;
    SubAddonsListAdapter subAddonsListAdapter;
    MenuSizeOptionListAdapter menuSizeOptionListAdapter;
    SubAddonList subAddonsList;
    LoginSession loginSession;
    MainAddonsListAdapter mainAddonsListAdapter;
    ServerRequest serverRequestHandler;
    Utility utility;
    LayoutInflater inflater;
    TextView menuNameTextView;


    //String objects
    String menuId, menuName, menuPrice, menuType, menuAddonCheck, instructionCheck, menuSizeOption, restauranID, dealmenu;
    String small, smallValue, medium, mediumValue, large, largeValue;
    String sliceName, slicePrice, sliceId;
    String mainAddonId, mainAddonName, mainaddonsCount, mainaddonsMiniCount;
    String selectedAddons = "";
    String getMenuSizeFromList, getMenuPriceFromList;
    String getMainAddon;
    String getMainAddonIdFromHash;
    String menuSizeValue = "";
    String getInstruction, deal;
    String selectedMenuSizeName, selectedMenuSizePrize, selectedMenuSizeId, productDetailId;

    //int Objects
    int limitation;
    int minLimitation;
    int lastPosition;
    int positionOfSize;
    int cartCount;

    //boolean objects
    boolean checked = false;

    //XML Objects
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    RelativeLayout addonListViewLayout, subAddonSlideLayout, visibleLayout;
    LinearLayout allContentLayout;
    TextView addonsBanner, menuSizeTextBanner, quantityTextView, addonCancelButton, actionBarTitleTextView;
    EditText instructionsEditText;
    ListView menuSizeOptionListView, mainAddonsListView, subAddonListView;
    Button addToCartButton, addAddonButton, plusButton, minusButton;
    CardView instructionCardView;
    ImageView backIconImageView, menuImage;

    TextView mainAddonNameTextView, limitTextView;

    //Create Hashmap
    LinkedHashMap<String, List<SubAddonList.ProductAddons>> selectedSubAddons = new LinkedHashMap<String, List<SubAddonList.ProductAddons>>();
    HashMap<String, String> mainAddonIdMap = new HashMap<String, String>();
    HashMap<String, String> subAddonLimit = new HashMap<String, String>();
    HashMap<String, String> subAddonMinLimit = new HashMap<String, String>();

    //Create Arraylist
    List<String> mainAddonsList = new ArrayList<String>();
    List<String> selectedMenuSize = new ArrayList<String>();
    List<String> selectedMenuPrice = new ArrayList<String>();
    List<SubAddonList.ProductAddons> subAddonList = new ArrayList<>();
    List<MenuSizeOptionList> sizeLists = new ArrayList<MenuSizeOptionList>();

    List<AddonList.MainAddon> dummyMainAddon = new ArrayList<>();
    List<String> MAINADDONNAME = new ArrayList<>();
    List<Integer> MAINADDONCOUNT = new ArrayList<>();

    AddonList addonList;
    boolean FIRST = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cart_screen);

        visibleLayout = (RelativeLayout) findViewById(R.id.visibleLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);
        allContentLayout = (LinearLayout) findViewById(R.id.allContentLayout);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        menuSizeTextBanner = (TextView) findViewById(R.id.menuSizeTextBanner);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuSizeOptionListView = (ListView) findViewById(R.id.menuSizeOptionListView);
        mainAddonsListView = (ListView) findViewById(R.id.addonsListView);
        subAddonSlideLayout = (RelativeLayout) findViewById(R.id.subAddonSlideLayout);
        addonListViewLayout = (RelativeLayout) findViewById(R.id.addonListViewLayout);
        subAddonListView = (ListView) findViewById(R.id.subAddonListView);
        addAddonButton = (Button) findViewById(R.id.addAddonButton);
        addonsBanner = (TextView) findViewById(R.id.addonsBanner);
        addonCancelButton = (TextView) findViewById(R.id.addonCancelButton);
        instructionCardView = (CardView) findViewById(R.id.instructionCardView);
        instructionsEditText = (EditText) findViewById(R.id.instructionsEditText);
        quantityTextView = (TextView) findViewById(R.id.quantityTextView);
        plusButton = (Button) findViewById(R.id.plusButton);
        minusButton = (Button) findViewById(R.id.minusButton);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);

        mainAddonNameTextView = (TextView) findViewById(R.id.mainAddonNameTextView);
        limitTextView = (TextView) findViewById(R.id.limitTextView);
        menuImage = (ImageView) findViewById(R.id.menuImage);
        menuNameTextView = findViewById(R.id.menuName);

        //Initialize objects
        databaseManager = DatabaseManager.getInstance(this);
        serverRequestHandler = ServerRequest.getInstance(this);
        loginSession = LoginSession.getInstance(this);
        utility = Utility.getInstance(this);

        //showStatusBar
        utility.showStatusBar();

        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);

        //Set List view choice mode
        menuSizeOptionListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        subAddonListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        cartCount = databaseManager.getCount();

        /**************Call Get Addon********************/

        Intent intent = getIntent();

        if (intent != null) {

            menuId = intent.getStringExtra("productid");
            dealmenu = intent.getStringExtra("dealmenu");
            actionBarTitleTextView.setText(intent.getStringExtra("productname"));
            loginSession.setDbResName(intent.getStringExtra("restaurantName"));

            if(getIntent().getStringExtra("image")!=null && !getIntent().getStringExtra("image").isEmpty()){
                Picasso.with(this)
                        .load(getIntent().getStringExtra("image"))
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(menuImage);
            }

            if (utility.isConnectingToInternet()) {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("productId", menuId);
                if (dealmenu.isEmpty() || dealmenu.equalsIgnoreCase("null")) {
                    params.put("deal", "No");
                }else {
                    params.put("deal", "Yes");
                }

                utility.showProgressDialog();
                serverRequestHandler.createRequest(this, params, RequestID.REQ_ADDON);
            } else {

                utility.toast(getResources().getString(R.string.noInternet));
            }
        }

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //mainAddonsListView clickevent
        mainAddonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //get main addon name
                String getMainAddonNAME = (String) mainAddonsListView.getItemAtPosition(position);

                String str = getMainAddonNAME;
                String[] tokens = str.split("\\(");
                getMainAddon = tokens[0].toString().trim();

                String limitttt = tokens[1].toString().trim();
                mainAddonNameTextView.setText(getResources().getString(R.string.choose) + getMainAddon);
                limitTextView.setText(limitttt.substring(0, limitttt.length() - 1));

                getMainAddonIdFromHash = mainAddonIdMap.get(getMainAddon);
                Log.e("Main addon id ", "Main addon id is : " + getMainAddonIdFromHash);

                //Get Addon limit from hash map

                String limit = subAddonLimit.get(getMainAddon);
                String minlimit = subAddonMinLimit.get(getMainAddon);

                try{
                    limitation = Integer.parseInt(limit != null ? limit : "0");
                } catch(NumberFormatException ex){ // handle your exception
                    limitation = 0;
                }
//                minLimitation = Integer.parseInt(minlimit != null ? minlimit : "0");
                Log.e("Addon Limit", "Addon limit : " + limitation);


                if (menuSizeOption.equalsIgnoreCase("Fixed")) {
                    if (selectedSubAddons.containsKey(getMainAddon)) {
                        subAddonList = selectedSubAddons.get(getMainAddon);
                        Log.e("Selected Addons", "Selected Addons : " + subAddonList);

                        if (subAddonList.size() > 0) {
                            subAddonsListAdapter = new SubAddonsListAdapter(AddToCartScreen.this, subAddonList);
                            subAddonListView.setAdapter(subAddonsListAdapter);
                            Utility.setListViewHeightBasedOnChildren(subAddonListView);
                            //open slide list
                            drawerLayout.openDrawer(subAddonSlideLayout);
                        } else {
                            if (utility.isConnectingToInternet()) {
                                //server method
                                GetSubAddons();

                            } else {
                                utility.toast(getResources().getString(R.string.noInternet));
                            }
                        }
                    } else {
                        if (utility.isConnectingToInternet()) {
                            //server method
                            GetSubAddons();
                        } else {
                            utility.toast(getResources().getString(R.string.noInternet));
                        }
                    }
                } else if (menuSizeOption.equalsIgnoreCase("size") || menuSizeOption.equalsIgnoreCase("slice")) {
                    if (!(menuSizeValue.equals(""))) {
                        if (selectedSubAddons.containsKey(getMainAddon)) {
                            subAddonList = selectedSubAddons.get(getMainAddon);

                            Log.e("Selected Addons", "Selected Addons : " + subAddonList);

                            if (subAddonList.size() > 0) {
                                subAddonsListAdapter = new SubAddonsListAdapter(AddToCartScreen.this, subAddonList);
                                subAddonListView.setAdapter(subAddonsListAdapter);
                                Utility.setListViewHeightBasedOnChildren(subAddonListView);
                                //open slide list
                                drawerLayout.openDrawer(subAddonSlideLayout);
                            } else {
                                if (utility.isConnectingToInternet()) {
                                    //server call method
                                    GetSubAddons();
                                } else {
                                    utility.toast(getResources().getString(R.string.noInternet));
                                }
                            }
                        } else {
                            if (utility.isConnectingToInternet()) {
                                //server call method
                                GetSubAddons();
                            } else {

                                utility.toast(getResources().getString(R.string.noInternet));
                            }
                        }
                    } else {
                        utility.toast(getResources().getString(R.string.selectAnyoneOfMenu) + menuSizeOption + getResources().getString(R.string.price));
                    }

                }

            }
        });


        //AddAddon Button
        addAddonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    subAddonsListAdapter.AddToHashMap();
                } catch (Exception e) {
                }
            }
        });


        //AddonCancel Button
        addonCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(subAddonSlideLayout);
            }
        });


        //AddToCart Button
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Quantitys = quantityTextView.getText().toString().trim();
                HashSet<String> YESNO = new HashSet<>();

                if (!(menuSizeValue.equals(""))) {

                    if (!(Quantitys.equals("0"))) {

                        for (int i = 0; i < MAINADDONNAME.size(); i++) {

                            ArrayList<String> LIMITCHECK = new ArrayList<String>();

                            if (Integer.parseInt(subAddonMinLimit.get(MAINADDONNAME.get(i))) > 0) {

                                if (selectedSubAddons.size() > 0) {

                                    Set<String> keys = selectedSubAddons.keySet();

                                    for (String Keys : keys) {

                                        selectedAddons = "";
                                        String mainAddonNAME = Keys;

                                        List<SubAddonList.ProductAddons> selected = null;

                                        if (!mainAddonNAME.equals("")) {
                                            selected = selectedSubAddons.get(Keys);

                                        }

                                        for (int j = 0; j < selected.size(); j++) {

                                            subAddons = selected.get(j);

                                            if (subAddons.isSelected()) {

                                                if (MAINADDONNAME.get(i).equalsIgnoreCase(mainAddonNAME)) {

                                                    LIMITCHECK.add(subAddons.subaddons_name);
                                                }

                                            }
                                        }

                                    }

                                    if (LIMITCHECK.size() >= MAINADDONCOUNT.get(i)) {

                                        YESNO.add("YES");

                                    } else {

                                        utility.toast(getResources().getString(R.string.selectMinimumAddons));
                                        YESNO.add("NO");
                                    }

                                } else {

                                    utility.toast(getResources().getString(R.string.selectMinimumAddons));
                                    YESNO.add("NO");
                                    break;
                                }
                            } else {

                                YESNO.add("YES");
                            }
                        }
                        if (!YESNO.contains("NO")) {

                            addMenusToDataBase();
                        }

                    } else {
                        utility.toast(getResources().getString(R.string.selectQuantity));
                    }
                } else {
                    utility.toast(getResources().getString(R.string.selectAnyoneOfMenu) + menuSizeOption + getResources().getString(R.string.price));
                }
            }
        });


    }


    private void addMenusToDataBase() {

        if (menuSizeOption.equalsIgnoreCase("Fixed")) {

            double totalOfSubaddons = 0;
            getInstruction = instructionsEditText.getText().toString();
            String Quantitys = quantityTextView.getText().toString().trim();

            String mainandsubaddons = "";

            Set<String> keys = selectedSubAddons.keySet();

            for (String Keys : keys) {
                selectedAddons = "";

                String mainAddonNAME = Keys;
                Log.e("Main addon name", "main addon anme" + mainAddonNAME);

                List<SubAddonList.ProductAddons> selected = null;

                if (!mainAddonNAME.equals("")) {
                    selected = selectedSubAddons.get(Keys);
                }

                for (int i = 0; i < selected.size(); i++) {
                    subAddons = selected.get(i);

                    if (subAddons.isSelected()) {
                        if (selectedAddons.isEmpty()) {
                            selectedAddons += subAddons.subaddons_name + "(" + subAddons.subaddons_price + ")";
                            totalOfSubaddons += Double.parseDouble(subAddons.subaddons_price);
                        } else {
                            selectedAddons += " + " + subAddons.subaddons_name + "(" + subAddons.subaddons_price + ")";
                            totalOfSubaddons += Double.parseDouble(subAddons.subaddons_price);
                        }
                    }
                }

                if (mainandsubaddons.equals("")) {
                    if (selectedAddons.equals("")) {
                        mainandsubaddons = "";
                    } else {
                        mainandsubaddons += mainAddonNAME + " (" + selectedAddons + ")";
                    }

                } else {
                    if (!selectedAddons.isEmpty()) {
                        mainandsubaddons += ", " + mainAddonNAME + " (" + selectedAddons + ")";
                    }
                }

            }

            double totalPrice = (Double.parseDouble(menuPrice) + totalOfSubaddons) * Integer.parseInt(Quantitys);

            CartDetails cartItems = new CartDetails();
            cartItems.setMenuId(menuId);
            cartItems.setResId(restauranID);
            if (dealmenu.isEmpty() || dealmenu.equalsIgnoreCase("null")) {
                cartItems.setMenuName(menuName);//menuName
            } else {
                cartItems.setMenuName(menuName + "[Deal :: " + dealmenu + "]");//menuName
            }
            cartItems.setMenuType(menuType);
            cartItems.setMenuSize(menuSizeValue);
            cartItems.setMenuPrice(menuPrice);
            cartItems.setAddonName(mainandsubaddons);
            cartItems.setAddonPrice(String.format("%.2f", totalOfSubaddons));
            cartItems.setQuantity(Quantitys);
            cartItems.setTotalPrice(String.format("%.2f", totalPrice));
            cartItems.setInstruction(getInstruction);

            Log.e("cartItems", "cartItems" + cartItems.getMenuId() + cartItems.getResId() + cartItems.getMenuName() + cartItems.getMenuType() + cartItems.getMenuSize() + cartItems.getMenuPrice() + cartItems.getAddonName() + cartItems.getAddonPrice() + cartItems.getQuantity() + cartItems.getTotalPrice() + cartItems.getInstruction());
            //values insert to database
            databaseManager.openDatabase();
            databaseManager.insert(cartItems);
            databaseManager.closeDatabase();

            Toast toast = Toast.makeText(this, getResources().getString(R.string.menuAddedSuccessfully), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();


            finish();

        } else if (menuSizeOption.equalsIgnoreCase("Size") || menuSizeOption.equalsIgnoreCase("slice")) {

            String mainAndSubaddons = "";
            double totalOfSubaddons = 0;
            getInstruction = instructionsEditText.getText().toString();
            String Quantitys = quantityTextView.getText().toString().trim();

            double selectMenuSizePrice = Double.parseDouble(getMenuPriceFromList);

            Log.e("Main addon Price", "main addon Price" + getMenuPriceFromList);

            Set<String> keys = selectedSubAddons.keySet();

            for (String Keys : keys) {

                selectedAddons = "";
                String mainAddonNAME = Keys;
                Log.e("Main addon name", "main addon anme" + mainAddonNAME);

                List<SubAddonList.ProductAddons> selected = null;

                if (!mainAddonNAME.equals("")) {
                    selected = selectedSubAddons.get(Keys);
                }

                for (int i = 0; i < selected.size(); i++) {

                    subAddons = selected.get(i);

                    if (subAddons.isSelected()) {

                        if (selectedAddons.isEmpty()) {
                            selectedAddons += subAddons.subaddons_name + "(" + subAddons.subaddons_price + ")";
                            totalOfSubaddons += Double.parseDouble(subAddons.subaddons_price);
                        } else {
                            selectedAddons += " + " + subAddons.subaddons_name + "(" + subAddons.subaddons_price + ")";
                            totalOfSubaddons += Double.parseDouble(subAddons.subaddons_price);
                        }
                    }
                }

                if (mainAndSubaddons.equals("")) {
                    if (selectedAddons.equals("")) {
                        mainAndSubaddons = "";
                    } else {
                        mainAndSubaddons += mainAddonNAME + " (" + selectedAddons + ")";
                    }

                } else {
                    if (!selectedAddons.isEmpty()) {
                        mainAndSubaddons += ", " + mainAddonNAME + " (" + selectedAddons + ")";
                    }

                }

            }

            double totalPrice = (selectMenuSizePrice + totalOfSubaddons) * Integer.parseInt(Quantitys);

            CartDetails cartItems = new CartDetails();
            cartItems.setMenuId(menuId);
            cartItems.setResId(restauranID);
            if (dealmenu.equalsIgnoreCase("null") || dealmenu.isEmpty()) {
                cartItems.setMenuName(menuName);//menuName
            } else {
                Log.e("deal", "2 " + dealmenu);
                cartItems.setMenuName(menuName + "[Deal :: " + dealmenu + "]");//menuName
            }
            cartItems.setMenuType(menuType);
            cartItems.setMenuSize(getMenuSizeFromList);
            cartItems.setMenuPrice(getMenuPriceFromList);
            cartItems.setAddonName(mainAndSubaddons);
            cartItems.setAddonPrice(String.format("%.2f", totalOfSubaddons));
            cartItems.setQuantity(Quantitys);
            cartItems.setTotalPrice(String.format("%.2f", totalPrice));
            cartItems.setInstruction(getInstruction);

            //values insert to database
            databaseManager.openDatabase();
            databaseManager.insert(cartItems);
            databaseManager.closeDatabase();

            Toast toast = Toast.makeText(this, getResources().getString(R.string.menuAddedSuccessfully), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();


            finish();
        }
    }


    public void GetSubAddons() {

        Map<String, String> params = new HashMap<>();
        // the POST parameters:

        if (menuSizeOption.equalsIgnoreCase("Fixed")) {
            params.put("action", "fixedAddonPrice");
            params.put("resid", restauranID);
            params.put("menu_id", menuId);
            params.put("main_addon_id", getMainAddonIdFromHash);

        } else if (menuSizeOption.equalsIgnoreCase("slice")) {

            params.put("action", "ProductSubAddon");
            //params.put("productId", restauranID);
            params.put("productId", menuId);
            params.put("productDetailId", productDetailId);
            params.put("mainAddonId", getMainAddonIdFromHash);

        } else if (menuSizeOption.equalsIgnoreCase("size")) {
            params.put("action", "sizeAddonPrice");
            params.put("size", getMenuSizeFromList);
            params.put("resid", restauranID);
            params.put("menu_id", menuId);
            params.put("main_addon_id", getMainAddonIdFromHash);
        }

        Log.e("Values to server", "" + params);
        utility.showProgressDialog();
        serverRequestHandler.createRequest(this, params, RequestID.REQ_SUBADDON);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.minusButton:

                String qty1 = quantityTextView.getText().toString().trim();

                int decrease = Integer.parseInt(qty1);
                Log.e("Count values is", "" + decrease);

                if (decrease == 1) {
                    utility.showAlertDialog(getResources().getString(R.string.minimum1));
                } else {
                    decrease -= 1;
                    quantityTextView.setText(String.valueOf(decrease));
                }

                break;

            case R.id.plusButton:

                String qty = quantityTextView.getText().toString().trim();

                int increase = Integer.parseInt(qty);
                Log.e("Count values is", "" + increase);

                if (increase >= 25) {
                    utility.showAlertDialog(getResources().getString(R.string.maximumAvailableQuantity));
                } else {
                    increase += 1;
                    quantityTextView.setText(String.valueOf(increase));
                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestId) {

        utility.hideProgressDialog();
        dummyMainAddon.clear();
        visibleLayout.setVisibility(RelativeLayout.VISIBLE);
        switch (requestId) {
            case REQ_ADDON:

//                Log.e("Addons Response", "" + result.toString());

                addonList = (AddonList) result;

                menuNameTextView.setText(addonList.result.productDetails.Details.menu_name+"\n"+addonList.result.productDetails.Details.menu_description);

                //slice list
                ArrayList<AddonList.Variants> Slice = null;
                //mainAddons list
                ArrayList<AddonList.MainAddon> MainAddon = null;

                //Product details get
                restauranID = addonList.result.productDetails.Details.restaurant_id;
                menuName = addonList.result.productDetails.Details.menu_name;
                menuType = addonList.result.productDetails.Details.menu_type;
                menuPrice = "0.00";
                menuAddonCheck = addonList.result.productDetails.Details.menu_addon;
                instructionCheck = "Yes";
                menuSizeOption = "slice";
                small = "";
                smallValue = "0.00";
                medium = "";
                mediumValue = "0.00";
                large = "";
                largeValue = "0.00";

                Slice = addonList.result.productDetails.variants;
                Log.e("Slice", "" + Slice);

                MainAddon = addonList.result.productDetails.MainAddon;
                dummyMainAddon = addonList.result.productDetails.MainAddon;
                Log.e("MainAddon", "" + MainAddon);


                MAINADDONNAME.clear();
                MAINADDONCOUNT.clear();
                //MainAddons Loop
                if (MainAddon != null) {
                    //check addon if yes get Main addons
                    if (menuAddonCheck.equalsIgnoreCase("Yes")) {
                        for (AddonList.MainAddon mainaddons : MainAddon) {

                            mainAddonId = mainaddons.mainaddons_id;
                            mainAddonName = mainaddons.mainaddons_name.trim();
                            mainaddonsCount = mainaddons.mainaddons_count;
                            mainaddonsMiniCount = mainaddons.mainaddons_mini_count;

                            MAINADDONNAME.add(mainaddons.mainaddons_name.trim());
                            MAINADDONCOUNT.add(Integer.parseInt(mainaddons.mainaddons_mini_count));

                            mainAddonIdMap.put(mainAddonName, mainAddonId);
                            subAddonLimit.put(mainAddonName, mainaddonsCount);
                            subAddonMinLimit.put(mainAddonName, mainaddonsMiniCount);
                            mainAddonsList.add(mainAddonName + getResources().getString(R.string.minimum) + mainaddonsMiniCount + getResources().getString(R.string.max) + mainaddonsCount + " )");
                            Log.e("mainAddonsList", "" + mainAddonsList);

                        }
                    }
                }

                //MenuSizeOption check
                if (menuSizeOption.equalsIgnoreCase("Size")) {

                    if (small.length() > 0) {

                        selectedMenuSize.add(small);
                        selectedMenuPrice.add(smallValue);
//                        sizeList.add(small + " = " + getResources().getString(R.string.CurrencySymbol) + smallValue);

                        selectedMenuSizeName = small;
                        selectedMenuSizePrize = smallValue;

                        menuSizeOptionList = new MenuSizeOptionList(selectedMenuSizeName, selectedMenuSizePrize, "", checked);
                        sizeLists.add(menuSizeOptionList);
                    }

                    if (medium.length() > 0) {
                        selectedMenuSize.add(medium);
                        selectedMenuPrice.add(mediumValue);
//                        sizeList.add(medium + " = " + getResources().getString(R.string.CurrencySymbol) + mediumValue);

                        selectedMenuSizeName = medium;
                        selectedMenuSizePrize = mediumValue;

                        menuSizeOptionList = new MenuSizeOptionList(selectedMenuSizeName, selectedMenuSizePrize, "", checked);
                        sizeLists.add(menuSizeOptionList);
                    }


                    if (large.length() > 0) {
                        selectedMenuSize.add(large);
                        selectedMenuPrice.add(largeValue);
//                        sizeList.add(large + " = " + getResources().getString(R.string.CurrencySymbol) + largeValue);

                        selectedMenuSizeName = large;
                        selectedMenuSizePrize = largeValue;

                        menuSizeOptionList = new MenuSizeOptionList(selectedMenuSizeName, selectedMenuSizePrize, "", checked);
                        sizeLists.add(menuSizeOptionList);
                    }

                } else if (menuSizeOption.equalsIgnoreCase("slice")) {
                    //slice loop
                    if (Slice != null) {

                        for (AddonList.Variants slice : Slice) {

                            sliceName = slice.sub_name.trim();
                            slicePrice = slice.orginal_price;
                            sliceId = slice.id;
                            selectedMenuSize.add(sliceName);
                            selectedMenuPrice.add(slicePrice);
                            //selectedMenuPrice.add(sliceId);
//                            sizeList.add(sliceName + "  =  " + getResources().getString(R.string.CurrencySymbol) + slicePrice);

                            selectedMenuSizeName = sliceName;
                            selectedMenuSizePrize = slicePrice;
                            selectedMenuSizeId = sliceId;

                            menuSizeOptionList = new MenuSizeOptionList(selectedMenuSizeName, selectedMenuSizePrize, selectedMenuSizeId, checked);
                            sizeLists.add(menuSizeOptionList);

                        }
                    }
                }


                //MainAddons List show and hide
                if (mainAddonsList.size() > 0) {

                    addonListViewLayout.setVisibility(View.VISIBLE);
                    addonsBanner.setVisibility(View.VISIBLE);

                    mainAddonsListAdapter = new MainAddonsListAdapter(AddToCartScreen.this, mainAddonsList);
                    mainAddonsListView.setAdapter(mainAddonsListAdapter);
                    getTotalHeightofListView(mainAddonsListView);

                } else {
                    addonListViewLayout.setVisibility(View.GONE);
                    addonsBanner.setVisibility(View.GONE);
                }

                //Size option check
                if (menuSizeOption.equalsIgnoreCase("Fixed")) {
                    //menuSizeOptionListView.setVisibility(View.GONE);
                    //menuSizeTextBanner.setVisibility(View.GONE);
                    menuSizeOptionListAdapter = new MenuSizeOptionListAdapter(AddToCartScreen.this, sizeLists);
                    menuSizeOptionListView.setAdapter(menuSizeOptionListAdapter);
                    menuSizeTextBanner.setVisibility(View.VISIBLE);
                } else if (menuSizeOption.equalsIgnoreCase("Size") || (menuSizeOption.equalsIgnoreCase("Slice"))) {
                    menuSizeOptionListAdapter = new MenuSizeOptionListAdapter(AddToCartScreen.this, sizeLists);
                    menuSizeOptionListView.setAdapter(menuSizeOptionListAdapter);
                    menuSizeTextBanner.setVisibility(View.VISIBLE);
                    try {
                        utility.setListViewHeightBasedOnChildren(menuSizeOptionListView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                //Instruction  show and hide
                if (instructionCheck.equalsIgnoreCase("No")) {
                    instructionCardView.setVisibility(View.GONE);
                }

                allContentLayout.setVisibility(RelativeLayout.VISIBLE);

                break;


            case REQ_SUBADDON:

                Log.e("subAddons Response", "" + result.toString());

                subAddonsList = (SubAddonList) result;
                List<SubAddonList.ProductAddons> subAddonsList1 = subAddonsList.result.productAddons;

                for (SubAddonList.ProductAddons subAddons : subAddonsList1) {

                    Log.e("subAddons Name", "" + subAddons.subaddons_name);

                }

                subAddonsListAdapter = new SubAddonsListAdapter(AddToCartScreen.this, subAddonsList1);
                subAddonListView.setAdapter(subAddonsListAdapter);
                Utility.setListViewHeightBasedOnChildren(subAddonListView);
                drawerLayout.openDrawer(subAddonSlideLayout);
                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestId) {

        utility.toast(error);
        utility.hideProgressDialog();

    }

    //MainAddonsListAdapter class
    public class MainAddonsListAdapter extends BaseAdapter {

        //Create objects
        Activity activity;
        List<String> mainAddonslist;

        public MainAddonsListAdapter(Activity activity, List<String> mainAddonslist) {
            this.activity = activity;
            this.mainAddonslist = mainAddonslist;
        }

        @Override
        public int getCount() {
            Log.e("mainAddonslist", "mainAddonslist" + mainAddonslist.size());
            return mainAddonslist.size();
        }

        @Override
        public Object getItem(int position) {
            return mainAddonslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            try {

                inflater = AddToCartScreen.this.getLayoutInflater();

                if (convertView == null)
                    convertView = inflater.inflate(R.layout.custom_addon, null);

                TextView mainAddonsTextView = (TextView) convertView.findViewById(R.id.mainAddonsTextView);

                mainAddonsTextView.setText(mainAddonslist.get(position));
            } catch (NullPointerException e) {
                Log.e("mainAddonslist", "mainAddonslist" + e);
            }
            return convertView;
        }
    }


    //SubAddonsListAdapter class
    public class SubAddonsListAdapter extends BaseAdapter {

        //Create objects
        Activity activity;
        List<SubAddonList.ProductAddons> subAddonLists = null;


        public SubAddonsListAdapter(Activity activity, List<SubAddonList.ProductAddons> subAddonLists) {
            this.activity = activity;
            this.subAddonLists = subAddonLists;

        }

        @Override
        public int getCount() {
            return subAddonLists.size();
        }

        @Override
        public Object getItem(int position) {
            return subAddonLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)
                convertView = inflater.inflate(R.layout.custom_subaddons, null);

            final TextView addonNameTextView = (TextView) convertView.findViewById(R.id.addonNameTextView);
            final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.addonSelectCheckBox);
            if (subAddonLists.get(position).subaddons_price.equalsIgnoreCase("0")) {
                addonNameTextView.setText(subAddonLists.get(position).subaddons_name
                        + "( " + "Free" + " )");
            } else {
                addonNameTextView.setText(subAddonLists.get(position).subaddons_name
                        + "(" + String.format("%.2f", Double.parseDouble(subAddonLists.get(position).subaddons_price)) + " " + loginSession.getCurrencyCode() + ")");

            }

            checkbox.setChecked(subAddonLists.get(position).isSelected());

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    ArrayList<String> getAddons = new ArrayList<String>();

                    for (int i = 0; i < subAddonLists.size(); i++) {

                        SubAddonList.ProductAddons productAddons = subAddonLists.get(i);

                        if (productAddons.isSelected()) {

                            getAddons.add(productAddons.subaddons_name);

                        }
                    }
                    Log.e("Count values is :", "" + getAddons);

                    if (isChecked) {

                        if (limitation == 0) {

                            subAddonLists.get(position).setSelected(isChecked);

                        } else {

                            if (getAddons.size() == limitation) {

                                checkbox.setChecked(false);
                                utility.showAlertDialog(getResources().getString(R.string.selectMaximum) + limitation + getResources().getString(R.string.addonsOnly));

                            } else {

                                subAddonLists.get(position).setSelected(isChecked);
                            }
                        }

                    } else {
                        subAddonLists.get(position).setSelected(false);
                    }

                    Log.e("11111111111", "" + subAddonLists.get(position));

                }
            });

            return convertView;
        }

        public void AddToHashMap() {

            if (!getMainAddon.equals("")) {

                selectedSubAddons.put(getMainAddon, subAddonLists);

                Log.e("Selected Addons", "HashMap values : " + selectedSubAddons);

                drawerLayout.closeDrawer(subAddonSlideLayout);

            }
        }
    }

    //Menu Sizeoption ListAdapter
    private class MenuSizeOptionListAdapter extends BaseAdapter {

        //Create objects
        Activity activity;
        List<MenuSizeOptionList> sizelist;

        public MenuSizeOptionListAdapter(Activity activity, List<MenuSizeOptionList> sizelist) {

            this.activity = activity;
            this.sizelist = sizelist;
        }

        @Override
        public int getCount() {

            return sizelist.size();
        }

        @Override
        public Object getItem(int position) {
            return sizelist.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null)
                convertView = inflater.inflate(R.layout.custom_menusize_list, null);

            final TextView menuSizeOptionNameTextView = (TextView) convertView.findViewById(R.id.menuSizeOptionNameTextView);
            final TextView menuSizeOptionPriceTextView = (TextView) convertView.findViewById(R.id.menuSizeOptionPriceTextView);
            final RadioButton radioButton = (RadioButton) convertView.findViewById(R.id.selectRadioButton);

            menuSizeOptionNameTextView.setText(sizelist.get(position).getMenuSizeName());
            menuSizeOptionPriceTextView.setText(String.format("%.2f", Double.parseDouble(sizelist.get(position).getMenuSizePrice())) + " " + loginSession.getCurrencyCode());
            if (FIRST) {
                sizelist.get(0).setChecked(true);
                productDetailId = sizelist.get(0).getMenuSizeId();
                positionOfSize = 0;
                lastPosition = positionOfSize;
                menuSizeValue = sizelist.get(0).getMenuSizeName() + " = " + loginSession.getCurrencyCode() + String.format("%.2f", Double.parseDouble(sizelist.get(0).getMenuSizePrice()));
                FIRST = false;


                try {
                    if (menuSizeOption.equalsIgnoreCase("Size") || menuSizeOption.equalsIgnoreCase("slice")) {

                        getMenuSizeFromList = selectedMenuSize.get(0);
                    }
                    getMenuPriceFromList = selectedMenuPrice.get(0);

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }

            if (sizelist.size() == 1) {
                radioButton.setChecked(true);
                for (int i = 0; i < sizelist.size(); i++) {
                    if (i == position) {
                        sizelist.get(i).setChecked(true);
                    } else {
                        sizelist.get(i).setChecked(false);
                    }
                }


                notifyDataSetChanged();

                if (lastPosition == position) {
                    Log.e("sample position", "Same position");

                } else {
                    selectedSubAddons.clear();
                    Log.e("position Changed", "position Changed");
                }

                productDetailId = sizelist.get(position).getMenuSizeId();

                positionOfSize = position;
                lastPosition = positionOfSize;
                Log.e("positionOfSize", "" + positionOfSize);

//                    menuSizeValue = (String) menuSizeOptionListView.getItemAtPosition(position);
                menuSizeValue = sizelist.get(position).getMenuSizeName() + " = " + loginSession.getCurrencyCode() + String.format("%.2f", Double.parseDouble(sizelist.get(position).getMenuSizePrice()));

                Log.e("Menu size values", "menu size values is : " + menuSizeValue);

                try {
                    if (menuSizeOption.equalsIgnoreCase("Size") || menuSizeOption.equalsIgnoreCase("slice")) {

                        getMenuSizeFromList = selectedMenuSize.get(position);
                    }
                    getMenuPriceFromList = selectedMenuPrice.get(position);

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                Log.e("Menu Selected", "menu size name: " + getMenuSizeFromList + " Size price=" + getMenuSizeFromList);

                if (addonList.result.productDetails.Details.price_option.equalsIgnoreCase("single")) {
                    //menuSizeOptionListView.setVisibility(View.GONE);
                    //menuSizeTextBanner.setVisibility(View.GONE);
                    menuSizeOptionListView.setVisibility(View.VISIBLE);
                    menuSizeTextBanner.setVisibility(View.VISIBLE);
                } else {
                    menuSizeOptionListView.setVisibility(View.VISIBLE);
                    menuSizeTextBanner.setVisibility(View.VISIBLE);
                }


            } else {
                radioButton.setChecked(false);
                boolean checked = sizelist.get(position).isChecked();

                if (checked) {
                    radioButton.setChecked(checked);

                } else {
                    radioButton.setChecked(false);
                }

                Log.e("customvalues", sizelist.get(position).getMenuSizeName());
            }

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("onClick", "onClick");
                    for (int i = 0; i < sizelist.size(); i++) {
                        if (i == position) {
                            sizelist.get(i).setChecked(true);
                        } else {
                            sizelist.get(i).setChecked(false);
                        }
                    }


                    notifyDataSetChanged();

                    if (lastPosition == position) {
                        Log.e("sample position", "Same position");

                    } else {
                        selectedSubAddons.clear();
                        Log.e("position Changed", "position Changed");
                    }

                    productDetailId = sizelist.get(position).getMenuSizeId();

                    positionOfSize = position;
                    lastPosition = positionOfSize;
                    Log.e("positionOfSize", "" + positionOfSize);

//                    menuSizeValue = (String) menuSizeOptionListView.getItemAtPosition(position);
                    menuSizeValue = sizelist.get(position).getMenuSizeName() + " = " + loginSession.getCurrencyCode() + String.format("%.2f", Double.parseDouble(sizelist.get(position).getMenuSizePrice()));

                    Log.e("Menu size values", "menu size values is : " + menuSizeValue);

                    try {
                        if (menuSizeOption.equalsIgnoreCase("Size") || menuSizeOption.equalsIgnoreCase("slice")) {

                            getMenuSizeFromList = selectedMenuSize.get(position);
                        }
                        getMenuPriceFromList = selectedMenuPrice.get(position);

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    Log.e("Menu Selected", "menu size name: " + getMenuSizeFromList + " Size price=" + getMenuSizeFromList);

                }
            });

            return convertView;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void onResume() {
        super.onResume();


       /* getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    RestaurantSubMenuScreen cardDetailScreen = new RestaurantSubMenuScreen();
                    FragmentManager fm2 = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft2 = fm2.beginTransaction();
                    fm2.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft2.replace(R.id.frameLayout, cardDetailScreen);
                    ft2.commit();
                    return true;
                }

                return false;
            }
        });
*/

    }

    public static void getTotalHeightofListView(ListView listView) {
        try {
            ListAdapter mAdapter = listView.getAdapter();

            int totalHeight = 0;

            for (int i = 0; i < mAdapter.getCount(); i++) {
                View mView = mAdapter.getView(i, null, listView);

                mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                totalHeight += mView.getMeasuredHeight();
                Log.w("HEIGHT" + i, String.valueOf(totalHeight));

            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();

        } catch (Exception e) {

        }
    }
}