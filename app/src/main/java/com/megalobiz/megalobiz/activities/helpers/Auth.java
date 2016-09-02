package com.megalobiz.megalobiz.activities.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.megalobiz.megalobiz.models.User;

/**
 * Created by KeitelRobespierre on 8/31/2016.
 */
public class Auth {

    private static User user;


    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Auth.user = user;
    }

    /**
     *  check if a User is Authenticated
     *  @return boolean
     */
    public static boolean check() {
        return user != null;
    }

    /**
     *  check if User is a guest
     *  @return boolean
     */
    public static boolean guest() {
        return user == null;
    }

    /**
     *  Clear any auth user
     *
     */
    public static void clear() {
        user = null;
    }
}
