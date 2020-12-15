package com.jhzl.mindstudy.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class LineView extends View {
    public LineView(Context context) {
        super(context);
    }

    private Point mStartPoint;
    private Point mEndPoint;

    public void setPoint(Point startPoint, Point endPoint) {
        this.mStartPoint = startPoint;
        this.mEndPoint = endPoint;
    }


    private int mColor = -1;

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    private Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mColor != -1) {
            mPaint.setColor(mColor);
        }
        if (mStartPoint != null && mEndPoint != null) {
            canvas.drawLine(mStartPoint.x, mStartPoint.y, mEndPoint.x, mEndPoint.y, mPaint);
        }
    }
}
