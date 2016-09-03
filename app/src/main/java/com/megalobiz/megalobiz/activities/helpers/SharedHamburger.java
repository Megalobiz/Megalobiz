package com.megalobiz.megalobiz.activities.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.AboutActivity;
import com.megalobiz.megalobiz.activities.ShowbizsActivity;
import com.megalobiz.megalobiz.activities.TopShowbizActivity;
import com.megalobiz.megalobiz.adapters.HamburgerArrayAdapter;
import com.megalobiz.megalobiz.utils.HamburgerItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class SharedHamburger {

    public static Context hamContext;
    public static HamburgerArrayAdapter hamAdapter;
    public static ArrayList<HamburgerItem> hamItems;
    public static ListView lvHamburger;
    public static DrawerLayout dlHamburger;
    public static LinearLayout llHamburger;
    public static ActionBarDrawerToggle drawerToggle;
    private static Activity activity;

    public static void cookHamburger(Context context, int resourceTitle) {
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

        // Populate the Navigation Drawer with options
        llHamburger = (LinearLayout) activity.findViewById(R.id.rlHamburger);
        lvHamburger = (ListView) activity.findViewById(R.id.lvHamburger);
        hamAdapter = new HamburgerArrayAdapter(hamContext, hamItems);
        lvHamburger.setAdapter(hamAdapter);

        // Drawer Item click listeners
        lvHamburger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalAnimation.animateViewAlphaSize(view);
                goToActivity(position);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(activity, dlHamburger, R.string.drawer_open, resourceTitle) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                // if grant type is Authorization Code, display Authenticated User views
                if (MegalobizApplication.grantType == MegalobizApplication.OAuthGrantType.AUTHORIZATION) {
                    SharedHamburger.setupAuthUserViews();
                }
            }

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

    public static void reCookHamburger(Context context, int resourceTitle) {
        cookHamburger(context, resourceTitle);
        SharedHamburger.dlHamburger.closeDrawer(SharedHamburger.llHamburger);
        SharedHamburger.drawerToggle.syncState();
    }

    public static void goToActivity(int position) {
        if (position == 0) {
            // Megalo Hits
            topShowizs();

        } else if (position == 1) {
            // bands
            listBands();

        } else if (position == 2) {
            // musicians
            listMusicians();

        } else if (position == 3) {
            // albums
            listAlbums();

        } else if (position == 4) {
            // songs
            listSongs();
        } else if (position == 5) {
            // about
            showAbout();
        }
    }

    public static void topShowizs() {
        if (! (activity instanceof TopShowbizActivity)) {
            Intent i = new Intent(hamContext, TopShowbizActivity.class);
            hamContext.startActivity(i);
        }
    }

    public static void listBands() {
        finishActivity();

        Intent i = new Intent(hamContext, ShowbizsActivity.class);
        i.putExtra("showbiz_type", "Band");
        hamContext.startActivity(i);
    }

    public static void listMusicians() {
        finishActivity();

        Intent i = new Intent(hamContext, ShowbizsActivity.class);
        i.putExtra("showbiz_type", "Musician");
        hamContext.startActivity(i);
    }

    public static void listAlbums() {
        finishActivity();

        Intent i = new Intent(hamContext, ShowbizsActivity.class);
        i.putExtra("showbiz_type", "Album");
        hamContext.startActivity(i);
    }

    public static void listSongs() {
        finishActivity();

        Intent i = new Intent(hamContext, ShowbizsActivity.class);
        i.putExtra("showbiz_type", "Song");
        hamContext.startActivity(i);
    }

    public static void finishActivity() {
        if (activity instanceof ShowbizsActivity) {
            activity.finish();
        }
    }

    public static void showAbout() {
        Intent i = new Intent(hamContext, AboutActivity.class);
        hamContext.startActivity(i);
    }

    public static void setupAuthUserViews() {
        ImageView ivAuthPicture = (ImageView) activity.findViewById(R.id.ivAuthPicture);
        TextView tvAuthName = (TextView) activity.findViewById(R.id.tvAuthName);

        tvAuthName.setText(Auth.getUser().getName());
        ivAuthPicture.setImageResource(0);

        String imageUrl = Auth.getUser().getTinyProfilePicture();
        Picasso.with(activity).load(imageUrl).into(ivAuthPicture);
    }
}
