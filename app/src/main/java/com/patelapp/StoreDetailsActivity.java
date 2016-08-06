package com.patelapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.patelapp.Custom.AlertDialogUtility;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.TouchImageView;
import com.patelapp.Entity.Cheeses;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.server.Communication;

import org.json.JSONObject;

/**
 * Created by Android on 1/5/2016.
 */
public class StoreDetailsActivity extends BaseActivity implements OnPermissionListener, View.OnClickListener {
    private LinearLayout llIvStore1,llIvStore2,llIvStore3,llTouchZoom;
    private TextView tvItemTitle, tvPrice, tvQty, tvItemDesc, tvContactName, tvContactNumber, tvAddress,tvCategory;
    private ImageView ivStore1, ivStore2, ivStore3;
    private TouchImageView ivTouchZoom;
    private String item_id;
    ImageView ivCall;
    private boolean is_full_display = false;
    private String[] image_array = new String[3];
    EntityStoreItem entityStoreItem;
    Typeface face;
    boolean canRemoveItem;
    Button btnRemoveItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        item_id = getIntent().getExtras().getString(GlobalData.KEY_ID);
        canRemoveItem = getIntent().getBooleanExtra(GlobalData.KEY_CAN_REMOVE,false);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        setPermissionListeners(this);
        fillViews();
        setupImages();
    }

    private void initViews() {
        ivStore1 = (ImageView) findViewById(R.id.ivStore1);
        ivStore2 = (ImageView) findViewById(R.id.ivStore2);
        ivStore3 = (ImageView) findViewById(R.id.ivStore3);
        ivTouchZoom = (TouchImageView)findViewById(R.id.ivTouchZoom);
        llTouchZoom = (LinearLayout)findViewById(R.id.llTouchZoom);

        llIvStore1 = (LinearLayout)findViewById(R.id.llIvStore1);
        llIvStore2 = (LinearLayout)findViewById(R.id.llIvStore2);
        llIvStore3 = (LinearLayout)findViewById(R.id.llIvStore3);

        tvItemTitle = (TextView) findViewById(R.id.tvItemTitle);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvQty = (TextView) findViewById(R.id.tvQty);
        tvItemDesc = (TextView) findViewById(R.id.tvItemDesc);
        tvContactName = (TextView) findViewById(R.id.tvContactName);
        tvContactNumber = (TextView) findViewById(R.id.tvContactNumber);
        tvCategory = (TextView)findViewById(R.id.tvCategory);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        ivCall = (ImageView) findViewById(R.id.ivCall);

        btnRemoveItem = (Button)findViewById(R.id.btnRemoveItem);

        btnRemoveItem.setVisibility(canRemoveItem ? View.VISIBLE :View.GONE);

        btnRemoveItem.setOnClickListener(this);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/"+getString(R.string.font_guj));

    }


    private void fillViews() {

        entityStoreItem = DataAccessManager.getStoreRecord(item_id);



        tvItemTitle.setText(entityStoreItem.getItem_name());
        tvItemDesc.setText(entityStoreItem.getItem_desc());
        tvPrice.setText(entityStoreItem.getPrice());
        tvQty.setText(entityStoreItem.getQty());
        tvCategory.setText(entityStoreItem.getCategory());
        tvAddress.setText(entityStoreItem.getAddress());
        tvContactName.setText(entityStoreItem.getPerson_name());
        tvContactNumber.setText(entityStoreItem.getContact());

        tvItemTitle.setTypeface(face);
        tvItemDesc.setTypeface(face);
        tvAddress.setTypeface(face);


        ivStore1.setOnClickListener(this);
        ivStore2.setOnClickListener(this);
        ivStore3.setOnClickListener(this);

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyCallPhonePermissions(StoreDetailsActivity.this)) {
                    makeCall();
                } else {
                    requestForCallPhonePermission();
                }
            }
        });
        /*tvContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + entityStoreItem.getContact()));
                startActivity(callIntent);
            }
        });
*/
    }

    private void setupImages(){

//        image_array[0] = Const.ImagePath + entityStoreItem.getUrl1();
//        image_array[1] = Const.ImagePath + entityStoreItem.getUrl2();
//        image_array[2] = Const.ImagePath + entityStoreItem.getUrl3();

        if(!entityStoreItem.getUrl1().equalsIgnoreCase(""))
            Glide.with(StoreDetailsActivity.this).load(Const.ImagePath + entityStoreItem.getUrl1()).centerCrop().into(ivStore1);
        else
            llIvStore1.setVisibility(View.GONE);
        if(!entityStoreItem.getUrl2().equalsIgnoreCase(""))
            Glide.with(StoreDetailsActivity.this).load(Const.ImagePath + entityStoreItem.getUrl2()).centerCrop().into(ivStore2);
        else
            llIvStore2.setVisibility(View.GONE);
        if(!entityStoreItem.getUrl3().equalsIgnoreCase(""))
            Glide.with(StoreDetailsActivity.this).load(Const.ImagePath + entityStoreItem.getUrl3()).centerCrop().into(ivStore3);
        else
            llIvStore3.setVisibility(View.GONE);


    }

    private void makeCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + entityStoreItem.getContact()));
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


    private void zoomImage(String imagepath){
        is_full_display = true;
        llTouchZoom.setVisibility(View.VISIBLE);
        Glide.with(StoreDetailsActivity.this).load(imagepath).centerCrop().into(ivTouchZoom);

    }

    @Override
    public void onBackPressed() {
        if(is_full_display)
        {
            is_full_display = false;
            llTouchZoom.setVisibility(View.GONE);
            return;
        }else {
            super.onBackPressed();
        }
    }
/*    private void gotoZoomActivity(int pos){
        Intent intent = new Intent(StoreDetailsActivity.this,ZoomImageActivity.class);
        intent.putExtra("array",image_array);
        intent.putExtra("position",pos);
        startActivity(intent);


    }*/
    /*private void loadBackdrop() {

        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }*/

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
            case R.id.ivStore1:
                zoomImage(Const.ImagePath + entityStoreItem.getUrl1());
                break;

            case R.id.ivStore2:
                zoomImage(Const.ImagePath + entityStoreItem.getUrl2());
                break;

            case R.id.ivStore3:
                zoomImage(Const.ImagePath + entityStoreItem.getUrl3());
                break;

            case R.id.btnRemoveItem:

                AlertDialogUtility.showAlertDialog(StoreDetailsActivity.this, getString(R.string.remove_item), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestRemoveItem(item_id);

                    }
                }).show();

                break;

        }
    }

    private void requestRemoveItem(final String item_id){

        Communication.getInstance().callForRemoveStoreItem(item_id).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {

                int status = response.optInt("response");
                String message = response.optString("success");
                if(status == 1) {
                    Toast.makeText(StoreDetailsActivity.this,"Your item has been removed successfully",Toast.LENGTH_SHORT).show();
                    DataAccessManager.removeStore(item_id);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },500);

                }else{
                    Toast.makeText(StoreDetailsActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(String mError) {

            }
        }).showProgress(StoreDetailsActivity.this);

    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onCallPermissionGranted() {
        makeCall();
    }
}

