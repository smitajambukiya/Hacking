package com.patelapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by AndroidDevloper on 2/28/2016.
 */


public class SearchActivity extends AppCompatActivity {

    private SearchView svFeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Set a Toolbar to replace the ActionBar.
        svFeed = (SearchView) findViewById(R.id.svFeed);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("search", "onQueryTextSubmit ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("search", "onQueryTextChange ");
                return false;
            }
        });
*/
        return true;
    }

}
