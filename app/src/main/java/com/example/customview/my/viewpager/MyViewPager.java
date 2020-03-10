package com.example.customview.my.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.customview.util.DensityUtil;


/**
 * @author LiuSaiSai
 * @description: 仿 ViewPager 的视图
 * 难点：※ ※ ※ ※ 怎样定义一个 监听的 接口？    参照点击事件
 * 重点：※ ※ ※ ※ 学会 事件传递 事件拦截
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
    private float downX;
    private float downY;

    /**
     * 触摸事件存在 BUG ，scrollView 只可以上下滑 而没有实现 左右滑，
     * 在这里 进行  <拦截>  解决。如果当前方法 返回 true；拦截事件将会触发当前控件的 onTouchEvent() 方法
     * 如果当前方法 返回 false；事件继续传递给 孩子。
     * × 这里返回 true 以后，scrollView 可以左右滑动 ，但又不可以上下滑动了。
     * 解决：监听 滑动距离，上下多就返回 false，继续响应滑动；左右滑动多，就返回 true。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);  //解决 上下滑动后再左右滑时，出现的BUG<时间冲突问题>；
        boolean result = false; //默认把结果传递给 孩子

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1. 记录坐标
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2. 记录 结束值
                float endX = ev.getX();
                float endY = ev.getY();

                //3. 计算绝对值
                float distanceX = Math.abs(endX - downX);
                float distanceY = Math.abs(endY - downY);

                if (distanceX > distanceY && distanceX > 5.0) {
                    result = true;
                }else {
                    scrollToPager(mCurrentIndex);
                }
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 触摸事件，
     *
     * @param event
     * @return
     */
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
    public void scrollToPager(int tempIndex) {

        if (tempIndex < 0) {
            tempIndex = 0;
        }
        if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }
        //当前页面的 下表 位置
        mCurrentIndex = tempIndex;

        if (mOnPagerChangedListener != null) {
            mOnPagerChangedListener.scrollToPager(mCurrentIndex);
        }

        float distanceX = mCurrentIndex * getWidth() - getScrollX();

        //scrollTo(mCurrentIndex * getWidth(), getScrollY());             //瞬间移动到 指定位置；生硬
        //scroller.startScroll(getScrollX(), getScrollY(), distanceX, 0); //缓慢移动，但是 没有 时长 限制
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, 0, Math.abs(distanceX));

        invalidate();   //调用  .onDraw() 和 .computeScroll() 方法执行
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            float currX = scroller.getCurrentX();

            scrollTo((int) currX, 0);
            invalidate();
        }
    }

    public interface OnPagerChangedListener {

        /**
         * 当页面改变的时候  回调本方法， 并将当前 页面的下标  回传
         *
         * @param position  当前页面的下标
         */
        void scrollToPager(int position);
    }

    private OnPagerChangedListener mOnPagerChangedListener;

    /**
     * 设置页面 改变 的监听
     *
     * @param onPagerChangedListener
     */
    public void setOnPagerChangedListener(OnPagerChangedListener onPagerChangedListener) {
        mOnPagerChangedListener = onPagerChangedListener;
    }

    /**
     * 测量的时候 测量多次，
     * .onMesaure()总结
     *  系统的onMesaure中所干的事：
     *  1、根据 widthMeasureSpec 求得宽度width，和父view给的模式
     *  2、根据自身的宽度width 和自身的padding 值，相减，求得子view可以拥有的宽度newWidth
     *  3、根据 newWidth 和模式求得一个新的MeasureSpec值:
     *   MeasureSpec.makeMeasureSpec(newSize, newmode);
     *  4、用新的MeasureSpec来计算子view
     *
     * @param widthMeasureSpec  父层视图 给当前视图的 宽
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int count = 0; count < getChildCount(); count++) {
            View childView = getChildAt(count);
            childView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}