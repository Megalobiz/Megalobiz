package com.megalobiz.megalobiz.fragments;

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
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;
import com.megalobiz.megalobiz.activities.helpers.SongPlayer;
import com.megalobiz.megalobiz.models.Song;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by KeitelRobespierre on 8/20/2016.
 */
public class SongsFragment extends Fragment {

    SongPlayer songPlayer;
    private ArrayList<Song> songs;
    private TableLayout table;
    private boolean forTop;
    private String title;
    private TextView tvTitle;
    private View rootView;
    private String songsListId;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_songs, container, false);
        rootView = v;

        table = (TableLayout) v.findViewById(R.id.tlSongs);
        tvTitle = (TextView) v.findViewById(R.id.tvTopSongs);

        songPlayer = SongPlayer.getInstance();

        if(songPlayer.getSongsListId() == null) {
            songPlayer.init(getContext(), v, songs, songsListId);

            // setup Media Player
            songPlayer.initializeSongPlayer();
            // select song
            songPlayer.selectSong(0);
            // set DataSource
            songPlayer.loadSong();
        }

        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set unique id to this songs list
        songsListId = UUID.randomUUID().toString();

        songs = (ArrayList<Song>) getArguments().getSerializable("songs");
        forTop = getArguments().getBoolean("forTop");
        title = getArguments().getString("title");

        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateSongsTable();
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

    @Override
    public void onResume() {
        super.onResume();

        if(songPlayer.getSongsListId() == null) {
            songPlayer.init(getContext(), rootView, songs, songsListId);
            // setup Media Player
            songPlayer.initializeSongPlayer();
            // select song
            songPlayer.selectSong(0);
            // set DataSource
            songPlayer.loadSong();
        }

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
        TextView tvOwner = (TextView) v.findViewById(R.id.tvAlbumsCount);

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
                SharedMenu.launchShowbizProfile(getActivity(), song);
            }
        });

        // set on click listener on song name to play song
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songPlayer.getSongsListId() != songsListId) {
                    songPlayer.init(getContext(), rootView, songs, songsListId);
                }

                songPlayer.initializeSongPlayer();
                // load song
                songPlayer.selectSong(songs.indexOf(song));
                songPlayer.loadSong();
                // play song
                songPlayer.prepareSong();
            }
        });


        return v;
    }

}
