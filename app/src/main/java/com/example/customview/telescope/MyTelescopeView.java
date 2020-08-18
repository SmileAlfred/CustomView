package com.example.customview.telescope;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;
import com.example.customview.loading.FallingBallImageView;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/18 20:21
 * @description: BUG: 放大镜的位置不合适；解决：半径和放大倍数设置为静态，即可；
 * 收获：activity 向 自定义 View 传值：在View中写set()方法，在activity中初始化后调用set();
 * 问题：为什么不设置静态就会出错？
 */

public class MyTelescopeView extends View {

    private Bitmap mBitmap;
    private ShapeDrawable mShapeDrawable;
    private  int radius = 320;
    private  int factor = 3;
    private Matrix mMatrix = new Matrix();

    private static final String TAG = MyTelescopeView.class.getSimpleName();
    private Bitmap mDecodeResource;

    public MyTelescopeView(Context context) {
        super(context);
        init();
    }

    public MyTelescopeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTelescopeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setParameter(int radius, int factor) {
        this.radius = radius;
        this.factor = factor;
        //解决，更新放大倍率、放大镜半径后，镜子位置岔劈的问题；
        mBitmap = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                //待办事项：表示的是绘制 Shader 的起始位置
                mMatrix.setTranslate(radius - factor * x, radius - factor * y);
                mShapeDrawable.getPaint().getShader().setLocalMatrix(mMatrix);

                // bounds，圆的外切矩形
                mShapeDrawable.setBounds(x - radius, y - radius, x + radius, y + radius);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mShapeDrawable.setBounds(-10, -10, 0, 0);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) {
            mDecodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.advance_woods_path);
            mBitmap = Bitmap.createScaledBitmap(mDecodeResource, getWidth(), getHeight(), false);

            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(mBitmap,
                    mBitmap.getWidth() * factor, mBitmap.getHeight() * factor, true),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mShapeDrawable = new ShapeDrawable(new OvalShape());
            mShapeDrawable.getPaint().setShader(shader);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
        mShapeDrawable.draw(canvas);
    }
}