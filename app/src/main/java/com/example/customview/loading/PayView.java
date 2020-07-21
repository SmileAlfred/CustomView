package com.example.customview.loading;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

import java.text.BreakIterator;

/**
 * @author LiuSaiSai
 * @date :2020/07/20 10:12
 * @description:实现支付动画；注意：当涉及到 float 类型的逐渐递增，最好不要使用 float f == int i 这种比较的操作；
 * 因为基本上，f = 0.99450225 和 1.0094247 时，是无法进入的。
 */
public class PayView extends View {

    private Paint mPaint;
    private Path mCirclePath;
    private Path mDstPath;
    private PathMeasure mPathMeasure;
    private Float mCurAnimValue;
    private float mStop;
    private int mRadius = 60;
    private int mCentX = 70;
    private int mCentY = 70;
    private static final String TAG = PayView.class.getClass().getSimpleName();

    public PayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);

        mCirclePath = new Path();
        mDstPath = new Path();
        mCirclePath.addCircle(mCentX, mCentY, mRadius, Path.Direction.CW);

        mCirclePath.moveTo(mCentX - mRadius / 2, mCentY);
        mCirclePath.lineTo(mCentX, mCentY + mRadius / 2);
        mCirclePath.lineTo(mCentX + mRadius / 2, mCentY - mRadius / 3);

        mPathMeasure = new PathMeasure(mCirclePath, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 2f);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAnimValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    //这里默认他已经绘制完圆形
    private boolean isCircleDrawed = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurAnimValue < 1) {
            //画出加载圆动画
            mStop = mPathMeasure.getLength() * mCurAnimValue;
            mPathMeasure.getSegment(0f, mStop, mDstPath, true);
            //Log.i(TAG, " mCurAnimValue = " + mCurAnimValue + " ; onDraw : mStop =  " + mStop);
        } else {
            //这里原本判断的是 mCurAnimValue == 1 时，和 mCurAnimValue > 1 时，但是判断中无法进入 mCurAnimValue == 1；
            if (isCircleDrawed) {
                //画完圆圈、并移动到下一个动画 Path;
                mPathMeasure.getSegment(0f, mPathMeasure.getLength(), mDstPath, true);
                mPathMeasure.nextContour();
                //Log.i(TAG, " mCurAnimValue = " + mCurAnimValue + " ; onDraw:mStop =  " + mStop);
                isCircleDrawed = false;
            }
            //画出圆内对勾
            mStop = mPathMeasure.getLength() * (mCurAnimValue - 1);
            mPathMeasure.getSegment(0f, mStop, mDstPath, true);
            //Log.i(TAG, " mCurAnimValue = " + mCurAnimValue + " ; onDraw:mStop =  " + mStop);
        }
        canvas.drawPath(mDstPath, mPaint);
    }
}
