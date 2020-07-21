package com.example.customview.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.security.ConfirmationNotAvailableException;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

import java.nio.MappedByteBuffer;


/**
 * @author LiuSaiSai
 * @date :2020/07/20 07:57
 * @description:截取一旦路径，实现圆圈加载动画。间《自定义控件》P175
 */
public class GetSegmentView extends View {

    private Paint mPaint;
    private Path mCirclePath;
    private Path mDstPath;
    private PathMeasure mPathMeasure;
    private Float mCurAnimValue;
    private float mStop;
    private Bitmap mBitmap;
    private int mRadius = 60;
    private int mCentX = 70;
    private int mCentY = 70;

    private float[] pos = new float[2];
    private float[] tan = new float[2];

    public GetSegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.down_arrow_popup_item);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);

        mCirclePath = new Path();
        mDstPath = new Path();
        mCirclePath.addCircle(mCentX, mCentY, mRadius, Path.Direction.CW);

        mPathMeasure = new PathMeasure(mCirclePath, true);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurAnimValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出加载圆动画
        mStop = mPathMeasure.getLength() * mCurAnimValue;
        mDstPath.reset();
        mPathMeasure.getSegment(0f, mStop, mDstPath, true);
        canvas.drawPath(mDstPath, mPaint);

        //用 getPosTan() 获取图像旋转角度，画出加载动画的“箭头”
        mPathMeasure.getPosTan(mStop, pos, tan);
        float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI) - 90.0f;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        matrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);
        canvas.drawBitmap(mBitmap, matrix, mPaint);

        //用 getMatrix() 获取图像旋转角度，画出加载动画的“箭头”
        //Matrix matrix2 = new Matrix();
        //mPathMeasure.getMatrix(mStop, matrix2, PathMeasure.POSITION_MATRIX_FLAG |
        //        PathMeasure.TANGENT_MATRIX_FLAG);
        //matrix2.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);
        //canvas.drawBitmap(mBitmap, matrix, mPaint);

    }
}
