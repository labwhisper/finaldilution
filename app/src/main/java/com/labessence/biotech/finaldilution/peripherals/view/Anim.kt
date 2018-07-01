package com.labessence.biotech.finaldilution.peripherals.view

import android.graphics.Color
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView

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

        class ColorAnimation : Animation() {
            private val colorFrom = (view as TextView).currentTextColor
            private val colorTo = Color.RED

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val currentRedValue = ((Color.red(colorTo) - Color.red(colorFrom)) * interpolatedTime + Color.red(colorFrom)).toInt()
                val currentColor = Color.rgb(currentRedValue, Color.green(colorFrom), Color.blue(colorFrom))
                (view as TextView).setTextColor(currentColor)
            }
        }

        val colorAnimator = ColorAnimation()
        colorAnimator.duration = 200
        colorAnimator.repeatMode = Animation.REVERSE
        colorAnimator.repeatCount = 1
        view.startAnimation(colorAnimator)
    }

}
