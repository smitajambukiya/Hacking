package com.patelapp.server;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class DownloadImageService extends IntentService{
    private static final String TAG = DownloadImageService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public DownloadImageService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String src = intent.getExtras().getString("image_url");
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            DataAccessManager.updateLoginUserImage(byteArray);
            Intent _intent = new Intent("image_update");
            sendBroadcast(_intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}