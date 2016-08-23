package com.megalobiz.megalobiz.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.helpers.SharedHamburger;
import com.megalobiz.megalobiz.models.Showbiz;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class ShowbizArrayAdapter extends ArrayAdapter<Showbiz> {

    public ShowbizArrayAdapter(Context context, List<Showbiz> showbizs) {
        super(context, android.R.layout.simple_list_item_1, showbizs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the Showbiz
        Showbiz showbiz = getItem(position);

        // find or inflate the template
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.showbiz_item, parent, false);
        }

        ImageView ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
        ivProfilePicture.setImageResource(0);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(showbiz.getName());

        // set the images with Picasso
        // set profile image
        String profileUrl = showbiz.getSmallProfilePicture();
        if(!TextUtils.isEmpty(profileUrl)) {
            Picasso.with(getContext()).load(profileUrl)
                    .transform(new RoundedCornersTransformation(3, 3))
                    .into(ivProfilePicture);
        }


        return convertView;
    }
}
