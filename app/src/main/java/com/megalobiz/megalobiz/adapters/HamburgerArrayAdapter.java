package com.megalobiz.megalobiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.utils.HamburgerItem;

import java.util.List;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class HamburgerArrayAdapter extends ArrayAdapter<HamburgerItem> {

    public HamburgerArrayAdapter(Context context, List<HamburgerItem> hamItems) {
        super(context, 0, hamItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Hamburger item
        final HamburgerItem hamItem = getItem(position);

        //find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.hamburger_item, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.ivIcon);

        titleView.setText(hamItem.getTitle());
        iconView.setImageResource(hamItem.getIcon());

        return convertView;
    }
}
