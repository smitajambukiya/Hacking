package com.patelapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityUser;
import com.patelapp.Fragment.DatePickerFragment;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Andr oid on 1/3/2016.
 */
public class SignUpActivity extends BaseActivity implements ImageChooserListener, View.OnClickListener, OnPermissionListener {

    private final String TAG = "tag";
    private ProgressBar progressBar;
    private Button btn_sighnup;
    private TextInputLayout input_layout_first, input_layout_last, input_layout_dob, input_layout_email, input_layout_mob,
            input_layout_password;
    private EditText etFirstName, etLastName, etDob, etEmail, etMobile, etPassword;

    private CheckBox cbContact;
    private AppCompatSpinner acs_commn_cat, acs_commn_type;

    private ImageView ivProfile;
    private ImageChooserManager imageChooserManager;

    private String filePath;

    private int chooserType;
    private boolean is_image_chosen = false;
    private String originalFilePath;
    private String thumbnailFilePath;
    private int contact_flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initViews();

        setPermissionListeners(this);

    }

    public void initViews() {
        input_layout_first = (TextInputLayout) findViewById(R.id.input_layout_first);
        input_layout_last = (TextInputLayout) findViewById(R.id.input_layout_last);
        input_layout_dob = (TextInputLayout) findViewById(R.id.input_layout_dob);
        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_mob = (TextInputLayout) findViewById(R.id.input_layout_mob);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etDob = (EditText) findViewById(R.id.etDob);
        etPassword = (EditText) findViewById(R.id.etPassword);

        acs_commn_cat = (AppCompatSpinner) findViewById(R.id.acs_commn_cat);
        acs_commn_type = (AppCompatSpinner) findViewById(R.id.acs_commn_type);

        cbContact = (CheckBox) findViewById(R.id.cbContact);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        progressBar.setVisibility(View.GONE);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btn_sighnup = (Button) findViewById(R.id.btn_signup);

        btn_sighnup.setOnClickListener(this);
        ivProfile.setOnClickListener(this);

        etDob.setInputType(InputType.TYPE_NULL);
        acs_commn_cat.getSelectedItemPosition();

        ArrayAdapter<String> arrayCategoryAdapter = new ArrayAdapter<String>(SignUpActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.community_categaory));
        acs_commn_cat.setAdapter(arrayCategoryAdapter);
        arrayCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        ArrayAdapter<String> arrayTypeAdapter = new ArrayAdapter<String>(SignUpActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.community_type));
        acs_commn_type.setAdapter(arrayTypeAdapter);
        arrayTypeAdapter .setDropDownViewResource(R.layout.spinner_dropdown_item);


        cbContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contact_flag = isChecked ? 1 : 0;
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "datePicker");

        newFragment.setCallBack(mDateListener);
    }

    private DatePickerFragment.DateListener mDateListener = new DatePickerFragment.DateListener() {
        @Override
        public void onDateSelected(int year, int month, int day) {

            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(c.getTime());
            etDob.setText(formattedDate);
        }

    };


    private void submitForm() {
        File image_file = null;
        EntityUser entityUser = new EntityUser();
        entityUser.setFname(etFirstName.getText().toString());
        entityUser.setLname(etLastName.getText().toString());
        entityUser.setEmail(etEmail.getText().toString());
        entityUser.setMobile(etMobile.getText().toString());
        entityUser.setDob(etDob.getText().toString());
        entityUser.setPassword(etPassword.getText().toString());
        entityUser.setCommunity_type(acs_commn_type.getSelectedItem().toString());
        entityUser.setCommunity_category(String.valueOf(acs_commn_cat.getSelectedItemPosition()-1));
        entityUser.setVisiblity(String.valueOf(contact_flag));
        File resize_file = null;
        if(!TextUtils.isEmpty(originalFilePath)) {
            image_file = new File(originalFilePath);
            resize_file = getResizeImage(originalFilePath);
        }
        requestSignup(entityUser,resize_file);
    }

    private void requestSignup(EntityUser entityUser,File resize_file){
        if(!NetworkReachability.isNetworkAvailable())
        {
            StaticMethodUtility.showNetworkToast(SignUpActivity.this);
            return;
        }
        Communication.getInstance().callForRegisterUser(entityUser, resize_file).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                // Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                if (response.has("response")) {
                    if (response.optInt("response") == 1) {
                        JSONObject data = response.optJSONObject("data");
                        final String mobile = data.optString("mobile");
                        final String otp = data.optString("otp");
                        gotoMainActivity(mobile,otp);
                    }else{
                        String message = response.optString("success");
                        Toast.makeText(SignUpActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void failure(String mError) {
                Toast.makeText(SignUpActivity.this, mError, Toast.LENGTH_SHORT).show();
                LogM.d(mError);
            }
        }).showProgress(SignUpActivity.this);
    }

    private void gotoMainActivity(String mobile, String otp) {
        Intent intent = new Intent(SignUpActivity.this, VerificationActiivty.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("otp", otp);
        startActivity(intent);
        finish();
    }

    @Override
    public void onImageChosen(final ChosenImage image) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                is_image_chosen = true;

                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                //thumbnailSmallFilePath = image.getFileThumbnailSmall();
                progressBar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");
                    loadImage(image.getFileThumbnail());
                    //loadImage(imageViewThumbSmall, image.getFileThumbnailSmall());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }


    private void loadImage(String path) {
        Glide.with(this).load(new File(path)).centerCrop().into(ivProfile);

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivProfile:
                if (verifyStoragePermissions(SignUpActivity.this))
                    showImagePickerDialog();
                else
                    requestExternalStoragePermission();
                break;

            case R.id.btn_signup:
                if (validateFirstName() && validateLastName() && validateDob() && validateEmail() && validateMobile() && validatePassword() && validateCommCategory() && validateCommType()) {
                    submitForm();
                }
                break;

        }

    }


    private boolean validateFirstName() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            input_layout_first.setError(getString(R.string.err_msg_fname));
            requestFocus(etFirstName);
            return false;
        } else {
            input_layout_first.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateLastName() {
        if (etLastName.getText().toString().trim().isEmpty()) {
            input_layout_last.setError(getString(R.string.err_msg_lname));
            requestFocus(etLastName);
            return false;
        } else {
            input_layout_last.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateMobile() {
        if (etMobile.getText().toString().trim().isEmpty()) {
            input_layout_mob.setError(getString(R.string.err_msg_mobile));
            requestFocus(etMobile);
            return false;
        } else if (etMobile.getText().length() < 10) {
            input_layout_mob.setError(getString(R.string.err_msg_mobile));
            requestFocus(etMobile);
            return false;
        } else {
            input_layout_mob.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDob() {
        if (etDob.getText().toString().equalsIgnoreCase("")) {
            input_layout_dob.setError(getString(R.string.err_msg_dob));
            requestFocus(etDob);
            return false;
        } else {
            input_layout_dob.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();
        if(email.isEmpty())
            return  true;
        if (email.isEmpty() || !isValidEmail(email)) {
            input_layout_email.setError(getString(R.string.err_msg_email));
            requestFocus(etEmail);
            return false;
        } else {
            input_layout_email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty()) {
            input_layout_password.setError(getString(R.string.err_msg_password));
            requestFocus(etPassword);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCommCategory() {
        if (acs_commn_cat.getSelectedItemPosition() == 0) {
            Toast.makeText(SignUpActivity.this, getString(R.string.err_msg_comm_cat), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateCommType() {
        if (acs_commn_type.getSelectedItemPosition() == 0) {
            Toast.makeText(SignUpActivity.this, getString(R.string.err_msg_comm_type), Toast.LENGTH_SHORT).show();
            return false;
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

    private void showImagePickerDialog() {


        CharSequence[] items = {"From Camera", "From Gallery"};
        CharSequence[] items_add = {"From Camera", "From Gallery", "Remove photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        builder.setItems(is_image_chosen ? items_add : items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                switch (which) {
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        chooseImage();
                        break;

                    case 2:
                        is_image_chosen = false;
                        imageChooserManager.clearOldFiles();
                        ivProfile.setImageResource(R.drawable.no_image_user);
                        break;
                }

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            progressBar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            progressBar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }

            imageChooserManager.submit(requestCode, data);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    // Should be called if for some reason the ImageChooserManager is null (Due
    // to destroying of activity for low memory situations)
    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    private File getResizeImage(String path) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(path);
        Bitmap out = Bitmap.createScaledBitmap(b, 250, 250, false);
        File file = new File(dir, "resize_profile.png");
        FileOutputStream fOut;
        try{
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void onPermissionGranted() {
        showImagePickerDialog();
    }

    @Override
    public void onCallPermissionGranted() {

    }


}

