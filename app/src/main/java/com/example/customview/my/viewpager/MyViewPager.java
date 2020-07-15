package com.example.customview.my.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
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

/**
 * 定义接口<以点击事件为例>
 * 1. 定义接口： public interface OnClickListener{void onClick();...} → 当监听的事件发生时 回调 .onClick() 方法
 * 2. 让使用者 传递接口的 实力进来 public void setOnclickListener(OnClickListener l){...}
 * 3. 调用方法：li.mOnClickListener.onClick(this)
 * 4. 用户使用：view.setOnclickListener(new View.OnclickListener(){...}
 * 5. 回调，实现 重写的 OnClick()
 */
public class MyViewPager extends ViewGroup {
    /**
     * 手势识别器
     * 1.定义出来
     * 2.<在 构造器 方法中> 实例化-把想要的方法给重写
     * 3.在onTouchEvent()把事件传递给手势识别器;它无法拦截事件，只是操作
     */
    private GestureDetector mGestureDetector;

    private int titleHeight = DensityUtil.dip2px(getContext(), 48);
    private int mCurrentIndex;  //当前页面的 坐标位置

    private MyScroll scroller;
    private VelocityTracker mTracker;

    public static final String  TAG = "MyViewPager";
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
        /**
         * 对滑动速度的 测试；VelocityTracker
         */
        mTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //遍历孩子，给每一个孩子指定 在屏幕的坐标。

        for (int i = 0; i < getChildCount(); i++) {
            View childrenView = getChildAt(i);
            if (childrenView.getVisibility() != View.GONE) {

                childrenView.layout(i * getWidth(), titleHeight, (i + 1) * getWidth(), childrenView.getMeasuredHeight());
            }
        }
    }

    private float startX;
    private float downX;
    private float downY;

    /**
     * 触摸事件存在 BUG ，scrollView 只可以上下滑 而没有实现 左右滑，
     * 在这里 进行  <拦截>  解决。→ 是否拦截 事件传递给孩子
     * 如果当前方法 返回 true；拦截事件将会触发当前控件的 onTouchEvent() 方法
     * 如果当前方法 返回 false；事件继续传递给 孩子,执行 ListView 的上下滚动。
     * × 这里返回 true 以后，scrollView 可以左右滑动 ，但又不可以上下滑动了。
     * 解决：监听 滑动距离，上下多就返回 false，继续响应滑动；左右滑动多，就返回 true。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * BUG：当 上下滚动时，拦截了左右滑动事件，不up直接左右滑，
         * 导致 .onTouchEvent() 手势识别器直接执行的是 DOWN ，并未执行 DOWN ，
         * 结果就是，手势识别器检测到的X轴数据猛增，出现指针闪动
         * 解决：在拦截事件中 也使用 手势识别器；不论是否拦截，都会将手势的移动及时传给手势识别器
         */
        mGestureDetector.onTouchEvent(ev);
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

                /**
                 * 检测结果是 水平滑动，就进行拦截，实现父视图的 ViewPager 走后华东；
                 */
                if (distanceX > distanceY && distanceX > 5.0) {
                    result = true;
                } else {
                    scrollToPager(mCurrentIndex);
                }
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 触摸事件，实现滑到其他页面;
     * 实现快速滑动；也会滑到下一页面 → 在 ACTION_UP 中对快速滑动进行处理；
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //3.在onTouchEvent()把事件传递给手势识别器
        mGestureDetector.onTouchEvent(event);
        /**
         * 用 addMovement(MotionEvent)函数将 Motion event加入到VelocityTracker类实例中;
         * 否则 测速结果为零；                    mTracker.computeCurrentVelocity(1000);
         */
        mTracker.addMovement(event);

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
                if (Math.abs(startX - endX) > getWidth() / 2) {
                    if ((startX - endX) > 0) {
                        //显示 下 一个页面
                        tempIndex++;
                    } else {
                        //显示 上 一个页面
                        tempIndex--;
                    }
                } else {
                    /**
                     * 1. 获取水平方向的速度；1s 内的移动像素速度
                     */
                    mTracker.computeCurrentVelocity(1000);
                    float xV = mTracker.getXVelocity();
                    Log.i(TAG, "onTouchEvent: xV = " + xV );
                    /**
                     * 2. 如果水平速度大于 50，就认定为快速滑动；进行切换页面
                     */
                    if (Math.abs(xV) > 50) {
                        if (xV > 0) {
                            tempIndex--;
                        } else {
                            tempIndex++;
                        }
                    }
                }

                /**
                 * 根据 下标位置 移动到指定页面;并进行了 错误下标准的矫正
                 */
                scrollToPager(tempIndex);
                /**
                 * 重置测速器
                 */
                mTracker.clear();
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
        /**
         * 滚动后 立刻刷新
         */
        invalidate();   //调用  .onDraw() 和 .computeScroll() 方法执行
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            float currX = scroller.getCurrentX();

            scrollTo((int) currX, 0);
            postInvalidate();
        }
    }

    public interface OnPagerChangedListener {

        /**
         * 当页面改变的时候  回调本方法， 并将当前 页面的下标  回传
         *
         * @param position 当前页面的下标
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
     * .onMeasure()总结
     *  系统的onMesaure中所干的事：
     *  1、根据 widthMeasureSpec 求得宽度 width，和父 view 给的模式
     *  2、根据自身的宽度 width 和自身的 padding 值，相减，求得子 view可以拥有的宽度 newWidth
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
        /**
         * 对 自定义 ViewGroup 的wrap_content 属性进行处理;
         * 这里没有对 padding 和 margin 等属性进行考虑
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * if()→如果没有子元素，就设置默认宽和高都为 0
         * else if() → 如果宽和高都是 AT_MOST，则宽高设置为所有子元素的宽度之和；高设置为第一个元素的高度
         * else if() → 如果宽度设置为 AT_MOST，则宽度设置为所有子元素的宽度之和；
         * else if() → 如果高度设置为 AT_MOST，则高度设置为所有子元素的宽度之和；
         */
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getWidth() * getChildCount();
            int childHeight = childOne.getHeight();
            setMeasuredDimension(childWidth, childHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getWidth() * getChildCount();
            setMeasuredDimension(childWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }

        /**
         * 解决 插入的页面 显示白屏的 BUG
         */
        for (int count = 0; count < getChildCount(); count++) {
            View childView = getChildAt(count);
            childView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}