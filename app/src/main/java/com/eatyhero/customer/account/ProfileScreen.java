package com.eatyhero.customer.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.CircleTransform;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.model.CustomerDetail;
import com.eatyhero.customer.model.UserImage;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 19-08-2016.
 */
public class ProfileScreen extends BaseActivity implements ServerListener{

    LoginSession loginSession;
    ServerRequest serverRequest;
    EditText nameEditText,lastEditText,emailEditText,phoneEditText;
    Button updateButton;
    ImageView backIconImageView;
    ImageView profileImageView;
    TextView changePhotoTextView;

    Dialog imageChooseDialog;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private String EncodeString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        Toolbar   toolbar       = (Toolbar)findViewById(R.id.toolbar);
        nameEditText            = (EditText)findViewById(R.id.nameEditText);
        lastEditText            = (EditText)findViewById(R.id.lastEditText);
        emailEditText           = (EditText)findViewById(R.id.emailAddressEditText);
        phoneEditText           = (EditText)findViewById(R.id.phoneEditText);
        updateButton            = (Button)findViewById(R.id.updateButton);
        changePhotoTextView     = (TextView)findViewById(R.id.changePhoto);
        profileImageView        = (ImageView)findViewById(R.id.profileImageView);
        backIconImageView       = (ImageView)toolbar.findViewById(R.id.backIconImageView);

        loginSession   = LoginSession.getInstance(this);
        serverRequest  = ServerRequest.getInstance(this);

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePhotoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageChooseDialog == null) {
                    imageChooseDialog = new Dialog(ProfileScreen.this);
                    imageChooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    imageChooseDialog.setContentView(R.layout.dialog_for_chooseimage);
                    imageChooseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    imageChooseDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView buttonTakePicture = (TextView)imageChooseDialog.findViewById(R.id.buttonTakePicture);
                    TextView buttonChooseImage = (TextView)imageChooseDialog.findViewById(R.id.buttonChooseImage);
                    buttonTakePicture.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            imageChooseDialog.cancel();
                            boolean result=checkCameraPermission(ProfileScreen.this);
                            userChoosenTask =getResources().getString(R.string.takePhoto);
                            if(result)
                                cameraIntent();
                        }
                    });
                    buttonChooseImage.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            imageChooseDialog.cancel();
                            boolean result=checkPermission(ProfileScreen.this);
                            userChoosenTask =getResources().getString(R.string.chooseFromLibrary);
                            if(result)
                                galleryIntent();

                        }
                    });

                }
                imageChooseDialog.setCancelable(true);
                imageChooseDialog.show();
            }
        });

        //get Profile info
        getCustomerDetails();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = nameEditText.getText().toString().trim();
                String lastName  = lastEditText.getText().toString().trim();
                String phone     = phoneEditText.getText().toString().trim();

                if(firstName.isEmpty()){

                    nameEditText.setError(getResources().getString(R.string.pleaseEnterYourFirstName));

                }else if(lastName.isEmpty()){

                    lastEditText.setError(getResources().getString(R.string.pleaseEnterYourLastName));

                }else if(phone.isEmpty()){

                    phoneEditText.setError(getResources().getString(R.string.pleaseEnterPhoneNumber));

                }else if(!checkInternet()){

                    noInternetAlertDialog();

                }else{

                    Map<String, String> params = new HashMap<>();
                    // the POST parameters:
                    params.put("action", "MyAccount");
                    params.put("customer_id", loginSession.getUserId());
                    params.put("page", "ProfileUpdate");
                    params.put("first_name", firstName);
                    params.put("last_name", lastName);
                    params.put("phone", phone);
                    params.put("image", "");

                    Log.e("Profile Update",""+params);
                    showProgressDialog();
                    serverRequest.createRequest(ProfileScreen.this,params, RequestID.REQ_PROFILE_UPADATE);

                }
            }
        });
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void getCustomerDetails()
    {
        if (checkInternet()) {

            Map<String, String> params = new HashMap<>();
            // the POST parameters:
            params.put("customer_id", loginSession.getUserId());
            params.put("page", "CustomerDetails");
            params.put("action", "MyAccount");
            showProgressDialog();
            serverRequest.createRequest( ProfileScreen.this, params, RequestID.REQ_GET_PROFILE);
        }else
        {
             noInternetAlertDialog();
        }

    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();
        switch (requestID) {

            case REQ_GET_PROFILE:

                CustomerDetail customerDetails = (CustomerDetail) result;

                loginSession.saveLogInStore(loginSession.getUserId(),loginSession.getFirstUser(),customerDetails.result.firstName, customerDetails.result.lastName, customerDetails.result.email, customerDetails.result.customerPhone, customerDetails.result.customerDetails.image);

                nameEditText.setText(customerDetails.result.firstName);
                lastEditText.setText(customerDetails.result.lastName);
                phoneEditText.setText(customerDetails.result.customerPhone);
                emailEditText.setText(customerDetails.result.email);

                if(!customerDetails.result.customerDetails.image.isEmpty()){
                    Picasso.with(this)
                            .load(customerDetails.result.customerDetails.image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .transform(new CircleTransform())
                            .into(profileImageView);
                }

                break;

            case REQ_PROFILE_UPADATE:

                toast(result.toString());
                loginSession.saveLogInStore(loginSession.getUserId(),loginSession.getFirstUser(),nameEditText.getText().toString().trim()
                        , lastEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), phoneEditText.getText().toString().trim(), loginSession.getUserImage());
                finish();
                break;

            case REQ_UPDATE_IMAGE:

                UserImage userImage = (UserImage) result;
                loginSession.saveDriverImage(userImage.getDriverImage());
                toast(userImage.getMessage());
                break;
        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {
        hideProgressDialog();
        toast(error);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getResources().getString(R.string.takePhoto)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getResources().getString(R.string.chooseFromLibrary)))
                        galleryIntent();
                } else {
                    //code for deny
                }

                break;

            case MY_PERMISSIONS_REQUEST_READ_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getResources().getString(R.string.takePhoto)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getResources().getString(R.string.chooseFromLibrary)))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileImageView.setImageBitmap(thumbnail);

        byte[] ba = bytes.toByteArray();
        EncodeString = Base64.encodeToString(ba, 0);
        uploadProfileImage();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profileImageView.setImageBitmap(bm);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        byte[] ba = bytes.toByteArray();
        EncodeString = Base64.encodeToString(ba, 0);
        uploadProfileImage();
    }

    private void uploadProfileImage() {

        if(checkInternet()) {
            try {
                String userId=loginSession.getUserId();
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("action","MyAccount");
                params.put("image",EncodeString);
                params.put("page","ProfileImageUpload");
                params.put("customer_id",userId);
                params.put("device","ANDROID");
                showProgressDialog();
                serverRequest.createRequest(ProfileScreen.this, params, RequestID.REQ_UPDATE_IMAGE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }else{

            noInternetAlertDialog();
        }

    }
}