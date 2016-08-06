package com.patelapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.patelapp.Custom.MySharedPrefs;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by Android on 1/30/2016.
 */
public class PatelApplication extends Application {


    /** The m instance. */
    private static PatelApplication mInstance;

    /** The m request queue. */
    private RequestQueue mRequestQueue;

    /** The Constant TAG. */
    public static final String TAG = PatelApplication.class.getSimpleName();


    private Realm realm;

    public RealmConfiguration realmConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        mInstance = this;
        initRealM(this);
        new MySharedPrefs(this);
    }

    private   void initRealM(Context context){

      /*  Realm.migrateRealm(realmConfiguration, new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        })*/;

        realmConfiguration = new RealmConfiguration.Builder(context).name("patel").schemaVersion(1).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfiguration);
        showStatus("Default0");
        showStatus(realm);
        realm.close();

        Realm.setDefaultConfiguration(realmConfiguration);
    }


    /**
     * Gets the single instance of SmartApplication.
     *
     * @return single instance of SmartApplication
     */
    public static synchronized PatelApplication getInstance() {
        return mInstance;
    }

    /**
     * Gets the request queue.
     *
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    //

    /**
     * Adds the to request queue.
     *
     * @param <T>
     *            the generic type
     * @param req
     *            the req
     * @param tag
     *            the tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        //        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Adds the to request queue.
     *
     * @param <T>
     *            the generic type
     * @param req
     *            the req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancel pending requests.
     *
     * @param tag
     *            the tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }





    private void showStatus(Realm realm) {
      //  showStatus(realmString(realm));
    }


    private void showStatus(String txt) {
        Log.i(TAG, txt);
    }
}
