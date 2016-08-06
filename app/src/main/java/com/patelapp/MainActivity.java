package com.patelapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.patelapp.Custom.AlertDialogUtility;
import com.patelapp.Custom.AutoScrollViewPager;
import com.patelapp.Custom.CircleImageView;
import com.patelapp.Custom.Const;
import com.patelapp.Custom.DataAccessManager;
import com.patelapp.Custom.MySharedPrefs;
import com.patelapp.Entity.EntityLoginUser;
import com.patelapp.Fragment.AboutFragment;
import com.patelapp.Fragment.DirectoryFragment;
import com.patelapp.Fragment.GalleryFragment;
import com.patelapp.Fragment.PatelFeedFragment;
import com.patelapp.Fragment.SettingFragment;
import com.patelapp.Fragment.StoreFragment;
import com.patelapp.Fragment.UserFragment;
import com.patelapp.adapter.FooterPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;
    AutoScrollViewPager autoScrollPager;
    boolean doubleBackToExitPressedOnce = false;
    public  CircleImageView ivLeftProfile;
    public  TextView tvLeftUser;
    public BroadcastReceiver imageUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        setFooterAdapter();

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

//        View nav_header = nvDrawer.inflateHeaderView(R.layout.nav_header);

        View nav_header = nvDrawer.getHeaderView(0);
        EntityLoginUser mLoginUser = DataAccessManager.getLoginUser();
        ivLeftProfile = (CircleImageView) nav_header.findViewById(R.id.ivLeftProfile);
        tvLeftUser = (TextView) nav_header.findViewById(R.id.tvLeftUser);

        if (MySharedPrefs.getInstance().isRegisteredUser() && mLoginUser != null) {
            tvLeftUser.setVisibility(View.VISIBLE);
            ivLeftProfile.setVisibility(View.VISIBLE);
            tvLeftUser.setText(mLoginUser.getFname() + " " + mLoginUser.getLname());
            Glide.with(MainActivity.this).load(Const.ImagePath + mLoginUser.getImage()).fitCenter().into(ivLeftProfile);
        } else {
            tvLeftUser.setVisibility(View.GONE);
            ivLeftProfile.setVisibility(View.GONE);
        }

        // set news feed
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new PatelFeedFragment()).commit();
        setTitle("News");
        //  ((MenuItem)findViewById(R.id.nav_news_fragment)).setChecked(true);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("image_update");
        imageUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUserView();
            }
        };

        registerReceiver(imageUpdateReceiver,intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(imageUpdateReceiver != null){
            unregisterReceiver(imageUpdateReceiver);
        }
    }

    public  void updateUserView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLeftUser.setText(DataAccessManager.getLoginUser().getFname()+" "+DataAccessManager.getLoginUser().getLname());
                byte[] bitmapdata = DataAccessManager.getLoginUser().getImagebyte();
                if(bitmapdata != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    ivLeftProfile.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void setFooterAdapter(){
        autoScrollPager = (AutoScrollViewPager)findViewById(R.id.pager_adv);
        final FooterPagerAdapter adapter = new FooterPagerAdapter(MainActivity.this,DataAccessManager.getAdvertise());
        autoScrollPager.setAdapter(adapter);

        autoScrollPager.startAutoScroll();

        autoScrollPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {

                /*if (pos == adapter.getCount() - 1) {
                    autoScrollPager.setCycle(false);
                    autoScrollPager.setCurrentItem(0, false);
                } else {
                    autoScrollPager.setCycle(true);
                }*/
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

/*
                if (pos == adapter.getCount() - 1) {
                    autoScrollPager.setCycle(false);
                    autoScrollPager.setCurrentItem(0, false);
                } else {
                    autoScrollPager.setCycle(true);
                }
*/
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

   /*     MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onSearchListener.onSearchClosed();
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a newlbanner fragment and specify the planet to show based on
        // position
        switch (menuItem.getItemId()) {
            case R.id.nav_news_fragment:
                replaceFragment(new PatelFeedFragment());
                break;
            case R.id.nav_store_fragment:
                replaceFragment(new StoreFragment());
                break;
            case R.id.nav_gallery_fragment:
                replaceFragment(new GalleryFragment());
                break;
            case R.id.nav_directory_fragment:
                replaceFragment(new DirectoryFragment());
                break;
            case R.id.nav_user_fragment:
                if (!MySharedPrefs.getInstance().isRegisteredUser()) {
                    AlertDialogUtility.showRegisteredUserAlertDialog(MainActivity.this).show();
                    return;
                } else {
                    replaceFragment(new UserFragment());
                }
                break;
            case R.id.nav_aboutus_fragment:
                replaceFragment(new AboutFragment());
                break;

            case R.id.nav_setting_fragment:
                if (!MySharedPrefs.getInstance().isRegisteredUser()) {
                    AlertDialogUtility.showRegisteredUserAlertDialog(MainActivity.this).show();
                    return;
                } else {
                    replaceFragment(new SettingFragment());
                }
                break;


            default:
                replaceFragment(new PatelFeedFragment());
        }

        /*try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    private void replaceFragment(Fragment myFragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, myFragment).commit();

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }


    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(MainActivity.this,"Press BACK again to exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },2000);

    }

}

