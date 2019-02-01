package com.labessence.biotech.finaldilution.peripherals.view

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView
import java.util.*

class Anim {

    fun blink(view: View?) {
        if (view == null) {
            return
        }
        val anim = AlphaAnimation(0.4f, 1.0f)
        anim.duration = 20 //blinking time
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = 4
        view.startAnimation(anim)
    }

    fun blinkWithRed(view: View?) {
        if (view == null) {
            return
        }

        if (!(view is TextView)) {
            return
        }

        if (animationRunning.contains(view.id)) return
        animationRunning.add(view.id)

        val name = Random().nextInt().toString()
        Log.d(TAG, "Before $name")
        val colorAnimator = ColorAnimation(view)
        Log.d(TAG, "In the $name")
        colorAnimator.duration = 200
        colorAnimator.repeatMode = Animation.REVERSE
        colorAnimator.repeatCount = 1
        colorAnimator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                Log.d(TAG, "Repeat $name")
            }

            override fun onAnimationEnd(p0: Animation?) {
                animationRunning.remove(view.id)
                Log.d(TAG, "Ended  $name")
            }

            override fun onAnimationStart(p0: Animation?) {
                Log.d(TAG, "Start  $name")
            }

        })
        view.startAnimation(colorAnimator)
    }

    companion object {
        var animationRunning: MutableSet<Int> = HashSet()
        const val TAG = "Anim"
    }
}

class ColorAnimation(val view: TextView) : Animation() {
    private val colorFrom = view.currentTextColor
    private val colorTo = Color.RED

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        blinkTransform(interpolatedTime)
    }

    private fun blinkTransform(interpolatedTime: Float) {
        val currentRedValue =
            ((Color.red(colorTo) - Color.red(colorFrom)) * interpolatedTime + Color.red(
                colorFrom
            )).toInt()
        val currentColor =
            Color.rgb(currentRedValue, Color.green(colorFrom), Color.blue(colorFrom))
        view.setTextColor(currentColor)
    }

    override fun reset() {
        view.setTextColor(colorFrom)
        super.reset()
    }
}

