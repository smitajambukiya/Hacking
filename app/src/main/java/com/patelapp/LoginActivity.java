package com.patelapp;

import android.app.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.server.Communication;
import com.patelapp.server.DownloadImageService;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    private EditText inputName, inputPassword, etMobile;
    private TextInputLayout inputLayoutName, inputLayoutPassword, input_layout_mob;
    private Button btnSignUp;
    TextView tv_register, tv_skipp, tv_forgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        findViews();


    }

    private void findViews() {

        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        input_layout_mob = (TextInputLayout) findViewById(R.id.input_layout_mob);
        etMobile = (EditText) findViewById(R.id.etMobile);

       /* etMobile.setText("9724368362");
        inputPassword.setText("111111");
*/
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_skipp = (TextView) findViewById(R.id.tv_skipp);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        tv_skipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainScreen();
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignupScreen();
            }
        });
        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Forgopassword();
            }


        });
    }


    private void gotoSignupScreen() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void Forgopassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);
        //finish();
    }

    /**
     * Validating form
     */
    private void submitForm() {

        hideKeyboard();
        if (!validateMobile() && !validatePassword()) {
            return;
        } else {
            requestLogin();
            //   gotoMainScreen();
        }


    }


    private void gotoMainScreen() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    private boolean validateMobile() {
        String mobile = etMobile.getText().toString();
        if (mobile.isEmpty() || mobile.length() < 10) {
            input_layout_mob.setError("Enter valid mobile no.");
            requestFocus(etMobile);
            return false;
        } else {
            input_layout_mob.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }


    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a newlbanner one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void requestLogin() {

        if (!NetworkReachability.isNetworkAvailable()) {
            StaticMethodUtility.showNetworkToast(LoginActivity.this);
            return;
        }
        Communication.getInstance().callForGetLogin(etMobile.getText().toString(), inputPassword.getText().toString()).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                int status = response.optInt("response");
                String message = response.optString("success");
                if (status == 1) {
                    try {
                        JSONObject user_json = response.optJSONObject("data");
                        DataAccessManager.insertLoginUser(user_json);
                        String fname = user_json.optString("fname");
                        String lname = user_json.optString("lname");
                        String r_id = user_json.optString("r_id");
                        MySharedPrefs.getInstance().StoreRegisterId(r_id);
                        String msg = "Welcome" + " " + fname + " " + lname;
                        String image_path = DataAccessManager.getLoginUser().getImage();
                        if (!TextUtils.isEmpty(image_path)) {
                            Intent intent = new Intent(LoginActivity.this, DownloadImageService.class);
                            intent.putExtra("image_url", Const.ImagePath + image_path);
                            startService(intent);
                        }
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gotoMainScreen();
                            }
                        }, 1000);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void failure(String mError) {

                Toast.makeText(LoginActivity.this, mError, Toast.LENGTH_SHORT).show();
            }
        }).showProgress(LoginActivity.this);


    }
}
