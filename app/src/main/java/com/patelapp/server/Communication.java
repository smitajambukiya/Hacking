package com.patelapp.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.builder.MultipartBodyBuilder;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Entity.EntityUser;
import com.patelapp.PatelApplication;
import com.patelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Communication implements ResponseReceiver.Listener {

    public static String currentMethod;

    public static OnResponse callback;

    public static Communication mInstanse;

    private ProgressDialog progress;

    private final String TAG = this.getClass().getSimpleName();

    public static synchronized Communication getInstance() {
        if (mInstanse == null) {
            mInstanse = new Communication();
        }
        return mInstanse;
    }

    public Communication setCallback(OnResponse communicationHandlerCallBack) {
        callback = communicationHandlerCallBack;
        return mInstanse;
    }

    public interface OnResponse {

        public void success(JSONObject response, boolean isSuccess);

        public void failure(String mError);

    }


	/*public Communication callForGetVsukhFeed(int page) {
        get("http://www.wesukh.net/vsukhadiya/ws2/postws.php?method=getAllFeeds&type=2"+"&"+WebElement.PAGE+"="+page);
		return mInstanse;
	}*/

    public Communication callForGetAllFeed(int page,String r_id) {

        get(Const.BASE_URL + Const.METHOD_NEWS_FEED + "?" + WebElement.PAGE + "=" + page+"&"+WebElement.R_ID +"="+r_id);
        return mInstanse;
    }

    public Communication callForGetAdvertise() {

        get(Const.BASE_URL + Const.METHOD_FOOTER_ADVERTISE);
        return mInstanse;
    }

    public Communication callForGetAllUsers(int page, String type) {

        get(Const.BASE_URL + Const.METHOD_GET_USER + "?" + WebElement.PAGE + "=" + page + "&" + WebElement.TYPE + "=" + type);
        return mInstanse;
    }


    public Communication callForGerUserSearch(int page, String fname) {
        get(Const.BASE_URL + Const.METHOD_GET_USER_SEARCH + "?" + WebElement.PAGE + "=" + page + "&" + WebElement.FNAME + "=" + fname);
        return mInstanse;

    }

    public Communication callForGetAllDirectory(int page, String city) {

        get(Const.BASE_URL + Const.METHOD_GET_DIRECTORY + "?" + WebElement.PAGE + "=" + page + "&" + WebElement.CITY + "=" + city);
        return mInstanse;
    }


    public Communication callForSearchStoreItem(int page, String r_id,String item_name) {
        get(Const.BASE_URL + Const.METHOD_GET_STORE_SEARCH + "?" + WebElement.PAGE + "=" + page + "&" + WebElement.R_ID + "=" +r_id +"&"+WebElement.ITEM_NAME +"="+item_name);
        return mInstanse;

    }

    public Communication callForGetLogin(String mobile, String password) {

        get(Const.BASE_URL + Const.METHOD_LOGIN_USER + "?" + WebElement.MOBILE + "=" + mobile + "&" + WebElement.PASSWORD + "=" + password +"&"+WebElement.D_TOKEN+"="+MySharedPrefs.getInstance().getGcmToken());
        return mInstanse;
    }

    public Communication callForGetVerify(String mobile, String otp) {

        get(Const.BASE_URL + Const.METHOD_GET_VERIFY + "?" + WebElement.MOBILE + "=" + mobile + "&" + WebElement.OTP + "=" + otp);
        return mInstanse;
    }

    public Communication callForGetGallery(int page, String type) {

        get(Const.BASE_URL + Const.METHOD_GET_GALLERY+ "?" + WebElement.PAGE + "=" +page +"&"+WebElement.TYPE+"="+type);
        return mInstanse;
    }

    public Communication callForGetStore(int page,String r_id) {
        currentMethod = Const.METHOD_GET_STORE;
        get(Const.BASE_URL + Const.METHOD_GET_STORE + "?" + WebElement.PAGE + "=" + page+"&"+WebElement.R_ID+"="+r_id);
        return mInstanse;
    }

    public Communication callForGetMyStore(int page, String register_id) {
        currentMethod = Const.METHOD_GET_MY_STORE;
        get(Const.BASE_URL + Const.METHOD_GET_MY_STORE + "?" + WebElement.PAGE + "=" + page + "&" + WebElement.REGISTER_ID + "=" + register_id);
        return mInstanse;
    }


    /*
    * call for change password
    * */

    public Communication callForChangePassword(String old_password, String new_password,String r_id) {
        currentMethod = Const.METHOD_CHANGE_PASSWORD;
        get(Const.BASE_URL + Const.METHOD_CHANGE_PASSWORD + "?" + WebElement.PASSWORD + "=" + old_password + "&" + WebElement.R_ID + "=" + r_id + "&" +WebElement.NEW_PASSWORD+"="+new_password);
        return mInstanse;
    }

    public Communication callForRemoveStoreItem(String id) {
        currentMethod = Const.METHOD_REMOVE_STORE;
        get(Const.BASE_URL + Const.METHOD_REMOVE_STORE + "?" + WebElement.ID + "=" + id);
        return mInstanse;
    }



/*
* call add like
* */

    public Communication callForAddLike(String nf_id,String r_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nf_id",nf_id);
            jsonObject.put("user_id",r_id);
            //   postJsonData(Const.BASE_URL+Const.METHOD_INSERT_LIKE,jsonObject.toString());
            post(Const.BASE_URL+Const.METHOD_INSERT_LIKE,jsonObject.toString(),null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }

        return mInstanse;


    }

    /*
    *  CALL FOR FORGET PASSWORD
    * */

    public Communication callForForgetPassword(String mobileno) {
        currentMethod = Const.METHOD_FORGET_PASSWORD;
        get(Const.BASE_URL + Const.METHOD_FORGET_PASSWORD+ "?" + WebElement.MOBILE + "=" +mobileno);
        return mInstanse;
    }

    public Communication callForOTP(String otp_url){
        get(otp_url);
        return mInstanse;
    }

    @SuppressWarnings("unused")
    private String urlEncode(String str) {
        if (str != null) {
            try {
                return URLEncoder.encode(str.trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        } else
            return "";
    }

    public void showProgress(Context mContext) {
        try {
            if (NetworkReachability.isNetworkAvailable()) {
                if (mContext != null) {
                    if (progress == null) {
                        progress = ProgressDialog.show(mContext,
                                "Please wait...", "Loading...", true);

                        progress.setCancelable(true);
                        progress.show();

                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void showProgress(Context mContext, boolean isProgress) {
        try {
            if (!isProgress)
                return;
            if (NetworkReachability.isNetworkAvailable()) {
                if (mContext != null) {
                    if (progress == null) {
                        progress = ProgressDialog.show(mContext,
                                "Please wait...", "Loading...", true);
                        progress.show();
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void showProgress(Context mContext, boolean isProgress,
                             String message) {
        try {
            if (isProgress)
                return;
            if (NetworkReachability.isNetworkAvailable()) {
                if (mContext != null) {
                    if (progress == null) {
                        progress = ProgressDialog.show(mContext,
                                "Please wait...", message + "...", true);
                        progress.show();
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (progress != null) {
                if (progress.isShowing()) {
                    progress.dismiss();
                    progress = null;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private RequestParams requestParams;

    public void prepareCall() {
        requestParams = new RequestParams();
    }


    public Communication callForRegisterUser(EntityUser user, File imageFile) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fname", user.getFname());
            jsonObject.put("lname", user.getLname());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("mobile", user.getMobile());
            jsonObject.put("dob", user.getDob());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("community_type", user.getCommunity_type());
            jsonObject.put("community_category", user.getCommunity_category());
            jsonObject.put("visiblity", user.getVisiblity());
            jsonObject.put("d_token",MySharedPrefs.getInstance().getGcmToken());

            //String encoded = new String(jsonObject.toString(),"UTF-8");
            File resize_image_file= null;

            if(imageFile != null){
                resize_image_file = StaticMethodUtility.getResizeImage(imageFile.getPath());
            }

            post(Const.BASE_URL + Const.METHOD_REGISTER_USER, jsonObject.toString(), resize_image_file, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstanse;
    }

    public Communication callForUpdateProfile(EntityUser user, File imageFile) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("r_id",MySharedPrefs.getInstance().getRegisterId());
            jsonObject.put("fname", user.getFname());
            jsonObject.put("lname", user.getLname());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("dob", user.getDob());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("community_type", user.getCommunity_type());
            jsonObject.put("community_category", user.getCommunity_category());
            jsonObject.put("education", user.getEducation());
            jsonObject.put("profession", user.getProfession());
            jsonObject.put("visiblity", user.getVisiblity());

            //String encoded = new String(jsonObject.toString(),"UTF-8");

            post(Const.BASE_URL + Const.METHOD_EDIT_PROFILE, jsonObject.toString(), imageFile, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstanse;
    }

/* call for insert like post*/



    public Communication callForAddStoreItem(EntityStoreItem entityStoreItem, ArrayList<File> alFile) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("register_id", MySharedPrefs.getInstance().getRegisterId());
            jsonObject.put("item_name", entityStoreItem.getItem_name());
            jsonObject.put("price", entityStoreItem.getPrice());
            jsonObject.put("qty", entityStoreItem.getQty());
            jsonObject.put("item_desc", entityStoreItem.getItem_desc());
            jsonObject.put("address", entityStoreItem.getAddress());
            jsonObject.put("category", entityStoreItem.getCategory());
            jsonObject.put("person_name", entityStoreItem.getPerson_name());
            jsonObject.put("contact", entityStoreItem.getContact());

            ArrayList<File> al_resize_file = new ArrayList<File>();
            if(alFile != null && alFile.size() > 0)
            {
                for(File file : alFile){
                    File resize_file = StaticMethodUtility.getResizeImage(file.getPath());
                    al_resize_file.add(resize_file);
                }
            }
            post(Const.BASE_URL + Const.METHOD_ADD_STORE_ITEM, jsonObject.toString(), null, alFile, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstanse;

    }


    public Communication callForLoginUser(String mobile, String password) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", mobile);
            jsonObject.put("password", password);

            post(Const.BASE_URL + Const.METHOD_LOGIN_USER, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInstanse;
    }


    public void post(String mURL, String data, File file, ArrayList<File>
            files, File videoFile) {
        if (NetworkReachability.isNetworkAvailable()) {

            MultipartBodyBuilder<Builders.Any.M> multiPartBuilder = Ion.with(PatelApplication.getInstance().getApplicationContext()).load(mURL)
                    .setTimeout(60 * 60 * 1000).setMultipartParameter("data", data);
            if (file != null) {
                multiPartBuilder.setMultipartFile("file", "image/jpeg", file);
            }
            if (videoFile != null) {
                multiPartBuilder.setMultipartFile("intro_video", "video/mp4", videoFile);
            }

            if (files != null) {

                for (int i = 0; i < files.size(); i++) {
                    int temp = i + 1;
                    multiPartBuilder.setMultipartFile("file" + temp,
                            "image/jpeg", files.get(i));
                }
            }
            multiPartBuilder.setMultipartContentType("multipart/form-data").asString()
                    .setCallback(new com.koushikdutta.async.future.FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String result) {
                            // TODO Auto-generated method stub

                            dismissDialog();
                            if (e != null) {
                                e.printStackTrace();
                                if (callback != null) {
                                    callback.failure(e.getLocalizedMessage());
                                }
                                return;
                            }
                            if (callback != null) {
                                System.out.println("Server says: " + result);
                                try {
                                    callback.success(new JSONObject(result), true);
                                } catch (JSONException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                            }

                        }
                    });
        } else {
            if (callback != null) {
//				Ting.showNagativeToast(R.string.message_no_internet);
//				callback.failure(SmartApplication.getInstance().getApplicationContext().getResources()
//						.getString(R.string.message_no_internet));
            }
        }
    }

	/*
     * Ion.with(SmartApplication.getInstance().getApplicationContext()).load(
	 * mURL) .setMultipartFile("photo", "image/jpeg",
	 * file).setMultipartParameter("data", data)
	 * .setMultipartContentType("multipart/form-data").asString()
	 * .setCallback(new FutureCallback<String>() {
	 * 
	 * @Override public void onCompleted(Exception e, String result) { // TODO
	 * Auto-generated method stub
	 * 
	 * dismissDialog(); if (e != null) { e.printStackTrace(); if (callback !=
	 * null) { callback.failure(e.getLocalizedMessage()); } return; } if
	 * (callback != null) { System.out.println("Server says: " + result); try {
	 * callback.success(new JSONObject(result), true); } catch (JSONException
	 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); } }
	 * 
	 * } });
	 */
    // }

    public void get(String mURL) {

        prepareCall();

        //	if (NetworkReachability.isNetworkAvailable()) {
        requestParams.setMethod(Request.Method.GET);
        requestParams.setUrl(mURL);
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        requestParams.setHeaders(headers);
        requestParams.setMethodName(mURL);
        LogM.d("url:" + mURL);
        PatelApplication.getInstance().getApplicationContext()
                .startService(createCallingIntent(requestParams));
//		} else {
//			if (callback != null) {
//				// StaticMethodUtility.showNegativeToast(PatelApplication.getInstance().getApplicationContext(),"No Internet Connection");
//				callback.failure(PatelApplication.getInstance()
//						.getApplicationContext().getResources()
//						.getString(R.string.no_internet));
//				// callback.failure("No Internet Connection");
//			}
//		}
    }

    // requestParams.setContentType("application/json");
    // header.put("Content-Type", "application/json");
    // header.put("Accept", "application/json")
    // requestParams.setContentBody(mStringData.toString().getBytes());

    private void post(String mURL, String mStringData) {
        prepareCall();

        if (NetworkReachability.isNetworkAvailable()) {
            requestParams.setMethod(Request.Method.POST);
            requestParams.setUrl(mURL);

            HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> header = new HashMap<String, String>();
            //
            header.put("Content-Type", "application/x-www-form-urlencoded");
            requestParams.setHeaders(header);

            params.put("data", mStringData);
            requestParams.setParams(params);
            requestParams.setMethodName(mURL);

            Log.e("Request Url :", mURL + "?data=" + mStringData);

            PatelApplication.getInstance().getApplicationContext()
                    .startService(createCallingIntent(requestParams));

        } else {
            if (callback != null) {
                // StaticMethodUtility.showNagativeToast(PatelApplication.getInstance().getApplicationContext(),
                // "No Internet Connection");
                callback.failure(PatelApplication.getInstance()
                        .getApplicationContext().getResources()
                        .getString(R.string.no_internet));
                // callback.failure("No Internet Connection");
            }
        }
    }

    private void postJsonData(String mURL, String mStringData) {
        prepareCall();

        if (NetworkReachability.isNetworkAvailable()) {
            URL url ;
            String response= "";
            try {
                url = new URL(mURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(mStringData);
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                Log.e("response", "response -----" + conn.getContent().toString());
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                    dismissDialog();
                    callback.success(new JSONObject(response), true);
                }
                else {
                    dismissDialog();
                    response="";
                    callback.success(new JSONObject(response),false);

                }
            }catch (Exception e){
                dismissDialog();
                e.printStackTrace();
                callback.failure(e.getLocalizedMessage());
            }

        } else {
            if (callback != null) {
                // StaticMethodUtility.showNagativeToast(PatelApplication.getInstance().getApplicationContext(),
                // "No Internet Connection");
                callback.failure(PatelApplication.getInstance()
                        .getApplicationContext().getResources()
                        .getString(R.string.no_internet));
                // callback.failure("No Internet Connection");
            }
        }
    }

    public void put(String mURL, HashMap<String, String> mStringData) {
        prepareCall();

        if (NetworkReachability.isNetworkAvailable()) {
            requestParams.setMethod(Request.Method.PUT);
            requestParams.setUrl(mURL);
            // requestParams.setContentType("application/json");
            // HashMap<String, String> header = new HashMap<String, String>();
            // header.put("Content-Type", "application/json");
            // header.put("Accept", "application/json");

            // HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> header = new HashMap<String, String>();
            //
            header.put("Content-Type", "application/x-www-form-urlencoded");
            requestParams.setHeaders(header);

            // requestParams.setHeaders(header);
            // params.put("", mStringData);
            requestParams.setParams(mStringData);
            requestParams.setMethodName("sdfyg asdhfhasjdfh ");
            // requestParams.setContentBody(mStringData.toString().getBytes());
            PatelApplication.getInstance().getApplicationContext()
                    .startService(createCallingIntent(requestParams));

        } else {
            if (callback != null) {
                // Ting.showNagativeToast(R.string.message_no_internet);
                callback.failure(PatelApplication.getInstance()
                        .getApplicationContext().getResources()
                        .getString(R.string.no_internet));
                // callback.failure("No Internet Connection");
            }
        }
    }

    public void delete(String mURL, String mStringData) {
        prepareCall();

        if (NetworkReachability.isNetworkAvailable()) {
            requestParams.setMethod(Request.Method.DELETE);
            requestParams.setUrl(mURL);
            requestParams.setContentType("application/json");
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Content-Type", "application/json");
            header.put("Accept", "application/json");

            requestParams.setHeaders(header);
            requestParams.setContentBody(mStringData.toString().getBytes());
            PatelApplication.getInstance().getApplicationContext()
                    .startService(createCallingIntent(requestParams));

        } else {
            if (callback != null) {
                // Ting.showNagativeToast(R.string.message_no_internet);
                callback.failure(PatelApplication.getInstance()
                        .getApplicationContext().getResources()
                        .getString(R.string.no_internet));
                // callback.failure("No Intetnet Connection");
            }
        }
    }

    private Intent createCallingIntent(RequestParams params) {
        Intent i = new Intent(PatelApplication.getInstance()
                .getApplicationContext(), CommunicationService.class);
        ResponseReceiver receiver = new ResponseReceiver(new Handler());
        receiver.setListener(Communication.this);
        i.putExtra("rec", receiver);
        i.putExtra("params", params);
        return i;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        // TODO Auto-generated method stub

        dismissDialog();
        if (resultCode == 200) {
            if (callback != null) {

                JSONObject jsonObject;
                try {

                    jsonObject = new JSONObject(
                            resultData.getString("response"));
                    if (jsonObject.has("STATUS")) {
                        if (jsonObject.optInt("STATUS") == 1) {
                            callback.success(jsonObject, true);
                        } else {
                            callback.success(jsonObject, false);
                        }
                    } else {
                        callback.success(jsonObject, false);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    callback.failure(resultData.getString("error"));
                }
            }
        } else {
            if (callback != null) {
                callback.failure(resultData.getString("error"));
            }
        }
    }
}
