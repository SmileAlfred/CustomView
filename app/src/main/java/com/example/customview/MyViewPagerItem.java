package com.example.customview;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author LiuSaiSai
 * @description: 自定义 防 ViewPager 项目；ViewPager 需要 adapter ；本方法不用
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view_pager);

        mMyViewPager = findViewById(R.id.my_view_pager);

        //添加页面
        for (int count = 0; count < imageIds.length; count++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[count]);

            //把 imageView 装到 mMyViewPager 中
            mMyViewPager.addView(imageView);
        }
    }
}

