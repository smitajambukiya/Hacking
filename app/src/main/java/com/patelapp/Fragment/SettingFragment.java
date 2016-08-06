package com.patelapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.patelapp.ChangePasswordActivity;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.EditProfileActivity;
import com.patelapp.Fragment.BaseFragment;
import com.patelapp.LoginActivity;
import com.patelapp.R;

/**
 * Created by Android on 1/10/2016.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout llEditProfile, llChangePassword, llLogout, llShareApp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
        llEditProfile = (LinearLayout) view.findViewById(R.id.llEditProfile);
        llChangePassword = (LinearLayout) view.findViewById(R.id.llChangePassword);
        llShareApp = (LinearLayout) view.findViewById(R.id.llShareApp);
        llLogout = (LinearLayout) view.findViewById(R.id.llLogout);

        llLogout.setOnClickListener(this);
        llEditProfile.setOnClickListener(this);
        llChangePassword.setOnClickListener(this);
        llShareApp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llLogout:
                showAlertDialog();
                break;
            case R.id.llEditProfile:
                callIntent();
                break;
            case R.id.llChangePassword:
                Intent intent = new Intent(mActivity, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.llShareApp:
                shareApp();
                break;

        }
    }

    public void callIntent() {

        startActivity(new Intent(getActivity(), EditProfileActivity.class));
    }

    private void showAlertDialog() {

        new AlertDialog.Builder(mActivity).setTitle(getString(R.string.log_out)).setMessage(getString(R.string.logout_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataAccessManager.DeleteLoginUser();
                        MySharedPrefs.getInstance().StoreRegisterId("");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                startActivity(intent);
                                mActivity.finish();
                            }
                        }, 500);
                    }
                }).setNegativeButton(getString(R.string.no), null).create().show();
    }

    private void shareApp() {
        try
        { Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String share = "https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT,share);
            startActivity(Intent.createChooser(i, "choose one"));
        }
        catch(Exception e)
        { //e.toString();
        }
    }
}