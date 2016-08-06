package com.patelapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.patelapp.Custom.AlertDialogUtility;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Entity.InterfaceModel;
import com.patelapp.R;
import com.patelapp.StoreDetailsActivity;
import com.patelapp.realmAdapter.StoreListAdapter;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/22/2016.
 */
public class MyStoreFragment extends BaseFragment implements InterfaceModel.OnTabChangeListener {

    private TextView tvNoRecords;
    int total_count = 0;
    int page_index = 1;
    int paging = 10;
    View loadingFooter;
    private StoreListAdapter adapter_store_list;
    ListView lvMyStore;

    public boolean is_service_called = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_my_store, null);

        InterfaceModel.getInstance(1).setListeners(this);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        tvNoRecords = (TextView) view.findViewById(R.id.tvNoRecords);
        lvMyStore = (ListView) view.findViewById(R.id.lvMyStore);
        loadingFooter = mActivity.getLayoutInflater().inflate(R.layout.progressbar_item, null, false);


        lvMyStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityStoreItem item = adapter_store_list.getItem(position);
                Intent intent = new Intent(mActivity, StoreDetailsActivity.class);
                intent.putExtra(GlobalData.KEY_ID, item.getId());
                intent.putExtra(GlobalData.KEY_CAN_REMOVE, true);
                startActivity(intent);
            }
        });

        lvMyStore.removeFooterView(loadingFooter);
        lvMyStore.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.d("list first visible", "" + firstVisibleItem);
                Log.d("list visible item", "" + visibleItemCount);
                Log.d("list total visible", "" + totalItemCount);

                if (firstVisibleItem + visibleItemCount == page_index * paging && total_count > totalItemCount && NetworkReachability.isNetworkAvailable()) {
                    page_index = page_index + 1;
                    lvMyStore.addFooterView(loadingFooter);
                    requestGetStore(false);
                }

            }
        });


    }

    private void requestGetStore(boolean is_progress) {

        Communication.getInstance().callForGetMyStore(page_index, MySharedPrefs.getInstance().getRegisterId()).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                if (Communication.currentMethod.equalsIgnoreCase(Const.METHOD_GET_MY_STORE)) {
                    is_service_called = true;
                    LogM.d(response.toString());
                    try {
                        String my_register_id = MySharedPrefs.getInstance().getRegisterId();
                        total_count = response.optInt(WebElement.TOTAL_COUNT);
                        JSONArray jsonArrayStore = response.optJSONArray("data");
                        if (page_index == 1)
                            DataAccessManager.insertMyStore(jsonArrayStore, MySharedPrefs.getInstance().getRegisterId());
                        else
                            DataAccessManager.updateStore(jsonArrayStore);

                        setAdapter(false,"");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(String mError) {
                Toast.makeText(mActivity, mError, Toast.LENGTH_SHORT).show();
            }
        }).showProgress(mActivity, is_progress);
    }
    private void requestSearchItem(boolean is_progress, final String search_text){

        Communication.getInstance().callForSearchStoreItem(1,MySharedPrefs.getInstance().getRegisterId(),search_text).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                is_service_called = true;
                LogM.d(response.toString());
                try {
                    total_count = response.optInt(WebElement.TOTAL_COUNT);
                    JSONArray jsonArrayStore = response.optJSONArray("data");
                    DataAccessManager.updateStore(jsonArrayStore);
                    setAdapter(true,search_text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String mError) {
                Toast.makeText(mActivity,mError,Toast.LENGTH_SHORT).show();
            }
        }).showProgress(mActivity,is_progress);

    }
    private void loadData() {
        if (NetworkReachability.isNetworkAvailable()) {
            requestGetStore(true);
        } else {
            setAdapter(false,"");
        }
    }


    private void setAdapter(boolean isSearch,String search_text) {

        if (page_index == 1) {
            String reg_id = MySharedPrefs.getInstance().getRegisterId();
            RealmResults<EntityStoreItem> all_store_list = isSearch ? DataAccessManager.getMyStoreSearchList(search_text,reg_id) : DataAccessManager.getMyStore(MySharedPrefs.getInstance().getRegisterId());
            if (all_store_list.size() > 0) {
                tvNoRecords.setVisibility(View.GONE);
                lvMyStore.setVisibility(View.VISIBLE);

                adapter_store_list = new StoreListAdapter(mActivity, all_store_list, true);
                lvMyStore.setAdapter(adapter_store_list);


            } else {
                tvNoRecords.setVisibility(View.VISIBLE);
                lvMyStore.setVisibility(View.GONE);
            }
        } else {
            lvMyStore.removeFooterView(loadingFooter);
            adapter_store_list.notifyDataSetChanged();
        }

    }


    @Override
    public void onTabLoaded() {

        if(!MySharedPrefs.getInstance().isRegisteredUser())
        {
            AlertDialogUtility.showRegisteredUserAlertDialog(mActivity).show();
            return;
        }

        if (is_service_called){
            setAdapter(false,"");
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 300);

    }

    @Override
    public void onSearchCalled(String search_text) {
        if (!TextUtils.isEmpty(search_text)){
            if (NetworkReachability.isNetworkAvailable())
                requestSearchItem(true,search_text);
            else
                setAdapter(true,search_text);
        }else{
            setAdapter(false,"");
        }
    }
}
