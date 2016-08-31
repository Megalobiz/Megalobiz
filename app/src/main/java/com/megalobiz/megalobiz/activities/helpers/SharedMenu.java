package com.megalobiz.megalobiz.activities.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.LoginActivity;
import com.megalobiz.megalobiz.activities.SearchActivity;
import com.megalobiz.megalobiz.activities.ShowbizProfileActivity;
import com.megalobiz.megalobiz.activities.TopShowbizActivity;
import com.megalobiz.megalobiz.models.Showbiz;

/**
 * Created by KeitelRobespierre on 8/17/2016.
 */
public class SharedMenu {

    private static Context menuContext;
    private static Menu menu;

    public static void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SharedMenu.menu = menu;
        inflater.inflate(R.menu.global_menu, menu);

        //remove logout item if User is connect through Client Credentials
        if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.CLIENT_CREDENTIALS) {
            menu.removeItem(R.id.action_logout);
        }
        // else remove login item
        else if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.AUTHORIZATION) {
            menu.removeItem(R.id.action_login);
        }
    }

    public static boolean setMenuActions(Context context, MenuItem item) {
        menuContext = context;
        int id = item.getItemId();

        //action search
        if (id == R.id.action_search) {
            searchAll();
            return true;
        }

        //action login
        if (id == R.id.action_login) {
            // even if user is not logged in via Authorization grant type
            // Megalobiz App is still logged in via Client Credentials grant type
            // logout from Client Credentials grant type
            logout();
            return true;
        }

        //action logout
        if (id == R.id.action_logout) {
            // logout from Authorization grant type
            logout();
            return true;
        }

        return false;
    }

    public static void searchAll() {
        // code to launch Search Activity here
        Activity activity = (Activity) menuContext;
        if (! (activity instanceof SearchActivity)) {
            Intent i = new Intent(menuContext, SearchActivity.class);
            menuContext.startActivity(i);
        }
    }

    // clear access token either from AUTHORIZATION or CLIENT_CREDENTIALS grant type
    public static void logout() {
        MegalobizClient client = MegalobizApplication.getRestClient();
        client.clearAccessToken();
        Intent i = new Intent(menuContext, LoginActivity.class);
        menuContext.startActivity(i);
    }

    public static void launchShowbizProfile(Context context, Showbiz showbiz) {
        Intent i = new Intent(context, ShowbizProfileActivity.class);
        i.putExtra("showbiz", showbiz);
        context.startActivity(i);
    }
}
