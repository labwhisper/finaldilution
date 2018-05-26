package com.labessence.biotech.finaldilution.peripherals.gestures;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.labessence.biotech.finaldilution.R;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 3/27/2018.
 */

public class CompoundListGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "CompoundListGestures ";
    private final Activity activity;
    private final View view;

    public CompoundListGestureListener(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.d(TAG, "Down ");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "Single tap ");
        view.performClick();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "Fling ");
        float ev1Y = e1.getY();
        float ev2Y = e2.getY();
//        final float ydistance = Math.abs(ev1Y - ev2Y);
//        final float yvelocity = Math.abs(velocityY);

//        double SWIPE_MIN_DISTANCE = 25;
//        double SWIPE_MIN_VELOCITY = 50;
//        if ((yvelocity < SWIPE_MIN_VELOCITY) || (ydistance < SWIPE_MIN_DISTANCE)) {
//            return false;
//        }

        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down);
        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.compoundsFragment);
        if (fragment.isVisible() && ev1Y < ev2Y) {
            fragmentTransaction.hide(fragment);
        } else if (!fragment.isVisible() && ev1Y > ev2Y) {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "Scroll ");
        float ev1Y = e1.getY();
        float ev2Y = e2.getY();
        //return super.onScroll(e1, e2, distanceX, distanceY);
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_down);
        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.compoundsFragment);
        if (fragment.isVisible() && ev1Y < ev2Y) {
            fragmentTransaction.hide(fragment);
        } else if (!fragment.isVisible() && ev1Y > ev2Y) {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
        return true;
    }
}
