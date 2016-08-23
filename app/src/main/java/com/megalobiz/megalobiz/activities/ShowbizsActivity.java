package com.megalobiz.megalobiz.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.adapters.ShowbizArrayAdapter;
import com.megalobiz.megalobiz.models.Showbiz;

import java.util.ArrayList;

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

        Toast.makeText(this, showbizType + " List will be loaded", Toast.LENGTH_LONG).show();

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

}
