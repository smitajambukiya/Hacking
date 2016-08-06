package com.patelapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.R;
import com.patelapp.server.Communication;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android on 1/20/2016.
 */
public class AddItemActivity extends BaseActivity implements View.OnClickListener, ImageChooserListener, OnPermissionListener {

    private ImageView ivStoreThird, ivStoreSecond, ivStoreFirst;
    private Button btnPost;
    private EditText etAddress, etDesc, etQty, etPrice, etItemName;
    private Spinner spinnerCategory;

    private final String TAG = "tag";

    private ImageChooserManager imageChooserManager;

    private String filePath;

    private File image_file1, image_file2, image_file3;

//    ArrayList<File> alImageFile = new ArrayList<File>();

    HashMap<Integer,File>hmSelectedImages = new HashMap<Integer, File>();

    private int chooserType;
    private boolean[] is_image_chosen = new boolean[]{false,false,false};

    private String originalFilePath;
    private int image_clicked_pos = -1;
    ArrayAdapter adapter_commn_type;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initViews();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Post Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setPermissionListeners(this);

    }

    private void initViews() {
        btnPost = (Button) findViewById(R.id.btnPost);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etQty = (EditText) findViewById(R.id.etQty);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etDesc = (EditText) findViewById(R.id.etDesc);
        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        ivStoreFirst = (ImageView) findViewById(R.id.ivStoreFirst);
        ivStoreSecond = (ImageView) findViewById(R.id.ivStoreSecond);
        ivStoreThird = (ImageView) findViewById(R.id.ivStoreThird);
        ivStoreFirst.setOnClickListener(this);
        ivStoreSecond.setOnClickListener(this);
        ivStoreThird.setOnClickListener(this);

        adapter_commn_type = new ArrayAdapter(AddItemActivity.this, R.layout.simple_list_item_single_choice,
                R.id.tvTitle, getResources().getStringArray(R.array.category));
        spinnerCategory.setAdapter(adapter_commn_type);

        btnPost.setOnClickListener(this);
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
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivStoreFirst:
                if (verifyStoragePermissions(AddItemActivity.this)) {
                    image_clicked_pos = 1;
                    showImagePickerDialog();
                } else {
                    requestExternalStoragePermission();
                }
                break;

            case R.id.ivStoreSecond:
                if (verifyStoragePermissions(AddItemActivity.this)) {
                    image_clicked_pos = 2;
                    showImagePickerDialog();
                }else{
                    requestExternalStoragePermission();
                }
                break;

            case R.id.ivStoreThird:
                if (verifyStoragePermissions(AddItemActivity.this)) {
                    image_clicked_pos = 3;
                    showImagePickerDialog();
                }else
                    break;


            case R.id.btnPost:
                if (validateItemImage() && validateCategory() && validateItemName() && validateItemPrice() && validateItemQty() && validateItemDesc() && validateItemAddress()) {

                    requestAddStoreItems();
                }
                break;
        }
    }

    private boolean validateItemImage() {
      //  if (alImageFile.size() == 0) {
        if(hmSelectedImages.isEmpty()){
            Toast.makeText(AddItemActivity.this, getString(R.string.select_item_image), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateCategory() {
        if (spinnerCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.choose_categaory), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateItemName() {
        if (etItemName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.enter_item_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateItemPrice() {
        if (etPrice.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.enter_item_price), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateItemQty() {
        if (etQty.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.enter_item_qty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateItemDesc() {
        if (etDesc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.enter_item_desc), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateItemAddress() {
        if (etAddress.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(AddItemActivity.this, getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void showImagePickerDialog() {


        CharSequence[] items = {"From Camera", "From Gallery"};
        CharSequence[] items_add = {"From Camera", "From Gallery", "Remove photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        builder.setItems(is_image_chosen[image_clicked_pos -1] ? items_add : items, new DialogInterface.OnClickListener() {
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
                        is_image_chosen[image_clicked_pos-1] = false;
                        imageChooserManager.clearOldFiles();
                        clearSelectedImageViews();
                        break;
                }

            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void clearSelectedImageViews() {
        switch (image_clicked_pos) {
            case 1:
                ivStoreFirst.setImageResource(R.drawable.selector);
                image_file1 = null;
                //alImageFile.remove(0);
                hmSelectedImages.remove(0);


                break;
            case 2:
                image_file2 = null;
  //              alImageFile.remove(1);
                hmSelectedImages.remove(1);
                ivStoreSecond.setImageResource(R.drawable.selector);
                break;

            case 3:
                image_file3 = null;
               // alImageFile.remove(2);
                hmSelectedImages.remove(2);
                ivStoreThird.setImageResource(R.drawable.selector);
                break;
        }
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
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
    public void onImageChosen(final ChosenImage chosenImage) {

        runOnUiThread(new Runnable() {


            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + chosenImage.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + chosenImage.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + chosenImage.getFileThumbnailSmall());
                is_image_chosen[image_clicked_pos-1] = true;

                originalFilePath = chosenImage.getFilePathOriginal();

                Log.d("chosen_image_path",chosenImage.getFilePathOriginal());
                //thumbnailSmallFilePath = image.getFileThumbnailSmall();
                if (chosenImage != null) {
                    Log.i(TAG, "Chosen Image: Is not null");
                    loadImage(chosenImage.getFileThumbnail());
                    //loadImage(imageViewThumbSmall, image.getFileThumbnailSmall());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });

    }

    @Override
    public void onError(String s) {

    }

    private void loadImage(String path) {
        switch (image_clicked_pos) {
            case 1:
                Glide.with(this).load(new File(path)).centerCrop().into(ivStoreFirst);
                image_file1 = new File(originalFilePath);
                //alImageFile.add(image_file1);
                hmSelectedImages.put(0,image_file1);

                break;
            case 2:
                Glide.with(this).load(new File(path)).centerCrop().into(ivStoreSecond);
                image_file2 = new File(originalFilePath);
                //alImageFile.add(image_file2);
                hmSelectedImages.put(1,image_file2);

                break;
            case 3:
                Glide.with(this).load(new File(path)).centerCrop().into(ivStoreThird);
                image_file3 = new File(originalFilePath);
               // alImageFile.add(image_file3);
                hmSelectedImages.put(2,image_file3);
                break;
        }
    }


    private void requestAddStoreItems() {

        EntityStoreItem mStoreItem = new EntityStoreItem();
        mStoreItem.setItem_name(etItemName.getText().toString());
        mStoreItem.setPrice(etPrice.getText().toString());
        mStoreItem.setQty(etQty.getText().toString());
        mStoreItem.setItem_desc(etDesc.getText().toString());
        mStoreItem.setAddress(etAddress.getText().toString());
        mStoreItem.setCategory(spinnerCategory.getSelectedItem().toString());
        mStoreItem.setPerson_name(DataAccessManager.getLoginUser().getFname()+ " "+DataAccessManager.getLoginUser().getLname());
        mStoreItem.setContact(DataAccessManager.getLoginUser().getMobile());


       ArrayList<File>alFile =  new ArrayList<File>(hmSelectedImages.values());

        Communication.getInstance().callForAddStoreItem(mStoreItem, alFile).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                if (isSuccess) {

                    JSONObject json = response.optJSONObject("data");
                    DataAccessManager.updateStore(json);
                    Toast.makeText(AddItemActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);

                }
            }

            @Override
            public void failure(String mError) {

            }
        }).showProgress(AddItemActivity.this);
    }


    @Override
    public void onPermissionGranted() {
        showImagePickerDialog();
    }

    @Override
    public void onCallPermissionGranted() {

    }
}
