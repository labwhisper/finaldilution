package com.labwhisper.biotech.finaldilution.peripherals.view

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import com.labwhisper.biotech.finaldilution.R
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

        if (view !is TextView) {
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

    fun animWrong(imageView: ImageView) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 500
        fadeIn.repeatMode = Animation.REVERSE
        fadeIn.repeatCount = 1

        val headShake = RotateAnimation(0f, 0f)
        headShake.duration = 200
        headShake.repeatMode = Animation.REVERSE
        headShake.repeatCount = 3

        headShake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                imageView.rotationY = (imageView.rotationY + 180f) % 360f
            }

            override fun onAnimationEnd(animation: Animation?) {
                imageView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                imageView.visibility = View.VISIBLE
            }

        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeIn)
        animationSet.addAnimation(headShake)
        imageView.startAnimation(animationSet)
    }

    fun animWellDone(imageView: ImageView) {
        imageView.rotationY = 180f
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000
        fadeIn.repeatMode = Animation.REVERSE
        fadeIn.repeatCount = 1

        val wellDoneSize =
            imageView.resources.getDimensionPixelSize(R.dimen.well_done_size).toFloat()


        val headShake = RotateAnimation(-1f, 8f, wellDoneSize / 2.2f, wellDoneSize / 1.1f)
        headShake.duration = 400
        headShake.repeatMode = Animation.REVERSE
        headShake.repeatCount = 3

        headShake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationEnd(animation: Animation?) {
                imageView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                imageView.visibility = View.VISIBLE
            }

        })

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeIn)
        animationSet.addAnimation(headShake)
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        imageView.startAnimation(animationSet)
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

