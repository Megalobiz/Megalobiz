package com.megalobiz.megalobiz.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;

import java.util.zip.Inflater;

public class TopShowbizActivity extends AppCompatActivity {

    MegalobizClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_showbiz);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setLogo(R.drawable.ic_megalobiz);
        ab.setTitle("Megalo Hits");

        client = MegalobizApplication.getRestClient();

        Toast.makeText(this,
                "Connected to API with "+ MegalobizApplication.grantType, Toast.LENGTH_LONG).show();

        if (client != null && client.checkAccessToken() != null) {
            Toast.makeText(this,
                    "Token is there :"+ client.checkAccessToken().getToken().subSequence(1, 5), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global_menu, menu);
        super.onCreateOptionsMenu(menu);

        //remove logout item if User is connect through Client Credentials
        if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.CLIENT_CREDENTIALS) {
            menu.removeItem(R.id.action_logout);
        }
        // else remove login item
        else if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.AUTHORIZATION) {
            menu.removeItem(R.id.action_login);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

        return super.onOptionsItemSelected(item);
    }

    // clear access token either from AUTHORIZATION or CLIENT_CREDENTIALS grant type
    public void logout() {
        //MegalobizClient client = MegalobizApplication.getRestClient();
        client.clearAccessToken();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

}
