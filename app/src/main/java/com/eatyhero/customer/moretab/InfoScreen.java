package com.eatyhero.customer.moretab;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.eatyhero.customer.R;
import com.eatyhero.customer.account.LoginFragment;
import com.eatyhero.customer.account.ProfileScreen;
import com.eatyhero.customer.base.NewBaseHomeScreen;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.common.Utility;
import com.eatyhero.customer.database.DatabaseManager;

/**
 * Created by admin on 15-03-2017.
 */
public class InfoScreen extends Fragment implements  View.OnClickListener {

    //Create objects
    LoginSession loginSession;
    Utility utility;
    Dialog alertDialog;
    NewBaseHomeScreen baseHomeScreen;
    DatabaseManager databaseManager;

    //Create xml file
    RelativeLayout myAddressButton, changePasswordButton,
            profileButton, appFeedbackButton, privacyPolicyButton,
            walletButton,bukatableButton,cardButton,rewardButton,referFriendButton, storeSignUpButton, driverSignUpButton;

    TextView logoutButton,versionNameTextView;

    String version="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.more_screen, container, false);

        //Initialize xml objects
        myAddressButton      = (RelativeLayout) rootView.findViewById(R.id.myAddressButton);
        changePasswordButton = (RelativeLayout) rootView.findViewById(R.id.changePasswordButton);
        profileButton        = (RelativeLayout) rootView.findViewById(R.id.profileButton);
        appFeedbackButton    = (RelativeLayout) rootView.findViewById(R.id.appFeedbackButton);
        privacyPolicyButton  = (RelativeLayout) rootView.findViewById(R.id.privacyPolicyButton);
        logoutButton         = (TextView) rootView.findViewById(R.id.logoutButton);
        versionNameTextView         = (TextView) rootView.findViewById(R.id.versionNameTextView);
        walletButton         = (RelativeLayout) rootView.findViewById(R.id.walletButton);
        bukatableButton      = (RelativeLayout) rootView.findViewById(R.id.bukatableButton);
        cardButton           = (RelativeLayout) rootView.findViewById(R.id.cardButton);
        rewardButton         = (RelativeLayout) rootView.findViewById(R.id.rewardButton);
        referFriendButton    = (RelativeLayout) rootView.findViewById(R.id.referFriendButton);
        storeSignUpButton    = (RelativeLayout) rootView.findViewById(R.id.storeSignUpButton);
        driverSignUpButton    = (RelativeLayout) rootView.findViewById(R.id.driverSignUpButton);

        loginSession    = LoginSession.getInstance(getActivity());
        utility         = Utility.getInstance(getActivity());
        databaseManager = DatabaseManager.getInstance(getActivity());

        //showStatusBar
        utility.showStatusBar();
        utility.CURRENT_SCREEN = "INFO_SCREEN";

        myAddressButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        appFeedbackButton.setOnClickListener(this);
        privacyPolicyButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        walletButton.setOnClickListener(this);
        bukatableButton.setOnClickListener(this);
        cardButton.setOnClickListener(this);
        rewardButton.setOnClickListener(this);
        referFriendButton.setOnClickListener(this);
        storeSignUpButton.setOnClickListener(this);
        driverSignUpButton.setOnClickListener(this);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionNameTextView.setText(getResources().getString(R.string.version)+version);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.myAddressButton:

                Intent i=new Intent(getActivity(),  AddressBookScreen.class);
                startActivity(i);

                break;

            case R.id.changePasswordButton:

                Intent i1=new Intent(getActivity(), ChangePasswordScreen.class);
                startActivity(i1);

                break;
            case R.id.appFeedbackButton:

                Intent i3=new Intent(getActivity(), TermsandConditionActivity.class);
                startActivity(i3);

                break;
            case R.id.privacyPolicyButton:

                Intent i5=new Intent(getActivity(), PrivacyPolicesActivity.class);
                startActivity(i5);

                break;
            case R.id.profileButton:

                Intent i9=new Intent(getActivity(), ProfileScreen.class);
                startActivity(i9);
                break;

            case R.id.bukatableButton:

                Intent buktable=new Intent(getActivity(), TableHistoryScreen.class);
                startActivity(buktable);
                break;
            case R.id.cardButton:

                Intent cardMGNT=new Intent(getActivity(), CardManagementScreen.class);
                startActivity(cardMGNT);
                break;

            case R.id.rewardButton:

                Intent reward=new Intent(getActivity(), RewardPonitScreen.class);
                startActivity(reward);
                break;

            case R.id.logoutButton:

                showAlertDialog();

                break;

            case R.id.walletButton:

                Intent i6=new Intent(getActivity(), WalletHistoryScreen.class);
                startActivity(i6);

                break;

            case R.id.referFriendButton:

                Intent i7 = new Intent(getActivity(), ReferaFriendScreen.class);
                startActivity(i7);

                break;
            case R.id.storeSignUpButton:

                Intent i8 = new Intent(getActivity(), StoreSignup.class);
                startActivity(i8);

                break;
            case R.id.driverSignUpButton:

                Intent driver = new Intent(getActivity(), DriverSignup.class);
                startActivity(driver);

                break;

        }
    }

    private void showAlertDialog() {

        if (alertDialog == null) {

            alertDialog = new Dialog(getActivity());
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.dialog_for_alert);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        TextView noTextView = (TextView) alertDialog.findViewById(R.id.noTextView);
        TextView yesTextView = (TextView) alertDialog.findViewById(R.id.yesTextView);

        noTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

        yesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseManager.clearTable();
                int cartCount = 0;
                loginSession.saveCardCount(cartCount);
                alertDialog.dismiss();
                loginSession.logout();
                LoginManager.getInstance().logOut();
                Fragment fragmentLogin = new LoginFragment();
                FragmentManager fragmentManagerLogin = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransactionLogin = fragmentManagerLogin.beginTransaction();
                fragmentTransactionLogin.replace(R.id.frameLayout, fragmentLogin);
                fragmentTransactionLogin.commit();

                baseHomeScreen.cartCountTextView.setVisibility(View.GONE);

            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();

    }


}
