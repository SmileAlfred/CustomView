package com.example.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * @author LiuSaiSai
 * @description: 仿 ViewPager 的视图
 * @date :2020/03/02 9:39
 */
public class MyViewPager extends ViewGroup {
    /**
     * 手势识别器
     * 1.定义出来
     * 2.<在 构造器 方法中> 实例化-把想要的方法给重写
     * 3.在onTouchEvent()把事件传递给手势识别器
     */
    private GestureDetector mGestureDetector;

    private int titleHeight = DensityUtil.dip2px(getContext(), 48);
    private int mCurrentIndex;  //当前页面的 坐标位置

    private MyScroll scroller;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        scroller = new MyScroll();
        //2.<在 构造器 方法中> 实例化-->>把想要的方法给重写
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 只写 动作的操作 没有用的，必须在 .onTouchEvent() 方法中返回 true; 并且！返回父类的值以作回调;并传回手势识别器
             * @param e
             */
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(context, "撒开你的咸猪手", Toast.LENGTH_SHORT).show();
            }

            /**
             * 是否滑动？
             * @param e1    触摸之前 的位置
             * @param e2    触摸之后 的位置
             * @param distanceX 在 X 轴移动了距离
             * @param distanceY
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                /**
                 * 要在 X 轴上移动的距离
                 */
                scrollBy((int) distanceX, getScrollY());    //从哪里 开始滑动；第一个参数 表示x 轴向移动；第二个表示 y方向就在这个高度上移动
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(context, "双击 666", Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历孩子，给每一个孩子指定 在屏幕的坐标。

        for (int i = 0; i < getChildCount(); i++) {
            View childrenView = getChildAt(i);

            childrenView.layout(i * getWidth(), titleHeight, (i + 1) * getWidth(), getHeight());
        }
    }

    private float startX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //3.在onTouchEvent()把事件传递给手势识别器
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1. 记录坐标
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
            case MotionEvent.ACTION_UP:
                //2. 记录新坐标
                float endX = event.getX();
                //记录下标位置
                int tempIndex = mCurrentIndex;
                if ((startX - endX) > getWidth() / 2) {
                    //显示 下 一个页面
                    tempIndex++;
                } else if ((endX - startX) > getWidth() / 2) {
                    //显示 上 一个页面
                    tempIndex--;
                }
                //根据 下标位置 移动到指定页面
                scrollToPager(tempIndex);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 1. 屏蔽非法值；
     * 2. 根据位置移动到指定页面
     *
     * @param tempIndex
     */
    private void scrollToPager(int tempIndex) {

        if (tempIndex < 0) {
            tempIndex = 0;
        }
        if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }
        //当前页面的 下表 位置
        mCurrentIndex = tempIndex;

        float distanceX = mCurrentIndex * getWidth() - getScrollX();

        //scrollTo(mCurrentIndex * getWidth(), getScrollY());        //瞬间移动到 指定位置；生硬
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, 0);

        invalidate();   //调用  .onDraw() 和 .computeScroll() 方法执行
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            float currX = scroller.getCurrentX();

            scrollTo((int) currX,0);
            invalidate();
        }
    }
}