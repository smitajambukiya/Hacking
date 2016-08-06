package com.patelapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.patelapp.Custom.CircleImageView;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityLoginUser;
import com.patelapp.Entity.EntityUser;
import com.patelapp.Fragment.DatePickerFragment;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.server.Communication;
import com.patelapp.server.DownloadImageService;
import com.patelapp.server.NetworkReachability;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AndroidDevloper on 3/3/2016.
 */
public class EditProfileActivity extends BaseActivity implements ImageChooserListener, View.OnClickListener, OnPermissionListener {

    private CircleImageView ivProfile;
    private ProgressBar progressBar;
    private TextInputLayout inputLayoutFirst,inputLayoutLast,inputLayoutDob,inputLayoutEmail;
    private EditText etFirstName,etLastName,etDob,etEmail;
    private CheckBox cbContact;
    private Spinner acsCommnCat,acsEducation,acsCommnType,acsProfession;
    private Button btnUpdate;

    private ImageChooserManager imageChooserManager;
    private String filePath;
    private int contact_flag = 0;

    private int chooserType;
    private boolean is_image_chosen = false;
    private String originalFilePath;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Profile");
        initViews();
        setSpinnerAdapter();
        fillViews();
        setPermissionListeners(this);
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-03-03 00:25:56 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void initViews() {
        ivProfile = (CircleImageView) findViewById(R.id.ivProfile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputLayoutFirst = (TextInputLayout) findViewById(R.id.input_layout_first);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        inputLayoutLast = (TextInputLayout) findViewById(R.id.input_layout_last);
        etLastName = (EditText) findViewById(R.id.etLastName);
        inputLayoutDob = (TextInputLayout) findViewById(R.id.input_layout_dob);
        etDob = (EditText) findViewById(R.id.etDob);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        etEmail = (EditText) findViewById(R.id.etEmail);
        cbContact = (CheckBox) findViewById(R.id.cbContact);
        acsCommnCat = (Spinner) findViewById(R.id.acs_commn_cat);
        acsCommnType = (Spinner) findViewById(R.id.acs_commn_type);
        acsEducation = (Spinner)findViewById(R.id.acsEducation);
        acsProfession = (Spinner)findViewById(R.id.acsProfession);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(this);
        ivProfile.setOnClickListener(this);



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


    private void setSpinnerAdapter(){
        ArrayAdapter<String> arrayCategoryAdapter = new ArrayAdapter<String>(EditProfileActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.community_categaory));
        acsCommnCat.setAdapter(arrayCategoryAdapter);
        arrayCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);


        ArrayAdapter<String> arrayTypeAdapter = new ArrayAdapter<String>(EditProfileActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.community_type));
        acsCommnType.setAdapter(arrayTypeAdapter);
        arrayTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);



        ArrayAdapter<String> arrayEducationAdapter = new ArrayAdapter<String>(EditProfileActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.Education));
        acsEducation.setAdapter(arrayEducationAdapter);
        arrayEducationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);



        ArrayAdapter<String> arrayProfessionAdapter = new ArrayAdapter<String>(EditProfileActivity.this,R.layout.spinner_item,getResources().getStringArray(R.array.Profession));
        acsProfession.setAdapter(arrayProfessionAdapter);
        arrayProfessionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ivProfile:
                if (verifyStoragePermissions(EditProfileActivity.this))
                    showImagePickerDialog();
                else
                    requestExternalStoragePermission();
                break;
            case R.id.btnUpdate:

                if (validateFirstName() && validateLastName() && validateDob() && validateEmail()  && validateCommCategory() && validateCommType() && validateEducation() && validateProfession()) {
                    sumbmitForm();
                }

                break;
        }
    }
    private boolean validateFirstName() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            inputLayoutFirst.setError(getString(R.string.err_msg_fname));
            requestFocus(etFirstName);
            return false;
        } else {
            inputLayoutFirst.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateLastName() {
        if (etLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLast.setError(getString(R.string.err_msg_lname));
            requestFocus(etLastName);
            return false;
        } else {
            inputLayoutLast.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDob() {
        if (etDob.getText().toString().equalsIgnoreCase("")) {
            inputLayoutDob.setError(getString(R.string.err_msg_dob));
            requestFocus(etDob);
            return false;
        } else {
            inputLayoutDob.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();
        if(email.isEmpty())
            return  true;
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(etEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateCommCategory() {
        if (acsCommnCat.getSelectedItemPosition() == 0) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.err_msg_comm_cat), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateCommType() {
        if (acsCommnType.getSelectedItemPosition() == 0) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.err_msg_comm_type), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateEducation() {
        if (acsEducation.getSelectedItemPosition() == 0) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.err_msg_education), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateProfession() {
        if (acsProfession.getSelectedItemPosition() == 0) {
            Toast.makeText(EditProfileActivity.this, getString(R.string.err_msg_profession), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private  void fillViews(){
        EntityLoginUser entityLoginUser =  DataAccessManager.getLoginUser();
        if(entityLoginUser != null){
            etFirstName.setText(entityLoginUser.getFname());
            etLastName.setText(entityLoginUser.getLname());
            String display_date = StaticMethodUtility.getDisplayDate(entityLoginUser.getDob());
            etDob.setText(display_date);
            etEmail.setText(entityLoginUser.getEmail());
           // setProfileImage(Const.ImagePath+entityLoginUser.getImage());

            byte[] bitmapdata = DataAccessManager.getLoginUser().getImagebyte();
            if(bitmapdata != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                ivProfile.setImageBitmap(bitmap);
            }

            setCommunityType(entityLoginUser.getCommunity_type());
            setCommunityCategory(entityLoginUser.getCommunity_category());
            setEducation(entityLoginUser.getEducation());
            setProfession(entityLoginUser.getProfession());


            contact_flag = Integer.parseInt(entityLoginUser.getVisiblity());

            cbContact.setChecked(contact_flag == 1 ? true : false);


        }

    }


    private void setProfileImage(String image_path){
        progressBar.setVisibility(View.VISIBLE);


        Glide.with(EditProfileActivity.this)
                .load(image_path)
                .asBitmap()
                .into(new BitmapImageViewTarget(ivProfile)
                      {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              super.onResourceReady(resource, glideAnimation);
                              progressBar.setVisibility(View.GONE);
                          }

                          @Override
                          public void onLoadFailed(Exception e, Drawable errorDrawable) {
                              super.onLoadFailed(e, errorDrawable);
                              progressBar.setVisibility(View.GONE);
                          }
                      }
                );


    }
    private void setCommunityType(String comm_type){
        if(TextUtils.isEmpty(comm_type))
        {
            acsCommnType.setSelection(0);
            return;
        }
        String[] arr_comm = getResources().getStringArray(R.array.community_type);
        for(int i = 0; i < arr_comm.length; i++){
            if(arr_comm[i].equalsIgnoreCase(comm_type)){
                acsCommnType.setSelection(i);
                return;
            }
        }
    }

    private void setCommunityCategory(String comm_cat){
        try {
            int value = Integer.parseInt(comm_cat);

            if (TextUtils.isEmpty(comm_cat)) {
                acsCommnCat.setSelection(0);
                return;
            }
            if (value == 0) {
                acsCommnCat.setSelection(1);
            } else {
                acsCommnCat.setSelection(2);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private void setEducation(String education){
        if(TextUtils.isEmpty(education)){
            acsEducation.setSelection(0);
            return;
        }
        String[] arr_education = getResources().getStringArray(R.array.Education);
        for(int i = 0; i < arr_education.length; i++){
            if(arr_education[i].equalsIgnoreCase(education)){
                acsEducation.setSelection(i);
                return;
            }
        }
    }






    private void setProfession(String profession){
        if(TextUtils.isEmpty(profession)){
            acsProfession.setSelection(0);
            return;
        }
        String[] arr_prof = getResources().getStringArray(R.array.Profession);
        for(int i = 0; i < arr_prof.length; i++){
            if(arr_prof[i].equalsIgnoreCase(profession)){
                acsProfession.setSelection(i);
                return;
            }
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
        Bitmap out = Bitmap.createScaledBitmap(b, b.getWidth()/2, b.getWidth()/2, false);
        File file = new File(dir, "resize_profile.png");
        FileOutputStream fOut;
        try {
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
    private void sumbmitForm(){
        File image_file = null;
        File resize_file = null;
        EntityUser entityUser = new EntityUser();
        entityUser.setFname(etFirstName.getText().toString());
        entityUser.setLname(etLastName.getText().toString());
        entityUser.setEmail(etEmail.getText().toString());
        entityUser.setDob(etDob.getText().toString());
        entityUser.setCommunity_type(acsCommnType.getSelectedItem().toString());
        entityUser.setCommunity_category(String.valueOf(acsCommnCat.getSelectedItemPosition()-1));
        entityUser.setEducation(acsEducation.getSelectedItem().toString());
        entityUser.setProfession(acsProfession.getSelectedItem().toString());
        entityUser.setVisiblity(String.valueOf(contact_flag));
        if(!TextUtils.isEmpty(originalFilePath)) {
            image_file = new File(originalFilePath);
            resize_file = getResizeImage(originalFilePath);
        }
        requestUpdateProfile(entityUser,resize_file);
    }
    private void requestUpdateProfile(EntityUser entityUser, File resize_file) {
        if(!NetworkReachability.isNetworkAvailable())
        {
            StaticMethodUtility.showNetworkToast(EditProfileActivity.this);
            return;
        }
        Communication.getInstance().callForUpdateProfile(entityUser, resize_file).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                {
                    int status = response.optInt("response");
                    String message = response.optString("success");
                    if (status == 1) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject user_json = response.optJSONObject("data");
                            DataAccessManager.insertLoginUser(user_json);
                            String image_path = DataAccessManager.getLoginUser().getImage();
                            if (!TextUtils.isEmpty(image_path)) {
                                Intent intent = new Intent(EditProfileActivity.this, DownloadImageService.class);
                                intent.putExtra("image_url", Const.ImagePath + image_path);
                                startService(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }else{
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void failure(String mError) {
                Toast.makeText(EditProfileActivity.this, mError, Toast.LENGTH_SHORT).show();
                LogM.d(mError);
            }
        }).showProgress(EditProfileActivity.this);
    }




}
