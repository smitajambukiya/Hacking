package com.patelapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Custom.StaticMethodUtility;
import com.patelapp.Details_Activity;
import com.patelapp.Entity.EntityNewsFeed;
import com.patelapp.R;
import com.patelapp.realmAdapter.PatelFeedAdapter;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmResults;

/**
 * Created by AndroidDevloper on 2/8/2016.
 */
public class PatelFeedFragment extends BaseFragment{


    PatelFeedAdapter patelFeedAdapter;
    SwipeRefreshLayout swlFeed;
    ListView lvFeed;
    int total_count = 0;
    int page_index = 1;
    int paging = 10;
    View loadingFooter;
    String user_id;
    private boolean isFooterVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.frag_patel_feed, null, false);
        initViews(view);

        user_id = MySharedPrefs.getInstance().getRegisterId();
        if(TextUtils.isEmpty(user_id))
            user_id = "0";

        if (NetworkReachability.isNetworkAvailable()) {
            requestNewsFeed(true);
        } else {
            setAdapter();
        }

        return view;
    }


    private void initViews(View view) {
        lvFeed = (ListView) view.findViewById(R.id.lvFeed);
        loadingFooter = mActivity.getLayoutInflater().inflate(R.layout.progressbar_item, null, false);

        swlFeed = (SwipeRefreshLayout)view.findViewById(R.id.swlFeed);

        swlFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!NetworkReachability.isNetworkAvailable()){
                    swlFeed.setRefreshing(false);
                    StaticMethodUtility.showNetworkToast(mActivity);
                    return;
                }else if(isFooterVisible){
                    swlFeed.setRefreshing(false);
                    return;
                }
                page_index = 1;
                requestNewsFeed(false);
            }
        });


        lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityNewsFeed item = patelFeedAdapter.getItem(position);
                Intent intent = new Intent(mActivity, Details_Activity.class);
                intent.putExtra(GlobalData.KEY_ID, item.getNf_id());
                startActivity(intent);

            }
        });


        lvFeed.removeFooterView(loadingFooter);

        lvFeed.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.d("list first visible", "" + firstVisibleItem);
                Log.d("list visible item", "" + visibleItemCount);
                Log.d("list total visible", "" + totalItemCount);


                if (firstVisibleItem + visibleItemCount == page_index * paging && total_count > totalItemCount && NetworkReachability.isNetworkAvailable() && !swlFeed.isRefreshing()) {

                    page_index = page_index + 1;
                    isFooterVisible = true;
                    lvFeed.addFooterView(loadingFooter);
                    requestNewsFeed(false);
                }

            }
        });

    }

    private void requestNewsFeed(boolean isLoading) {
        Communication.getInstance().callForGetAllFeed(page_index,user_id).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                LogM.d(response.toString());
                try {
                    total_count = response.optInt(WebElement.TOTAL_COUNT);
                    JSONArray jsonArrayFeed = response.optJSONArray("data");
                    if (page_index == 1)
                        DataAccessManager.insertPatelFeed(jsonArrayFeed);
                    else
                        DataAccessManager.updatePatelFeed(jsonArrayFeed);


                    setAdapter();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(String mError) {
                LogM.d(mError);
            }
        }).showProgress(mActivity, isLoading);

    }

    private void setAdapter() {
        if(swlFeed.isRefreshing())
            swlFeed.setRefreshing(false);
        if (page_index == 1) {
            RealmResults<EntityNewsFeed> feeds = DataAccessManager.getPatelFeed();
            patelFeedAdapter = new PatelFeedAdapter(mActivity, feeds, true);
            lvFeed.setAdapter(patelFeedAdapter);
        } else {
            lvFeed.removeFooterView(loadingFooter);
            isFooterVisible = false;
            patelFeedAdapter.notifyDataSetChanged();
        }

    }

}

