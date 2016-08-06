package com.patelapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.gcm.RegistrationIntentService;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by AndroidDevloper on 3/3/2016.
 */
public class SplashScreen extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreen";
    private TextView tvSpalsh;
    Animation zoomAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        tvSpalsh = (TextView)findViewById(R.id.tvSpalsh);

        zoomAnimation = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.zoomt_to_medium);
        tvSpalsh.setAnimation(zoomAnimation);

        if (checkPlayServices() && NetworkReachability.isNetworkAvailable()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        if(NetworkReachability.isNetworkAvailable())
            requestForAdvertise();
        else
          gotoNextScreen(3000);

    }



    private void requestForAdvertise(){
        Communication.getInstance().callForGetAdvertise().setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                JSONArray arr_data = response.optJSONArray("data");
                DataAccessManager.insertAdvertise(arr_data);

                gotoNextScreen(100);
            }

            @Override
            public void failure(String mError) {

            }
        });

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void gotoNextScreen(int duration){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(MySharedPrefs.getInstance().getRegisterId()))
                {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }

            }
        },duration);

    }
    }


