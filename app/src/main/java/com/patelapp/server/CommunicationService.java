package com.patelapp.server;

/*
 * 
 */

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.patelapp.PatelApplication;


// TODO: Auto-generated Javadoc

/**
 * The Class WebIntentService.
 */
public class CommunicationService extends IntentService {

	/**
	 * Instantiates a new web intent service.
	 */
	public CommunicationService() {
		super("WebIntentService");
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		final RequestParams params = (RequestParams) intent.getSerializableExtra("params");

		String tag_string_req = params.getMethodName();

		String url = params.getUrl();

		final ResultReceiver rec = (ResultReceiver) intent.getParcelableExtra("rec");

		
		StringRequest strReq = new StringRequest(params.getMethod(), url,new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Bundle b = new Bundle();
				b.putString("response", response);
				b.putString("method", params.getMethodName());
				rec.send(200, b);
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Bundle b = new Bundle();

				if (error instanceof NetworkError) {
					b.putString("error", "NetworkError : " + error.getLocalizedMessage());
				} else if (error instanceof ServerError) {
					b.putString("error", "ServerError : " + error.getLocalizedMessage());
				} else if (error instanceof AuthFailureError) {
					b.putString("error", "AuthFailureError : " + error.getLocalizedMessage());
				} else if (error instanceof ParseError) {
					b.putString("error", "ParseError : " + error.getLocalizedMessage());
				} else if (error instanceof NoConnectionError) {
					b.putString("error", "NoConnectionError : " + error.getLocalizedMessage());
				} else if (error instanceof TimeoutError) {
					b.putString("error", "TimeoutError : " + error.getLocalizedMessage());
                   // StaticMethodUtility.showNegativeToast(PatelApplication.getInstance().getApplicationContext() , "Request timeout");
				
					Toast.makeText(PatelApplication.getInstance().getApplicationContext(), "Request Timeout", Toast.LENGTH_SHORT).show();
				}

				b.putString("method", params.getMethodName());
				rec.send(0, b);
				VolleyLog.d("error", "Error: " + error.getMessage());
				
			}
		});
		
		
		int socketTimeout = 30000;// 30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		strReq.setRetryPolicy(policy);
        strReq.setShouldCache(false);
		PatelApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

}