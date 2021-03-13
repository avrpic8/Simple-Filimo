package com.smartelectronics.filimo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.smartelectronics.filimo.R;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.List;

public class BuyAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBuy1, btnBuy2, btnBuy3, btnBuy4, btnBuy5;
    private String price1, price2, price3, price4, price5;

    private int expire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_account);

        init();

        /**
         * When User Return to Application From IPG on Browser
         */
        Uri data = getIntent().getData();
        ZarinPal.getPurchase(this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
            @Override
            public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {

                if (isPaymentSuccess) {
                    /* When Payment Request is Success :) */
                    String message = "Your Payment is Success :) " + refID;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    /* When Payment Request is Failure :( */
                    String message = "Your Payment is Failure :(";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initButtons(){

        btnBuy1 = findViewById(R.id.btn_buy1);
        btnBuy1.setOnClickListener(this);

        btnBuy2 = findViewById(R.id.btn_buy2);
        btnBuy2.setOnClickListener(this);

        btnBuy3 = findViewById(R.id.btn_buy3);
        btnBuy3.setOnClickListener(this);

        btnBuy4 = findViewById(R.id.btn_buy4);
        btnBuy4.setOnClickListener(this);

        btnBuy5 = findViewById(R.id.btn_buy5);
        btnBuy5.setOnClickListener(this);
    }
    private void getPricesFromServer(){

        ParseQuery<ParseObject> prices = ParseQuery.getQuery("Plans");
        prices.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                btnBuy1.setVisibility(View.VISIBLE);
                btnBuy2.setVisibility(View.VISIBLE);
                btnBuy3.setVisibility(View.VISIBLE);
                btnBuy4.setVisibility(View.VISIBLE);
                btnBuy5.setVisibility(View.VISIBLE);

                price1 = objects.get(0).getString("Price");
                price2 = objects.get(1).getString("Price");
                price3 = objects.get(2).getString("Price");
                price4 = objects.get(3).getString("Price");
                price5 = objects.get(4).getString("Price");


                btnBuy1.setText("خرید اکانت " + objects.get(0).getString("Month") + " به قیمت " + price1 + " تومان");
                btnBuy2.setText("خرید اکانت " + objects.get(1).getString("Month") + " به قیمت " + price2 + " تومان");
                btnBuy3.setText("خرید اکانت " + objects.get(2).getString("Month") + " به قیمت " + price3 + " تومان");
                btnBuy4.setText("خرید اکانت " + objects.get(3).getString("Month") + " به قیمت " + price4 + " تومان");
                btnBuy5.setText("خرید اکانت " + objects.get(4).getString("Month") + " به قیمت " + price5 + " تومان");
            }
        });
    }
    private void payment(String price){

        ZarinPal purchase = ZarinPal.getPurchase(this);
        PaymentRequest payment  = ZarinPal.getPaymentRequest();

        payment.setMerchantID("71c705f8-bd37-11e6-aa0c-000c295eb8fc");
        payment.setAmount(Integer.parseInt(price));
        payment.setDescription("In App Purchase Test SDK");
        payment.setCallbackURL("app://app");     /* Your App Scheme */
        payment.setMobile("09355106005");            /* Optional Parameters */
        payment.setEmail("imannamix@gmail.com");     /* Optional Parameters */


        purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
            @Override
            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {


                if (status == 100) {
                   /*
                   When Status is 100 Open Zarinpal PG on Browser
                   */
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Payment Failure :(", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void init(){
        initButtons();
        getPricesFromServer();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_buy1:
                expire = 1;
                payment(price1);
                break;

            case R.id.btn_buy2:
                expire = 2;
                payment(price2);
                break;

            case R.id.btn_buy3:
                expire = 3;
                payment(price3);
                break;

            case R.id.btn_buy4:
                expire = 4;
                payment(price4);
                break;

            case R.id.btn_buy5:
                expire = 5;
                payment(price5);
                break;
        }
    }
}
