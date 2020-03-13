package com.example.customview.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @description: 自定义 控件  用于显示 滑动菜单 的 item 项目
 * BUG:当侧滑出来后，再上下滑动其他位置，侧边栏还在 >>> 现在仅实现，已经打开某个 item 时，必须点击 改item 才能 收进去。
 * 并没有做到，点击任何位置 都收进去。
 * 已用 接口 解决
 * @date :2020/03/04 18:07
 */
public class SlideLayout extends FrameLayout {

    private View mContentView;
    private View mMenuView;

    private int contentWidth;
    private int menuWidth;
    private int viewHight;

    private float downX;    // 只赋值一次
    private float downY;

    /**
     * 滚动器
     */
    private Scroller mScroller;


    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    /**
     * 当布局文件 加载完成后  回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mMenuView = getChildAt(1);
    }

    /**
     * 在测量方法中 得到 空间的 宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = mContentView.getMeasuredWidth();
        menuWidth = mMenuView.getMeasuredWidth();
        viewHight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /**
         * 指定 菜单的 位置
         */
        mMenuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHight);
    }

    private float startX;
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1. 按下记录坐标
                downX = startX = event.getX();
                downY = startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2. 记录结束值
                float endX = event.getX();
                float endY = event.getY();
                //3. 计算偏移量
                float distanceX = endX - startX;

                /**
                 * content 和 menu 同时移动；注意 toScrollX 的值
                 */
                int toScrollX = (int) (getScrollX() - distanceX);

                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }

                scrollTo(toScrollX, getScrollY());

                startX = event.getX();
                startY = event.getY();
                /**
                 * 真正滑动的距离！
                 */
                float DX = Math.abs(endX - downX);
                float DY = Math.abs(endY - downY);

                if (DX > DY && DX > 5) {
                    //响应 左右滑动，反拦截事件； 响应 slideLayout;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX = getScrollX();    //偏移量
                if (totalScrollX < menuWidth / 3) {
                    //关闭 menu
                    closeMenu();
                } else {
                    // 打开menu
                    openMenu();
                }
                break;
        }
        return true;

    }

    /**
     * 返回 true 拦截孩子<点击>的 事件；但会 执行 当前控件的 onTouchEvent()<滑动> 方法
     * 返回 false 不拦截 孩子的事件，事件继续 传递
     * 解决 点击事件和 滑动事件的 冲突
     * @param ev
     * @return
     */
  @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1. 按下记录坐标
                downX = startX = ev.getX();

                if (onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //2. 记录结束值
                float endX = ev.getX();
                //3. 计算偏移量
                float distanceX = endX - startX;

                startX = ev.getX();

                float DX = Math.abs(endX - downX);

                if (DX > 5) {
                    intercept = true;
                } else {
                    intercept = false;
                    closeMenu();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return intercept;

    }

    private void openMenu() {
        int distanceX = menuWidth - getScrollX();
        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
        invalidate();
    }

    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, 0);
        if (onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 监听 slideLayout 状态的改变
     */
    public interface OnStateChangeListener {
        void onClose(SlideLayout layout);

        void onDown(SlideLayout layout);

        void onOpen(SlideLayout layout);
    }

    /**
     * 设置 slideLayout 状态的 监听
     *
     * @param onStateChangeListener
     */
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    private OnStateChangeListener onStateChangeListener;
}
