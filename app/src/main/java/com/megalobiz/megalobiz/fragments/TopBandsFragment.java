package com.megalobiz.megalobiz.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.megalobiz.megalobiz.MegalobizApplication;
import com.megalobiz.megalobiz.MegalobizClient;
import com.megalobiz.megalobiz.R;
import com.megalobiz.megalobiz.activities.TopShowbizActivity;
import com.megalobiz.megalobiz.adapters.ShowbizArrayAdapter;
import com.megalobiz.megalobiz.models.Band;
import com.megalobiz.megalobiz.models.Showbiz;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KeitelRobespierre on 8/18/2016.
 */
public class TopBandsFragment extends Fragment {

    protected MegalobizClient client;
    private String showbizType;
    private ArrayList<Band> bands;
    private TableLayout table;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Context mContext;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    protected int circlePosition = 1;
    protected Animation.AnimationListener mAnimationListener;
    protected View parentView;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_bands, container, false);

        return v;
    }

    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = MegalobizApplication.getRestClient();
        mContext = getContext();

        bands = (ArrayList<Band>) getArguments().getSerializable("bands");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentView = view;
        setupViewFlipper();
    }

    public static TopBandsFragment newInstance(ArrayList<Band> bands) {
        TopBandsFragment fg = new TopBandsFragment();
        Bundle args = new Bundle();
        args.putSerializable("bands", bands);
        fg.setArguments(args);

        return fg;
    }

    public void setupViewFlipper() {
        mViewFlipper = (ViewFlipper) parentView.findViewById(R.id.view_flipper);

        parentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        for(Band band : bands) {
            View v = createTopBandView(band);
            mViewFlipper.addView(v);
        }

        // move circle to top 1
        moveCirclePosition();

        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(6000);
        mViewFlipper.startFlipping();

        mAnimationListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                moveCirclePosition();
            }

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {}
        };

        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);

        // set onOnclik listener to go to Band Profile
        /*mViewFlipper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = mViewFlipper.indexOfChild(mViewFlipper.getCurrentView());
                TopShowbizActivity activity = (TopShowbizActivity) getActivity();
                activity.launchShowbizProfile(bands.get(position));
            }
        });*/

    }

    public View createTopBandView(final Band band) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.top_band, null);
        TextView tvTopBandName = (TextView) v.findViewById(R.id.tvTopBandName);
        ImageView imageView = (ImageView) v.findViewById(R.id.ivTopBandPicture);
        imageView.setBackgroundResource(0);

        tvTopBandName.setText(String.format("Band: %s", band.getName()));

        // load image
        String imageUrl = band.getBigWallPicture();
        Picasso.with(mContext).load(imageUrl).into(imageView);

        // onclick
        tvTopBandName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TopShowbizActivity activity = (TopShowbizActivity) getActivity();
                activity.launchShowbizProfile(band);
            }
        });

        return v;
    }

    protected void moveCirclePosition() {
        // find circle text views
        TextView tvTop1 = (TextView) parentView.findViewById(R.id.tvTop1);
        TextView tvTop2 = (TextView) parentView.findViewById(R.id.tvTop2);
        TextView tvTop3 = (TextView) parentView.findViewById(R.id.tvTop3);
        TextView tvTop4 = (TextView) parentView.findViewById(R.id.tvTop4);
        TextView tvTop5 = (TextView) parentView.findViewById(R.id.tvTop5);

        // clear circles
        ArrayList<TextView> tvTops = new ArrayList<>();
        tvTops.add(tvTop1);
        tvTops.add(tvTop2);
        tvTops.add(tvTop3);
        tvTops.add(tvTop4);
        tvTops.add(tvTop5);

        for (TextView tvTop : tvTops) {
            tvTop.setBackgroundResource(R.drawable.circle_empty);
            tvTop.setTextColor(Color.parseColor("#158EC6"));
        }

        // highlight circle
        circlePosition = mViewFlipper.getDisplayedChild() + 1;

        TextView tvHighlighted = tvTop1;
        if (circlePosition == 1) {
            tvHighlighted = tvTop1;
        } else if (circlePosition == 2) {
            tvHighlighted = tvTop2;
        } else if (circlePosition == 3) {
            tvHighlighted = tvTop3;
        } else if (circlePosition == 4) {
            tvHighlighted = tvTop4;
        } else if (circlePosition == 5) {
            tvHighlighted = tvTop5;
        }

        tvHighlighted.setBackgroundResource(R.drawable.circle_filled);
        tvHighlighted.setTextColor(Color.parseColor("#FFFFFF"));
    }

    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showNext();
                    // move circle
                    moveCirclePosition();
                    return true;

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showPrevious();
                    // move circle
                    moveCirclePosition();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

}
