package com.example.customview.telescope;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.customview.R;

/**
 * Created by qijian on 16/7/10.
 */
public class TelescopeView extends View {
    private Bitmap bitmap;
    private ShapeDrawable drawable;
// 放大镜的半径

    private static final int RADIUS = 320;
// 放大倍数

    private static final int FACTOR = 3;
    private final Matrix matrix = new Matrix();

    public TelescopeView(Context context) {
        super(context);
        init();
    }

    public TelescopeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TelescopeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        // 这个位置表示的是，画shader的起始位置
        matrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y * FACTOR);
        drawable.getPaint().getShader().setLocalMatrix(matrix);

        // bounds，就是那个圆的外切矩形
        drawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pikachu);
            bitmap = Bitmap.createScaledBitmap(bmp, getWidth(), getHeight(), false);

            BitmapShader shader = new BitmapShader(Bitmap.createScaledBitmap(bitmap,
                    bitmap.getWidth() * FACTOR, bitmap.getHeight() * FACTOR, true),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setShader(shader);
            drawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);
        }

        canvas.drawBitmap(bitmap, 0, 0, null);
        drawable.draw(canvas);

        
    }

}
