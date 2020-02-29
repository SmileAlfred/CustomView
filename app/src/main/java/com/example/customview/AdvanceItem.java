package com.example.customview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description: 实现广告条效果；可以滑动；下边有点数，可以循环，每一页下面有文字备注；
 * 1. 设置无限循环：⚪ .getCount() 返回超大数；⚪ mViewPager.setCurrentItem(50)解决不能左滑；其余状态小改
 * 2. 怎么突然小红点消失了?并没有消失；只是 params 设置的尺寸太小了
 * 3. 实现自动滑动？1. for + sleep ? 2. handler
 * 4. 按住就不再自动滑动
 * BUG： 小红点消失了？！ 实际上是 params 设置的太小了，看不到！
 * @date :2020/02/27 11:50
 */
public class AdvanceItem extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView mTextAdvanceItem;
    private LinearLayout mPointAdvanceItem;

    private List<ImageView> mImageViewList;
    /**
     * 图片资源
     */
    private final int[] imageIds = {R.drawable.advance_3d,
            R.drawable.advance_forest,
            R.drawable.advance_road,
            R.drawable.advance_sumer,
            R.drawable.advance_woods_path,};

    private final String[] imageDescriptions = {
            "3D环绕", "广袤森林", "一路向前", "万园之园", "", "俯瞰马路"
    };

    /**
     * 记录 上一个点的位置
     */
    private int prePosition = 0;
    /**
     * 是否 已经 滑动
     */
    private boolean isDragging = false;    //广告标题集合


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1); //自动化东到下一页

            mHandler.sendEmptyMessageDelayed(0, 3000);  //发延迟消息
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_item);

        mViewPager = findViewById(R.id.view_pager);
        mTextAdvanceItem = findViewById(R.id.text_advance_item);
        mPointAdvanceItem = findViewById(R.id.point_advance_item);

        /**
         * ViewPager 的使用，类似于 ListView;
         * 1. 布局中定义；
         * 2. 代码中实例化；
         * 3. 准备数据；
         * 添加点，点是在 drawable 中新建 xml 文件，<shape ... oval ...
         */
        mImageViewList = new ArrayList<>();

        for (int i = 0; i < imageIds.length; i++) {

            ImageView imageView = new ImageView(this);

            imageView.setBackgroundResource(imageIds[i]);   //这里使用的是 setBackgroundResource；不是 src 避免图片不能充满；也不是setBackground

            mImageViewList.add(imageView);                  //添加到自己的 数据集合中

            ImageView point = new ImageView(this);  // 添加点

            point.setBackgroundResource(R.drawable.point_selector);//丢了关键一步！这里设置的是 selector；而selector设置了true或false

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(28, 28);   //  params 用于设置 点的 间隔

            if (i == 0) {
                point.setEnabled(true);     //显示红色
            } else {
                point.setEnabled(false);    //显示灰色
                params.leftMargin = 18;     //不是 第0 个点，就距离左边 8 个像素。
            }
            point.setLayoutParams(params);

            mPointAdvanceItem.addView(point);
        }

        /**
         * 4. 设置(PagerAdapter)适配器 item 布局 → 绑定数据
         *          BUG ；可以无限向右滑动，但初始时，不能左滑；
         *          解决：.setCurrentItem() 将初始位置设置在中间
         */
        mViewPager.setAdapter(new MyPagerAdapter());

        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());   //设置监听 点的位置的改变

        mViewPager.setCurrentItem(50);  //如果你想初始位置在首页，那么就让其能整除 页面总数；

        mTextAdvanceItem.setText(imageDescriptions[prePosition]);

        mHandler.sendEmptyMessageDelayed(0, 3000);  //发消息
    }

    /**
     * 自定义监听器 继承自 ViewPager.OnPageChangeListener
     * 当页面滑动式，响应本监听器
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滑动后，回调本方法
         *
         * @param position             当前页面的位置；从 0 开始
         * @param positionOffset       滑动了 页面的 百分之几？
         * @param positionOffsetPixels 在屏幕上 滑动了多少个像素？
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选择了的时候 回调
         *
         * @param position 被选中的 页面的 位置
         */
        @Override
        public void onPageSelected(int position) {

            int realPosition = position % mImageViewList.size();            //实现无限循环时，需要对其取模；

            mTextAdvanceItem.setText(imageDescriptions[realPosition]);      //设置 对应的 文本信息

            mPointAdvanceItem.getChildAt(prePosition).setEnabled(false);    //mPointAdvanceItem 是点所在的 LinearLayout

            mPointAdvanceItem.getChildAt(realPosition).setEnabled(true);    //把当前页面设置成 高亮

            prePosition = realPosition;
        }

        /**
         * 页面滚动状态改变的时候  回调本方法
         * 静止 → 滑动      滑动 → 静止     静止 → 拖拽
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {   //拖拽
                isDragging = true;
                mHandler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) { //空闲
                isDragging = false;
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessageDelayed(0, 4000);
            }
        }

    }

    /**
     * 为 ViewPager 设置适配器；此处重写了四个方法
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            return mImageViewList.size(); //本方法无法实现 无限循环
            return 100;
        }

        /**
         * 相当于 ListView 中的 .getView()；
         *
         * @param container ViewPager 自身
         * @param position  当前实例化页面的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = position % mImageViewList.size();    //实现无限循环时，需要对其取模；

            final ImageView imageView = mImageViewList.get(realPosition);
            container.addView(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:   //手指摁下
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE:   //手指滑动
                            break;
                        case MotionEvent.ACTION_CANCEL:   //手指滑动
//                            mHandler.removeCallbacksAndMessages(null);
//                            mHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        case MotionEvent.ACTION_UP:     //手指离开
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            /**
             * 设置图片的点击事件；
             * 1. 设置 .setTag() 是为了指导此处是哪个位置;
             * 2. 将触摸事件返回 false； 否则点击事件没有用;
             * 3. position 取模，不然就角标越界。
             */
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag() % mImageViewList.size();
                    String imageDescriptionText = imageDescriptions[position];
                    Toast.makeText(AdvanceItem.this, imageDescriptionText, Toast.LENGTH_SHORT).show();
                }
            });
            return imageView;
        }

        /**
         * 比较 view 和 object 是不是一个实例
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        /**
         * 释放资源
         *
         * @param container view pager
         * @param position  要释放的位置
         * @param object    要释放的界面
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
