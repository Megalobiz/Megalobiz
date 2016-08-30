package com.megalobiz.megalobiz.activities.helpers;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by KeitelRobespierre on 8/28/2016.
 */
public class SongPlayer {

    private static SongPlayer _instance;

    Context context;
    Activity activity;
    View view;

    private MediaPlayer mediaPlayer;
    private Song loadedSong;
    private ArrayList<Song> songs;
    private String songsListId;

    // player views
    private TextView tvCurrentSong;
    private ImageView play;
    private ImageView stop;
    private ImageView next;
    private ImageView previous;
    private SeekBar sbPlayer;
    private TextView tvProgressTime;
    private TextView tvTotalTime;

    private Boolean isStopped = true;

    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    public static int oneTimeOnly = 0;


    public String getSongsListId() {
        return songsListId;
    }

    public synchronized static SongPlayer getInstance()
    {
        if (_instance == null)
        {
            _instance = new SongPlayer();
        }
        return _instance;
    }

    public void init(Context context, View view, ArrayList<Song> songs, String songsListId) {

        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        this.context = context;
        this.activity = (Activity) context;
        this.view = view;
        this.songs = songs;
        this.songsListId = songsListId;

        // before setting up views, make sure to reset previous views if already initialized
        if (songsListId != null) {
            stopSong();
        }

        tvCurrentSong = (TextView) view.findViewById(R.id.tvCurrentSong);
        play = (ImageView) view.findViewById(R.id.ivPlay);
        stop = (ImageView) view.findViewById(R.id.ivStop);
        next = (ImageView) view.findViewById(R.id.ivNext);
        previous = (ImageView) view.findViewById(R.id.ivPrevious);
        sbPlayer = (SeekBar) view.findViewById(R.id.sbPlayer);
        tvProgressTime = (TextView) view.findViewById(R.id.tvProgressTime);
        tvTotalTime = (TextView) view.findViewById(R.id.tvTotalTime);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSong();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousSong();
            }
        });

        sbPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!isStopped && mediaPlayer != null) {
                    int p = seekBar.getProgress();
                    mediaPlayer.seekTo((int) (p * finalTime / 1000));
                }
            }
        });
    }

    public void initializeSongPlayer() {
        stopSong();

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
                play.setImageResource(R.drawable.mb_song_player_pause);
                isStopped = false;

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();
                sbPlayer.setProgress((int)startTime);
                displayTime(tvProgressTime, (long) startTime);
                displayTime(tvTotalTime, (long) finalTime);

                myHandler.postDelayed(UpdateSongTime, 100);
            }
        });

        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });*/

    }

    public void selectSong(int index) {
        loadedSong = songs.get(index);
        tvCurrentSong.setText(String.format("Song : %s", loadedSong.getName()));

        displayTime(tvProgressTime, (long) startTime);
        displayTime(tvTotalTime, (long) finalTime);
    }

    public void loadSong() {
        String url = loadedSong.getFullPath();
        //url = "https://dl.dropboxusercontent.com/u/10281242/sample_audio.mp3";
        //url = "http://192.168.1.100/songs/album/album_1000/song_You%20don't%20want%20me_1379645637.mp3";
        url = url.replace(" ", "%20");

        // Set the data source to the remote URL
        try {
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(context, "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(context, "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(!isStopped) {
                startTime = mediaPlayer.getCurrentPosition();

                displayTime(tvProgressTime, (long) startTime);
                displayTime(tvTotalTime, (long) finalTime);

                sbPlayer.setProgress((int) (startTime * 1000 / finalTime));

                myHandler.postDelayed(this, 100);
            }
        }
    };

    public void prepareSong() {
        // Trigger an async preparation which will file listener when completed
        try {
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(context, "The Song could not be found on the Server! "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void playSong() {
        if(mediaPlayer == null) {
            return;
        }

        if(isStopped) {
            initializeSongPlayer();
            // load song
            loadSong();
            prepareSong();
        } else {
            if (mediaPlayer.isPlaying()) {
                pauseSong();
            } else {
                mediaPlayer.start();
                play.setImageResource(R.drawable.mb_song_player_pause);
            }
        }
    }

    public void stopSong() {
        if(mediaPlayer != null) {
            if (!isStopped) {
                mediaPlayer.stop();
                mediaPlayer.release();
                play.setImageResource(R.drawable.mb_song_player_play);
                isStopped = true;

                myHandler.removeCallbacks(UpdateSongTime);
                sbPlayer.setProgress(0);
            }
        }
    }

    public void pauseSong() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
            play.setImageResource(R.drawable.mb_song_player_play);
        }
    }

    public void nextSong() {
        int curIndex = songs.indexOf(loadedSong);

        if(curIndex < songs.size()) {
            initializeSongPlayer();
            // load song
            selectSong(curIndex + 1);
            loadSong();
            // play song
            prepareSong();
        }
    }

    public void previousSong() {
        int curIndex = songs.indexOf(loadedSong);

        if(curIndex > 0) {
            initializeSongPlayer();
            // load song
            selectSong(curIndex - 1);
            loadSong();
            // play song
            prepareSong();
        }
    }

    public void displayTime(TextView tvTime, long time) {
        tvTime.setText(String.format("%d.%02d",

                TimeUnit.MILLISECONDS.toMinutes((long) time),
                TimeUnit.MILLISECONDS.toSeconds((long) time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) time)))
        );
    }
}
