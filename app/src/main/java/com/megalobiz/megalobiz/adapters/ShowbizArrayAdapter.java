package com.megalobiz.megalobiz.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.models.Showbiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class ShowbizArrayAdapter extends ArrayAdapter<Showbiz> {

    public ShowbizArrayAdapter(Context context, List<Showbiz> showbizs) {
        super(context, android.R.layout.simple_list_item_1, showbizs);
    }
}
