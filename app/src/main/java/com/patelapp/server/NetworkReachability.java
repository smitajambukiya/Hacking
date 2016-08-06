package com.patelapp.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.patelapp.PatelApplication;


public class NetworkReachability {

	NetworkReachability mInstance;
	
	
	public static boolean isNetworkAvailable(){
		ConnectivityManager connectivity = (ConnectivityManager) PatelApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}
}
