package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.Auth;
import com.megalobiz.megalobiz.activities.helpers.GlobalAnimation;
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
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShowbizProfileActivity extends AppCompatActivity {

    private MegalobizClient client;
    private String showbizType;
    private Showbiz showbiz;

    private Boolean firstRun = true;
    private int resourceTitle;
    private int backupMyRating;

    //Views attributes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //find Showbiz model
        showbiz = (Showbiz) getIntent().getSerializableExtra("showbiz");
        showbizType = showbiz.getShowbizType();

        if (showbiz.getName() == null) {
            Toast.makeText(this, showbizType+" Profile error", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (showbizType.equals("Band") || showbizType.equals("Musician")) {
            setContentView(R.layout.activity_showbiz_profile);
        } else if (showbizType.equals("Album") || showbizType.equals("Song")) {
            setContentView(R.layout.activity_showbiz_media_profile);
        }

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

        // check Network and Internet, close Activity if no internet
        NetworkState nt = new NetworkState(this);
        nt.closeIfNoConnection();

        try {
            fetchShowbiz(showbiz.getId(), showbiz.getShowbizType());
        } catch (Exception e) {
            e.printStackTrace();
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
                        Log.d("DEBUG", response.getString("message"));
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
                            displayAlbum();

                        } else if(showbizType.equals("Song")) {
                            showbiz = Song.fromJSON(response.getJSONObject("song"));
                            displaySong();
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
            setupMusicianMembers(band.getMusicians(), null);
        }

        if(band.getAlbums() != null && (band.getAlbums().size() > 0)) {
            setupAlbumMembers(band.getAlbums());
        }

        if(band.getSongs() != null && band.getSongs().size() > 0) {
            setupSongMembers(band.getSongs(), "Songs");
        }

        // click listeners
        if(Auth.check()) {
            setupRespectListener();
        }
    }

    public void displayMusician() {
        Musician musician = (Musician) showbiz;

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvBand = (TextView) findViewById(R.id.tvBand);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        TextView tvAlbumsCount = (TextView) findViewById(R.id.tvAlbumsCount);
        TextView tvSongsCount = (TextView) findViewById(R.id.tvSongsCount);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        ImageView ivWall = (ImageView) findViewById(R.id.ivWall);

        tvName.setText(musician.getName());
        tvGenre.setText(musician.getGenreName());
        //tvBand.setText(musician);

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

        // click listeners
        if(Auth.check()) {
            setupRespectListener();
        }
    }

    public void displayAlbum() {
        final Album album = (Album) showbiz;

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        final TextView tvOwner = (TextView) findViewById(R.id.tvOwner);
        TextView tvLabelOwner = (TextView) findViewById(R.id.tvLabelOwner);
        TextView tvSongsCount = (TextView) findViewById(R.id.tvSongsCount);
        TextView tvLabelSongs = (TextView) findViewById(R.id.tvLabelSongs);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        TextView tvRating = (TextView) findViewById(R.id.tvRating);
        RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);

        tvName.setText(album.getName());
        tvGenre.setText(album.getGenreName());

        if(album.getOwner() != null) {
            tvLabelOwner.setText(album.getOwner().getShowbizType());
            tvOwner.setText(album.getOwner().getName());
        }

        tvSongsCount.setText(String.valueOf(0));
        if (album.getSongs() != null) {
            tvSongsCount.setText(String.valueOf(album.getSongs().size()));
        }
        tvSongsCount.setVisibility(View.VISIBLE);
        tvLabelSongs.setVisibility(View.VISIBLE);

        tvRespectsCount.setText(String.valueOf(album.getRespects()));
        tvYear.setText(String.valueOf(album.getYear()));

        // rating
        tvRating.setText(String.format("%.1f", album.getRating()));
        rbRating.setRating(album.getRating());
        rbRating.setScaleX(1.5f);
        rbRating.setScaleY(1.5f);

        // load profile image
        String profileUrl = album.getSmallProfilePicture();
        Picasso.with(this).load(profileUrl)
                .into(ivProfile);
        
        if(album.getSongs() != null && album.getSongs().size() > 0) {
            setupSongMembers(album.getSongs(), "Songs");
        }

        // click listeners
        if(album.getOwner() != null) {
            tvOwner.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    GlobalAnimation.animateTextView(tvOwner);
                    launchShowbizProfile(album.getOwner());
                }
            });
        }

        if(Auth.check()) {
            setupRespectListener();
        }
    }

    public void displaySong() {
        final Song song = (Song) showbiz;

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        final TextView tvOwner = (TextView) findViewById(R.id.tvOwner);
        TextView tvLabelOwner = (TextView) findViewById(R.id.tvLabelOwner);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
        TextView tvRating = (TextView) findViewById(R.id.tvRating);
        RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);
        TextView tvTotalVotes = (TextView) findViewById(R.id.tvTotalVotes);
        TextView tvMyVote = (TextView) findViewById(R.id.tvMyVote);
        RatingBar rbMyRating = (RatingBar) findViewById(R.id.rbMyRating);

        tvName.setText(song.getName());
        tvGenre.setText(song.getGenreName());

        if(song.getOwner() != null) {
            tvLabelOwner.setText(song.getOwner().getShowbizType());
            tvOwner.setText(song.getOwner().getName());
        }

        tvRespectsCount.setText(String.valueOf(song.getRespects()));
        tvYear.setText(String.valueOf(song.getYear()));

        // rating
        tvRating.setText(String.format("%.1f", song.getRating()));
        rbRating.setRating(song.getRating());
        rbRating.setScaleX(1.5f);
        rbRating.setScaleY(1.5f);
        if(song.getRateCount() == 0) {
            tvTotalVotes.setText(R.string.no_vote_yet);
        } else {
            tvTotalVotes.setText(String.format("%d votes", song.getRateCount()));
        }

        tvTotalVotes.setVisibility(View.VISIBLE);

        // my rating only for Authenticated User
        if(Auth.check()) {
            rbMyRating.setRating(song.getMyRating());
            rbMyRating.setVisibility(View.VISIBLE);
            tvMyVote.setVisibility(View.VISIBLE);
        }

        // load profile image
        String profileUrl = song.getSmallProfilePicture();
        Picasso.with(this).load(profileUrl)
                .into(ivProfile);

        if(song.getFeaturingMusicians() != null && song.getFeaturingMusicians().size() > 0) {
            setupMusicianMembers(song.getFeaturingMusicians(), "Featuring Musicians");
        }

        ArrayList<Song> songs = new ArrayList<>();
        songs.add(song);
        setupSongMembers(songs, "Song");

        // click listeners
        if(song.getOwner() != null) {
            tvOwner.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    GlobalAnimation.animateTextView(tvOwner);
                    launchShowbizProfile(song.getOwner());
                }
            });
        }

        // click listeners
        if(Auth.check()) {
            setupRespectListener();
            setupRatingListener();
        }
    }

    public void setupMusicianMembers(ArrayList<Musician> musicians, String title) {
        try {
            // setup showbiz member Fragment
            ArrayList<Showbiz> mShowbizs = new ArrayList<>();
            mShowbizs.addAll(musicians);

            ShowbizMembersFragment fgMusician = ShowbizMembersFragment.newInstance(mShowbizs, "Musician", false);
            if (title != null) {
                fgMusician.setTitle(title);
            }
            FragmentTransaction ftMusician = getSupportFragmentManager().beginTransaction();
            ftMusician.replace(R.id.flMusicians, fgMusician);
            ftMusician.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupAlbumMembers(ArrayList<Album> albums) {
        try {
            // setup showbiz member Fragment
            ArrayList<Showbiz> aShowbizs = new ArrayList<>();
            aShowbizs.addAll(albums);

            ShowbizMembersFragment fgAlbum = ShowbizMembersFragment.newInstance(aShowbizs, "Album", false);
            FragmentTransaction ftAlbum = getSupportFragmentManager().beginTransaction();
            ftAlbum.replace(R.id.flAlbums, fgAlbum);
            ftAlbum.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupSongMembers(ArrayList<Song> songs, String title) {
        try {
            // setup songs Fragment
            SongsFragment fgSong = SongsFragment.newInstance(songs, true, title);
            FragmentTransaction ftSong = getSupportFragmentManager().beginTransaction();
            ftSong.replace(R.id.flSongs, fgSong);
            ftSong.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupFeaturingSongMembers(ArrayList<Song> songs, String title) {
        try {
            // setup songs Fragment
            SongsFragment fgSong = SongsFragment.newInstance(songs, true, title);
            FragmentTransaction ftSong = getSupportFragmentManager().beginTransaction();
            ftSong.replace(R.id.flFeaturingSongs, fgSong);
            ftSong.commit();

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchShowbizProfile(Showbiz showbiz) {
        Intent i = new Intent(this, ShowbizProfileActivity.class);
        i.putExtra("showbiz", showbiz);
        startActivity(i);
    }

    public void setupRespectListener() {
        ImageView ivRespect = (ImageView) findViewById(R.id.ivRespect);
        TextView tvRespectState = (TextView) findViewById(R.id.tvRespectState);

        if(showbiz.getRespected()) {
            ivRespect.setImageResource(R.drawable.mb_icon_respected);
            ivRespect.setScaleX(1.3f);
            ivRespect.setScaleY(1.3f);
            tvRespectState.setTextColor(Color.parseColor("#158EC6"));
        }

        ivRespect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ShowbizProfileActivity.this, "Respect", Toast.LENGTH_SHORT).show();
                if(showbiz.getRespected()) {
                    unrespect();
                } else {
                    respect();
                }
            }
        });
    }

    public void respect() {
        // set as respected before sending the request in background
        setAsRespected();

        //if error is true or failure we call setAsUnrespected to reset
        client.respectShowbiz(showbiz, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("message"));
                        Toast.makeText(ShowbizProfileActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                        setAsUnrespected();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    setAsUnrespected();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                setAsUnrespected();
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setAsUnrespected();
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", throwable.getMessage());
            }
        });

    }

    public void unrespect() {
        // set as respected before sending the request in background
        setAsUnrespected();

        //if error is true or failure we call setAsRespected to reset
        client.unrespectShowbiz(showbiz, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("message"));
                        Toast.makeText(ShowbizProfileActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                        setAsRespected();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    setAsRespected();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                setAsRespected();
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                setAsRespected();
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", throwable.getMessage());
            }
        });
    }

    public void setAsRespected() {
        ImageView ivRespect = (ImageView) findViewById(R.id.ivRespect);
        TextView tvRespectState = (TextView) findViewById(R.id.tvRespectState);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);

        ivRespect.setImageResource(R.drawable.mb_icon_respected);
        showbiz.setRespected(true);

        int duration = 200;
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(ivRespect, "alpha", 0.1f, 1.0f).setDuration(duration);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(ivRespect, "scaleX", 1.0f, 1.3f).setDuration(duration);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(ivRespect, "scaleY", 1.0f, 1.3f).setDuration(duration);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fadeAnim, scaleXAnim, scaleYAnim);
        animSet.start();

        tvRespectState.setText(R.string.respected);
        tvRespectState.setTextColor(Color.parseColor("#158EC6"));
        showbiz.incrementRespects();
        tvRespectsCount.setText(String.valueOf(showbiz.getRespects()));
    }

    public void setAsUnrespected() {
        ImageView ivRespect = (ImageView) findViewById(R.id.ivRespect);
        TextView tvRespectState = (TextView) findViewById(R.id.tvRespectState);
        TextView tvRespectsCount = (TextView) findViewById(R.id.tvRespectsCount);

        ivRespect.setImageResource(R.drawable.mb_icon_unrespected);
        showbiz.setRespected(false);

        int duration = 200;
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(ivRespect, "alpha", 0.1f, 1.0f).setDuration(duration);
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(ivRespect, "scaleX", 1.3f, 1.0f).setDuration(duration);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(ivRespect, "scaleY", 1.3f, 1.0f).setDuration(duration);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fadeAnim, scaleXAnim, scaleYAnim);
        animSet.start();

        tvRespectState.setText(R.string.unrespected);
        tvRespectState.setTextColor(Color.parseColor("#222222"));
        showbiz.decrementRespects();
        tvRespectsCount.setText(String.valueOf(showbiz.getRespects()));
    }

    public void setupRatingListener() {
        Song song = (Song) showbiz;
        // set backup rating
        backupMyRating = song.getMyRating();

        final RatingBar rbMyRating = (RatingBar) findViewById(R.id.rbMyRating);

        rbMyRating.setScaleX(1.5f);
        rbMyRating.setScaleY(1.5f);

        rbMyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //Toast.makeText(ShowbizProfileActivity.this, "Rated: "+ String.valueOf(rbMyRating.getRating()), Toast.LENGTH_SHORT).show();
                setRating((int) rbMyRating.getRating());
            }
        });
    }

    public void setRating(final int rating) {
        final RatingBar rbMyRating = (RatingBar) findViewById(R.id.rbMyRating);
        //if error is true or failure we set backupMyRating to reset
        client.rateShowbiz(showbiz, rating, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("error")) {
                        Log.d("DEBUG", response.getString("message"));
                        Toast.makeText(ShowbizProfileActivity.this,
                                "Error: " + response.getString("message"), Toast.LENGTH_LONG).show();
                        rbMyRating.setRating(backupMyRating);
                    } else {
                        backupMyRating = rating;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    rbMyRating.setRating(backupMyRating);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                rbMyRating.setRating(backupMyRating);
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                rbMyRating.setRating(backupMyRating);
                Toast.makeText(ShowbizProfileActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", throwable.getMessage());
            }
        });
    }
}
