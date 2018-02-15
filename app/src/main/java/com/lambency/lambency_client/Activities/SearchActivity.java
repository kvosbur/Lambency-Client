package com.lambency.lambency_client.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.lambency.lambency_client.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        MenuItem searchAction = menu.findItem( R.id.search );
        SearchView searchView = (SearchView) searchAction.getActionView();
        searchView.setQueryHint("Search Lambency");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        return super.onCreateOptionsMenu(menu);
    }
}
