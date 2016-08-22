package com.megalobiz.megalobiz.models;

import com.megalobiz.megalobiz.MegalobizApi;

import java.io.Serializable;

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
}
