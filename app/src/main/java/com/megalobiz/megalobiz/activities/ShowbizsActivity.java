package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.adapters.ShowbizArrayAdapter;
import com.megalobiz.megalobiz.models.Album;
import com.megalobiz.megalobiz.models.Band;
import com.megalobiz.megalobiz.models.Musician;
import com.megalobiz.megalobiz.models.Showbiz;
import com.megalobiz.megalobiz.models.Song;
import com.megalobiz.megalobiz.utils.EndlessScrollListener;
import com.megalobiz.megalobiz.utils.NetworkState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShowbizsActivity extends AppCompatActivity {

    private MegalobizClient client;
    private String showbizType;
    private ShowbizArrayAdapter aShowbiz;
    private ArrayList<Showbiz> showbizs;
    private GridView gvShowbiz;

    private Boolean firstRun = true;
    private int resourceTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbizs);

        //find Type
        showbizType = getIntent().getStringExtra("showbiz_type");

        // set default showbiz type
        if(showbizType == null)
            showbizType = "Band";

        resourceTitle = R.string.bands_title;;
        if (showbizType.equals("Musician"))
            resourceTitle = R.string.musicians_title;
        else if (showbizType.equals("Album"))
            resourceTitle = R.string.albums_title;
        else if (showbizType.equals("Song"))
            resourceTitle = R.string.songs_title;

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.drawable.ic_megalobiz);
        ab.setTitle(resourceTitle);

        client = MegalobizApplication.getRestClient();

        // set the Hamburger menu with shared static class
        SharedHamburger.cookHamburger(this, resourceTitle);

        gvShowbiz = (GridView) findViewById(R.id.gvShowbiz);
        showbizs = new ArrayList<>();
        aShowbiz = new ShowbizArrayAdapter(this, showbizs);
        gvShowbiz.setAdapter(aShowbiz);

        // Check Network and Internet, close Activity if no internet
        NetworkState nt = new NetworkState(this);
        nt.closeIfNoConnection();

        populateShowbizs(0);

        setupGridView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SharedHamburger.drawerToggle.syncState();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (! firstRun) {
            SharedHamburger.reCookHamburger(this,resourceTitle);
        } else {
            firstRun = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        SharedMenu.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (SharedMenu.setMenuActions(this, item))
            return true;

        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (SharedHamburger.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupGridView() {
        // Attach the listener to the AdapterView onCreate
        gvShowbiz.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int index = showbizs.size();
                populateShowbizs(index);
                return true;
            }
        });

        // hook up listener for GridView click
        gvShowbiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // get article object at position i
                Showbiz showbiz = (Showbiz) adapterView.getItemAtPosition(position);

                displayShowbiz(showbiz);
            }
        });
    }

    public void populateShowbizs(int index) {
        client.getShowbizs(index, showbizType, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //Log.d("DEBUG", response.toString());
                    if(showbizType.equals("Band")) {
                        aShowbiz.addAll(Band.fromJSONArray(response.getJSONArray("bands")));

                    } else if(showbizType.equals("Musician")) {
                        aShowbiz.addAll(Musician.fromJSONArray(response.getJSONArray("musicians")));

                    } else if(showbizType.equals("Album")) {
                        aShowbiz.addAll(Album.fromJSONArray(response.getJSONArray("albums")));

                    } else if(showbizType.equals("Song")) {
                        aShowbiz.addAll(Song.fromJSONArray(response.getJSONArray("songs")));
                    }

                    aShowbiz.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void displayShowbiz(Showbiz showbiz) {
        // create intent to display article
        Intent i = new Intent(this, ShowbizProfileActivity.class);
        // pass the article into intent
        i.putExtra("showbiz", showbiz);
        startActivity(i);
    }
}
