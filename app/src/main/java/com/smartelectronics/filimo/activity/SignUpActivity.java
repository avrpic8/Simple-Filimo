package com.smartelectronics.filimo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.util.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {


    private Button btnSignUp;
    private EditText edtUserPhone;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserRegistered();
        setContentView(R.layout.activity_sign_up);
        init();
    }


    // my methods >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void initToolBar(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
    }
    private void initButtons(){

        btnSignUp = findViewById(R.id.button_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = edtUserPhone.getText().toString();
                if(phoneNumber.startsWith("09")) {

                    String randCode = String.valueOf((int) (Math.random() * 1000000));
                    sendRequest(phoneNumber, randCode);
                }else
                    Toast.makeText(SignUpActivity.this,
                                "شماره تلفن خود را به صورت صحیح وارد کنید.",
                                Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initEditText(){

        edtUserPhone = findViewById(R.id.edit_text_user_phone);


    }
    private void init() {

        initToolBar();
        initButtons();
        initEditText();
    }
    private void checkUserRegistered(){

        boolean registered = PrefUtils.getFromPrefs(this, "registered", false);
        //if(registered)
           startActivity(new Intent(SignUpActivity.this, MainActivity.class));
    }
    private void sendRequest(final String phoneNumber, final String randomCode){

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://api.kavenegar.com/" +
                        "v1/706A4B5158344B366E4C4337654667757A6855704E6D466C66443769686B363152376C35636F6B386767413D/" +
                        "verify/lookup.json?receptor="+phoneNumber+"&token="+randomCode+"&template=verify",

                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject result = response.getJSONObject("return");

                            String message = result.getString("message");
                            int     status = result.getInt("status");

                            if(status == 200) {

                                Intent intent = new Intent(SignUpActivity.this, VerifyPhoneActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("randomCode", randomCode);
                                startActivity(intent);
                            }
                        } catch (JSONException e) { e.printStackTrace();}
                        Log.i("response", "onResponse: "+ response.toString());

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SignUpActivity.this, "عملیات موفقیت آمیز نبود.", Toast.LENGTH_SHORT).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        requestQueue.add(request);
    }
}
