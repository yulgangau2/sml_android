package com.eatyhero.customer.moretab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.AddressList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19-08-2016.
 */
public class AddressBookScreen extends BaseActivity  implements ServerListener {

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequest;
    AddressList addressList;
    AddressBookListAdapterClass addressBookListAdapter;


    //Create xml objects
    ListView addressListView;
    ImageView addButton;
    ImageView backIconImageView;
    TextView actionBarTitleTextView;
    RelativeLayout noAddressLayout;

    List<AddressList.AddressBook> addressList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addressbook_screen);

        //Initialize Objects
        loginSession     = LoginSession.getInstance(this);
        serverRequest    = ServerRequest.getInstance(this);

        //Initialize xml files
        Toolbar toolbar          = (Toolbar) findViewById(R.id.toolbar) ;
        addressListView          = (ListView)findViewById(R.id.addressListView);
        addButton                = (ImageView)toolbar.findViewById(R.id.addButton);
        backIconImageView        = (ImageView)toolbar. findViewById(R.id.backIconImageView);
        noAddressLayout          =  (RelativeLayout)findViewById(R.id.noAddressLayout);
        actionBarTitleTextView   = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        actionBarTitleTextView.setText(getResources().getString(R.string.addressBook));


        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddressBookScreen.this,AddAddressScreen.class);
                intent.putExtra("addressId","");
                intent.putExtra("screen","Add Address");
                intent.putExtra("title","");
                intent.putExtra("phone","");
                intent.putExtra("address","");
                intent.putExtra("doorNumber","");
                startActivity(intent);
            }
        });
    }

    public void getAddressList() {

        if (checkInternet()) {
            Map<String, String> params = new HashMap<>();
            // POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "AddressBookList");
            showProgressDialog();
            serverRequest.createRequest(this, params, RequestID.REQ_ADDRESSLIST);
        } else {
            noInternetAlertDialog();
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        addressList1.clear();
        switch (requestID){

            case REQ_ADDRESSLIST:
                try{
                    noAddressLayout.setVisibility(View.GONE);
                    addressListView.setVisibility(View.VISIBLE);
                    addressList = (AddressList)result;
                    addressList1=addressList.result.addressBook ;
                    addressBookListAdapter=new AddressBookListAdapterClass(this,addressList1,this);
                    addressListView.setAdapter(addressBookListAdapter);
                    addressBookListAdapter.notifyDataSetChanged();
                }catch (Exception e){e.printStackTrace();}
                break;
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        try{
        hideProgressDialog();
        addressListView.setAdapter(null);
        noAddressLayout.setVisibility(View.VISIBLE);
        addressListView.setVisibility(View.GONE);
        toast(error);
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void onResume() {
        super.onResume();
        getAddressList();
    }

    public class AddressBookListAdapterClass extends BaseAdapter implements ServerListener {

        //Create objects
        Activity activity;
        List<AddressList.AddressBook> addresses;
        Utility utility;
        LoginSession loginSession;
        AddressBookScreen addressBookScreen;
        ServerRequest serverRequestHandler;

        //XML objects
        TextView addressTitleTextView, addressTextView ,check_Primary;
        Switch addressStatusSwitch;
        ImageView menuButton;

        public AddressBookListAdapterClass(Activity activity, List<AddressList.AddressBook>addresses, AddressBookScreen fragment) {
            this.activity = activity;
            this.addresses = addresses;
            this.addressBookScreen=fragment;
            utility = Utility.getInstance(activity);
            loginSession = LoginSession.getInstance(activity);
            serverRequestHandler = ServerRequest.getInstance(activity);

        }

        @Override
        public int getCount() {
            return addresses.size();
        }

        @Override
        public Object getItem(int i) {
            return addresses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            final LayoutInflater inflater = activity.getLayoutInflater();

            if (view == null)

                view = inflater.inflate(R.layout.addressbook_custom, null);

            //registering the id values
            addressTitleTextView = (TextView)view.findViewById(R.id.addressTitleTextView);
            addressTextView      = (TextView)view.findViewById(R.id.addressTextView);
            check_Primary        = (TextView)view.findViewById(R.id.check_Primary);
            addressStatusSwitch  = (Switch) view.findViewById(R.id.addressStatusSwitch);
            menuButton           = (ImageView) view.findViewById(R.id.menuButton);

            addressTitleTextView.setText(addresses.get(position).title);
            addressTextView.setText(addresses.get(position).flat_no+", "+addresses.get(position).address);
      //      check_Primary.setText(addresses.get(position).getIs_primary());

            if(addresses.get(position).status.equalsIgnoreCase("1")){

                addressStatusSwitch.setChecked(true);

            }else{

                addressStatusSwitch.setChecked(false);
            }

            addressStatusSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(utility.isConnectingToInternet()){

                        Map<String, String> params = new HashMap<>();
                        // POST parameters:
                        params.put("action", "MyAccount");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("page","AddressBook");
                        params.put("addressAction","AddressBookStatus");
                        params.put("addressBookId",addresses.get(position).id);

                        utility.showProgressDialog();

                        serverRequestHandler.createRequest(AddressBookListAdapterClass.this, params, RequestID.REQ_ADDRESS_STATUS);

                    }else{

                        utility.noInternetAlertDialog();

                    }

                }
            });


            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                    alertDialog.setTitle(getResources().getString(R.string.addressManagement));

                    alertDialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(activity,AddAddressScreen.class);
                            intent.putExtra("addressId",addresses.get(position).id);
                            intent.putExtra("title",addresses.get(position).title);
                            intent.putExtra("phone",addresses.get(position).address_phone);
                            intent.putExtra("address",addresses.get(position).address);
                            intent.putExtra("doorNumber",addresses.get(position).flat_no);
                            intent.putExtra("latitude",addresses.get(position).latitude);
                            intent.putExtra("longitude",addresses.get(position).longitude);
                            intent.putExtra("screen","Edit Address");
                            activity.startActivity(intent);
                        }
                    });

                    /*alertDialog.setNeutralButton("SET PRIMARY" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.e("addressPrimary","addressPrimary"+addresses.get(position).id);

                            if(!utility.isConnectingToInternet()){

                                utility.noInternetAlertDialog();

                            } else{

                                Map<String, String> params = new HashMap<>();
                                // the POST parameters:
                                params.put("action", "MyAccount");
                                params.put("customer_id",loginSession.getUserId());
                                params.put("page","AddressBook");
                                params.put("addressAction","AddressBookPrimary");
                                params.put("addressBookId",addresses.get(position).id);

                                utility.showProgressDialog();
                                serverRequestHandler.createRequest(AddressBookListAdapterClass.this,params, RequestID.REQ_ADDRESS_STATUS);
                            }

                        }
                    });*/

                    alertDialog.setNegativeButton(getResources().getString(R.string.Delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                            alertDialog.setTitle(getResources().getString(R.string.sureToDeleteAddress));
                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if(!utility.isConnectingToInternet()){

                                        utility.noInternetAlertDialog();

                                    } else{

                                        Map<String, String> params = new HashMap<>();
                                        // the POST parameters:
                                        params.put("action", "MyAccount");
                                        params.put("customer_id",loginSession.getUserId());
                                        params.put("page","AddressBook");
                                        params.put("addressAction","AddressBookDelete");
                                        params.put("addressBookId",addresses.get(position).id);

                                        utility.showProgressDialog();
                                        serverRequestHandler.createRequest(AddressBookListAdapterClass.this,params, RequestID.REQ_DELETE_ADDRESS);
                                    }

                                    dialog.cancel();
                                }
                            });

                            alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });

                            alertDialog.show();
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();
                }
            });

            return  view;
        }

        @Override
        public void onSuccess(Object result, RequestID requestId) {

            utility.hideProgressDialog();


            switch (requestId){

                case REQ_ADDRESS_STATUS:

                    utility.toast(result.toString());
                    addressBookScreen.getAddressList();
                    break;

                case REQ_DELETE_ADDRESS:

                    utility.toast(result.toString());
                    addressBookScreen.getAddressList();


                    break;
            }

        }

        @Override
        public void onFailure(String error, RequestID requestId) {

            utility.toast(error);
            utility.hideProgressDialog();
        }

    }

}
