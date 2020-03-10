package com.example.customview.quick.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @description: 自定义 联系人快速索引 右边栏 快速索引 View
 * 绘制快速索引的字母步骤：
 * 1.把26个字母放入数组
 * 2.在onMeasure计算每条的高itemHeight和宽itemWidth,
 * 3.在onDraw和wordWidth,wordHeight,wordX,wordY
 * @date :2020/03/04 11:05
 */
public class IndexView extends View {

    /**
     * 每条 Item 的宽和高
     */
    private int itemWidth;
    private int itemHeight;

    private Paint mPaint;

    private String[] words = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);   //设置颜色
        mPaint.setAntiAlias(true);      //抗锯齿
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);  //设置粗体
        mPaint.setTextSize(42);                     //设置字体大小
    }

    /**
     * 测量方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            /**
             * 当选中某一位置，.onTouchEvent()中，强制绘制本方法；
             * 当恰好 绘制的 i == touchIndex 时，将其画笔设置为 红色；否则默认还黑色
             */
            if (i == touchIndex) {
                mPaint.setColor(Color.RED);
            } else {
                mPaint.setColor(Color.BLACK);
            }
            /**
             * 计算每一个 文字的 大小，以及坐标
             */
            String word = words[i];

            /**
             * 定义一个新矩形，用以 框住 字母，以便计算字母的大小及坐标
             */
            Rect rect = new Rect();
            /**
             * 画笔：0，1 表示只取一个字母
             */
            mPaint.getTextBounds(word, 0, 1, rect);
            /**
             * 以矩形 的尺寸 得到字母的 宽高
             */
            int wordWidth = rect.width();
            int wordHeight = rect.height();
            /**
             * 计算 得到字母的 坐标 >>> 该座标 是以  举行的 <左下角> 计算的！！
             */
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;

            canvas.drawText(word, wordX, wordY, mPaint);
        }
    }

    /**
     * 字母的下标位置
     */
    private int touchIndex = -1;

    /**
     * 手指按下文字变色
     * 1.重写onTouchEvent(),返回true,在down/move的过程中计算
     * int touchIndex = Y / itemHeight; 强制绘制
     * <p>
     * 2.在onDraw()方法对于的下标设置画笔变色
     * <p>
     * 3.在up的时候
     * touchIndex  = -1；
     * 强制绘制
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float Y = event.getY();
                int index = (int) (Y / itemHeight);   // 字母索引

                if (index != touchIndex) {
                    touchIndex = index;
                    invalidate();
                    if (onIndexChangeListener != null && touchIndex < words.length ) {
                        onIndexChangeListener.onIndexChange(words[touchIndex]);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }

        return true;
    }

    /**
     * 字母下标 改变的监听器；定义接口，当滑动时，回传所在的 字母
     */
    public interface OnIndexChangeListener {
        /**
         * 当字母下表 发生改变的 时候 回调
         *
         * @param word 字母
         */
        void onIndexChange(String word);
    }

    private OnIndexChangeListener onIndexChangeListener;

    /**
     * 设置 字母下表索引变化的 监听
     *
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }


}