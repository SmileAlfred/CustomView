package com.example.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @description: 自定义控件 状态开关按钮
 * 一个视图从创建到显示过程中的主要方法:
 * 1. 构造方法；实例化类(extends View)
 * 2. 测量 measure(int,int)； → 重写onMeasure()
 * 2.1. 如果当前的 View 是 ViewGroup 还要测量孩子；
 * 2.2. 孩子有建议权
 * 3. 指定位置 layout → 抽象onLayout()；View 不必重写，ViewGroup 才重写
 * 4. 绘制视图； draw() → onDraw(canvas)；根据上面两个方法参数，进入绘制
 * BUG:× × × × × × × 不响应 Move 和 Up >>>原因：case MotionEvent.ACTION_DOWN:  而非 MotionEvent.ACTION_POINTER_DOWN
 * @date :2020/03/01 10:46
 */
public class MyToggleButton extends View implements View.OnClickListener {
    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;

    private int slideMarginMax; // 滑动后，距边的最大距离
    private Paint paint;
    private int slideLeft;      // 距离左边的值；用以判断 开关状态

    private boolean isOpen = true;  //是否点击事件 生效

    private boolean enableClicked;

    private float startX;
    private float isMove;   // 滑动？还是点击？


    /**
     * 在布局文件中 使用该类，将会用这个构造方法实例该类；没有就崩溃
     *
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);   //设置画笔 光滑；抗锯齿

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);

        slideMarginMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();

        slideLeft = slideMarginMax; // 设置开关默认为 开 的状态

        setOnClickListener(this); //设置触摸事件，暂时停用点击事件
    }

    /**
     * 视图的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight()); //保存显示的尺寸；
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slidingBitmap, slideLeft, 0, paint);
    }

    @Override
    public void onClick(View v) {
        if (enableClicked) {
            isOpen = !isOpen;
            reFreshView();
        }
    }

    private void reFreshView() {
        if (isOpen) {
            slideLeft = slideMarginMax;
        } else {
            slideLeft = 0;
        }
        invalidate();   //该方法 会调用 .onDraw() 方法执行
    }

    /**
     * 设置触摸事件;有返回值的方法，要及时返回，重写的方法，不要随意删除 .super()
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);                // 这里的 .super() 不能省略
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = startX = event.getX();   //记录一开始时 的X坐标；.getX()获取子布局内X坐标  .getRawX()获取 父布局中X坐标
                enableClicked = true;
                break;
            case MotionEvent.ACTION_MOVE:   // 移动手指，滑块一起移动
                float distanceX = event.getX() - startX;
                slideLeft += distanceX;     //实时记录 滑块 据左边的距离；
                //屏蔽非法(越界)值
                if (slideLeft < 0) {
                    slideLeft = 0;
                } else if (slideLeft > slideMarginMax) {
                    slideLeft = slideMarginMax;
                }
                //刷新
                invalidate();
                //如果移动距离超过 5 像素 就认为是滑动了，
                if (Math.abs(distanceX) > 5) {
                    enableClicked = false;
                }
                //还原数据
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP: // 如果移动到一半，松开手指回弹
                if (!enableClicked) {
                    if (slideLeft < slideMarginMax / 2) {
                        isOpen = false;
                    } else {
                        isOpen = true;
                    }
                    reFreshView();
                }
                break;
            default:
                break;
        }
        return true;
    }
}