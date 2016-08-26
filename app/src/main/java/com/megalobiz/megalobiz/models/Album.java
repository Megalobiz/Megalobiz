package com.megalobiz.megalobiz.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/18/2016.
 */
public class Album extends Showbiz {

    private SimpleDateFormat releaseDate;
    private int year;
    private float rating;

    private Showbiz owner;
    private ArrayList<Song> songs;


    public Showbiz getOwner() {
        return owner;
    }

    public float getRating() {
        return rating;
    }

    public SimpleDateFormat getReleaseDate() {
        return releaseDate;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getYear() {
        return year;
    }

    public Album() {
        showbizType = "Album";
    }

    public static Album fromJSON(JSONObject json) {
        Album album = new Album();

        try {
            album.id = json.getInt("album_id");
            album.name = json.getString("album_name");
            album.year = json.getInt("theyear");
            album.respects = json.getInt("respects");

            // pictures
            // get profile basepath
            album.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            album.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");

            // Genre Name
            // Genre Name
            if(json.has("music_genre")) {
                album.genreName = json.getJSONObject("music_genre").getString("genre_name");
            }

            // owner (Band or Musician)
            if(json.has("owner")) {
                JSONObject jsonOwner = json.getJSONObject("owner");

                // if owner is a Band
                if(jsonOwner.has("band_name")) {
                    album.owner = Band.fromJSON(jsonOwner);
                }
                else if(jsonOwner.has("musician_name")) {
                    //owner is a Musician
                    album.owner = Musician.fromJSON(jsonOwner);
                }
            }

            // Collection Models
            // Songs
            if(json.has("songs")) {
                album.songs = Song.fromJSONArray(json.getJSONArray("songs"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return album;
    }

    public static ArrayList<Album> fromJSONArray(JSONArray json) {
        ArrayList<Album> albums = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                Album album = Album.fromJSON(json.getJSONObject(i));

                if (album != null) {
                    albums.add(album);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return albums;
    }
}

