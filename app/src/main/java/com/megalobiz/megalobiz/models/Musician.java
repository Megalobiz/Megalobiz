package com.megalobiz.megalobiz.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/18/2016.
 */

public class Musician extends Showbiz {

    private User user;
    private Band band;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;
    private ArrayList<Song> featuringSongs;

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Song> getFeaturingSongs() {
        return featuringSongs;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public User getUser() {
        return user;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Musician() {
        showbizType = "Musician";
    }

    public static Musician fromJSON(JSONObject json) {
        Musician musician = new Musician();

        try {
            musician = (Musician) Showbiz.fromJSON(json, musician);

            musician.id = json.getInt("musician_id");
            musician.name = json.getString("musician_name");
            musician.respects = json.getInt("respects");

            // pictures
            if(json.has("pictures")) {
                // get profile basepath
                musician.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
                // get profile path
                musician.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");
                // get wall basepath
                musician.wallBasepath = json.getJSONObject("pictures").getJSONObject("wall").getString("base_path");
                // get profile path
                musician.wallFilename = json.getJSONObject("pictures").getJSONObject("wall").getString("path");
            }

            // Genre Name
            if(json.has("music_genre")) {
                musician.genreName = json.getJSONObject("music_genre").getString("genre_name");
            }

            // Collection Models
            // Albums
            if(json.has("albums")) {
                musician.albums = Album.fromJSONArray(json.getJSONArray("albums"));
            }

            // Songs
            if(json.has("owned_songs")) {
                musician.songs = Song.fromJSONArray(json.getJSONArray("owned_songs"));
            }

            // featuring songs
            if(json.has("featuring_songs")) {
                if(json.getJSONArray("featuring_songs") != null) {
                    musician.featuringSongs = Song.fromJSONArray(json.getJSONArray("featuring_songs"));

                } else if(json.getJSONObject("featuring_songs") != null) {
                    JSONArray jsonSongs = new JSONArray();

                    if(json.getJSONObject("featuring_songs").has("11")) {
                        JSONObject jsonSong = json.getJSONObject("featuring_songs").getJSONObject("11");
                        jsonSongs.put(jsonSong);
                        musician.featuringSongs = Song.fromJSONArray(jsonSongs);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return musician;
    }

    public static ArrayList<Musician> fromJSONArray(JSONArray json) {
        ArrayList<Musician> musicians = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                Musician musician = Musician.fromJSON(json.getJSONObject(i));

                if (musician != null) {
                    musicians.add(musician);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return musicians;
    }
}
