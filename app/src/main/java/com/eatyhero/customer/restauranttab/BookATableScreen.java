package com.eatyhero.customer.restauranttab;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 19-06-2017.
 */

public class BookATableScreen extends BaseActivity implements ServerListener {

    //Create Objects
    Utility utility;
    LoginSession loginSession;
    ServerRequest serverRequest;
    SharedPreferences preferences;
    ServerRequest serverRequestHandler;

    //create xml objects
    Toolbar toolbar;
    TextView actionBarTitleTextView;
    ImageView backIconImageView;
    Spinner noTableSpinner, timeSpin;
    EditText dateET, bookNameET, bookEmailET, bookPhoneET, bookInsET;
    Button submitButton;

    //String creation
    String nameET,
            emailET,
            getDate,
            phoneET,
            insET;

    String restaurantID;
    String selecttime;
    String selectCount;

    Calendar c;


    //calendar variables
    int date, month, year;

    List<String> resOpeningTimes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_table_layout);

        //tool bar widgets
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarTitleTextView = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        backIconImageView = (ImageView) toolbar.findViewById(R.id.backIconImageView);

        //spinner initialization
        noTableSpinner = (Spinner) findViewById(R.id.noTableSpinner);
        timeSpin = (Spinner) findViewById(R.id.timeSpin);

        //EditText initialization
        dateET = (EditText) findViewById(R.id.dateET);
        bookNameET = (EditText) findViewById(R.id.bookNameET);
        bookEmailET = (EditText) findViewById(R.id.bookEmailET);
        bookPhoneET = (EditText) findViewById(R.id.bookPhoneET);
        bookInsET = (EditText) findViewById(R.id.bookInsET);

        //Button initialization
        submitButton = (Button) findViewById(R.id.submitButton);

        //Initialize Objects
        utility = Utility.getInstance(BookATableScreen.this);
        loginSession = LoginSession.getInstance(BookATableScreen.this);
        serverRequest = ServerRequest.getInstance(BookATableScreen.this);
        serverRequestHandler = ServerRequest.getInstance(BookATableScreen.this);

        //tool bar textview and backarrow
        actionBarTitleTextView.setText(getResources().getString(R.string.bookATable));
        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });



        //Get Preference value
        preferences = BookATableScreen.this.getSharedPreferences("restaurant", BookATableScreen.this.MODE_PRIVATE);
        restaurantID = preferences.getString("resid", "");


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setLenient(false);
        Date curDate = new Date();
        String curTime = formatter.format(curDate);

        getDate = curTime;

        dateET.setText(getDate);

        if (!(getDate.equals(""))) {
            if (utility.isConnectingToInternet()) {

                Map<String, String> param = new HashMap<>();
                // POST parameters:

                param.put("date", getDate);
                param.put("resid", restaurantID);
                param.put("action", "restaurantTiming");
                showProgressDialog();

                serverRequestHandler.createRequest((ServerListener) BookATableScreen.this, param, RequestID.REQ_TIME);

            } else {

                noInternetAlertDialog();
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameET = bookNameET.getText().toString().trim();
                emailET = bookEmailET.getText().toString().trim();
                phoneET = bookPhoneET.getText().toString().trim();
                insET = bookInsET.getText().toString().trim();
                getDate = dateET.getText().toString().trim();

                Log.e("SelectCountclick", "SelectCountclick" + selectCount +nameET+emailET+insET+selecttime);

                Log.e("selectDate","selectDate"+getDate);

                if(selectCount.equalsIgnoreCase("Select Guest Count")) {

                    toast(getResources().getString(R.string.selectCount));

                }else if (getDate.isEmpty()){

                    Log.e("selectDate2","selectDate2"+getDate);
                    dateET.setError(getResources().getString(R.string.selectDate));

                }else if (selecttime.equalsIgnoreCase("Select Time")) {

                    toast(getResources().getString(R.string.selectTiming));

                }else if (nameET.isEmpty()) {

                    bookNameET.setError(getResources().getString(R.string.enterCustomerNmae));

                } else if (emailET.isEmpty()) {

                    bookEmailET.setError(getResources().getString(R.string.enterCustomerEmail));

                } else if (phoneET.isEmpty()) {

                    bookPhoneET.setError(getResources().getString(R.string.enterCustomerPhone));

                } else {


                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:

                    params.put("page", "BookaTable");
                    params.put("customer_id", loginSession.getUserId());
                    params.put("store_id",restaurantID);
                    params.put("guest_count", String.valueOf(selectCount));
                    params.put("booking_date", getDate);
                    params.put("booking_time", selecttime);
                    params.put("customer_name", nameET);
                    params.put("booking_email", emailET);
                    params.put("booking_phone", phoneET);
                    params.put("booking_instruction", insET);

                    showProgressDialog();

                    serverRequest.createRequest(BookATableScreen.this, params, RequestID.REQ_TO_BOOKTABLE);

                }


            }
        });


        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookATableScreen.this, new datePickerDialog(), year, month, date);

                DatePicker datePicker = datePickerDialog.getDatePicker();

                Calendar calendar = Calendar.getInstance();

                datePicker.setMinDate(calendar.getTimeInMillis() - 1000);

                calendar.add(Calendar.DATE, 15);
                //       datePicker.setMaxDate(calendar.getTimeInMillis());

                datePickerDialog.show();

            }
        });


        //Date Edit Text Text watcher
        dateET.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                getDate = dateET.getText().toString();
                if (!(getDate.equals(""))) {
                    if (utility.isConnectingToInternet()) {

                        Map<String, String> param = new HashMap<>();
                        // POST parameters:
                        param.put("date", getDate);
                        param.put("resid", restaurantID);
                        param.put("action", "restaurantTiming");

                        showProgressDialog();

                        serverRequestHandler.createRequest((ServerListener) BookATableScreen.this, param, RequestID.REQ_TIME);

                    } else {

                       noInternetAlertDialog();
                    }


                } else {

                }
            }
        });



     /*   //timeSpinner
        ArrayAdapter<String> RestaurantTimingAdapter = new ArrayAdapter<String>(BookATableScreen.this, R.layout.custom_spinner_textview, getResources().getStringArray(R.array.Timehour));
        RestaurantTimingAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        timeSpin.setAdapter(RestaurantTimingAdapter);
*/
        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecttime = timeSpin.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Guest CountSpinner
        ArrayList<String> countArray = new ArrayList<>();
        countArray.add(getResources().getString(R.string.selectGuestCount));
        countArray.add("1");
        countArray.add("2");
        countArray.add("3");
        countArray.add("4");
        countArray.add("5");
        countArray.add("6");
        countArray.add("7");
        countArray.add("8");
        countArray.add("9");
        countArray.add("10");
        countArray.add("11");
        countArray.add("12");
        countArray.add("13");
        countArray.add("14");
        countArray.add("15");
        countArray.add("16");
        countArray.add("17");
        countArray.add("18");
        countArray.add("19");
        countArray.add("20");


        Log.e("Get REQ_DATE Response", "" + resOpeningTimes);

        ArrayAdapter<String> RestaurantTimingAdapter1 = new ArrayAdapter<String>(BookATableScreen.this, R.layout.custom_spinner_textview, countArray);
        RestaurantTimingAdapter1.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        noTableSpinner.setAdapter(RestaurantTimingAdapter1);


        noTableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  selectCount =  noTableSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //datePickerDialog
     class datePickerDialog implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateET.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

        }

    }


    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID) {

            case REQ_TIME:

                Log.e("Get REQ_DATE Response", "" + result.toString());

                String restaurantOpenningTime = result.toString();

                String array[] = restaurantOpenningTime.split(",");

                resOpeningTimes = Arrays.asList(array);

                Log.e("Get REQ_DATE Response", "" + resOpeningTimes);

                ArrayAdapter<String> RestaurantTimingAdapter = new ArrayAdapter<String>(BookATableScreen.this, R.layout.custom_spinner_textview, resOpeningTimes);
                RestaurantTimingAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                timeSpin.setAdapter(RestaurantTimingAdapter);

                submitButton.setVisibility(View.VISIBLE);
                break;

            case REQ_TO_BOOKTABLE:

                toast(result.toString());
                finish();
        }
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error.toString());
        switch (requestID) {

            case REQ_TIME:

             /*   String restaurantOpenningTime=error;
                resOpeningTimes= Arrays.asList(restaurantOpenningTime);*/

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("Closed");
                ArrayAdapter<String> RestaurantTimingAdapter = new ArrayAdapter<String>(BookATableScreen.this, R.layout.custom_spinner_textview, arrayList);
                RestaurantTimingAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                timeSpin.setAdapter(RestaurantTimingAdapter);
                submitButton.setVisibility(View.GONE);
                //toast(error);
                break;


        }
    }
}