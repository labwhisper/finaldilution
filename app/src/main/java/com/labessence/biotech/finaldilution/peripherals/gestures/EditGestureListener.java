package com.labessence.biotech.finaldilution.peripherals.gestures;

import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.labessence.biotech.finaldilution.peripherals.view.StartupActivity;
import com.labessence.biotech.finaldilution.solution.view.EditActivity;
import com.labessence.biotech.finaldilution.solution.view.PrepActivity;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */
public class EditGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "EditActivityGestures ";
    private EditActivity editActivity;

    public EditGestureListener(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "Down ");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "Fling ");
        final float xdistance = Math.abs(e1.getX() - e2.getX());
        final float ydistance = Math.abs(e1.getY() - e2.getY());
        final float xvelocity = Math.abs(velocityX);

        double SWIPE_MIN_DISTANCE = 50;
        double SWIPE_MIN_VELOCITY = 50;
        if ((xvelocity < SWIPE_MIN_VELOCITY) || (xdistance < SWIPE_MIN_DISTANCE)) {
            return false;
        }
        if (!isTrulyHorizontal(xdistance, ydistance)) {
            return false;
        }
        if (e1.getX() > e2.getX()) {
            Intent intent = new Intent(editActivity, PrepActivity.class);
            editActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(editActivity, StartupActivity.class);
            editActivity.startActivity(intent);
            editActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        }

        return true;
    }

    private boolean isTrulyHorizontal(float xdistance, float ydistance) {
        Log.d(TAG, "xdistance =  " + xdistance + ", ydistance = " + ydistance);
        return xdistance > 2 * ydistance;
    }
}
