package com.smartelectronics.filimo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.util.PrefUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerifyPhoneActivity extends AppCompatActivity {

    // views
    private TextView txtVerifyTimer, txtVerifyNumber, txtCorrectPhone;
    private EditText edtVerifyCode;
    private Button   btnVerifyCode, btnResendCode;


    // timer
    private CountDownTimer downTimer;


    @Override
    protected void attachBaseContext(Context newBase)  {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        init();
    }



    // my methods >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void initToolBar(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("کد تایید");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
    }
    private void initButton(){

        btnVerifyCode   = findViewById(R.id.button_verify_code);
        btnResendCode = findViewById(R.id.button_verify_code_resend);


        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(edtVerifyCode.getText().toString().equals(getRandomVerifyCode())) {
                    Toast.makeText(VerifyPhoneActivity.this, "کد تایید شد.", Toast.LENGTH_SHORT).show();
                    PrefUtils.saveToPrefs(
                                    VerifyPhoneActivity.this,
                                    "registered",
                                    true);
                }
                else
                    Toast.makeText(VerifyPhoneActivity.this, "کد وارد شده صحیح نمی باشد.", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void initEditText(){

        edtVerifyCode = findViewById(R.id.edit_text_verify_code);
    }
    private void initTextViews(){

        txtVerifyNumber = findViewById(R.id.txt_phone_verify);
        txtVerifyTimer  = findViewById(R.id.txt_verify_timer);
        txtCorrectPhone = findViewById(R.id.txt_correct_phone);


        String verify_text = "کد تایید به شماره ی " + getPhoneNumber() + " ارسال خواهد شد. لطفا آن را در فیلد زیر وارد کنید.";
        txtVerifyNumber.setText(verify_text);

        txtCorrectPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(VerifyPhoneActivity.this, SignUpActivity.class));
                finish();
            }
        });
    }
    private void initTimers(){

        downTimer = new CountDownTimer(50000, 1000) {
            @Override
            public void onTick(long l) {

                txtVerifyTimer.setText(String.valueOf(l / 1000)+ " " + getResources().getString(R.string.text_downTimer));
            }

            @Override
            public void onFinish() {

                txtVerifyTimer.setText(0 + " " + getResources().getString(R.string.text_downTimer));
                btnResendCode.setVisibility(View.VISIBLE);
            }
        }.start();

    }
    private void init(){

        initToolBar();
        initButton();
        initEditText();
        initTextViews();
        initTimers();
    }
    private String getRandomVerifyCode(){

        Intent intent = getIntent();
        return intent.getStringExtra("randomCode");
    }
    private String getPhoneNumber(){

        Intent intent = getIntent();
        return intent.getStringExtra("phoneNumber");
    }
}
