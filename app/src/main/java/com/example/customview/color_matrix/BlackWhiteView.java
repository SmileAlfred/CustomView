package com.example.customview.color_matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/18 07:56
 * @description: 色彩投射运算 - 彩图黑白化
 */
public class BlackWhiteView extends View {
    private Paint mPaint = new Paint();
    private Bitmap mBitmap;

    public BlackWhiteView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        mPaint.setAntiAlias(true);
        //获取位图
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBitmap(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        //生成色彩变换矩阵 R;G;B;A; 由于人眼对不同色彩的识别度不一样，以下参数是 Google最终给出的颜色值
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0, 0, 0, 1, 0,
        });

        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(mBitmap, null, new Rect(0, 0, 500, 500 * mBitmap.getHeight() / mBitmap.getWidth()), mPaint);
        canvas.save();
    }
}
