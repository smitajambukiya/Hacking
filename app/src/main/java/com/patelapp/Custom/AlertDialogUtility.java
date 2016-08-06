package com.patelapp.Custom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.patelapp.R;
import com.patelapp.SignUpActivity;

/**
 * Created by AndroidDevloper on 2/27/2016.
 */
public class AlertDialogUtility {

    public static AlertDialog showRegisteredUserAlertDialog(final Context context){

        return new AlertDialog.Builder(context).setTitle(context.getString(R.string.app_name)).setMessage(context.getString(R.string.unregistered_user))
                .setPositiveButton(context.getText(R.string.goto_register), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, SignUpActivity.class);
                        context.startActivity(intent);
                    }
                }).setNegativeButton(context.getText(R.string.cancel),null).create();
    }



    public static AlertDialog showAlertDialog(final Context context, String message, DialogInterface.OnClickListener onYesClick){

        return new AlertDialog.Builder(context).setTitle(context.getString(R.string.app_name)).setMessage(message)
                .setPositiveButton(context.getText(R.string.yes),onYesClick).setNegativeButton(context.getText(R.string.no),null).create();
    }


}
