package com.megalobiz.megalobiz.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.megalobiz.megalobiz.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {

    }

    // when click Home Screen icon or text, client will connect
    // with client credentials grant_type
    public void onHomeScreen(View view) {

    }

    // go to register activity
    public void onRegister(View view) {

    }
}
