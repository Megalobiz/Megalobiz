package com.megalobiz.megalobiz.activities.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.adapters.HamburgerArrayAdapter;
import com.megalobiz.megalobiz.utils.HamburgerItem;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class SharedHamburger {

    public static Context hamContext;
    public static HamburgerArrayAdapter hamAdapter;
    public static  ArrayList<HamburgerItem> hamItems;
    public static ListView lvHamburger;
    public static DrawerLayout dlHamburger;
    public static ActionBarDrawerToggle drawerToggle;
    private static Activity activity;

    public static void cookHamburger(Context context) {
        hamContext = context;
        activity = (Activity) hamContext;

        hamItems = new ArrayList<>();
        hamItems.add(new HamburgerItem("Megalo Hits", R.drawable.mb_icon_home_white));
        hamItems.add(new HamburgerItem("Bands", R.drawable.mb_icon_band_white));
        hamItems.add(new HamburgerItem("Musicians", R.drawable.mb_icon_musician_white));
        hamItems.add(new HamburgerItem("Albums", R.drawable.mb_icon_album_white));
        hamItems.add(new HamburgerItem("Songs", R.drawable.mb_icon_song_white));
        hamItems.add(new HamburgerItem("About", R.drawable.mb_icon_about_white));

        // DrawerLayout
        dlHamburger = (DrawerLayout) activity.findViewById(R.id.dlHamburger);

        // Populate the Navigtion Drawer with options
        //mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        lvHamburger = (ListView) activity.findViewById(R.id.lvHamburger);
        hamAdapter = new HamburgerArrayAdapter(hamContext, hamItems);
        lvHamburger.setAdapter(hamAdapter);

        // Drawer Item click listeners
        lvHamburger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToActivity(position);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(activity, dlHamburger, R.string.drawer_open, R.string.drawer_home_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                activity.invalidateOptionsMenu();
            }
        };

        dlHamburger.setDrawerListener(drawerToggle);
    }

    public static void goToActivity(int position) {
        if (position == 0) {
            Toast.makeText(hamContext, "Top Showbiz Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 1) {
            Toast.makeText(hamContext, "Band Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 2) {
            Toast.makeText(hamContext, "Musician Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 3) {
            Toast.makeText(hamContext, "Album Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 4) {
            Toast.makeText(hamContext, "Song Showbizs Activity", Toast.LENGTH_SHORT).show();
        } else if (position == 5) {
            Toast.makeText(hamContext, "About Showbizs Activity", Toast.LENGTH_SHORT).show();
        }
    }

}
