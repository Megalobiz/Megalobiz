package com.megalobiz.megalobiz.fragments;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.TopShowbizActivity;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.models.Song;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/20/2016.
 */
public class SongsFragment extends Fragment {

    MediaPlayer mediaPlayer;
    private ArrayList<Song> songs;
    private TableLayout table;
    private boolean forTop;
    private String title;
    private TextView tvTitle;
    private Song loadedSong;
    private TextView tvCurrentSong;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_top_songs, container, false);
        //parentView = v;
        table = (TableLayout) v.findViewById(R.id.tlSongs);
        tvCurrentSong = (TextView) v.findViewById(R.id.tvCurrentSong);
        tvTitle = (TextView) v.findViewById(R.id.tvTopSongs);
        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songs = (ArrayList<Song>) getArguments().getSerializable("songs");
        forTop = getArguments().getBoolean("forTop");
        title = getArguments().getString("title");
        //mediaPlayer = new MediaPlayer();
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateSongsTable();

        // load default first song
        loadSong(songs.get(0));
    }

    public static SongsFragment newInstance(ArrayList<Song> songs, boolean forTop, String title) {
        SongsFragment fg = new SongsFragment();
        Bundle args = new Bundle();
        args.putSerializable("songs", songs);
        args.putBoolean("forTop", forTop);
        args.putString("title", title);
        fg.setArguments(args);

        return fg;
    }

    public void populateSongsTable() {
        //Set title
        tvTitle.setText(title);

        // Rows - count songs
        for (int i = 0; i < songs.size(); i++) {

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // 1 column - no need for iteration
            View v = createSongView(songs.get(i), i + 1);

            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            //tv.setBackgroundResource(R.drawable.cell_shape);
            v.setPadding(5, 5, 5, 5);

            row.addView(v);

            table.addView(row);
        }
    }

    public View createSongView(final Song song, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.top_song, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvMusicianPosition = (TextView) v.findViewById(R.id.tvPosition);
        TextView tvOwner = (TextView) v.findViewById(R.id.tvOwner);

        ImageView imageView = (ImageView) v.findViewById(R.id.ivProfilePicture);
        //imageView.setBackgroundResource(0);

        tvName.setText(song.getName());
        tvMusicianPosition.setText(String.valueOf(position));

        String owner = "Unknown Artist";
        if(song.getOwner() != null) {
            owner = String.format("%s: %s", song.getOwner().getShowbizType(), song.getOwner().getName());

        }

        tvOwner.setText(owner);

        // load image
        /*
        String imageUrl = song.getSmallProfilePicture();
        Picasso.with(getContext()).load(imageUrl).into(imageView);
        */

        // set on click listener on image view to go to son page
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedMenu.launchShowbizProfile(getContext(), song);
            }
        });

        // set on click listener on song name to play song
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SongsFragment.this.getContext(),
                        "song is starting...", Toast.LENGTH_SHORT).show();
                // load song
                loadSong(song);
                // play song
                playSong();
            }
        });


        return v;
    }

    private void loadSong(Song song) {
        this.loadedSong = song;
        tvCurrentSong.setText(String.format("Song : %s", song.getName()));
    }

    private void playSong() {

        String url = loadedSong.getFullPath();

        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        //url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3";
        //url = "http://192.168.1.100/songs/album/album_1000/song_You%20don't%20want%20me_1379645637.mp3";
        url = url.replace(" ", "%20");

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Listen for if the audio file can't be prepared
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // ... react appropriately ...
                // The MediaPlayer has moved to the Error state, must be reset!
                return false;
            }
        });

        // Attach to when audio file is prepared for playing
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });*/

        // Set the data source to the remote URL
        try {
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getContext(), "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getContext(), "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Trigger an async preparation which will file listener when completed
        try {
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(getContext(), "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
