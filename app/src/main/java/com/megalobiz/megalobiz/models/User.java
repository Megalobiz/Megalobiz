package com.megalobiz.megalobiz.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/18/2016.
 */
public class User extends Showbiz {

    private String firstName;
    private String lastName;
    private String gender;
    private SimpleDateFormat birthDate;
    private String email;

    public static User fromJSON(JSONObject json) {
        User user = new User();
        user.showbizType = "User";

        try {
            //JSONObject jsonUser = json.getJSONObject("users");
            user.id = json.getInt("user_id");
            user.firstName = json.getString("first_name");
            user.lastName = json.getString("last_name");
            user.name = user.firstName + " " + user.lastName;


            // pictures
            // get profile basepath
            user.profileBasepath = json.getJSONObject("pictures").getJSONObject("profile").getString("base_path");
            // get profile path
            user.profileFilename = json.getJSONObject("pictures").getJSONObject("profile").getString("path");

            // Collection Models
            // Songs

            user.birthDate = new SimpleDateFormat("yyyy-MM-dd");
            user.birthDate.parse(json.getString("birth_date"));

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static ArrayList<User> fromJSONArray(JSONArray json) {
        ArrayList<User> users = new ArrayList<>();;

        for (int i = 0; i < json.length(); i++) {
            try {
                User user = User.fromJSON(json.getJSONObject(i));

                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }
}

