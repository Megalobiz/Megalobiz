package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.codepath.oauth.OAuthBaseClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.megalobiz.megalobiz.MegalobizApi;
import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;

public class LoginActivity extends OAuthLoginActionBarActivity<MegalobizClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Extract the uri data and call authorize to retrieve access token
    // This is why after the browser redirects to the app, authentication is completed
    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();
        Class clientClass = MegalobizClient.class;
        // Extracts the authenticated url data after the user
        // authorizes the OAuth app in the browser
        Uri uri = getIntent().getData();

        try {
            OAuthBaseClient client = OAuthBaseClient.getInstance(clientClass, this);

            client.authorize(uri, this); // fetch access token (if needed)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {

        //When App is skipping login to connect with stored token verify preferences
        // get preferences for grant_type connection
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("old_grant_type")) {
            int oldGrantId = preferences.getInt("old_grant_type", 100);
            // 0 for CLIENT_CREDENTIALS
            // 1 for AUTHORIZATION

            // if old grant type is the same as
            if (oldGrantId != 100) {
                MegalobizApplication.grantType = MegalobizApplication.OAuthGrantType.values()[oldGrantId];
            }
        }

        Intent i = new Intent(this, TopShowbizActivity.class);
        startActivity(i);
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        // Authorization grant type must be used
        MegalobizApplication.grantType = MegalobizApplication.OAuthGrantType.AUTHORIZATION;

        // store preferences for this grant type
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("old_grant_type", MegalobizApplication.OAuthGrantType.AUTHORIZATION.ordinal());
        editor.apply();

        getClient().connect();
    }

    // when click Home Screen icon or text, client will connect
    // with client credentials grant_type
    public void onHomeScreen(View view) {
        // Client Credentials grant type must be used
        MegalobizApplication.grantType = MegalobizApplication.OAuthGrantType.CLIENT_CREDENTIALS;

        // store preferences for this grant type
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("old_grant_type", MegalobizApplication.OAuthGrantType.CLIENT_CREDENTIALS.ordinal());
        editor.apply();

        getClient().clientCredentials(
                Uri.parse(MegalobizApi.HOST+"/oauth/callback?code=f1mzoTBrao6D87y5YcdN3EliTSztRUZqyYmUIZlH"), this);
        //Intent i = new Intent(this, TopShowbizActivity.class);
        //startActivity(i);
    }

    // go to register activity
    public void onRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
