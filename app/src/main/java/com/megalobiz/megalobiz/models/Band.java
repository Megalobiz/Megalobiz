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

    public Band() {
        showbizType = "Band";
    }

    public static Band fromJSON(JSONObject json) {
        Band band = new Band();

        try {
            band.id = json.getInt("band_id");
            band.name = json.getString("band_name");

            // pictures
            // get profile basepath
            band.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            band.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");
            // get wall basepath
            band.wallBasepath = json.getJSONObject("pictures").getJSONObject("wall").getString("base_path");
            // get profile path
            band.wallFilename = json.getJSONObject("pictures").getJSONObject("wall").getString("path");

            // Collection Models
            // Musicians

            // Albums

            // Songs


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
