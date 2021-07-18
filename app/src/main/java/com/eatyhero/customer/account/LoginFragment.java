package com.eatyhero.customer.account;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseFragment;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.fcm.Config;
import com.eatyhero.customer.model.LoginDetails;
import com.eatyhero.customer.moretab.InfoScreen;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by admin on 22-03-2017.
 */

public class LoginFragment extends BaseFragment implements ServerListener,GoogleApiClient.OnConnectionFailedListener {

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequest;
    Dialog forgotPasswordDialog;

    //Create xml objects
    EditText emailEditText,passwordEditText,forgotemailEditText;
    CheckBox rememberMeCheckbox;
    Button signinButton,fbButton,gplusButton;
    TextView forgotpasswordButton,showHideButton,signupButton;

    //Facebook objects
    public static CallbackManager callbackmanager;
    LoginButton facebookLginButton;
    String fbFirstName="";
    String fbLastName="";
    String fbEmail ="";
    String fbid ="";

    //Gmail Objects
    private GoogleApiClient mGoogleApiClient;
    SignInButton googleSigninbutton;
    private static final int RC_SIGN_IN = 007;

    //Fcm objects
    FirebaseMessaging fcm;
    String token="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_screen,container,false);

        //Initialize xml objects
        emailEditText            = (EditText)rootView.findViewById(R.id.emailEditText);
        passwordEditText         = (EditText)rootView.findViewById(R.id.passwordEditText);
        rememberMeCheckbox       = (CheckBox)rootView.findViewById(R.id.rememberMeCheckbox);
        signinButton             = (Button)  rootView.findViewById(R.id.signinButton);
        signupButton             = (TextView)rootView.findViewById(R.id.signupButton);
        fbButton                 = (Button)rootView.findViewById(R.id.fbButton);
        gplusButton              = (Button)rootView.findViewById(R.id.gplusButton);
        forgotpasswordButton     = (TextView)rootView. findViewById(R.id.forgotpasswordButton);
        showHideButton           = (TextView)rootView.findViewById(R.id.showHideButton);
        facebookLginButton       = (LoginButton)rootView.findViewById(R.id.facebookLginButton);
        googleSigninbutton       = (SignInButton)rootView.findViewById(R.id.googleSigninbutton);


        //Initialize objects
        callbackmanager = CallbackManager.Factory.create();
        loginSession      = LoginSession.getInstance(getActivity());
        serverRequest     = ServerRequest.getInstance(getActivity());
        fcm               =  FirebaseMessaging.getInstance();

        Utility utility = Utility.getInstance(getActivity());
        utility.hideStatusBar();
        utility.CURRENT_SCREEN = "INFO_SCREEN";

        token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        token = pref.getString("regId",null);
//        Log.e("FcmTokenloginscreen", token);
        loginSession.saveFcmId(token);
//        Log.e("FcmTokeninSession", loginSession.getFcmId());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSigninbutton.setScopes(gso.getScopeArray());

        if(loginSession.isRemember()){
            emailEditText.setText(loginSession.getRememberEmail());
            passwordEditText.setText(loginSession.getRememberPassword());
        }

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email     = emailEditText.getText().toString();
                String password  = passwordEditText.getText().toString();

                if(email.isEmpty()){

                    emailEditText.setError(getResources().getString(R.string.pleaseEnterYourEmail));

                }else if(!validEmail(email)){

                    emailEditText.setError(getResources().getString(R.string.PleaseEnterValidEmail));

                }else if(password.isEmpty()){

                    passwordEditText.setError(getResources().getString(R.string.pleaseEnterYourPassword));

                }else if(!isConnectingToInternet()){

                    noInternetAlertDialog();

                }else{

                    if(rememberMeCheckbox.isChecked()){

                        loginSession.saveRemember(email,password);

                    }else{

                        loginSession.saveRemember("","");
                    }

                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("username", email);
                    params.put("password", password);
                    params.put("device_id", loginSession.getFcmId());
                    params.put("device_type", "ANDROID");

                    showProgressDialog();
                    serverRequest.createRequest(LoginFragment.this, params, RequestID.REQ_TO_LOGIN);

                }

            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),SignupScreen.class);
                startActivity(intent);
            }
        });


        googleSigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        forgotpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPasswordDialog = new Dialog(getActivity());
                forgotPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                forgotPasswordDialog.setContentView(R.layout.forgot_password_screen);
                forgotPasswordDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

                //email edit text
                forgotemailEditText      = (EditText) forgotPasswordDialog.findViewById(R.id.emailidEditText);
                Button submit            = (Button) forgotPasswordDialog.findViewById(R.id.submitButton);
                ImageView closeImageView = (ImageView) forgotPasswordDialog.findViewById(R.id.closeImageView);

                //submit button onclick
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String forgotEmail = forgotemailEditText.getText().toString();

                        //Validation Part
                        if (forgotEmail.isEmpty()) {

                            forgotemailEditText.setError(getResources().getString(R.string.pleaseEnterYourEmail));

                        } else if (!validEmail(forgotEmail)) {

                            forgotemailEditText.setError(getResources().getString(R.string.PleaseEnterValidEmail));

                        } else if(!isConnectingToInternet()){

                            noInternetAlertDialog();

                        } else{

                            //Server request listener

                            Map<String, String> params = new HashMap<>();
                            // the POST parameters:

                            params.put("username", forgotEmail);

                            showProgressDialog();

                            serverRequest.createRequest(LoginFragment.this,params, RequestID.REQ_FORGOT_PASSWORD);
                        }

                    }
                });
                forgotPasswordDialog.show();


                closeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        forgotPasswordDialog.dismiss();

                    }
                });
            }
        });

        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(showHideButton.getHint().equals("Show")){

                    showHideButton.setText(getResources().getString(R.string.hide));
                    showHideButton.setHint("Hide");
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }else if(showHideButton.getHint().equals("Hide")){


                    showHideButton.setText(getResources().getString(R.string.show));
                    showHideButton.setHint("Show");
                    passwordEditText.setInputType(129);

                }
            }
        });

        // Facebook permissions
        List<String> permissionNeeds = Arrays.asList(/*"user_photos",*/ "email", /*"user_birthday",*/ "public_profile");
        facebookLginButton.setReadPermissions(permissionNeeds);
        facebookLginButton.setFragment(this);

        //facebook login button
        facebookLginButton.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.e("LoginActivity", response.toString());

                                try {

                                    fbFirstName = object.getString("first_name");
                                    fbLastName = object.getString("last_name");
                                    fbEmail = object.getString("email");
                                    fbid = object.getString("id");

                                    LoginManager.getInstance().logOut();

                                    // the POST parameters:
                                    Map<String, String> params = new HashMap<>();

                                    params.put("action", "SocialLogin");
                                    params.put("first_name", fbFirstName);
                                    params.put("last_name",fbLastName);
                                    params.put("username", fbEmail);
                                    params.put("device_id", loginSession.getFcmId());
                                    params.put("device_name","ANDROID");

                                    Log.e("fb loging",""+params);

                                    showProgressDialog();

                                    serverRequest.createRequest(LoginFragment.this, params, RequestID.REQ_SOCIAL_LOGIN);

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    LoginManager.getInstance().logOut();

                                    toast(getResources().getString(R.string.cantFetchEamil));

                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
//                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                facebookLginButton.performClick();
            }
        });

        gplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        return rootView;
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {
        hideProgressDialog();

        switch (requestID){

            case REQ_TO_LOGIN:

                LoginDetails loginDetails  = (LoginDetails)result;
                loginSession.saveLogInStore(loginDetails.result.customerId,loginDetails.result.first_user,loginDetails.result.firstName,loginDetails.result.lastName,loginDetails.result.email,loginDetails.result.customerPhone,"");
                toast(loginDetails.result.message);
                loginSession.saveDriverImage("");

                Fragment fragment = new InfoScreen();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
                 break;

            case REQ_FORGOT_PASSWORD:

                forgotPasswordDialog.dismiss();
                toast(String.valueOf(result));

                break;

            case REQ_SOCIAL_LOGIN:

                LoginDetails loginDetails1  = (LoginDetails)result;
                loginSession.saveLogInStore(loginDetails1.result.customerId,loginDetails1.result.first_user,loginDetails1.result.firstName,loginDetails1.result.lastName,loginDetails1.result.email,loginDetails1.result.customerPhone,"");
                toast(loginDetails1.result.message);
                Log.e("REQ_SOCIAL_LOGIN","REQ_SOCIAL_LOGIN");

                    Fragment fragment1 = new InfoScreen();
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.frameLayout, fragment1);
                    fragmentTransaction1.commit();

                break;
         }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        hideProgressDialog();
        toast(error);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{

            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("", "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e("", "Name: " + personName + ", email: " + email
                    + ", Image: " + "");

            Map<String, String> params = new HashMap<>();

            params.put("action", "SocialLogin");
            params.put("first_name", personName);
            params.put("last_name", "");
            params.put("username", email);
            params.put("device_id", loginSession.getFcmId());
            params.put("device_name", "ANDROID");

            Log.e("fb loging",""+params);

            showProgressDialog();

            serverRequest.createRequest(LoginFragment.this, params, RequestID.REQ_SOCIAL_LOGIN);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(loginSession.isLoggedIn()){

            Fragment fragment = new InfoScreen();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();

        }else {

            String regid=loginSession.getFcmId();
            Log.e("GCM ID",regid);
            if(regid.isEmpty())
            {
                if(!isConnectingToInternet()){
                    noInternetAlertDialog();
                }else {
                    try{ String fcmId =  FirebaseInstanceId.getInstance().getToken();
                        loginSession.saveFcmId(fcmId);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}
