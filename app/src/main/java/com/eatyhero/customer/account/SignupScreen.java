package com.eatyhero.customer.account;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 19-08-2016.
 */
public class SignupScreen extends BaseActivity implements ServerListener {

    //Create objects
    ServerRequest serverRequest;

    //Create xml objects
    Toolbar toolbar;
    TextView actionBarTitle;
    ImageView backIconImageView;
    EditText firstNameEditText,lastNameEditText,emailEditText,phoneEditText,passwordEditText,confirmPasswordEditText,referCodeEditText;
    Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        //Initialize xml objects
        toolbar                   = (Toolbar)findViewById(R.id.toolbar);
        actionBarTitle            = (TextView)findViewById(R.id.actionBarTitleTextView);
        backIconImageView         = (ImageView)findViewById(R.id.backIconImageView);
        firstNameEditText       = (EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText        = (EditText)findViewById(R.id.lastNameEditText);
        emailEditText           = (EditText)findViewById(R.id.emailEditText);
        phoneEditText           = (EditText)findViewById(R.id.phoneEditText);
        passwordEditText        = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
        referCodeEditText = (EditText)findViewById(R.id.referCodeEditText);
        signinButton            = (Button)findViewById(R.id.signinButton);

        actionBarTitle.setText("SignUp");

        //Initialize objects
        serverRequest       = ServerRequest.getInstance(this);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstNameEditText.getText().toString().trim();
                String lastName  = lastNameEditText.getText().toString().trim();
                String email     = emailEditText.getText().toString().trim();
                String phone     = phoneEditText.getText().toString().trim();
                String password  = passwordEditText.getText().toString().trim();
                String cpassword = confirmPasswordEditText.getText().toString().trim();
                String referCode = referCodeEditText.getText().toString().trim();


                if(firstName.isEmpty()){

                    firstNameEditText.setError(getResources().getString(R.string.pleaseEnterYourFirstName));

                }else if(lastName.isEmpty()){

                    lastNameEditText.setError(getResources().getString(R.string.pleaseEnterYourLastName));

                }else if(email.isEmpty()){

                    emailEditText.setError(getResources().getString(R.string.pleaseEnterYourEmail));

                } else if(!validEmail(email)){

                    emailEditText.setError(getResources().getString(R.string.PleaseEnterValidEmail));

                }else if(phone.isEmpty()){

                    phoneEditText.setError(getResources().getString(R.string.pleaseEnterPhoneNumber));

                }else if(password.isEmpty()){

                    passwordEditText.setError(getResources().getString(R.string.pleaseEnterYourPassword));

                }else if(cpassword.isEmpty()){

                    confirmPasswordEditText.setError(getResources().getString(R.string.pleaseEnterConfirmPasssword));

                }else if(!password.equals(cpassword)){

                    confirmPasswordEditText.setError(getResources().getString(R.string.passwordMismatch));

                }else if(!checkInternet()){

                    noInternetAlertDialog();

                }else{

                    //Server request listener
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:

                    params.put("first_name", firstName);
                    params.put("last_name", lastName);
                    params.put("password", password);
                    params.put("phone_number", phone);
                    params.put("username", email);
                    params.put("referral_code", referCode);

                    showProgressDialog();

                    serverRequest.createRequest(SignupScreen.this,params, RequestID.REQ_TO_SIGNUP);

                }

            }
        });


        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        toast(result.toString());
        finish();
    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error.toString());
    }

}
