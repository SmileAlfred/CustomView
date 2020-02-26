package com.example.customview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author LiuSaiSai
 * @description:优酷菜单实现.
 * 1. 关于其布局，要先添加 外面的 大的半圆，这样小圆也可以点击，
 * @date :2020/02/26 20:50
 */
public class YoukuMenu extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youku_menu);
    }
}
