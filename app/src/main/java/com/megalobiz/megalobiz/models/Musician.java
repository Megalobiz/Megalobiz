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
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;
    private ArrayList<Song> featuringSongs;

    public static Musician fromJSON(JSONObject json) {
        Musician musician = new Musician();
        musician.showbizType = "Musician";

        try {
            musician.id = json.getInt("musician_id");
            musician.name = json.getString("musician_name");

            // pictures
            // get profile basepath
            musician.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            musician.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");
            // get wall basepath
            musician.wallBasepath = json.getJSONObject("pictures").getJSONObject("wall").getString("base_path");
            // get profile path
            musician.wallFilename = json.getJSONObject("pictures").getJSONObject("wall").getString("path");

            // Collection Models
            // Albums

            // Songs

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
