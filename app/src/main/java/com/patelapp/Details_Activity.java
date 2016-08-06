package com.patelapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.AlertDialogUtility;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.Cheeses;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONObject;

/**
 * Created by Android on 1/5/2016.
 */
public class Details_Activity extends BaseActivity implements OnPermissionListener, View.OnClickListener {

    ImageView ivPatelNews;
    FloatingActionButton fabShare;
    TextView tvSource, tvDate, tvTitle, tvDesc, tvLike;
    LinearLayout llLike;
    ImageView ivLike;
    String feed_id;
    Bitmap bitmap_full_image = null;
    EntityNewsFeed mEntityNewsFeed;
    int like_count;
    Typeface face;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setPermissionListeners(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Details");


        initViews();
        fillViews();

    }

    private void initViews() {
        fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        ivPatelNews = (ImageView) findViewById(R.id.ivPatelNews);
        ivLike = (ImageView) findViewById(R.id.ivLike);
        llLike = (LinearLayout) findViewById(R.id.llLike);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvSource = (TextView) findViewById(R.id.tvSource);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLike = (TextView) findViewById(R.id.tvLike);

        face = Typeface.createFromAsset(getAssets(),
                "fonts/"+getString(R.string.font_guj));
        tvLike.setTypeface(face);
        tvTitle.setTypeface(face);
        tvDesc.setTypeface(face);

        if(MySharedPrefs.getInstance().getRegisterId().equalsIgnoreCase("")){
            llLike.setVisibility(View.GONE);
        }
        llLike.setOnClickListener(this);
        fabShare.setOnClickListener(this);

    }


    private void fillViews() {
        if (getIntent().getExtras() != null) {
            feed_id = getIntent().getExtras().getString(GlobalData.KEY_ID);
            mEntityNewsFeed = DataAccessManager.getPatelFeed(feed_id);
            if (mEntityNewsFeed == null)
                return;
            tvSource.setText("Source : " + mEntityNewsFeed.getPost_by());
            tvTitle.setText(mEntityNewsFeed.getTitle());
            String Desc = mEntityNewsFeed.getDesc().toString().replaceAll("^\\s+", "");
            tvDesc.setText(Desc);
            tvDate.setText("Posted on: "+StaticMethodUtility.getDisplayDate(mEntityNewsFeed.getCreated()));
            tvLike.setText(mEntityNewsFeed.getView());
            like_count = Integer.parseInt(mEntityNewsFeed.getView());
            int selected = Integer.parseInt(mEntityNewsFeed.getIs_like());
            ivLike.setSelected(selected == 1 ? true : false);
            Glide.with(this).load(Const.ImagePath + mEntityNewsFeed.getImg_url()).centerCrop().into(ivPatelNews);
        }

    }


    private void loadBackdrop() {

        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
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
                if (!ivLike.isSelected()) {
                    if (NetworkReachability.isNetworkAvailable()) {
                        requestLikePost();
                    } else {

                    }

                } else {
                    Toast.makeText(Details_Activity.this, getString(R.string.already_liked), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.fabShare:
                shareImage();
                break;
        }
    }

    private void shareImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bitmap_full_image = StaticMethodUtility.getScreenShot(getWindow().getDecorView());
                if (bitmap_full_image != null) {
                    if (verifyStoragePermissions(Details_Activity.this))
                        StaticMethodUtility.shareBitmap(Details_Activity.this, bitmap_full_image);
                    else
                        requestExternalStoragePermission();
                } else
                    Toast.makeText(Details_Activity.this, "No image to share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestLikePost() {
        if(!NetworkReachability.isNetworkAvailable())
        {
            StaticMethodUtility.showNetworkToast(Details_Activity.this);
            return;
        }
        Communication.getInstance().callForAddLike(feed_id, MySharedPrefs.getInstance().getRegisterId()).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(Details_Activity.this, getString(R.string.liked_post), Toast.LENGTH_SHORT).show();
                    DataAccessManager.updateFeedLike(feed_id);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivLike.setSelected(true);
                            like_count = like_count + 1;
                            tvLike.setText("" + like_count);
                        }
                    });
                }
            }

            @Override
            public void failure(String mError) {

            }
        }).showProgress(Details_Activity.this);

    }

    @Override
    public void onPermissionGranted() {
        StaticMethodUtility.shareBitmap(Details_Activity.this, bitmap_full_image);

    }

    @Override
    public void onCallPermissionGranted() {
    }


}

