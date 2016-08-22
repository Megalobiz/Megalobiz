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
import com.megalobiz.megalobiz.activities.helpers.SharedMenu;

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
        super.onCreateOptionsMenu(menu);
        SharedMenu.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (SharedMenu.setMenuActions(this, item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
