package com.patelapp;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONObject;

/**
 * Created by Android on 1/5/2016.
 */
public class ForgotActivity extends AppCompatActivity {

    private EditText etMobile;
    private Button btnSubmit;
    String mobile, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);

        findViews();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = etMobile.getText().toString();
                if(TextUtils.isEmpty(mobile))
                {
                    Toast.makeText(getApplicationContext(), "Enter mobile no.", Toast.LENGTH_SHORT).show();
                }else if (etMobile.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Invalid mobile no.", Toast.LENGTH_SHORT).show();

                }else{
                    requestPassword(mobile);
                }
            }
        });
    }

    private void requestPassword(final String mobile){

        if(!NetworkReachability.isNetworkAvailable())
        {
            StaticMethodUtility.showNetworkToast(ForgotActivity.this);
            return;
        }
        Communication.getInstance().callForForgetPassword(mobile).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                String pass = response.optString("password");
                if(!TextUtils.isEmpty(pass)){
                    sendVerificationMessage(mobile,pass);
                }

            }

            @Override
            public void failure(String mError) {

            }
        }).showProgress(ForgotActivity.this);

    }


    private void findViews() {
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }



    private void sendOTP(final String mobile,final String otp){
        String url = Const.MESSAGE_URL + mobile + "&text=" + "Your passwords is " + otp + "&priority=ndnd&stype=normal";



    }


    public void sendVerificationMessage(final String mobile, final String otp) {

        String url = Const.MESSAGE_URL + mobile + "&text=" +"Your%20password%20OTP%20of%20THE%20PATIDAR%20App%20is%20:" + otp + "&priority=ndnd&stype=normal";

        Communication.getInstance().callForOTP(url).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

            }

            @Override
            public void failure(String mError) {

            }
        });
    }








}
