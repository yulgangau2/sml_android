package com.eatyhero.customer.moretab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.model.AddressList;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 22-03-2017.
 */

public class NewAddressBookScreen extends Fragment implements ServerListener {

    //Create objects
    LoginSession loginSession;
    Utility utility;
    ServerRequest serverRequest;
    AddressList addressList;
    AddressBookListAdapter addressBookListAdapter;
    Dialog progressdialog;

    //Create xml objects
    ListView addressListView;
    FloatingActionButton addButton;
    ImageView backIconImageView;

    //Create Bundle
    Bundle bundle = new Bundle();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.addressbook_screen, container, false);

        //Initialize Objects

        loginSession   = LoginSession.getInstance(getActivity());
        serverRequest  = ServerRequest.getInstance(getActivity());
        utility        = Utility.getInstance(getActivity());

        //Initialize xml files
        addressListView   = (ListView) rootView.findViewById(R.id.addressListView);

        backIconImageView = (ImageView) rootView.findViewById(R.id.backIconImageView);

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InfoScreen infoScreen = new InfoScreen();
                FragmentManager fm3 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                ft3.replace(R.id.frameLayout, infoScreen);
                ft3.commit();
            }
        });

        getAddressList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new  NewAddAddressScreen();
                bundle.putString("addressId", "");
                bundle.putString("screen", "Add Address");
                bundle.putString("title", "");
                bundle.putString("phone", "");
                bundle.putString("address", "");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();

            }
        });

        return rootView;
    }

    public void getAddressList() {

        if (utility.isConnectingToInternet()) {

            Map<String, String> params = new HashMap<>();
            // POST parameters:
            params.put("action", "MyAccount");
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "AddressBookList");
            params.put("view", "");

            showProgressDialog();

            serverRequest.createRequest(this, params, RequestID.REQ_ADDRESSLIST);

        } else {

            utility.noInternetAlertDialog();
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {


        switch (requestID) {

            case REQ_ADDRESSLIST:

                hideProgressDialog();
                addressList = (AddressList) result;
                List<AddressList.AddressBook> addressList1 = addressList.result.addressBook;

                addressBookListAdapter = new AddressBookListAdapter(getActivity(), addressList1, this);
                addressListView.setAdapter(addressBookListAdapter);
                addressBookListAdapter.notifyDataSetChanged();


                break;

            case REQ_ADDRESS_STATUS:

                hideProgressDialog();
                utility.toast(result.toString());
                getAddressList();
                break;

            case REQ_DELETE_ADDRESS:
                hideProgressDialog();
                utility.toast(result.toString());
                getAddressList();

                break;

        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        utility.toast(error);
        addressListView.setAdapter(null);
    }

    class AddressBookListAdapter extends BaseAdapter {

        //Create objects
        Activity activity;
        List<AddressList.AddressBook> addresses;
        Utility utility;
        LoginSession loginSession;
        NewAddressBookScreen addressBookScreen;
        ServerRequest serverRequestHandler;

        //XML objects
        TextView addressTitleTextView, addressTextView;
        Switch addressStatusSwitch;
        ImageView menuButton;


        public AddressBookListAdapter(Activity activity, List<AddressList.AddressBook> addresses, NewAddressBookScreen fragment) {
            this.activity          = activity;
            this.addresses         = addresses;
            this.addressBookScreen = fragment;
            utility                = Utility.getInstance(activity);
            loginSession           = LoginSession.getInstance(activity);
            serverRequestHandler   = ServerRequest.getInstance(activity);

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
            addressTitleTextView  = (TextView) view.findViewById(R.id.addressTitleTextView);
            addressTextView       = (TextView) view.findViewById(R.id.addressTextView);
            addressStatusSwitch   = (Switch) view.findViewById(R.id.addressStatusSwitch);
            menuButton            = (ImageView) view.findViewById(R.id.menuButton);

            addressTitleTextView.setText(addresses.get(position).title);
            addressTextView.setText(addresses.get(position).address);

            if (addresses.get(position).status.equalsIgnoreCase("1")) {

                addressStatusSwitch.setChecked(true);

            } else {

                addressStatusSwitch.setChecked(false);
            }


            addressStatusSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (utility.isConnectingToInternet()) {

                        Map<String, String> params = new HashMap<>();
                        // POST parameters:
                        params.put("action", "MyAccount");
                        params.put("customer_id", loginSession.getUserId());
                        params.put("page", "AddressBook");
                        params.put("addressAction", "AddressBookStatus");
                        params.put("addressBookId", addresses.get(position).id);

                        showProgressDialog();

                        serverRequestHandler.createRequest(NewAddressBookScreen.this, params, RequestID.REQ_ADDRESS_STATUS);

                    } else {

                        utility.noInternetAlertDialog();

                    }

                }
            });


            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                    alertDialog.setTitle(getResources().getString(R.string.addressManagement));

                    alertDialog.setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Fragment fragment = new NewAddAddressScreen();
                            bundle.putString("addressId", addresses.get(position).id);
                            bundle.putString("title", addresses.get(position).title);
                            bundle.putString("phone", addresses.get(position).address_phone);
                            bundle.putString("address", addresses.get(position).address);
                            bundle.putString("screen", "Edit Address");
                            fragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayout, fragment);
                            fragmentTransaction.commit();
                        }
                    });

                    alertDialog.setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                            alertDialog.setTitle(getResources().getString(R.string.sureToDeleteAddress));

                            alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (!utility.isConnectingToInternet()) {

                                        utility.noInternetAlertDialog();

                                    } else {

                                        Map<String, String> params = new HashMap<>();
                                        // the POST parameters:
                                        params.put("action", "MyAccount");
                                        params.put("customer_id", loginSession.getUserId());
                                        params.put("page", "AddressBook");
                                        params.put("addressAction", "AddressBookDelete");
                                        params.put("addressBookId", addresses.get(position).id);

                                        showProgressDialog();

                                        serverRequestHandler.createRequest(NewAddressBookScreen.this, params, RequestID.REQ_DELETE_ADDRESS);
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

            return view;
        }


    }

    //ProgressDialog Open Close Method
    public void showProgressDialog() {
        if (progressdialog == null) {
            progressdialog = new Dialog(getActivity());
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressdialog.setContentView(R.layout.custom_progressbar);
            progressdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        progressdialog.setCancelable(false);
        progressdialog.show();
    }

    public void hideProgressDialog() {
        try {
            if (progressdialog.isShowing()) {
                progressdialog.dismiss();
            }

        } catch (Exception e) {

        }

    }


}
