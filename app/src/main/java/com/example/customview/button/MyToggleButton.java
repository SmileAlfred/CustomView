package com.example.customview.button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description: 自定义控件 状态开关按钮
 * 一个视图从创建到显示过程中的主要方法:
 * 1. 构造方法；实例化类(extends View)
 * 2. 测量 measure(int,int)； → 重写onMeasure()
 * 2.1. 如果当前的 View 是 ViewGroup 还要测量孩子；
 * 2.2. 孩子有建议权
 * 3. 指定位置 activity_main → 抽象onLayout()；View 不必重写，ViewGroup 才重写
 * 4. 绘制视图； draw() → onDraw(canvas)；根据上面两个方法参数，进入绘制
 * BUG:× × × × × × × 不响应 Move 和 Up >>>原因：case MotionEvent.ACTION_DOWN:  而非 MotionEvent.ACTION_POINTER_DOWN
 *
 * ※ ※ ※ View 的绘制过程
 *
 * @date :2020/03/01 10:46
 */

/**
 * View绘制过程
 *
 * 1.测量-measure()---onMeasure();
 * 2.指定在屏幕的位置--layout()--onLayout()
 *  子类只有建议权，父类才有决定权
 *  一般view中不使用，并且源码中是空的方法；
 *  ViewGroup中改方法是抽象的，必须要实现，因为要指定位置孩子的位置
 * 3.绘制控件到屏幕上--draw()---onDraw()
 *
 * 自定义View的时候一般重新onMeasure(int,int)和onDraw(canvas);
 *
 *
 *
 * 基本操作由三个函数完成：measure()、layout()、draw()，其内部又分别包含了onMeasure()、onLayout()、onDraw()三个子方法。具体操作如下：
 * 1、measure操作
 *
 *      measure操作主要用于计算视图的大小，即视图的宽度和长度。在view中定义为final类型，要求子类不能修改。measure()函数中又会调用下面的函数：
 *
 *     （1）onMeasure()，视图大小的将在这里最终确定，也就是说measure只是对onMeasure的一个包装，子类可以覆写onMeasure()方法实现自己的计算视图大小的方式，并通过setMeasuredDimension(width, height)保存计算结果。
 * 2、layout操作
 *
 *      layout操作用于设置视图在屏幕中显示的位置。在view中定义为final类型，要求子类不能修改。layout()函数中有两个基本操作：
 *
 *      （1）setFrame（l,t,r,b），l,t,r,b即子视图在父视图中的具体位置，该函数用于将这些参数保存起来；
 *
 *      （2）onLayout()，在View中这个函数什么都不会做，提供该函数主要是为viewGroup类型布局子视图用的；
 *
 * 3、draw操作
 *
 *      draw操作利用前两部得到的参数，将视图显示在屏幕上，到这里也就完成了整个的视图绘制工作。子类也不应该修改该方法，因为其内部定义了绘图的基本操作：
 *
 *      （1）绘制背景；
 *
 *      （2）如果要视图显示渐变框，这里会做一些准备工作；
 *
 *      （3）绘制视图本身，即调用onDraw()函数。在view中onDraw()是个空函数，也就是说具体的视图都要覆写该函数来实现自己的显示（比如TextView在这里实现了绘制文字的过程）。而对于ViewGroup则不需要实现该函数，因为作为容器是“没有内容“的，其包含了多个子view，而子View已经实现了自己的绘制方法，因此只需要告诉子view绘制自己就可以了，也就是下面的dispatchDraw()方法;
 *
 *      （4）绘制子视图，即dispatchDraw()函数。在view中这是个空函数，具体的视图不需要实现该方法，它是专门为容器类准备的，也就是容器类必须实现该方法；
 *
 *      （5）如果需要（应用程序调用了setVerticalFadingEdge或者setHorizontalFadingEdge），开始绘制渐变框；
 *
 *      （6）绘制滚动条；
 *
 *       从上面可以看出自定义View需要最少覆写onMeasure()和onDraw()两个方法。
 * ViewGroup中的扩展操作：
 *
 *      首先Viewgroup是一个抽象类。
 * 1、对子视图的measure过程
 *
 *      （1）measureChildren()，内部使用一个for循环对子视图进行遍历，分别调用子视图的measure()方法；
 *
 *      （2）measureChild()，为指定的子视图measure，会被 measureChildren调用；
 *
 *      （3）measureChildWithMargins()，为指定子视图考虑了margin和padding的measure；
 *
 *       以上三个方法是ViewGroup提供的3个对子view进行测量的参考方法，设计者需要在实际中首先覆写onMeasure()，之后再对子view进行遍历measure，这时候就可以使用以上三个方法，当然也可以自定义方法进行遍历。
 * 2、对子视图的layout过程
 *
 *      在ViewGroup中onLayout()被定义为abstract类型，也就是具体的容器必须实现此方法来安排子视图的布局位置，实现中主要考虑的是视图的大小及视图间的相对位置关系，如gravity、layout_gravity。
 * 3、对子视图的draw过程
 *
 *    （1）dispatchDraw()，该方法用于对子视图进行遍历然后分别让子视图分别draw，方法内部会首先处理布局动画（也就是说布局动画是在这里处理的），如果有布局动画则会为每个子视图产生一个绘制时间，之后再有一个for循环对子视图进行遍历，来调用子视图的draw方法（实际为下边的drawChild()）；
 *
 *     （2）drawChild()，该方法用于具体调用子视图的draw方法，内部首先会处理视图动画（也就是说视图动画是在这里处理的），之后调用子视图的draw()。
 *
 *     从上面分析可以看出自定义viewGroup的时候需要最少覆写onMeasure()和onLayout()方法，其中onMeasure方法中可以直接调用measureChildren等已有的方法，而onLayout方法就需要设计者进行完整的定义；一般不需要覆写以dispatchDraw()和drawChild()这两个方法，因为上面两个方法已经完成了基本的事情。但是可以通过覆写在该基础之上做一些特殊的效果，比如
 *
 *
 * 其他
 *
 *       从以上分析可以看出View树的绘制是一个递归的过程，从ViewGroup一直向下遍历，直到所有的子view都完成绘制，那这一切的源头在什么地方（是谁最发起measure、layout和draw的）？当然就是在View树的源头了——ViewRoot！，ViewRoot中包含了窗口的总容器DecorView，ViewRoot中的performTraversal()方法会依次调用decorView的measure、layout、draw方法，从而完成view树的绘制。
 *
 *      invalidate()方法
 *
 *      invalidate()方法会导致View树的重新绘制，而且view中的状态标志mPrivateFlags中有一个关于当前视图是否需要重绘的标志位DRAWN，也就是说只有标志位DRAWN置位的视图才需要进行重绘。当视图调用invalidate()方法时，首先会将当前视图的DRAWN标志置位，之后有一个循环调用parent.invalidateChildinParent()，这样会导致从当前视图依次向上遍历直到根视图ViewRoot，这个过程会将需要重绘的视图标记DRAWN置位，之后ViewRoot调用performTraversals()方法，完成视图的绘制过程。
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
     * 触摸事件，必须 先 super，最后返回 true；
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