package com.megalobiz.megalobiz.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.models.Album;
import com.megalobiz.megalobiz.models.Showbiz;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by KeitelRobespierre on 8/17/2016.
 */
public class ShowbizMembersFragment extends Fragment {

    private String showbizType;
    private ArrayList<Showbiz> showbizs;
    private boolean forTop;
    private TableLayout table;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        int res;

        if (forTop && showbizType == "Musician") {
            res = R.layout.fragment_top_musicians;
        } else if (forTop && showbizType == "Album") {
            res = R.layout.fragment_top_albums;
        } else {
            res = R.layout.fragment_showbiz_members;
        }

        v = inflater.inflate(res, container, false);

        table = (TableLayout) v.findViewById(R.id.tlShowbizs);

        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showbizs = (ArrayList<Showbiz>) getArguments().getSerializable("showbizs");
        showbizType = getArguments().getString("showbizType");
        forTop = getArguments().getBoolean("forTop");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (forTop && showbizType == "Musician") {
            populateTopMusiciansTable();
        } else if (forTop && showbizType == "Album") {
            populateTopAlbumsTable();
        } else {
            populateShowbizsTable();
        }

    }

    public static ShowbizMembersFragment newInstance(ArrayList<Showbiz> showbizs, String showbizType, Boolean forTop) {
        ShowbizMembersFragment fg = new ShowbizMembersFragment();
        Bundle args = new Bundle();
        args.putSerializable("showbizs", showbizs);
        args.putString("showbizType", showbizType);
        args.putBoolean("forTop", forTop);
        fg.setArguments(args);

        return fg;
    }

    public void populateShowbizsTable() {
        int modulus = showbizs.size()%4;
        int rowsCount = 1;

        if(modulus > 0)
            rowsCount = (int)(showbizs.size()/4) + 1;
        else
            rowsCount = (int) showbizs.size()/4;

        // Rows - outer for
        for (int i = 0; i < rowsCount; ++i) {

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // 5 columns - inner for loop
            for (int j = 0; j < 4; j++) {

                // We have 4 columns (j)
                // so for each showbiz item we need to multiply i by 4 + j (ix4) +j
                int index = i*4 + j;

                if (index < showbizs.size()) {
                    View v = createShowbizView(showbizs.get(index));

                    v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    v.setPadding(15, 5, 15, 5);

                    row.addView(v);
                }
            }

            table.addView(row);
        }

    }

    public void populateTopMusiciansTable() {
        // 1 Row - no need to do iteration
        TableRow row = new TableRow(getContext());
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // 5 columns - inner for loop
        for (int j = 0; j < 5; j++) {
            View v = createTopMusicianView(showbizs.get(j), j+1);

            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            //tv.setBackgroundResource(R.drawable.cell_shape);
            v.setPadding(5, 5, 5, 5);

            row.addView(v);

        }

        table.addView(row);

    }

    public void populateTopAlbumsTable() {
        int modulus = showbizs.size()%2;
        int rowsCount = 1;

        if(modulus > 0)
            rowsCount = (int)(showbizs.size()/2) + 1;
        else
            rowsCount = (int) showbizs.size()/2;

        // Rows - outer for
        for (int i = 0; i < rowsCount; ++i) {

            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // 5 columns - inner for loop
            for (int j = 0; j < 2; j++) {

                // We have 2 columns (j)
                // so for each showbiz item we need to multiply i by 2 + j (ix2) +j
                int index = i*2 + j;

                if (index < showbizs.size()) {
                    View v = createTopAlbumView(showbizs.get(index), index+1);

                    v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    v.setPadding(15, 5, 15, 5);

                    row.addView(v);
                }
            }

            table.addView(row);
        }

    }

    public View createShowbizView(Showbiz showbiz) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.showbiz_item, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        ImageView imageView = (ImageView) v.findViewById(R.id.ivProfilePicture);
        imageView.setBackgroundResource(0);

        tvName.setText(showbiz.getName());

        // load image
        String imageUrl = showbiz.getSmallProfilePicture();
        Picasso.with(getContext()).load(imageUrl)
                .resize(0, 100)
                .transform(new RoundedCornersTransformation(5, 5))
                .into(imageView);

        return v;
    }

    public View createTopMusicianView(Showbiz showbiz, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.top_musician, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvMusicianPosition = (TextView) v.findViewById(R.id.tvMusicianPosition);

        ImageView imageView = (ImageView) v.findViewById(R.id.ivProfilePicture);
        imageView.setBackgroundResource(0);

        tvName.setText(showbiz.getName());
        tvMusicianPosition.setText(String.valueOf(position));

        // load image
        String imageUrl = showbiz.getSmallProfilePicture();
        Picasso.with(getContext()).load(imageUrl)
                .resize(120, 0)
                .transform(new RoundedCornersTransformation(5, 5))
                .into(imageView);

        return v;
    }

    public View createTopAlbumView(Showbiz showbiz, int position) {
        // showbiz is an album
        Album album = (Album) showbiz;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.top_album, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvMusicianPosition = (TextView) v.findViewById(R.id.tvPosition);
        TextView tvOwner = (TextView) v.findViewById(R.id.tvOwner);

        ImageView imageView = (ImageView) v.findViewById(R.id.ivProfilePicture);
        imageView.setBackgroundResource(0);

        tvName.setText(album.getName());
        tvMusicianPosition.setText(String.valueOf(position));

        String owner = "Unknown Artist";
        if(!TextUtils.isEmpty(album.getOwner().getName()));
        tvOwner.setText(album.getOwner().getName());

        // load image
        String imageUrl = showbiz.getSmallProfilePicture();
        Picasso.with(getContext()).load(imageUrl)
                .resize(120, 0)
                .transform(new RoundedCornersTransformation(5, 5))
                .into(imageView);

        return v;
    }


}
