package com.example.customview.my.viewpager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;


/**
 * @author LiuSaiSai
 * @description: 自定义 防 ViewPager 项目；<ViewPager 需要 adapter> ；本方法不用
 * @date :2020/03/02 9:46
 */
public class MyViewPagerItem extends AppCompatActivity {
    private MyViewPager mMyViewPager;
    private int[] imageIds = {
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6
    };
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view_pager);

        mMyViewPager = findViewById(R.id.my_view_pager);
        mRadioGroup = findViewById(R.id.radio_group_my_view_pager);

        //添加页面  和 点
        for (int count = 0; count < imageIds.length; count++) {

            //添加页面
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[count]);

            //添加页面 >>>   把 imageView 装到 mMyViewPager 中
            mMyViewPager.addView(imageView);
            if (count == 2) {
                /**
                 * 添加 测试 页面 ;出现 白屏 BUG，为什么？
                 * 因为 View 类中 没有 测量 .onMeasure()
                 * 这里存在BUG；就是插入的第二个页面后，并没有位置添加点；导致点数后延，最后一个 pager 没有点
                 */
                View testView = View.inflate(this, R.layout.activity_test_my_view_pager, null);
                mMyViewPager.addView(testView, 2);
            }
            //添加点
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(count);
            if (count == 0) {
                radioButton.setChecked(true);
            }
            // 添加点 >>>  添加到 RadioGroup
            mRadioGroup.addView(radioButton);

        }


        /**
         *  设置 RadioGroup 选中 状态的变化； → 当点击 点 的时候，切换页面
         */
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             * 回传 点中的 ID 数
             * @param group
             * @param checkedId 点中的 id；
             */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mMyViewPager.scrollToPager(checkedId);  // 根据 下标位置 定位到 具体的 某个页面
            }
        });

        /**
         * 设置 页面 监听 的改变，→  页面 滑动点跟着 动
         */
        mMyViewPager.setOnPagerChangedListener(new MyViewPager.OnPagerChangedListener() {
            @Override
            public void scrollToPager(int position) {
                /**
                 * 选中哪个位置的点
                 */
                mRadioGroup.check(position);
            }
        });
    }
}