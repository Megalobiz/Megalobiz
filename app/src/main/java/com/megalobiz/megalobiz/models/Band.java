package com.megalobiz.megalobiz.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/13/2016.
 */
public class Band extends Showbiz {

    private ArrayList<Musician> musicians;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public ArrayList<Musician> getMusicians() {
        return musicians;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Band() {
        showbizType = "Band";
    }

    public static Band fromJSON(JSONObject json) {
        Band band = new Band();

        try {
            band = (Band) Showbiz.fromJSON(json, band);

            band.id = json.getInt("band_id");
            band.name = json.getString("band_name");
            band.respects = json.getInt("respects");

            // pictures
            if(json.has("pictures")) {
                // get profile basepath
                band.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
                // get profile path
                band.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");
                // get wall basepath
                band.wallBasepath = json.getJSONObject("pictures").getJSONObject("wall").getString("base_path");
                // get profile path
                band.wallFilename = json.getJSONObject("pictures").getJSONObject("wall").getString("path");
            }

            // Genre Name
            if(json.has("music_genre")) {
                band.genreName = json.getJSONObject("music_genre").getString("genre_name");
            }

            // Collection Models
            // Musicians
            if(json.has("musicians")) {
                band.musicians = Musician.fromJSONArray(json.getJSONArray("musicians"));
            }

            // Albums
            if(json.has("albums")) {
                band.albums = Album.fromJSONArray(json.getJSONArray("albums"));
            }

            // Songs
            if(json.has("songs")) {
                band.songs = Song.fromJSONArray(json.getJSONArray("songs"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return band;
    }

    public static ArrayList<Band> fromJSONArray(JSONArray json) {
        ArrayList<Band> bands = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                Band band = Band.fromJSON(json.getJSONObject(i));

                if (band != null) {
                    bands.add(band);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bands;
    }
}
