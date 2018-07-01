package com.labessence.biotech.finaldilution.compound.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

import com.labessence.biotech.finaldilution.R

class AddCompoundButtonView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {

        val yTop = 0f
        val yBottom = height.toFloat()
        val xLeft = 0f
        val xRight = width.toFloat()
        val xMid = width / 2.0f
        val barWidth = width / 2.0f

        val path = Path()

        // Top Direction Shape
        path.moveTo(xLeft, yBottom)
        path.moveTo(xMid - barWidth / 2, yBottom)
        path.cubicTo(xMid - barWidth / 4, yBottom, xMid - barWidth / 4, yTop, xMid, yTop)
        path.cubicTo(xMid + barWidth / 4, yTop, xMid + barWidth / 4, yBottom, xMid + barWidth / 2, yBottom)
        path.moveTo(xRight, yBottom)
        path.lineTo(xLeft, yBottom)

        path.close()

        val p = Paint()
        p.color = ContextCompat.getColor(context, R.color.grey1)
        p.isAntiAlias = true

        canvas.drawPath(path, p)
        super.onDraw(canvas)

    }
}
