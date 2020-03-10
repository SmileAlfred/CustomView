package com.example.customview.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @description:
 * @date :2020/03/04 23:10
 */
public class WaveView extends View {
    private Paint mpaint;
    private float mDownY;
    private float mDownX;

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radium = 100;
        mpaint = new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setAntiAlias(true);
        mpaint.setStyle(Paint.Style.STROKE);    //圆环
        mpaint.setStrokeWidth(radium / 3);

        invalidate();
    }

    private int radium = 105;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            radium += 5;

            int alpha = mpaint.getAlpha();
            alpha -= 5;
            if (alpha < 0) {
                alpha = 0;
            }
            mpaint.setAlpha(alpha);
            mpaint.setStrokeWidth(radium / 3);
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mpaint.getAlpha() > 0 && mDownX > 0) {
            canvas.drawCircle(mDownX, mDownY, radium, mpaint);
            mHandler.sendEmptyMessageDelayed(0, 30);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                initView();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
