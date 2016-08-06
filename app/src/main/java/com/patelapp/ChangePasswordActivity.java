package com.patelapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONObject;

/**
 * Created by AndroidDevloper on 3/8/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    TextInputLayout tilCurrentpassword,tilNewPassword,tilConfirmPassword;
    EditText etConfirmPassword,etNewPassword,etCurrentPassword;
    Button btnSubmit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();

    }


    private void initViews(){
        tilConfirmPassword = (TextInputLayout)findViewById(R.id.tilConfirmPassword);
        tilNewPassword = (TextInputLayout)findViewById(R.id.tilNewPassword);
        tilCurrentpassword = (TextInputLayout)findViewById(R.id.tilCurrentPassword);
        etCurrentPassword = (EditText)findViewById(R.id.etCurrentPassword);

        btnSubmit = (Button)findViewById(R.id.btnSubmit);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCurrentPassword() && validateNewPassword() && validateConfirmPassword() && isCurrentNewAreSame()){

                    requestChangePassword();
                }



            }
        });

    }

    private void requestChangePassword(){
        if(!NetworkReachability.isNetworkAvailable())
        {
            StaticMethodUtility.showNetworkToast(ChangePasswordActivity.this);
            return;
        }
        Communication.getInstance().callForChangePassword(etCurrentPassword.getText().toString(),etConfirmPassword.getText().toString(), MySharedPrefs.getInstance().getRegisterId()).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                int status = response.optInt("response");
                String message = response.optString("success");
                if(status == 1) {

                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }else{

                    Toast.makeText(ChangePasswordActivity.this,message,Toast.LENGTH_SHORT).show();
                    etCurrentPassword.getText().clear();
                    etNewPassword.getText().clear();
                    etConfirmPassword.getText().clear();

                }
            }

            @Override
            public void failure(String mError) {


            }
        }).showProgress(ChangePasswordActivity.this);

    }



    private boolean validateCurrentPassword() {
        if (etCurrentPassword.getText().toString().trim().isEmpty()) {
            tilCurrentpassword.setError(getString(R.string.err_msg_current_password));
            requestFocus(etCurrentPassword);
            return false;
        }else if(etCurrentPassword.length() < 6)
        {
            tilCurrentpassword.setError(getString(R.string.err_msg_valid_current_password));
            requestFocus(etCurrentPassword);
            return false;
        }
        else {
            tilCurrentpassword.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateNewPassword() {
        if (etNewPassword.getText().toString().trim().isEmpty()) {
            tilNewPassword.setError(getString(R.string.err_msg_new_password));
            requestFocus(etNewPassword);
            return false;
        }else if(etNewPassword.length() < 6)
        {
            tilNewPassword.setError(getString(R.string.err_msg_valid_new_password));
            requestFocus(etNewPassword);
            return false;
        }
        else {
            tilNewPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isCurrentNewAreSame(){
        if(!etNewPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())){
            Toast.makeText(ChangePasswordActivity.this,getString(R.string.err_msg_valid_password_match),Toast.LENGTH_SHORT).show();
            return  false;
        }else{
            return  true;
        }
    }


    private boolean validateConfirmPassword() {
        if (etConfirmPassword.getText().toString().trim().isEmpty()) {
            tilConfirmPassword.setError(getString(R.string.err_msg_cofirm_password));
            requestFocus(etConfirmPassword);
            return false;
        }else if(etConfirmPassword.length() < 6)
        {
            tilConfirmPassword.setError(getString(R.string.err_msg_valid_confirm_password));
            requestFocus(etConfirmPassword);
            return false;
        }
        else {
            tilConfirmPassword.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

}
