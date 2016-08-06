package com.patelapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Interfaces.OnSMSPermissionListener;
import com.patelapp.server.Communication;
import com.patelapp.server.DownloadImageService;

import org.json.JSONObject;

/**
 * Created by shivam on 3/6/2016.
 */
public class VerificationActiivty extends BaseActivity implements View.OnClickListener,OnSMSPermissionListener {


    private EditText etOtp;
    private Button btnSubmit;
    String mobile, otp;
    private BroadcastReceiver otpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSMSPermissionListener(this);
        setContentView(R.layout.activity_verification);
        Intent in = getIntent();
        if (in != null) {
            mobile = in.getStringExtra("mobile");
            otp = in.getStringExtra("otp");
            sendVerificationMessage(mobile, otp);
        }


        Log.d("Mobile==>", mobile);
        Log.d("Otp==>", otp);
        findViews();

        requestSMSReadPermission();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalData.OTP_ACTION);


        otpReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getExtras() != null){
                    String _otp = intent.getExtras().getString("otp");
                    if(!TextUtils.isEmpty(_otp)){
                        etOtp.setText(_otp);
                    }
                }
            }
        };
        registerReceiver(otpReceiver,intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(otpReceiver);
    }

    private void findViews() {
        etOtp = (EditText) findViewById(R.id.etOtp);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enter OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void sendVerificationMessage(final String mobile, final String otp) {


//        String url = Const.MESSAGE_URL + mobile + "&text=" +"Your%20Verification%20OTP%20for%20THE%20PATIDAR%20App%20is%20:"+ otp + "&priority=ndnd&stype=normal";

        String url = Const.MESSAGE_URL + mobile + "&text=" +otp+"%20is%20your%20Verification%20OTP%20Number%20for%20THE%20PATIDAR%20App"+"&priority=ndnd&stype=normal";


        Communication.getInstance().callForOTP(url).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

            }

            @Override
            public void failure(String mError) {

            }
        });



        // Request a string response
       /* StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response==>", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
*/

    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            // Handle clicks for btnForgot
            if (etOtp.getText().toString().equalsIgnoreCase("")) {
                etOtp.setError("enter code");
            } else {
                if (etOtp.getText().toString().equals(otp)) {
                    requestVerification();
                } else {
                    Toast.makeText(VerificationActiivty.this, "Invalid Otp", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestVerification() {

        Communication.getInstance().callForGetVerify(mobile, otp).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                try {
                    JSONObject user_json = response.optJSONObject("data");
                    DataAccessManager.insertLoginUser(user_json);
                    String fname = user_json.optString("fname");
                    String lname = user_json.optString("lname");
                    String r_id = user_json.optString("r_id");

                    MySharedPrefs.getInstance().StoreRegisterId(r_id);

                    String msg = "Welcome" + " " + fname + " " + lname;

                    Toast.makeText(VerificationActiivty.this, msg, Toast.LENGTH_SHORT).show();
                    String image_path = DataAccessManager.getLoginUser().getImage();
                    if (!TextUtils.isEmpty(image_path)) {
                        Intent intent = new Intent(VerificationActiivty.this, DownloadImageService.class);
                        intent.putExtra("image_url", Const.ImagePath + image_path);
                        startService(intent);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gotoMainScreen();
                        }
                    }, 1000);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(String mError) {

                Toast.makeText(VerificationActiivty.this, mError, Toast.LENGTH_SHORT).show();
            }
        }).showProgress(VerificationActiivty.this);

    }


    private void gotoMainScreen() {

        Intent intent = new Intent(VerificationActiivty.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSMSPermissionGranted() {

    }
}
