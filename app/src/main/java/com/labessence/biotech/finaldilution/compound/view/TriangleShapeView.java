package com.labessence.biotech.finaldilution.compound.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.labessence.biotech.finaldilution.R;

public class TriangleShapeView extends ViewGroup {

    private int shapeColor;
    private int shapeDirection;

    public TriangleShapeView(Context context) {
        super(context);
        stuff(null);
    }

    public TriangleShapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        stuff(attrs);
    }

    public TriangleShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stuff(attrs);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

//    public TriangleShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//
//        stuff(attrs);
//
//    }

    private void stuff(AttributeSet attrs) {

        if (attrs != null) {

            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TriangleShapeView);
            int color = array.getColor(R.styleable.TriangleShapeView_shape_bg_color, ContextCompat.getColor(getContext(), R.color.gray));
            int direction = array.getInt(R.styleable.TriangleShapeView_shape_direction, 0);
            setColor(color);
            setShapeDirection(direction);
            array.recycle();
        }
    }

    private void setShapeDirection(int direction) {
        this.shapeDirection = direction;
    }

    public void setColor(int color) {
        this.shapeColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() / 2;
        int height = getHeight() / 2;

        Path path = new Path();

        if (shapeDirection == 0) {
            // Top Direction Shape
            path.moveTo(0, 2 * height);
            path.lineTo(width, 0);
            path.lineTo(2 * width, 2 * height);
            path.lineTo(0, 2 * height);

        } else if (shapeDirection == 1) {
            //Right Direction Shape
            path.moveTo(0, 0);
            path.lineTo(2 * width, height);
            path.lineTo(0, 2 * height);
            path.lineTo(0, 0);

        } else if (shapeDirection == 2) {

            //Bottom Direction Shape

            path.moveTo(0, 0);
            path.lineTo(width, 2 * height);
            path.lineTo(2 * width, 0);
            path.lineTo(0, 0);

        } else if (shapeDirection == 3) {

            //Left Direction Shape
            path.moveTo(2 * width, 0);
            path.lineTo(0, height);
            path.lineTo(2 * width, 2 * height);
            path.lineTo(2 * width, 0);

        } else if (shapeDirection == 4) {

            //Promotion Direction Shape

            path.moveTo(width, 0);
            path.lineTo(width * 2, 0);
            path.lineTo(width * 2, width);
            path.lineTo(width, 0);

        }

        path.close();

        Paint p = new Paint();
        p.setColor(shapeColor);
        p.setAntiAlias(true);

        canvas.drawPath(path, p);

    }
}
