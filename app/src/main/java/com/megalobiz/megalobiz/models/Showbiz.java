package com.megalobiz.megalobiz.models;

import com.megalobiz.megalobiz.MegalobizApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/13/2016.
 */
public class Showbiz implements Serializable{

    protected String showbizType;

    protected int id;
    protected String name;
    protected String profileBasepath;
    protected String profileFilename;
    protected String wallBasepath;
    protected String wallFilename;
    protected int respects;
    protected String genreName;
    protected Boolean isRespected;

    public String getShowbizType() {
        return showbizType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTinyProfilePicture() {
        return fullPicturePath("tiny", this.profileBasepath, this.profileFilename);
    }

    public String getSmallProfilePicture() {
        return fullPicturePath("small", this.profileBasepath, this.profileFilename);
    }

    public String getMediumProfilePicture() {
        return fullPicturePath("medium", this.profileBasepath, this.profileFilename);
    }

    public String getBigProfilePicture() {
        return fullPicturePath("big", this.profileBasepath, this.profileFilename);
    }

    public String getTinyWallPicture() {
        return fullPicturePath("tiny", this.wallBasepath, this.wallFilename);
    }

    public String getSmallWallPicture() {
        return fullPicturePath("small", this.wallBasepath, this.wallFilename);
    }

    public String getMediumWallPicture() {
        return fullPicturePath("medium", this.wallBasepath, this.wallFilename);
    }

    public String getBigWallPicture() {
        return fullPicturePath("big", this.wallBasepath, this.wallFilename);
    }


    public String fullPicturePath(String dim, String basepath, String filename) {
        return String.format("%s%s%s_%s", MegalobizApi.HOST, basepath, dim, filename);
    }


    public int getRespects() {
        return respects;
    }

    public void incrementRespects() {
        this.respects++;
    }

    public void decrementRespects() {
        this.respects--;
    }

    public String getGenreName() {
        return genreName;
    }

    public Boolean getRespected() {
        return isRespected;
    }

    public void setRespected(Boolean respected) {
        isRespected = respected;
    }

    // for common attributes
    public static Showbiz fromJSON(JSONObject json, Showbiz showbiz) {
        try {
            if(json.has("is_respected")) {
                showbiz.isRespected = json.getBoolean("is_respected");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return showbiz;
    }

    // For Mixed Models like in Search
    public static ArrayList<Showbiz> fromMixedJSONArray(JSONArray json) {
        ArrayList<Showbiz> showbizs = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                Showbiz showbiz = null;

                if (json.getJSONObject(i).has("band_name")) {
                    showbiz = Band.fromJSON(json.getJSONObject(i));

                } else if (json.getJSONObject(i).has("musician_name")) {
                    showbiz = Musician.fromJSON(json.getJSONObject(i));

                } else if (json.getJSONObject(i).has("album_name")) {
                    showbiz = Album.fromJSON(json.getJSONObject(i));

                } else if (json.getJSONObject(i).has("song_name")) {
                    showbiz = Song.fromJSON(json.getJSONObject(i));
                }

                if (showbiz != null) {
                    showbizs.add(showbiz);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return showbizs;
    }
}
