package com.megalobiz.megalobiz.activities.helpers;

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
     *  Check if a User is Authenticated
     *  @return boolean
     */
    public static boolean Check() {
        return user != null;
    }

    /**
     *  Check if User is a guest
     *  @return boolean
     */
    public static boolean Guest() {
        return user == null;
    }
}
