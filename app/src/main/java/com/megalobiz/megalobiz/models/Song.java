package com.megalobiz.megalobiz.models;

import com.megalobiz.megalobiz.MegalobizApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/18/2016.
 */
public class Song extends Showbiz {

    private int track;
    private int year;
    private int totalNote;
    private int rateCount;
    private String fullPath;
    private int length;

    private Showbiz owner;
    private ArrayList<Musician> featuringMusicians;

    public ArrayList<Musician> getFeaturingMusicians() {
        return featuringMusicians;
    }

    public int getLength() {
        return length;
    }

    public Showbiz getOwner() {
        return owner;
    }

    public int getRateCount() {
        return rateCount;
    }

    public int getTotalNote() {
        return totalNote;
    }

    public int getTrack() {
        return track;
    }

    public int getYear() {
        return year;
    }

    public String getFullPath() {
        return String.format("%s%s", MegalobizApi.HOST, fullPath);
    }

    public Song() {
        showbizType = "Song";
    }

    public static Song fromJSON(JSONObject json) {
        Song song = new Song();

        try {
            song.id = json.getInt("song_id");
            song.name = json.getString("song_name");
            song.year = json.getInt("theyear");
            song.track = json.getInt("track");
            song.length = json.getInt("length");
            song.totalNote = json.getInt("total_note");
            song.rateCount = json.getInt("rate_count");
            song.respects = json.getInt("respects");

            song.fullPath = json.getString("full_path");

            // pictures
            // get profile basepath
            song.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            song.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");

            // Genre Name
            // Genre Name
            if(json.has("music_genre")) {
                song.genreName = json.getJSONObject("music_genre").getString("genre_name");
            }

            // Owner of Song (Album, Band or Musician)
            // if owner is a Band
            if(json.has("owner")) {
                JSONObject jsonOwner = json.getJSONObject("owner");

                // if owner is a Band
                if(jsonOwner.has("album_name")) {
                    song.owner = Album.fromJSON(jsonOwner);
                }
                else if(jsonOwner.has("band_name")) {
                    song.owner = Band.fromJSON(jsonOwner);
                }
                else if(jsonOwner.has("musician_name")) {
                    //owner is a Musician
                    song.owner = Musician.fromJSON(jsonOwner);
                }
            }

            // Collection Models
            // Featuring Musicians
            if(json.has("featuring_musicians")) {
                if(json.getJSONArray("featuring_musicians") != null) {
                    song.featuringMusicians = Musician.fromJSONArray(json.getJSONArray("featuring_musicians"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return song;
    }

    public static ArrayList<Song> fromJSONArray(JSONArray json) {
        ArrayList<Song> songs = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                Song song = Song.fromJSON(json.getJSONObject(i));

                if (song != null) {
                    songs.add(song);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return songs;
    }
}


