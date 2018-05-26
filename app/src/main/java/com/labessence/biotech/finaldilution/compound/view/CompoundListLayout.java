package com.labessence.biotech.finaldilution.compound.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Final Dilution
 * Created by dawid.chmielewski on 4/3/2018.
 */

public class CompoundListLayout extends LinearLayout {
    private static final String TAG = "Compound List Layout";
    private Context context;

    public CompoundListLayout(Context context) {
        super(context);
        this.context = context;
    }

    public CompoundListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CompoundListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

//    public float getYFraction() {
//        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
//        if (screenHeight != 0) {
//            Log.d(TAG, "Height = " + screenHeight + ", y = " + getY());
//            Log.d(TAG, "getY() / screenHeight = " + getY() / screenHeight);
//        } else {
//            Log.d(TAG, "Height = " + screenHeight + ", y = " + getY());
//        }
//        if (screenHeight != 0) return getY() / screenHeight;
//        else return getY();
//    }

    public void setYFraction(float yFraction) {
        int statusBarHeight = 0;
        int statusBar = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBar > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(statusBar);
        }
        //int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenHeight = ((Activity) context).getWindow().getDecorView().getHeight() - statusBarHeight;
        Log.d(TAG, "Screen height = " + screenHeight);
        Log.d(TAG, "y = " + getY());
        Log.d(TAG, "getHight() = " + getHeight());
        float listScreenFraction = Math.abs(getHeight() / (float) screenHeight);
        Log.d(TAG, "listScreenFraction = " + listScreenFraction);
        Log.d(TAG, "setTo = " + ((screenHeight > 0) ? (Math.max(yFraction, 1 - listScreenFraction) * screenHeight) : -9999));
        setY((screenHeight > 0) ? (Math.max(yFraction, 1 - listScreenFraction) * screenHeight) : -9999);
    }
}
