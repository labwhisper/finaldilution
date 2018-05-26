package com.labessence.biotech.finaldilution.peripherals.gestures;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.labessence.biotech.finaldilution.solution.view.VolumePanel;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/2/2018.
 */

public class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "TapGestures ";
    private final TapGestureController controller;

    public TapGestureListener(VolumePanel controller) {
        this.controller = controller;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "Down ");
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "Single tap ");
        controller.onTap();
        return true;
    }

}
