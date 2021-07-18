package com.eatyhero.customer.moretab;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eatyhero.customer.R;
import com.eatyhero.customer.base.BaseActivity;
import com.eatyhero.customer.common.LoginSession;
import com.eatyhero.customer.service.RequestID;
import com.eatyhero.customer.service.ServerListener;
import com.eatyhero.customer.service.ServerRequest;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 17-03-2017.
 */
public class AddCardScreen extends BaseActivity implements ServerListener{

    //Create objects
    LoginSession loginSession;
    ServerRequest serverRequest;

    //Create xml file
    Toolbar toolbar;
    ImageView backIconImageView;
    TextView actionBarTitleTextView;
    EditText cardholderNameEditText;
    EditText cardnumberEditText;
    AutoCompleteTextView cardAddressEditText;
    Spinner monthSpinner;
    Spinner yearSpinner;
    EditText cvvEditText;
    Button addCardButton;

    //Create String objects
    String save_cardholderName;
    String save_cardnumber;
    String save_cardAddress;
    String save_postalCode;
    String save_month;
    String save_year;
    String save_cvv;

    String a;
    int keyDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_screen);

        //Initialize object
        loginSession   = LoginSession.getInstance(this);
        serverRequest  = ServerRequest.getInstance(this);

        //Initialize xml layout
        toolbar                  = (Toolbar)findViewById(R.id.toolbar);
        backIconImageView        = (ImageView)toolbar.findViewById(R.id.backIconImageView);
        actionBarTitleTextView   = (TextView) toolbar.findViewById(R.id.actionBarTitleTextView);
        cardholderNameEditText   = (EditText)findViewById(R.id.cardholderNameEditText);
        cardnumberEditText       = (EditText)findViewById(R.id.cardnumberEditText);
        cardAddressEditText      = (AutoCompleteTextView)findViewById(R.id.cardAddressEditText);
        monthSpinner             = (Spinner)findViewById(R.id.monthSpinner);
        yearSpinner              = (Spinner)findViewById(R.id.yearSpinner);
        cvvEditText              = (EditText)findViewById(R.id.cvvEditText);
        addCardButton            = (Button) findViewById(R.id.addCardButton);

        actionBarTitleTextView.setText(getResources().getString(R.string.addCard));

        cardnumberEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = cardnumberEditText.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    cardnumberEditText.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((cardnumberEditText.getText().length() + 1) % 5) == 0) {

                            if (cardnumberEditText.getText().toString().split("-").length <= 3) {
                                cardnumberEditText.setText(cardnumberEditText.getText() + "-");
                                cardnumberEditText.setSelection(cardnumberEditText.getText().length());
                            }
                        }
                        a = cardnumberEditText.getText().toString();
                    } else {
                        a = cardnumberEditText.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    cardnumberEditText.setText(a);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_cardholderName = cardholderNameEditText.getText().toString().trim();
                save_cardnumber     = cardnumberEditText.getText().toString().trim().replace("-","");
                save_cardAddress    = cardAddressEditText.getText().toString().trim();
                save_month          = monthSpinner.getSelectedItem().toString().trim();
                save_year           = yearSpinner.getSelectedItem().toString().trim();
                save_cvv            = cvvEditText.getText().toString().trim();

                if (save_cardholderName.isEmpty()) {

                    cardholderNameEditText.setError(getResources().getString(R.string.PleaseEnterCardHoldername));

                } else if (save_cardnumber.isEmpty()) {

                    cardnumberEditText.setError(getResources().getString(R.string.PleaseEnterCardNumber));

                }  else if (save_month.equalsIgnoreCase(getResources().getString(R.string.MONTH))) {

                    toast(getResources().getString(R.string.PleaseEnterexpiryMonth));

                } else if (save_year.equalsIgnoreCase(getResources().getString(R.string.YEAR))) {

                    toast(getResources().getString(R.string.PleaseEnterExpiryYear));

                } else if (save_cvv.isEmpty()) {

                    cvvEditText.setError(getResources().getString(R.string.PleaseEnterCvvnumber));

                } else {

                    final Card card = new Card(save_cardnumber, Integer.parseInt(save_month), Integer.parseInt(save_year), save_cvv);

                    boolean validation = card.validateCard();

                    if (validation) {

                        showProgressDialog();

                        new Stripe(getApplication()).createToken(
                                card,
                                loginSession.getPublishKey(),
                                new TokenCallback() {

                                    public void onSuccess(Token token) {

                                        Log.e("Token", "Token" + token.getCard().getLast4());
                                        String stripeToken = token.getId();

                                        if (checkInternet()) {

                                            Map<String, String> params = new HashMap<>();

                                            params.put("action", "MyAccount");
                                            params.put("page", "addCard");
                                            params.put("customer_id", loginSession.getUserId());
                                            params.put("stripe_token_id",stripeToken);
                                            params.put("card_id","");
                                            params.put("address_zip","");
                                            params.put("card_brand",card.getBrand());
                                            params.put("country","");
                                            params.put("exp_month", save_month);
                                            params.put("exp_year",save_year);
                                            params.put("card_type",card.getType());
                                            params.put("card_number", token.getCard().getLast4());
                                            params.put("client_ip","");

                                            params.put("card_brand",token.getCard().getBrand());
                                            Log.e("params", "params" + params);
                                            showProgressDialog();

                                            serverRequest.createRequest(AddCardScreen.this, params, RequestID.REQ_ADD_STRIPE_CARD);


                                        } else {

                                            noInternetAlertDialog();
                                        }

                                    }

                                    public void onError(Exception error) {

                                    }
                                });

                    } else if (!card.validateNumber()) {

                        toast(getResources().getString(R.string.TheCardNumberInvalid));

                    } else if (!card.validateExpiryDate()) {

                        toast(getResources().getString(R.string.TheExpirationDateInvalid));

                    } else if (!card.validateCVC()) {

                        toast(getResources().getString(R.string.TheCVVInvalid));

                    } else {

                        toast(getResources().getString(R.string.CardInvalid));
                    }

                }

            }
        });

        backIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    public void onSuccess(Object result, RequestID requestID) {

        hideProgressDialog();

        switch (requestID){

            case REQ_ADD_STRIPE_CARD:

                toast(getResources().getString(R.string.CardAddeddMessage));
                setResult(2);
                finish();

                break;
        }

    }

    @Override
    public void onFailure(String error, RequestID requestID) {

        hideProgressDialog();
        toast(error);
    }
}
