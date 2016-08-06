package com.patelapp.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Entity.EntityGallery;
import com.patelapp.Entity.InterfaceModel;
import com.patelapp.R;
import com.patelapp.realmAdapter.GalleryPhotoAdapter;
import com.patelapp.realmAdapter.GalleryVideoAdapter;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/21/2016.
 */
public class VideoFragment extends BaseFragment implements InterfaceModel.OnTabChangeListener {

    GridView gvVideo;
    TextView tvEmptyView;
    GalleryVideoAdapter galleryVideoAdapter;
    public boolean is_service_called = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_video, null);
        initViews(view);
        InterfaceModel.getInstance(1).setListeners(this);
        return view;
    }

    private void initViews(View view) {
        gvVideo = (GridView) view.findViewById(R.id.gvVideo);
        tvEmptyView = (TextView)view.findViewById(R.id.tvEmptyView);
    }

    private void setAdapter() {
        RealmResults<EntityGallery> realmResults = DataAccessManager.getGalleryList(Const.GALLERY_TYPE_VIDEO);

        if(realmResults.size() > 0 )
        {
            gvVideo.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
            galleryVideoAdapter = new GalleryVideoAdapter(mActivity,realmResults,false);
            gvVideo.setAdapter(galleryVideoAdapter);
        }else{
            gvVideo.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);
        }

    }

    private void loadData() {
        if (NetworkReachability.isNetworkAvailable()) {
            requestGallery(1,Const.GALLERY_TYPE_VIDEO);
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
                    DataAccessManager.insertGallery(jsonArrayGallery,Const.GALLERY_TYPE_VIDEO);
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

    @Override
    public void onTabLoaded() {
        if(is_service_called)
            return;
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
