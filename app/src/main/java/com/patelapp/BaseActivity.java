package com.patelapp;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.patelapp.Interfaces.OnPermissionListener;
import com.patelapp.Interfaces.OnSMSPermissionListener;
import com.patelapp.permission.PermissionUtil;

/**
 * Created by AndroidDevloper on 2/27/2016.
 */
public class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{


    public static final String TAG = "BaseActivity";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CALL_PHONE = 2;
    private static final int REQUEST_SMS = 3;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static String[] PERMISSIONS_SMS = {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
    };


    private static String[] PERMISSION_CALL_PHONE = {Manifest.permission.CALL_PHONE};


    OnSMSPermissionListener onSMSPermissionListener;
    OnPermissionListener onPermissionListener;

    public void setSMSPermissionListener(OnSMSPermissionListener _onSMSPermissionListener){
        this.onSMSPermissionListener = _onSMSPermissionListener;
    }
    public void setPermissionListeners(OnPermissionListener _onPermissionListener){
        this.onPermissionListener = _onPermissionListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public  boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return  true;
        }
        return  false;
    }



    public  boolean verifyCallPhonePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return  true;
        }
        return  false;
    }



    public void requestExternalStoragePermission() {
        Log.i(TAG, "External Storage permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying External Storage permission rationale to provide additional context.");

            ActivityCompat.requestPermissions(
                    BaseActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        } else {

            // External Storage permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                    BaseActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        // END_INCLUDE(camera_permission_request)
    }


    /*
    *  request for CALL_PHONE permission
    * */

    public void requestForCallPhonePermission(){


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(BaseActivity.this,PERMISSION_CALL_PHONE,REQUEST_CALL_PHONE);

        }else{
            ActivityCompat.requestPermissions(BaseActivity.this,PERMISSION_CALL_PHONE,REQUEST_CALL_PHONE);
        }
    }


    /**
     * request for SMS permissions
     */

    public void requestSMSReadPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_SMS)){
            ActivityCompat.requestPermissions(BaseActivity.this,PERMISSIONS_SMS,REQUEST_SMS);
        }else{
            ActivityCompat.requestPermissions(BaseActivity.this,PERMISSIONS_SMS,REQUEST_SMS);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for External Storage permission request.");

            // Check if the only required permission has been granted
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // Read and write sd card permission has been granted, preview can be displayed
                onPermissionListener.onPermissionGranted();
            } else {
                Log.i(TAG, "External Storage permission was NOT granted.");
                showSettingAlertDialog(getString(R.string.permission_storage_message));
            }


        }else if(requestCode == REQUEST_CALL_PHONE){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                onPermissionListener.onCallPermissionGranted();
            }else{
                showSettingAlertDialog(getString(R.string.permission_call_phone_message));
            }
        }
        else if(requestCode == REQUEST_SMS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                onSMSPermissionListener.onSMSPermissionGranted();
            }else{
                showSettingAlertDialog(getString(R.string.permission_read_sms));
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    private void showSettingAlertDialog(String message){
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(message)
                .setPositiveButton(getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoSettingScreen();
                    }
                }).setNegativeButton(getString(R.string.cancel),null).create().show();
    }

    private  void gotoSettingScreen(){
        try {
            String packageName = getPackageName();
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);

        } catch ( ActivityNotFoundException e ) {
            //e.printStackTrace();
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);

        }

    }



}
