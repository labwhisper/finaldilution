package com.labessence.biotech.finaldilution.peripherals.gestures

import android.content.Intent
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

import com.labessence.biotech.finaldilution.init.view.StartupActivity
import com.labessence.biotech.finaldilution.solution.view.EditActivity

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 11/2/2017.
 */
class EditGestureListener(private val editActivity: EditActivity) : GestureDetector.SimpleOnGestureListener() {

    override fun onDown(e: MotionEvent): Boolean {
        Log.d(TAG, "Down ")
        return true
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        Log.d(TAG, "Fling ")
        val xdistance = Math.abs(e1.x - e2.x)
        val ydistance = Math.abs(e1.y - e2.y)
        val xvelocity = Math.abs(velocityX)

        val SWIPE_MIN_DISTANCE = 50.0
        val SWIPE_MIN_VELOCITY = 50.0
        if (xvelocity < SWIPE_MIN_VELOCITY || xdistance < SWIPE_MIN_DISTANCE) {
            return false
        }
        if (!isTrulyHorizontal(xdistance, ydistance)) {
            return false
        }
        if (e1.x <= e2.x) {
            val intent = Intent(editActivity, StartupActivity::class.java)
            editActivity.startActivity(intent)
            editActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        }

        return true
    }

    private fun isTrulyHorizontal(xdistance: Float, ydistance: Float): Boolean {
        Log.d(TAG, "xdistance =  $xdistance, ydistance = $ydistance")
        return xdistance > 2 * ydistance
    }

    companion object {
        private val TAG = "EditActivityGestures "
    }
}
