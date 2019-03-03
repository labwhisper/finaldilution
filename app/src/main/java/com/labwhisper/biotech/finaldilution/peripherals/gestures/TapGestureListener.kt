package com.labwhisper.biotech.finaldilution.peripherals.gestures

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent

import com.labwhisper.biotech.finaldilution.solution.view.VolumePanel

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 4/2/2018.
 */

class TapGestureListener(controller: VolumePanel) : GestureDetector.SimpleOnGestureListener() {
    private val controller: TapGestureController

    init {
        this.controller = controller
    }

    override fun onDown(e: MotionEvent): Boolean {
        Log.d(TAG, "Down ")
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        Log.d(TAG, "Single tap ")
        controller.onTap()
        return true
    }

    companion object {
        private val TAG = "TapGestures "
    }

}
