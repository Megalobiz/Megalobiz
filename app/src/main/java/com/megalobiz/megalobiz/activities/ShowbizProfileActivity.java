package com.megalobiz.megalobiz.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.models.Showbiz;

public class ShowbizProfileActivity extends AppCompatActivity {

    private MegalobizClient client;
    private String showbizType;
    private Showbiz showbiz;

    private Boolean firstRun = true;
    private int resourceTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showbiz_profile);

        //find Showbiz model
        showbiz = (Showbiz) getIntent().getSerializableExtra("showbiz");
        showbizType = showbiz.getShowbizType();

        resourceTitle = R.string.showbiz_title;

        if (showbizType.equals("Band")) {
            resourceTitle = R.string.band_title;
        } else if (showbizType.equals("Musician"))
            resourceTitle = R.string.musician_title;
        else if (showbizType.equals("Album"))
            resourceTitle = R.string.album_title;
        else if (showbizType.equals("Song"))
            resourceTitle = R.string.song_title;

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.drawable.ic_megalobiz);

        // title
        String title = showbiz.getName();
        if(title.length() > 14) {
            title = title.substring(0, 13);
        }
        ab.setTitle(title);

        client = MegalobizApplication.getRestClient();

        // set the Hamburger menu with shared static class
        SharedHamburger.cookHamburger(this, resourceTitle);

        Toast.makeText(this, ab.getTitle() + " will be loaded", Toast.LENGTH_LONG).show();

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
            SharedHamburger.reCookHamburger(this, resourceTitle);
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
