package com.patelapp.receiver;

/**
 * Created by AndroidDevloper on 4/14/2016.
 */

import com.patelapp.Custom.GlobalData;
import com.patelapp.VerificationActiivty;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * Created by Ravi on 09/07/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(GlobalData.SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);

                    Intent _intent = new Intent(GlobalData.OTP_ACTION);
                    _intent.putExtra("otp", verificationCode);
                    context.sendBroadcast(_intent);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(GlobalData.OTP_DELIMITER);

        if (index != -1) {
            int start = index + 2;
            int length = 6;
            //code = message.substring(start, start + message.length());
            code = message.substring(0,index);
            return code;
        }

        return code;
    }
}
