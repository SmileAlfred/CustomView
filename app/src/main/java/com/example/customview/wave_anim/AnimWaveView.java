package com.example.customview.wave_anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @date :2020/07/22 10:05
 * @description: 水波纹动画
 */
public class AnimWaveView extends View {

    private Paint mPaint;
    private Paint mCirPaint;
    private Path mPath;
    private int mWaveLength = 800;
    private int dx;
    private int originY = 850;

    public AnimWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0XFF3DDC84);

        mCirPaint = new Paint();
        mCirPaint.setStyle(Paint.Style.STROKE);
        mCirPaint.setStrokeWidth(5);
        mCirPaint.setColor(0XFF3DDC84);

        setLayerType(View.LAYER_TYPE_SOFTWARE , null);
        startAnim();
    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                originY -= (2 * dx / mWaveLength);
                postInvalidate();
            }
        });

        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //剪裁画布，让动画只出现在圆形内，
        Path cirPath = new Path();
        cirPath.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CW);
        canvas.clipPath(cirPath);

        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mCirPaint);

        mPath.reset();
        int halfWaveLength = mWaveLength / 2;
        mPath.moveTo(-mWaveLength + dx, originY);
        for (int i = -mWaveLength; i <= getWidth() + mWaveLength; i += mWaveLength) {
            mPath.rQuadTo(halfWaveLength / 2, -100, halfWaveLength, 0);
            mPath.rQuadTo(halfWaveLength / 2, 100, halfWaveLength, 0);
        }
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPaint);
        if (originY <= -10) {
            originY = 850;
        }
    }
}
