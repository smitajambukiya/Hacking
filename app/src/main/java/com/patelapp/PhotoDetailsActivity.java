package com.patelapp;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Custom.ZoomOutPageTransformer;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.Entity.ImageItem;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.adapter.PhotoPagerAdapter;

import java.util.ArrayList;

public class PhotoDetailsActivity extends BaseActivity implements OnPermissionListener {


    Toolbar toolbar;
    ViewPager vpPhotos;
    Bitmap bitmap_full_image = null;
    //  InkPageIndicator indicator;
    PhotoPagerAdapter adapter;
    int selected_pos = -1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        findViews();
        setSupportActionBar(toolbar);
        toolbar.setTitle("Photo Details");
        setPermissionListeners(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            selected_pos = getIntent().getExtras().getInt("key_position");
        }
        setData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void setData() {
        ArrayList<EntityGallery> alPhotos = new ArrayList<EntityGallery>();
        alPhotos = DataAccessManager.getAllGallery(Const.GALLERY_TYPE_IMAGE);
        adapter = new PhotoPagerAdapter(PhotoDetailsActivity.this, alPhotos);
        adapter.setListener(new PhotoPagerAdapter.OnShareListener() {
            @Override
            public void onShareClick(int pos) {
                shareImage();
            }
        });

        vpPhotos.setAdapter(adapter);
        if (selected_pos != -1)
            vpPhotos.setCurrentItem(selected_pos);

        vpPhotos.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        vpPhotos = (ViewPager) findViewById(R.id.vpPhotos);
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
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    private void shareImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bitmap_full_image = StaticMethodUtility.getScreenShot(getWindow().getDecorView());
                if (bitmap_full_image != null) {
                    if (verifyStoragePermissions(PhotoDetailsActivity.this))
                        StaticMethodUtility.shareBitmap(PhotoDetailsActivity.this, bitmap_full_image);
                    else
                        requestExternalStoragePermission();
                } else
                    Toast.makeText(PhotoDetailsActivity.this, "No image to share", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onPermissionGranted() {
        StaticMethodUtility.shareBitmap(PhotoDetailsActivity.this, bitmap_full_image);
    }

    @Override
    public void onCallPermissionGranted() {

    }
}
