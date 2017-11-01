package com.labessence.biotech.finaldilution.peripherals.view;

import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

public class Anim {

    public void blink(View view) {
        if (view == null) {
            return;
        }
        Animation anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(20); //blinking time
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(4);
        view.startAnimation(anim);
    }

    public void blinkWithRed(final View view) {
        if (view == null) {
            return;
        }

        class ColorAnimation extends Animation {
            private static final String TAG = "Color Animation";
            private int colorFrom = ((TextView) view).getCurrentTextColor();
            private int colorTo = Color.RED;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int currentRedValue = (int) ((Color.red(colorTo) - Color.red(colorFrom)) * interpolatedTime + Color.red(colorFrom));
                int currentColor = Color.rgb(currentRedValue, Color.green(colorFrom), Color.blue(colorFrom));
                ((TextView) (view)).setTextColor(currentColor);
            }
        }

        ColorAnimation colorAnimator = new ColorAnimation();
        colorAnimator.setDuration(200);
        colorAnimator.setRepeatMode(Animation.REVERSE);
        colorAnimator.setRepeatCount(1);
        view.startAnimation(colorAnimator);
    }

}
