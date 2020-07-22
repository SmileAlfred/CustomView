package com.example.customview.gesture_track;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @date :2020/07/22 07:39
 * @description:
 */
public class NormalGestureTrackView extends View {
    private Paint mPaint;
    private Path mPath;

    //起始点坐标
    private float mPreX;
    private float mPreY;

    //结束点坐标
    private float mEndX;
    private float mEndY;

    public NormalGestureTrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPreX = event.getX();
                mPreY = event.getY();
                mPath.moveTo(mPreX, mPreY);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (GestureTrackActivity.isLineTo) {
                    mEndX = event.getX();
                    mEndY = event.getY();
                    mPath.lineTo(mEndX, mEndY);
                } else {
                    mEndX = (event.getX() + mPreX) / 2;
                    mEndY = (event.getY() + mPreY) / 2;
                    mPath.quadTo(mPreX, mPreY, mEndX, mEndY);
                    mPreX = event.getX();
                    mPreY = event.getY();
                }
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
