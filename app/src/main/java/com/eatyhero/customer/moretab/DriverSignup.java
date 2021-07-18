package com.eatyhero.customer.moretab;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.HashMap;
import java.util.Map;

public class DriverSignup extends BaseActivity implements ServerListener {
    LoginSession loginSession;
    ServerRequest serverRequest;

    String payoutString="M";
    //XML
    Button signupButton;
    EditText nameeditText,emaileditText,numbereditText,passwordeditText,confirmeditText,vechileET,payoutAmountET;
    RadioGroup payoutRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_signup);


        nameeditText             =(EditText)findViewById(R.id.nameET);
        emaileditText            =(EditText)findViewById(R.id.emailET);
        numbereditText           =(EditText)findViewById(R.id.contactNumberET);
        passwordeditText         =(EditText)findViewById(R.id.passwordET);
        confirmeditText          =(EditText)findViewById(R.id.confirmPasswordET);
        vechileET                =(EditText)findViewById(R.id.vechileET);
        payoutAmountET           =(EditText)findViewById(R.id.payoutAmountET);
        payoutRadioGroup         =(RadioGroup) findViewById(R.id.payoutRadioGroup);
        signupButton             =(Button)findViewById(R.id.driverRegister);
//Initialize Object
        loginSession              =new LoginSession(this);
        serverRequest             =ServerRequest.getInstance(this);

        payoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i)
                {
                    case R.id.perOrderRadioButton:
                        payoutString="perorder";
                        break;
                    case R.id.distanceRadioButton:
                        payoutString="distance";
                        break;
                }
            }
        });


        //Register Button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name     =nameeditText.getText().toString();
                String email    =emaileditText.getText().toString();
                String number   =numbereditText.getText().toString();
                String password =passwordeditText.getText().toString();
                String confirmPassword =confirmeditText.getText().toString();
                String vechileName =vechileET.getText().toString();
                String payoutAmount =payoutAmountET.getText().toString();
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
                }else  if (payoutString.isEmpty())
                {
                    toast("Please Select Your Gender");
                }else  if (vechileName.isEmpty())
                {
                    toast("Please Enter Your Vechile");
                }else  if (payoutAmount.isEmpty())
                {
                    toast("Please Enter Your payoutAmount");
                }else {
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:

                    params.put("driver_name",name);
                    params.put("driver_email",email);
                    params.put("username",number);
                    params.put("password",password);
                    params.put("payout",payoutString);
                    params.put("payout_amount",payoutAmount);
                    params.put("vechile_name",vechileName);
                    showProgressDialog();
                    Log.e("Parameter pass",""+ params);
                    showProgressDialog();

                    serverRequest.createRequest(DriverSignup.this, params, RequestID.REQ_DRIVERSIGNUP);
                }

            }
        });


    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID)
        {

            case REQ_DRIVERSIGNUP:

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
