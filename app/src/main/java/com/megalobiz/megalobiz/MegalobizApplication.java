package com.megalobiz.megalobiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.activeandroid.app.Application;
import com.megalobiz.megalobiz.models.User;

/**
 * Created by KeitelRobespierre on 8/13/2016.
 */

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     MegalobizClient client = MegalobizApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class MegalobizApplication extends com.activeandroid.app.Application {

    private static Context context;

    private static User authUser;

    public enum OAuthGrantType {
        CLIENT_CREDENTIALS, AUTHORIZATION
    }

    public static OAuthGrantType grantType = OAuthGrantType.AUTHORIZATION;


    @Override
    public void onCreate() {
        super.onCreate();
        MegalobizApplication.context = this;

    }

    public static MegalobizClient getRestClient() {
        try {
            return (MegalobizClient) MegalobizClient.getInstance(MegalobizClient.class, MegalobizApplication.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getAuthUser() {
        return MegalobizApplication.authUser;
    }

    public static void setUser(User user) {
        MegalobizApplication.authUser = user;
    }
}