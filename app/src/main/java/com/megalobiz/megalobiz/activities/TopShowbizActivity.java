package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.adapters.HamburgerArrayAdapter;
import com.megalobiz.megalobiz.utils.HamburgerItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class TopShowbizActivity extends AppCompatActivity {

    private HamburgerArrayAdapter hamAdapter;
    private ArrayList<HamburgerItem> hamItems;
    private ListView lvHamburger;
    private DrawerLayout dlHamburger;
    private ActionBarDrawerToggle drawerToggle;

    MegalobizClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_showbiz);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.drawable.ic_megalobiz);
        ab.setTitle("Megalo Hits");

        client = MegalobizApplication.getRestClient();

        hamItems = new ArrayList<>();
        hamItems.add(new HamburgerItem("Megalo Hits", R.drawable.mb_icon_home_white));
        hamItems.add(new HamburgerItem("Bands", R.drawable.mb_icon_band_white));
        hamItems.add(new HamburgerItem("Musicians", R.drawable.mb_icon_musician_white));
        hamItems.add(new HamburgerItem("Albums", R.drawable.mb_icon_album_white));
        hamItems.add(new HamburgerItem("Songs", R.drawable.mb_icon_song_white));
        hamItems.add(new HamburgerItem("About", R.drawable.mb_icon_about_white));

        // DrawerLayout
        dlHamburger = (DrawerLayout) findViewById(R.id.dlHamburger);

        // Populate the Navigtion Drawer with options
        //mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        lvHamburger = (ListView) findViewById(R.id.lvHamburger);
        hamAdapter = new HamburgerArrayAdapter(this, hamItems);
        lvHamburger.setAdapter(hamAdapter);

        // Drawer Item click listeners
        lvHamburger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToActivity(position);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, dlHamburger, R.string.drawer_open, R.string.drawer_home_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }
        };

        dlHamburger.setDrawerListener(drawerToggle);

        Toast.makeText(this,
                "Connected to API with "+ MegalobizApplication.grantType, Toast.LENGTH_LONG).show();

        if (client != null && client.checkAccessToken() != null) {
            Toast.makeText(this,
                    "Token is there :"+ client.checkAccessToken().getToken().subSequence(1, 5), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void goToActivity(int position) {
        if (position == 0) {
            Toast.makeText(this, "Top Showbiz Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 1) {
            Toast.makeText(this, "Band Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 2) {
            Toast.makeText(this, "Musician Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 3) {
            Toast.makeText(this, "Album Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 4) {
            Toast.makeText(this, "Song Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 5) {
            Toast.makeText(this, "About Showbizs Activity", Toast.LENGTH_SHORT).show();
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
