package com.eatyhero.customer.moretab;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 19-08-2016.
 */
public class ChangePasswordScreen extends BaseActivity implements ServerListener {

    LoginSession loginSession;
    ServerRequest serverRequest;

    EditText passwordEditText,confirmPasswordEditText,oldpasswordEditText;
    Button changePassButton;
    ImageView backIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_screen);

        Toolbar toolbar         = (Toolbar)findViewById(R.id.toolbar);
        passwordEditText        = (EditText)findViewById(R.id.passwordEditText);
        oldpasswordEditText        = (EditText)findViewById(R.id.oldpasswordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
        changePassButton        = (Button) findViewById(R.id.changePassButton);
        backIconImageView       =(ImageView)toolbar.findViewById(R.id.backIconImageView);

        loginSession = LoginSession.getInstance(this);
        serverRequest = ServerRequest.getInstance(this);

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpass        = passwordEditText.getText().toString().trim();
                String confirmpass = confirmPasswordEditText.getText().toString().trim();
                String oldpass = oldpasswordEditText.getText().toString().trim();

                if(newpass.isEmpty()){

                    passwordEditText.setError(getResources().getString(R.string.enterNewPassword));

                }else if(confirmpass.isEmpty()){

                    confirmPasswordEditText.setError(getResources().getString(R.string.enterValidPassword));

                }else if(oldpass.isEmpty()){

                    oldpasswordEditText.setError(getResources().getString(R.string.enterOldPassword));

                }else if(!newpass.equals(confirmpass)){

                    toast(getResources().getString(R.string.passwordMismatch));

                }else if(!checkInternet()){

                    noInternetAlertDialog();

                }else{

                    //Server request listener
                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:

                    params.put("action", "MyAccount");
                    params.put("customer_id",loginSession.getUserId());
                    params.put("page","ChangePassword");
                    params.put("password", newpass);
                    params.put("oldPassword", oldpass);

                    showProgressDialog();

                    serverRequest.createRequest(ChangePasswordScreen.this,params, RequestID.REQ_TO_CHANGE_PASSWORD);

                }
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
