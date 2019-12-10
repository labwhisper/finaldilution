package com.labwhisper.biotech.finaldilution.compound.view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent

import com.labwhisper.biotech.finaldilution.R

class AddComponentButtonView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val screenWidth =
        DisplayMetrics().apply {
            (context as Activity).windowManager.defaultDisplay.getMetrics(this)
        }.widthPixels


    val p = Paint().apply {
        color = ContextCompat.getColor(context, R.color.grey1)
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        val path = getPath()
        canvas.drawPath(path, p)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val x = event.x
        if (!isInShape(x)) {
            return false
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                performClick()
                true
            }
            else -> false
        }
    }

    private fun isInShape(x: Float): Boolean {
        return x > (screenWidth / 3) && x < (screenWidth * 2 / 3)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun getPath(): Path {
        return Path().apply {

            val yTop = 0f
            val yBottom = height.toFloat()
            val xLeft = 0f
            val xRight = width.toFloat()
            val xMid = width / 2.0f
            val barWidth = width / 2.0f

            moveTo(xLeft, yBottom)
            moveTo(xMid - barWidth / 2, yBottom)
            cubicTo(
                xMid - barWidth / 4, yBottom,
                xMid - barWidth / 4, yTop,
                xMid, yTop
            )
            cubicTo(
                xMid + barWidth / 4, yTop,
                xMid + barWidth / 4, yBottom,
                xMid + barWidth / 2, yBottom
            )
            moveTo(xRight, yBottom)
            lineTo(xLeft, yBottom)
            close()
        }
    }
}
