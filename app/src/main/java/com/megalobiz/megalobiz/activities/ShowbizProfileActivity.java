package com.megalobiz.megalobiz.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.fragments.ShowbizMembersFragment;
import com.megalobiz.megalobiz.fragments.SongsFragment;
import com.megalobiz.megalobiz.models.Album;
import com.megalobiz.megalobiz.models.Band;
import com.megalobiz.megalobiz.models.Musician;
import com.megalobiz.megalobiz.models.Showbiz;
import com.megalobiz.megalobiz.models.Song;
import com.megalobiz.megalobiz.utils.NetworkState;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ShowbizProfileActivity extends AppCompatActivity {

    private MegalobizClient client;
    private String showbizType;
    private Showbiz showbiz;

    private Boolean firstRun = true;
    private int resourceTitle;

    //Views attributes


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
        if (title.length() > 14) {
            title = title.substring(0, 13);
        }
        ab.setTitle(title);

        client = MegalobizApplication.getRestClient();

        // set the Hamburger menu with shared static class
        SharedHamburger.cookHamburger(this, resourceTitle);

        // prepare the view objects

        // Check Network and Internet, close Activity if no internet
        NetworkState nt = new NetworkState(this);
        nt.closeIfNoConnection();

        fetchShowbiz(showbiz.getId(), showbiz.getShowbizType());

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


    public void fetchShowbiz(int id, final String showbizType) {
        client.getShowbiz(id, showbizType, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("mesage"));
                        Toast.makeText(ShowbizProfileActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                    } else {

                        // Parse top models from JSON
                        if(showbizType.equals("Band")) {
                            showbiz = Band.fromJSON(response.getJSONObject("band"));
                            displayBand();

                        } else if(showbizType.equals("Musician")) {
                            showbiz = Musician.fromJSON(response.getJSONObject("musician"));
                            displayMusician();

                        } else if(showbizType.equals("Album")) {
                            showbiz = Album.fromJSON(response.getJSONObject("album"));
                            //displayFullAlbum();

                        } else if(showbizType.equals("Song")) {
                            showbiz = Song.fromJSON(response.getJSONObject("song"));
                            //displayFullSong();
                        }
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

    public void displayBand() {
        Band band = (Band) showbiz;

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        TextView tvMusiciansCount = (TextView) findViewById(R.id.tvMusiciansCount);
        TextView tvLabelMusicians = (TextView) findViewById(R.id.tvLabelMusicians);
        TextView tvAlbumsCount = (TextView) findViewById(R.id.tvAlbumsCount);
        TextView tvSongsCount = (TextView) findViewById(R.id.tvSongsCount);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ImageView ivWall = (ImageView) findViewById(R.id.ivWall);

        tvName.setText(band.getName());
        tvGenre.setText(band.getGenreName());

        tvMusiciansCount.setText(String.valueOf(0));
        if (band.getMusicians() != null) {
            tvMusiciansCount.setText(String.valueOf(band.getMusicians().size()));
        }
        tvMusiciansCount.setVisibility(View.VISIBLE);
        tvLabelMusicians.setVisibility(View.VISIBLE);

        tvAlbumsCount.setText(String.valueOf(0));
        if (band.getAlbums() != null) {
            tvAlbumsCount.setText(String.valueOf(band.getAlbums().size()));
        }

        tvSongsCount.setText(String.valueOf(0));
        if (band.getSongs() != null) {
            tvSongsCount.setText(String.valueOf(band.getSongs().size()));
        }

        tvRespectsCount.setText(String.valueOf(band.getRespects()));

        // load wall image
        String wallUrl = band.getBigWallPicture();
        Picasso.with(this).load(wallUrl)
                .into(ivWall);

        // load profile image
        String profileUrl = band.getSmallProfilePicture();
        Picasso.with(this).load(profileUrl)
                .into(ivProfile);


        // set Members
        if(band.getMusicians() != null && band.getMusicians().size() > 0) {
            setupMusicianMembers(band.getMusicians());
        }

        if(band.getAlbums() != null && (band.getAlbums().size() > 0)) {
            setupAlbumMembers(band.getAlbums());
        }

        if(band.getSongs() != null && band.getSongs().size() > 0) {
            setupSongMembers(band.getSongs(), "Songs");
        }
    }

    public void displayMusician() {
        Musician musician = (Musician) showbiz;

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        TextView tvAlbumsCount = (TextView) findViewById(R.id.tvAlbumsCount);
        TextView tvSongsCount = (TextView) findViewById(R.id.tvSongsCount);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ImageView ivWall = (ImageView) findViewById(R.id.ivWall);

        tvName.setText(musician.getName());
        tvGenre.setText(musician.getGenreName());

        tvAlbumsCount.setText(String.valueOf(0));
        if (musician.getAlbums() != null) {
            tvAlbumsCount.setText(String.valueOf(musician.getAlbums().size()));
        }

        tvSongsCount.setText(String.valueOf(0));
        if (musician.getSongs() != null) {
            tvSongsCount.setText(String.valueOf(musician.getSongs().size()));
        }

        tvRespectsCount.setText(String.valueOf(musician.getRespects()));

        // load wall image
        String wallUrl = musician.getBigWallPicture();
        Picasso.with(this).load(wallUrl)
                .into(ivWall);

        // load profile image
        String profileUrl = musician.getSmallProfilePicture();
        Picasso.with(this).load(profileUrl)
                .into(ivProfile);

        // set Members
        if(musician.getAlbums() != null && musician.getAlbums().size() > 0) {
            setupAlbumMembers(musician.getAlbums());
        }

        if(musician.getSongs() != null && musician.getSongs().size() > 0) {
            setupSongMembers(musician.getSongs(), "Songs");
        }

        if(musician.getFeaturingSongs() != null && musician.getFeaturingSongs().size() > 0) {
            setupFeaturingSongMembers(musician.getFeaturingSongs(), "Featuring Songs");
        }
    }

    public void setupMusicianMembers(ArrayList<Musician> musicians) {
        // setup showbiz member Fragment
        ArrayList<Showbiz> mShowbizs = new ArrayList<>();
        mShowbizs.addAll(musicians);

        ShowbizMembersFragment fgMusician = ShowbizMembersFragment.newInstance(mShowbizs, "Musician", false);
        FragmentTransaction ftMusician = getSupportFragmentManager().beginTransaction();
        ftMusician.replace(R.id.flMusicians, fgMusician);
        ftMusician.commit();
    }

    public void setupAlbumMembers(ArrayList<Album> albums) {
        // setup showbiz member Fragment
        ArrayList<Showbiz> aShowbizs = new ArrayList<>();
        aShowbizs.addAll(albums);

        ShowbizMembersFragment fgAlbum = ShowbizMembersFragment.newInstance(aShowbizs, "Album", false);
        FragmentTransaction ftAlbum = getSupportFragmentManager().beginTransaction();
        ftAlbum.replace(R.id.flAlbums, fgAlbum);
        ftAlbum.commit();
    }

    public void setupSongMembers(ArrayList<Song> songs, String title) {
        // setup songs Fragment
        SongsFragment fgSong = SongsFragment.newInstance(songs, true, title);
        FragmentTransaction ftSong = getSupportFragmentManager().beginTransaction();
        ftSong.replace(R.id.flSongs, fgSong);
        ftSong.commit();
    }

    public void setupFeaturingSongMembers(ArrayList<Song> songs, String title) {
        // setup songs Fragment
        SongsFragment fgSong = SongsFragment.newInstance(songs, true, title);
        FragmentTransaction ftSong = getSupportFragmentManager().beginTransaction();
        ftSong.replace(R.id.flFeaturingSongs, fgSong);
        ftSong.commit();
    }
}
