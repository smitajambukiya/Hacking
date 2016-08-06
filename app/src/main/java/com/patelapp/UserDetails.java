package com.patelapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.Entity.EntityUser;
import com.patelapp.Interfaces.OnPermissionListener;

/**
 * Created by AndroidDevloper on 2/19/2016.
 */
public class UserDetails extends  BaseActivity implements OnPermissionListener, View.OnClickListener {


    //Declaration

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivUser;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView imgUser;
    private TextView tvName;
    private TextView tvCategory;
    private ImageView imgEmail;
    private TextView tvEmail;
    private TextView tvDob;
    private ImageView imgCall;
    private TextView tvMobile,tvEducation,tvProfession;

    private FloatingActionButton fabShare;
    private LinearLayout llContacts;

    EntityUser mEntityUser;
    Bitmap bitmap_full_image = null;
    String TYPE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setPermissionListeners(this);
        findViews();
        fillViews();
    }

    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivUser = (ImageView) findViewById(R.id.ivUser);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        tvName = (TextView) findViewById(R.id.tvName);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        imgEmail = (ImageView) findViewById(R.id.imgEmail);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDob = (TextView) findViewById(R.id.tvDob);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        llContacts = (LinearLayout)findViewById(R.id.llContacts);
        tvEducation = (TextView)findViewById(R.id.tvEducation);
        tvProfession = (TextView)findViewById(R.id.tvProfession);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgCall.setOnClickListener(this);
        fabShare.setOnClickListener(this);
    }

    private void fillViews() {
        if (getIntent().getExtras() != null) {
            String r_id = getIntent().getExtras().getString(GlobalData.KEY_ID);
            mEntityUser = DataAccessManager.getUserDetail(r_id);
            if (mEntityUser == null)
                return;
            tvName.setText(mEntityUser.getFname() + " " + mEntityUser.getLname());


            if (mEntityUser.getCommunity_category() != null) {
                String[]arr_catgory = getResources().getStringArray(R.array.community_categaory);
                String cat = mEntityUser.getCommunity_category().toString();
                int indext_cat = Integer.parseInt(cat);

                TYPE = arr_catgory[indext_cat +1];
               /* if (mEntityUser.getCommunity_category().equalsIgnoreCase("1")) {
                    TYPE = "Kadva";
                } else {
                    TYPE = "Laiwa";
                }*/
            }
            tvCategory.setText(TYPE + " " + mEntityUser.getCommunity_type());
            tvEmail.setText(mEntityUser.getEmail());

            String date = mEntityUser.getDob();
            if(!date.equalsIgnoreCase(""))
                tvDob.setText(StaticMethodUtility.getDisplayDate(date));
            tvMobile.setText(mEntityUser.getMobile());

            String profession = mEntityUser.getProfession().equalsIgnoreCase("") ? "Not available" : mEntityUser.getProfession();
            tvProfession.setText(profession);

            String education = mEntityUser.getEducation().equalsIgnoreCase("") ? "Not available" : mEntityUser.getEducation();
            tvEducation.setText(education);
            collapsingToolbar.setTitle(mEntityUser.getFname());
            Glide.with(this).load(Const.ImagePath + mEntityUser.getImage()).centerCrop().into(ivUser);

            boolean visiblity = mEntityUser.getVisiblity().equalsIgnoreCase("1")? true :false;

            llContacts.setVisibility(visiblity ? View.VISIBLE : View.GONE);
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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgCall:
                if (verifyCallPhonePermissions(UserDetails.this)) {
                    makeCall();
                } else {
                    requestForCallPhonePermission();
                }

                break;
            case R.id.fabShare:
                shareImage();
                break;
        }

    }

    private void makeCall(){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mEntityUser.getMobile()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestForCallPhonePermission();
            return;
        }
        startActivity(callIntent);

    }

    private void shareImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bitmap_full_image = StaticMethodUtility.getScreenShot(getWindow().getDecorView());
                if (bitmap_full_image != null) {
                    if (verifyStoragePermissions(UserDetails.this))
                        StaticMethodUtility.shareBitmap(UserDetails.this, bitmap_full_image);
                    else
                        requestExternalStoragePermission();
                } else
                    Toast.makeText(UserDetails.this, "No image to share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPermissionGranted() {
        StaticMethodUtility.shareBitmap(UserDetails.this, bitmap_full_image);
    }

    @Override
    public void onCallPermissionGranted() {
        makeCall();
    }
}
