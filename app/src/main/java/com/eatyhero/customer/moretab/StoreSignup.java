package com.eatyhero.customer.moretab;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.HashMap;
import java.util.Map;

public class StoreSignup extends BaseActivity implements ServerListener {

    LoginSession loginSession;
    ServerRequest serverRequest;
    //XML
    Button signupButton;
    EditText nameeditText,emaileditText,numbereditText,passwordeditText,confirmeditText,restaurantET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_signup);


        nameeditText             =(EditText)findViewById(R.id.nameET);
        emaileditText            =(EditText)findViewById(R.id.emailET);
        numbereditText           =(EditText)findViewById(R.id.contactNumberET);
        passwordeditText         =(EditText)findViewById(R.id.passwordET);
        confirmeditText          =(EditText)findViewById(R.id.confirmPasswordET);
        restaurantET                =(EditText)findViewById(R.id.restaurantET);
        signupButton             =(Button)findViewById(R.id.driverRegister);
//Initialize Object
        loginSession              =new LoginSession(this);
        serverRequest             =ServerRequest.getInstance(this);


        //Register Button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name     =nameeditText.getText().toString();
                String email    =emaileditText.getText().toString();
                String number   =numbereditText.getText().toString();
                String password =passwordeditText.getText().toString();
                String confirmPassword =confirmeditText.getText().toString();
                String restaurantName =restaurantET.getText().toString();
                //genderRadioGroup

                if (name.isEmpty())
                {
                    toast("Please Enter Driver name");
                }else  if (email.isEmpty())
                {
                    toast("Please Enter Email");
                }else  if (!validEmail(email))
                {
                    toast("Please enter a valid email address");
                }else  if (number.isEmpty())
                {
                    toast("Please Enter Contact Number");
                }else  if (password.isEmpty())
                {
                    toast("Please Enter Password");

                }else if (confirmPassword.isEmpty())
                {
                    toast("Please Enter Confirm Password");

                }else  if (!password.equals(confirmPassword))
                {
                    toast("Password and Confirm Password Mismatch");
                }else  if (restaurantName.isEmpty())
                {
                    toast("Please Enter Your Restaurant Name");
                }else {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:

                    params.put("contact_name",name);
                    params.put("username",email);
                    params.put("contact_phone",number);
                    params.put("password",password);
                    params.put("restaurant_name",restaurantName);
                    showProgressDialog();
                    Log.e("Parameter pass",""+ params);
                    showProgressDialog();
                    serverRequest.createRequest(StoreSignup.this, params, RequestID.REQ_STORESIGNUP);
                }

            }
        });
    }


    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID)
        {

            case REQ_STORESIGNUP:

                toast(result.toString());
                finish();
                break;
        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        toast(error);
        hideProgressDialog();
    }
}
