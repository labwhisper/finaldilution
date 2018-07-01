package com.labessence.biotech.finaldilution.compound.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.labessence.biotech.finaldilution.R;

public class AddCompoundButtonView extends android.support.v7.widget.AppCompatTextView {
    public AddCompoundButtonView(Context context) {
        super(context);
    }

    public AddCompoundButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddCompoundButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float yTop = 0;
        float yBottom = getHeight();
        float xLeft = 0;
        float xRight = getWidth();
        float xMid = getWidth() / 2.0f;
        float barWidth = getWidth() / 2.0f;

        Path path = new Path();

        // Top Direction Shape
        path.moveTo(xLeft, yBottom);
        path.moveTo(xMid - barWidth / 2, yBottom);
        path.cubicTo(xMid - barWidth / 4, yBottom, xMid - barWidth / 4, yTop, xMid, yTop);
        path.cubicTo(xMid + barWidth / 4, yTop, xMid + barWidth / 4, yBottom, xMid + barWidth / 2, yBottom);
        path.moveTo(xRight, yBottom);
        path.lineTo(xLeft, yBottom);

        path.close();

        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.grey1));
        p.setAntiAlias(true);

        canvas.drawPath(path, p);
        super.onDraw(canvas);

    }
}
