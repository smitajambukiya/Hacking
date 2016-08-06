package com.patelapp.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.LogM;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.Entity.InterfaceModel;
import com.patelapp.R;
import com.patelapp.realmAdapter.GalleryPhotoAdapter;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/20/2016.
 */
public class PhotoFragment extends BaseFragment implements InterfaceModel.OnTabChangeListener{

    GridView gvPhoto;
    TextView tvEmptyView;
    GalleryPhotoAdapter galleryPhotoAdapter;
    public boolean is_service_called = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_photo, null);
        initViews(view);

        InterfaceModel.getInstance(0).setListeners(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        },300);


        return view;
    }

    private void initViews(View view) {

        tvEmptyView = (TextView) view.findViewById(R.id.tvEmptyView);
        gvPhoto = (GridView) view.findViewById(R.id.gvPhoto);


    }

    private void loadData() {
        if (NetworkReachability.isNetworkAvailable()) {
            requestGallery(1,Const.GALLERY_TYPE_IMAGE);
        } else {
            setAdapter();
        }
    }

    private void requestGallery(int page, String type){
        Communication.getInstance().callForGetGallery(page,type).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                LogM.d(response.toString());
                is_service_called = true;
                try{
                    JSONArray jsonArrayGallery = response.optJSONArray("data");
                    DataAccessManager.insertGallery(jsonArrayGallery,Const.GALLERY_TYPE_IMAGE);
                    setAdapter();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String mError) {
                LogM.d(mError);
            }
        }).showProgress(mActivity);

    }
    private void setAdapter() {

            RealmResults<EntityGallery> realmResults = DataAccessManager.getGalleryList(Const.GALLERY_TYPE_IMAGE);

            if (realmResults.size() > 0) {
                gvPhoto.setVisibility(View.VISIBLE);
                tvEmptyView.setVisibility(View.GONE);
                galleryPhotoAdapter = new GalleryPhotoAdapter(mActivity, realmResults, true);
                gvPhoto.setAdapter(galleryPhotoAdapter);
            } else {
                gvPhoto.setVisibility(View.GONE);
                tvEmptyView.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public void onTabLoaded() {
        if(is_service_called) {
           // setAdapter();
            return;
        }
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        },300);
    }

    @Override
    public void onSearchCalled(String search_text) {

    }
}
