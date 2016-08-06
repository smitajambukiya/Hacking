package com.patelapp.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.GlobalData;
import com.patelapp.Custom.LogM;
import com.patelapp.Details_Activity;
import com.patelapp.DirectoryDetails;
import com.patelapp.Entity.EntityDirectory;
import com.patelapp.Entity.EntityStoreItem;
import com.patelapp.Entity.EntityUser;
import com.patelapp.R;
import com.patelapp.UserDetails;
import com.patelapp.realmAdapter.UserListAdapter;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmResults;

/**
 * Created by Android on 12/30/2015.
 */
public class UserFragment extends BaseFragment {

    private TextView tvNoRecords;

    Spinner spinnerType;
    protected Handler handler;
    //Array of City
    ArrayAdapter adapter_commn_type;

    private String search_text = "";
    UserListAdapter adapter_user_list;
    ListView lvUser;
    int total_count = 0;
    int page_index = 1;
    int paging = 10;
    View loadingFooter;

    String selected_cat = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_list, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        // stop auto scroll when onPause
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = mActivity.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);

        SearchManager searchManager = (SearchManager) mActivity.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchItem.getActionView().setVisibility(View.VISIBLE);
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(mActivity.getComponentName()));
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(mActivity, "Search called", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(query.toString())) {
                    search_text = query.toString();
                    if (NetworkReachability.isNetworkAvailable())
                        requestSearchUser(query.toString());
                    else
                        setAdapter(true);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setAdapter(false);
                return false;
            }
        });
    }
    private void initViews(View view) {
        tvNoRecords = (TextView) view.findViewById(R.id.tvNoRecords);
        spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        handler = new Handler();
        adapter_commn_type = new ArrayAdapter(getActivity(), R.layout.simple_list_item_single_choice, R.id.tvTitle, getResources().getStringArray(R.array.community_type));
        spinnerType.setAdapter(adapter_commn_type);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_cat = spinnerType.getItemAtPosition(position).toString();
                page_index = 1;
                loadData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loadingFooter = mActivity.getLayoutInflater().inflate(R.layout.progressbar_item, null, false);

        lvUser = (ListView) view.findViewById(R.id.lvUser);


        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityUser item = adapter_user_list.getItem(position);
                Intent intent = new Intent(mActivity, UserDetails.class);
                intent.putExtra(GlobalData.KEY_ID, item.getR_id());
                startActivity(intent);

            }
        });


        lvUser.removeFooterView(loadingFooter);

        lvUser.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    lvUser.addFooterView(loadingFooter);
                    requestUserList(false);
                }

            }
        });



    }

    private void loadData() {
        if (NetworkReachability.isNetworkAvailable()) {
            requestUserList(true);
        } else {
            setAdapter(false);
        }
    }

    private void requestUserList(boolean isLoading) {
        String strCat = selected_cat.replace(" ","%20");
        Communication.getInstance().callForGetAllUsers(page_index, strCat).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                LogM.d(response.toString());
                try {
                    total_count = response.optInt(WebElement.TOTAL_COUNT);
                    JSONArray jsonArrayUser = response.optJSONArray("data");
                    if (page_index == 1)
                        DataAccessManager.insertUser(jsonArrayUser, selected_cat);
                    else
                        DataAccessManager.updateUser(jsonArrayUser);


                    setAdapter(false);

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

    private void setAdapter(boolean isSearch) {
        if (page_index == 1) {
            RealmResults<EntityUser> userList = isSearch ? DataAccessManager.getUserSearchList(search_text,selected_cat) : DataAccessManager.getUserList(selected_cat);
            if (userList.size() > 0) {
                tvNoRecords.setVisibility(View.GONE);
                lvUser.setVisibility(View.VISIBLE);

                adapter_user_list = new UserListAdapter(mActivity, userList, true);
                lvUser.setAdapter(adapter_user_list);
            } else {
                tvNoRecords.setVisibility(View.VISIBLE);
                lvUser.setVisibility(View.GONE);
            }
        } else {
            lvUser.removeFooterView(loadingFooter);
            adapter_user_list.notifyDataSetChanged();
        }

    }


    private void requestSearchUser(final String first_name) {
        Communication.getInstance().callForGerUserSearch(1, first_name).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                LogM.d(response.toString());
                try {
                    total_count = response.optInt(WebElement.TOTAL_COUNT);
                    JSONArray jsonArrayUser = response.optJSONArray("data");
                    DataAccessManager.updateUser(jsonArrayUser);
                    setAdapter(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(String mError) {

            }
        }).showProgress(mActivity);


    }

}
