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
}
