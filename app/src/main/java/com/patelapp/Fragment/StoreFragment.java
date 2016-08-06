package com.patelapp.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.patelapp.AddItemActivity;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Entity.InterfaceModel;
import com.patelapp.Interfaces.OnStoreFragmentListener;
import com.patelapp.R;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;
import com.patelapp.server.WebElement;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Android on 12/30/2015.
 */

public class StoreFragment extends BaseFragment {
    View view;
    Toolbar toolbar;
    ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    OnStoreFragmentListener onStoreFragmentListener;
    int total_count = 0;
    String search_text = "";
    private int selected_tab = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_store, container, false);

        findViews();

        setHasOptionsMenu(true);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        return view;
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
                if (!TextUtils.isEmpty(query.toString())) {
                    search_text = query.toString();
                    InterfaceModel.getInstance(selected_tab).OnSearchCalled(query);
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
                InterfaceModel.getInstance(selected_tab).OnSearchCalled("");
                return false;
            }
        });

    }

    private void setupViewPager(final ViewPager viewPager) {
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(myFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selected_tab = position;
                InterfaceModel.getInstance(position).OnTabLoaded();
                fab.setVisibility(position == 1 ? View.VISIBLE :View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void findViews() {

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

    }


    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {

            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new AllStoreFragment();
                case 1:
                    return new MyStoreFragment();

                default:
                    return  new AllStoreFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = position == 0 ? "All Store" : "My Store";
            return title;
        }


        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

}