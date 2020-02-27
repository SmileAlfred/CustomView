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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuSaiSai
 * @description:实现广告条效果；可以滑动；下边有点数，可以循环，每一页下面有文字备注 设置了无限循环，怎么突然小红点消失了?
 * 实现自动滑动？1. for + sleep ? 2. handler
 * 按住就不再自动滑动
 * @date :2020/02/27 11:50
 */
public class AdvanceItem extends AppCompatActivity {

    private LinearLayout mPointAdvanceItem;
    private TextView mTextAdvanceItem;
    private ViewPager mViewPager;

    /**
     * ViewPager 的使用，类似于 ListView;
     * 1. 布局中定义；
     * 2. 代码中实例化；
     * 3. 准备数据；
     * 4. 设置(PagerAdapter)适配器 item 布局 → 绑定数据
     */
    private List<ImageView> mImageViewList;

    //图片资源
    private final int[] imageIds = {R.drawable.advance_3d,
            R.drawable.advance_forest,
            R.drawable.advance_road,
            R.drawable.advance_sumer,
            R.drawable.advance_woods_path,};
    //  记录 上一个点的位置
    private int prePosition = 0;
    //广告标题集合
    private final String[] imageDescriptions = {
            "3D环绕", "广袤森林", "一路向前", "万园之园", "", "俯瞰马路"
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int item = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(item);
            //延迟发消息
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_item);

        mViewPager = findViewById(R.id.view_pager);
        mTextAdvanceItem = findViewById(R.id.text_advance_item);
        mPointAdvanceItem = findViewById(R.id.point_advance_item);

        mImageViewList = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {

            ImageView imageView = new ImageView(this);
            //这里使用的是 setBackgroundResource；不是 src 避免图片不能充满；也不是setBackground
            imageView.setBackgroundResource(imageIds[i]);
            mImageViewList.add(imageView);  //添加到自己的 数据集合中

            // 添加点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            //  params 用于设置 点的 间隔
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i == 0) {
                point.setEnabled(true);     //显示红色
            } else {
                point.setEnabled(false);    //显示灰色
                params.leftMargin = 8;      //不是 第0 个点，就距离左边 8 个像素。
            }

            point.setLayoutParams(params);
            mPointAdvanceItem.addView(point);
        }
        //设置适配器
        mViewPager.setAdapter(new MyPagerAdapter());
        //设置监听 点的位置的改变
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 存在的 BUG ；可以无限向右滑动，但初始时，不能左滑；解决：将初始位置设置在中间
        //如果你想初始位置在首页，那么就让其能整除 页面总数；
        mViewPager.setCurrentItem(200);

        mTextAdvanceItem.setText(imageDescriptions[prePosition]);

        //发消息
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当页面滑动后，回调本方法
         *
         * @param position             当前页面的位置
         * @param positionOffset       滑动了 页面的 百分之几？
         * @param positionOffsetPixels 在屏幕上 滑动了多少个像素？
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中了的时候 回调
         *
         * @param position 被选中的 页面的 位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % mImageViewList.size();    //实现无限循环时，需要对其取模；
            //设置 对应的 文本信息
            mTextAdvanceItem.setText(imageDescriptions[realPosition]);
            //把上一个高亮的设置成 灰色
            mPointAdvanceItem.getChildAt(prePosition).setEnabled(false);
            //把当前页面设置成 高亮
            mPointAdvanceItem.getChildAt(realPosition).setEnabled(true);

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

            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_IDLE) { //空闲
            }
        }

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            return mImageViewList.size(); //本方法无法实现 无限循环
            return 400;
        }

        /**
         * 比较 view 和 object 是不是一个实例
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 相当于 ListView 中的 .getView()
         *
         * @param container ViewPager 自身
         * @param position  当前实例化页面的位置
         * @return
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int realPosition = position % mImageViewList.size();    //实现无限循环时，需要对其取模；
            ImageView imageView = mImageViewList.get(realPosition);
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
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        case MotionEvent.ACTION_UP:     //手指离开
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendEmptyMessageDelayed(0, 3000);
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            return imageView;
        }

        /**
         * 释放资源
         *
         * @param container view pager
         * @param position  要释放的位置
         * @param object    要释放的界面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

}
