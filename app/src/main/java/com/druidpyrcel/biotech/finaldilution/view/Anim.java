package com.druidpyrcel.biotech.finaldilution.view;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Anim {
    public void blink(View view) {
        Animation anim = new AlphaAnimation(0.4f, 1.0f);
        anim.setDuration(20); //blinking time
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(4);
        view.startAnimation(anim);
    }
}
