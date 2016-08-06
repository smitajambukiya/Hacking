package com.patelapp.gcm;

/**
 * Created by AndroidDevloper on 3/6/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.gcm.GcmListenerService;
import com.patelapp.Custom.BadgeUtils;
import com.patelapp.Custom.Const;
import com.patelapp.MainActivity;
import com.patelapp.R;
import com.patelapp.SplashScreen;


public class MyGcmListenerService extends GcmListenerService {


    private static final String TAG = "MyGcmListenerService";


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        String title = data.getString("title");
        String desc = data.getString("desc");
        String image= data.getString("image");
//        String image_path = "";
//        if(!TextUtils.isEmpty(image))
//             image_path = Const.ImagePath + image;
//
        sendNotification(title,desc);
    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param title GCM message received.
     */
    private void sendNotification(String title,String desc, String image_path) {
//        Intent intent = new Intent(this, MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MyGcmListenerService.this);
        taskStackBuilder.addNextIntent(new Intent(MyGcmListenerService.this,MainActivity.class));

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);




        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_call)
                .setContentTitle(title)
                .setContentText(desc)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title ,String message)
    {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MyGcmListenerService.this);
        taskStackBuilder.addNextIntent(new Intent(MyGcmListenerService.this,MainActivity.class));

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bt_icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        // update badge count

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_stat_tp)
                    .setLargeIcon(bt_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

        }else{
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_stat_tp)
                    .setLargeIcon(bt_icon)
                    .setContentTitle(title)
                    .setColor(ContextCompat.getColor(this,R.color.blue_light))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
        }

    }

}

