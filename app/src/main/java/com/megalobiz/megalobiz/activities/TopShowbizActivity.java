package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.net.NetworkRequest;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.Auth;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.fragments.ShowbizMembersFragment;
import com.megalobiz.megalobiz.fragments.SongsFragment;
import com.megalobiz.megalobiz.fragments.TopBandsFragment;
import com.megalobiz.megalobiz.models.Album;
import com.megalobiz.megalobiz.models.Band;
import com.megalobiz.megalobiz.models.Musician;
import com.megalobiz.megalobiz.models.Showbiz;
import com.megalobiz.megalobiz.models.Song;
import com.megalobiz.megalobiz.models.User;
import com.megalobiz.megalobiz.utils.NetworkState;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TopShowbizActivity extends AppCompatActivity {

    private MegalobizClient client;
    private Boolean firstRun = true;

    private User authUser;
    ArrayList<Band> bands;
    ArrayList<Musician> musicians;
    ArrayList<Album> albums;
    ArrayList<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_showbiz);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.drawable.ic_megalobiz);
        ab.setTitle(R.string.top_showbiz_title);

        client = MegalobizApplication.getRestClient();

        // set the Hamburger menu with shared static class
        SharedHamburger.cookHamburger(this, R.string.top_showbiz_title);

        /*Toast.makeText(this,
                "Connected to API with "+ MegalobizApplication.grantType, Toast.LENGTH_LONG).show();

        if (client != null && client.checkAccessToken() != null) {
            Toast.makeText(this,
                    "Token is there :"+ client.checkAccessToken().getToken().subSequence(1, 5), Toast.LENGTH_LONG).show();
        }*/

        // Check Network and Internet before proceeding
        NetworkState nt = new NetworkState(this);
        if (nt.isNetworkAvailable() && NetworkState.isOnline()) {
            // start
            fetchTopShowbiz();

            // if grant type is Authorization Code, fetch Authenticated User
            if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.AUTHORIZATION) {
                fetchAuthUser();
            }

        } else {
            Toast.makeText(this, "Please Check your Internet Connection!", Toast.LENGTH_LONG).show();
        }
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
            SharedHamburger.reCookHamburger(this, R.string.top_showbiz_title);
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

    public void fetchTopShowbiz() {
        client.getTopShowbiz(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("mesage"));
                        Toast.makeText(TopShowbizActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        // Parse top models from JSON
                        JSONObject jsonModels = response.getJSONObject("models");

                        bands = Band.fromJSONArray(jsonModels.getJSONArray("bands"));
                        musicians = Musician.fromJSONArray(jsonModels.getJSONArray("musicians"));
                        albums = Album.fromJSONArray(jsonModels.getJSONArray("albums"));
                        songs = Song.fromJSONArray(jsonModels.getJSONArray("songs"));

                        // prepare the Top views
                        setupTopViews();
                    }

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

    public void setupTopViews() {
        // TOP 5 Bands -------
        //setup View Flipper for sliding bands with Fragment
        TopBandsFragment fgBand = TopBandsFragment.newInstance(bands);
        FragmentTransaction ftBand = getSupportFragmentManager().beginTransaction();
        ftBand.replace(R.id.flTopBands, fgBand);
        ftBand.commit();

        // TOP 5 Musicians -----
        // setup showbiz member Fragment
        ArrayList<Showbiz> mShowbizs = new ArrayList<>();
        mShowbizs.addAll(musicians);

        ShowbizMembersFragment fgMusician = ShowbizMembersFragment.newInstance(mShowbizs, "Musician", true);
        FragmentTransaction ftMusician = getSupportFragmentManager().beginTransaction();
        ftMusician.replace(R.id.flTopMusicians, fgMusician);
        ftMusician.commit();

        // TOP 5 Albums -----
        // setup showbiz member Fragment
        ArrayList<Showbiz> aShowbizs = new ArrayList<>();
        aShowbizs.addAll(albums);

        ShowbizMembersFragment fgAlbum = ShowbizMembersFragment.newInstance(aShowbizs, "Album", true);
        FragmentTransaction ftAlbum = getSupportFragmentManager().beginTransaction();
        ftAlbum.replace(R.id.flTopAlbums, fgAlbum);
        ftAlbum.commit();

        // TOP 5 Songs ------
        // setup songs Fragment
        SongsFragment fgSong = SongsFragment.newInstance(songs, true, "Top Songs");
        FragmentTransaction ftSong = getSupportFragmentManager().beginTransaction();
        ftSong.replace(R.id.flTopSongs, fgSong);
        ftSong.commit();
    }


    public void launchShowbizProfile(Showbiz showbiz) {
        Intent i = new Intent(this, ShowbizProfileActivity.class);
        i.putExtra("showbiz", showbiz);
        startActivity(i);
    }

    public void fetchAuthUser() {
        client.getAuthUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("mesage"));
                        Toast.makeText(TopShowbizActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        // Parse top models from JSON
                        authUser = User.fromJSON(response.getJSONObject("user"));
                        Auth.setUser(authUser);
                    }

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

}
