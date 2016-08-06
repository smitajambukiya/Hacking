package com.patelapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityDirectory;
import com.patelapp.Entity.EntityUser;
import com.patelapp.Interfaces.OnPermissionListener;

/**
 * Created by AndroidDevloper on 2/19/2016.
 */
public class DirectoryDetails extends BaseActivity implements OnPermissionListener, View.OnClickListener {


    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivDirectory;
    private Toolbar toolbar;
    private TextView tvTitle;
    private TextView tvType;
    private TextView tvCity;
    private TextView tvMobile;
    private ImageView ivCall;
    private TextView tvDesc;
    private FloatingActionButton fabShare;
    EntityDirectory mEntityDir;
    Bitmap bitmap_full_image = null;

    Typeface face;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_details);

        setPermissionListeners(this);

        findViews();

        fillViews();


    }

    private void fillViews() {
        face = Typeface.createFromAsset(getAssets(),
                "fonts/"+getString(R.string.font_guj));

        if (getIntent().getExtras() != null) {
            String r_id = getIntent().getExtras().getString(GlobalData.KEY_ID);
            mEntityDir = DataAccessManager.getDirectoryDetail(r_id);

            tvTitle.setText(mEntityDir.getTitle());
            tvType.setText(mEntityDir.getType());
            tvCity.setText(mEntityDir.getCity());
            tvMobile.setText(mEntityDir.getMobile());
//            ivCall.setText(mEntityDir.getTitle());
            tvDesc.setText(mEntityDir.getAddress());
            tvTitle.setTypeface(face);
            tvDesc.setTypeface(face);

            collapsingToolbar.setTitle(mEntityDir.getTitle());

            Glide.with(this).load(Const.ImagePath + mEntityDir.getImage()).centerCrop().into(ivDirectory);

        }
    }


    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivDirectory = (ImageView) findViewById(R.id.ivDirectory);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvType = (TextView) findViewById(R.id.tvType);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        ivCall = (ImageView) findViewById(R.id.ivCall);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        fabShare = (FloatingActionButton) findViewById(R.id.fabShare);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabShare.setOnClickListener(this);

        ivCall.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.llLike:
                break;
            case R.id.fabShare:
                shareImage();
                break;

            case R.id.ivCall:
                makeCall();
                break;
        }
    }

    private void makeCall(){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mEntityDir.getMobile()));
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
                    if (verifyStoragePermissions(DirectoryDetails.this))
                        StaticMethodUtility.shareBitmap(DirectoryDetails.this, bitmap_full_image);
                    else
                        requestExternalStoragePermission();
                } else
                    Toast.makeText(DirectoryDetails.this, "No image to share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPermissionGranted() {
        StaticMethodUtility.shareBitmap(DirectoryDetails.this, bitmap_full_image);
    }

    @Override
    public void onCallPermissionGranted() {
        makeCall();
    }
}
