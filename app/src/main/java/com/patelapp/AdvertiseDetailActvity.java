package com.patelapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityAdvertise;
import com.patelapp.Interfaces.OnPermissionListener;

/**
 * Created by Android on 1/27/2016.
 */
public class AdvertiseDetailActvity extends BaseActivity implements OnPermissionListener,View.OnClickListener
{

    FloatingActionButton fabShareAdverise;
    ImageView ivFullAdvetrise;
    ProgressBar progressBar;
    String image_url;
    Bitmap bitmap_full_image;
    String mobile;
    TextView tvTitle,tvContactPerson,tvContactMobile,tvDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_full);
        initViews();
        setPermissionListeners(this);

        if(getIntent().getExtras() != null){
//            image_url = getIntent().getExtras().getString("image_url");
            String a_id = getIntent().getExtras().getString("a_id");
            EntityAdvertise advertise = DataAccessManager.getAdvertiseItem(a_id);
            image_url = advertise.getImg_large();
            mobile = advertise.getMobile();
            setImage();
            fillData(advertise);
        }
    }

    private void initViews(){
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvContactPerson = (TextView)findViewById(R.id.tvContactPerson);
        tvContactMobile = (TextView)findViewById(R.id.tvContactMobile);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        fabShareAdverise = (FloatingActionButton)findViewById(R.id.fabShareAdverise);
        ivFullAdvetrise = (ImageView)findViewById(R.id.ivFullAdvetrise);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        tvContactMobile.setOnClickListener(this);
        fabShareAdverise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap_full_image != null) {
                    if (verifyStoragePermissions(AdvertiseDetailActvity.this))
                        StaticMethodUtility.shareBitmap(AdvertiseDetailActvity.this, bitmap_full_image);
                    else
                        requestExternalStoragePermission();

                }
                else
                    Toast.makeText(AdvertiseDetailActvity.this,"No image to share",Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void fillData(EntityAdvertise advertise){

        tvTitle.setText(advertise.getTitle());
        tvDesc.setText(advertise.getA_desc());
        tvContactPerson.setText(advertise.getContactperson());
        tvContactMobile.setText(advertise.getMobile());


    }
    private void setImage(){

        progressBar.setVisibility(View.VISIBLE);

        Glide.with(AdvertiseDetailActvity.this)
                .load(Const.ImagePath+image_url)
                .asBitmap()
                .placeholder(R.drawable.no_image_user)
                .into(new BitmapImageViewTarget(ivFullAdvetrise)
                      {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              super.onResourceReady(resource, glideAnimation);
                              progressBar.setVisibility(View.GONE);
                              bitmap_full_image = resource;
                          }

                          @Override
                          public void onLoadFailed(Exception e, Drawable errorDrawable) {
                              super.onLoadFailed(e, errorDrawable);
                              progressBar.setVisibility(View.GONE);
                              Toast.makeText(AdvertiseDetailActvity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                          }
                      }
                );
    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile));
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



    @Override
    public void onPermissionGranted() {
        StaticMethodUtility.shareBitmap(AdvertiseDetailActvity.this, bitmap_full_image);
    }

    @Override
    public void onCallPermissionGranted() {
        makeCall();

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.tvContactMobile:
             if (verifyCallPhonePermissions(AdvertiseDetailActvity.this)) {
                 makeCall();
             } else {
                 requestForCallPhonePermission();
             }
             break;
     }
    }
}
