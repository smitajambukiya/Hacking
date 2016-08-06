package com.patelapp.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.LogM;
import com.patelapp.Entity.InterfaceModel;
import com.patelapp.R;
import com.patelapp.server.Communication;
import com.patelapp.server.NetworkReachability;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Android on 12/30/2015.
 */
public class GalleryFragment extends BaseFragment {
    View view;
    Toolbar toolbar;
    ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_gallery, container, false);

        findViews();

        setFragmentAdapter();
//        if(NetworkReachability.isNetworkAvailable())
//            requestGallery();
//        else
//            setFragmentAdapter();
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                InterfaceModel.getInstance(position).OnTabLoaded();
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
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

    }


    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fragmentManager){

            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new PhotoFragment();
                case 1:
                    return new VideoFragment();
                default:
                    return new PhotoFragment();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = position == 1 ? "Video" : "Photo";
            return title;
        }
    }


    private void setFragmentAdapter(){
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);
    }



}
