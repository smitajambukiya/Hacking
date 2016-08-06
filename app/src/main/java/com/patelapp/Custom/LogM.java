package com.patelapp.Custom;

import android.util.Log;


public class LogM{
	public static final String TAG ="tag";
	public static void d(String message) {
		Log.d(TAG, message);
//		if (BuildConfig.DEBUG) {
//		}
	}



}
