package com.patelapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.patelapp.Entity.EntityUser;
import com.patelapp.R;
import com.patelapp.realmAdapter.DirectoryAdapter;
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
public class DirectoryFragment extends BaseFragment {

    private TextView tvNoRecords;

    //Spinner spinnerType;

    EditText etSearchCity;
    protected Handler handler;
    //Array of City
    String commn_array[] = {"Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Jamnagar", "Nadiad",
            "Bharuch", "Junagadh", "Navsari", "Gandhinagar", "Veraval", "Porbandar", "Anand",
            "Surendranagar", "Gandhidham", "Bhuj", "Godhra", "Patan", "Morvi", "Vejalpur"};


    ArrayAdapter adapter_commn_type;

Button btnSearch;
    DirectoryAdapter adapter_user_list;
    ListView lvUser;
    int total_count = 0;
    int page_index = 1;
    int paging = 10;
    View loadingFooter;


   // String selected_cat = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_directory, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop auto scroll when onPause
    }

    private void initViews(View view) {


        tvNoRecords = (TextView) view.findViewById(R.id.tvNoRecords);
        //spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        etSearchCity = (EditText)view.findViewById(R.id.etSearchCity);
        btnSearch = (Button)view.findViewById(R.id.btnSearch);
        handler = new Handler();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSearchCity.getText().toString().equalsIgnoreCase("")){
                    return ;
                }

                loadData(etSearchCity.getText().toString());

            }
        });

        /*adapter_commn_type = new ArrayAdapter(getActivity(), R.layout.simple_list_item_single_choice,
                R.id.tvTitle, commn_array);
        spinnerType.setAdapter(adapter_commn_type);
        spinnerType.setSelection(3);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_cat = spinnerType.getItemAtPosition(position).toString();
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

  etSearchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          if(actionId == EditorInfo.IME_ACTION_GO){
              if(etSearchCity.getText().toString().equalsIgnoreCase("")){
                  return false;
              }

              loadData(etSearchCity.getText().toString());

          }

          return true;
      }
  });

        loadingFooter = mActivity.getLayoutInflater().inflate(R.layout.progressbar_item, null, false);

        lvUser = (ListView) view.findViewById(R.id.lvUser);


        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EntityDirectory item = adapter_user_list.getItem(position);
                Intent intent = new Intent(mActivity, DirectoryDetails.class);
                intent.putExtra(GlobalData.KEY_ID, item.getD_id());
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
                if (firstVisibleItem + visibleItemCount == page_index * paging && total_count > totalItemCount && NetworkReachability.isNetworkAvailable()) {
                    page_index = page_index + 1;
                    lvUser.addFooterView(loadingFooter);
                    requestAllDirectory(false,etSearchCity.getText().toString());
                }

            }
        });


    }

    private void loadData(String city_name) {
        if (NetworkReachability.isNetworkAvailable()) {
            requestAllDirectory(true,city_name);
        } else {
            setAdapter(city_name);
        }
    }

    private void requestAllDirectory(boolean isLoading, final String city_name) {
        Communication.getInstance().callForGetAllDirectory(page_index, city_name).setCallback(new Communication.OnResponse() {
            @Override
            public void success(JSONObject response, boolean isSuccess) {
                LogM.d(response.toString());
                try {
                   total_count = response.optInt(WebElement.TOTAL_COUNT);
                    JSONArray jsonArrayUser = response.optJSONArray("data");
                    if (page_index == 1)
                        DataAccessManager.insertDirectory(jsonArrayUser, city_name);
                    else
                        DataAccessManager.updateDirectory(jsonArrayUser);


                    setAdapter(city_name);

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

    private void setAdapter(String city_name) {

        if (page_index == 1) {
            RealmResults<EntityDirectory> userList = DataAccessManager.getDirectoryList(city_name);
            if (userList.size() > 0) {
                tvNoRecords.setVisibility(View.GONE);
                lvUser.setVisibility(View.VISIBLE);

                adapter_user_list = new DirectoryAdapter(mActivity, userList, true);
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

}
