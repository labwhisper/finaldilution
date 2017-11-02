package com.labessence.biotech.finaldilution.peripherals.view;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.labessence.biotech.finaldilution.solution.view.EditActivity;
import com.labessence.biotech.finaldilution.solution.view.PrepActivity;

import static com.labessence.biotech.finaldilution.ApplicationContext.SWIPE_MIN_DISTANCE;
import static com.labessence.biotech.finaldilution.ApplicationContext.SWIPE_MIN_VELOCITY;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */
public class EditGestureListener extends GestureDetector.SimpleOnGestureListener {
    private EditActivity editActivity;

    public EditGestureListener(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float ev1X = e1.getX();
        float ev2X = e2.getX();
        final float xdistance = Math.abs(ev1X - ev2X);
        final float xvelocity = Math.abs(velocityX);

        if ((xvelocity < SWIPE_MIN_VELOCITY) || (xdistance < SWIPE_MIN_DISTANCE)) {
            return false;
        }
        if (ev1X > ev2X) {
            Intent intent = new Intent(editActivity, PrepActivity.class);
            editActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(editActivity, StartupActivity.class);
            editActivity.startActivity(intent);
            editActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        }

        return true;
    }
}
