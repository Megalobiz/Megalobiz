package com.megalobiz.megalobiz.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;


import com.megalobiz.megalobiz.MegalobizApi;

import java.io.IOException;

/**
 * Created by KeitelRobespierre on 8/23/2016.
 */
public class NetworkState {

    private Context context;
    private Activity activity;

    public NetworkState(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    public Boolean isNetworkAvailable() {
        // for local dev
        if(!MegalobizApi.HOST.contains("megalobiz"))
            return true;

        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        // for local dev
        if(!MegalobizApi.HOST.contains("megalobiz"))
            return true;

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public void closeIfNoConnection() {
        if (! isNetworkAvailable() || !isOnline()) {

            Toast.makeText(activity, "Please Check your Internet Connection!", Toast.LENGTH_LONG).show();

            // end this activity after a while
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.finish();
                }
            }, 3000);
        }
    }

}
