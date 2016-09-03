package com.megalobiz.megalobiz.activities.helpers;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by KeitelRobespierre on 9/2/2016.
 */
public class GlobalAnimation {

    public static void animateViewAlphaSize(View view) {
        int duration = 200;

        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1.0f).setDuration(duration);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1.0f).setDuration(duration);
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0.6f, 1.0f).setDuration(duration);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fadeAnim, scaleXAnim, scaleYAnim);
        animSet.start();
    }

    public static void animateTextView(TextView tv) {
        int duration = 200;
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(tv, "alpha", 0.6f, 1.0f).setDuration(duration);

        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(tv, "scaleX", 1.0f, 1.2f).setDuration(duration);
        scaleXAnim.setRepeatCount(1);
        scaleXAnim.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(tv, "scaleY", 1.0f, 1.2f).setDuration(duration);
        scaleYAnim.setRepeatCount(1);
        scaleYAnim.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fadeAnim, scaleXAnim, scaleYAnim);
        animSet.start();
    }

    public static void animateTextViewAlphaColor(TextView tv, int color) {
        int duration = 200;

        ObjectAnimator colorAnim = ObjectAnimator.ofObject(tv, "textColor", new ArgbEvaluator(),
                tv.getCurrentTextColor(), color);
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(tv, "alpha", 0.6f, 1.0f).setDuration(duration);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(colorAnim, fadeAnim);
        animSet.start();
    }

}
