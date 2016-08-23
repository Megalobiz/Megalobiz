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
            song.totalNote = json.getInt("total_note");

            song.fullPath = json.getString("full_path");

            // pictures
            // get profile basepath
            song.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            song.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");

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


