package com.patelapp.Custom;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AndroidDevloper on 2/28/2016.
 */
public class MySharedPrefs {

    public static  final String KEY_REGISTER_ID = "key_register_id";

    public static  final String KEY_GCM_TOKEN = "key_gcm_token";
    public  static  final String PREF_NAME = "pref_name";

    private static MySharedPrefs mInstance;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref ;
    public MySharedPrefs(Context context){
        mInstance = this;
        sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public static MySharedPrefs getInstance(){
        return mInstance;
    }
    public void StoreGcmToken(String gcm_token ){

        editor.putString(KEY_GCM_TOKEN,gcm_token).apply();
    }
    public String getGcmToken(){
        return sharedPref.getString(KEY_GCM_TOKEN,"");
    }
    public boolean isRegisteredUser(){

        boolean is_registered = getRegisterId().isEmpty() ? false : true;

        return is_registered;
    }



    public void StoreRegisterId(String reg_id ){

        editor.putString(KEY_REGISTER_ID,reg_id).apply();
    }
    public String getRegisterId(){
        return sharedPref.getString(KEY_REGISTER_ID,"");
    }

    public void clearPrefs(){
        editor.clear().commit();
    }
}
