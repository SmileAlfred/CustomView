package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.customview.advance.AdvanceItem;
import com.example.customview.attribute.AutoAttributeItem;
import com.example.customview.button.MyToggleButtonItem;
import com.example.customview.dispatch.DispatchActivity;
import com.example.customview.dy_loading.DYLoadingActivity;
import com.example.customview.hktech.HK;
import com.example.customview.loading.LoadingActvity;
import com.example.customview.my.viewpager.MyViewPagerItem;
import com.example.customview.popup.windows.PopupWindowsItem;
import com.example.customview.property.animation.PropertyAnimation;
import com.example.customview.quick.index.QuickIndexItem;
import com.example.customview.scrollbyandscrollto.TestScrollByAndScrollTo;
import com.example.customview.slide.SlideMenuItem;
import com.example.customview.touch_eventtest.TouchEventActivity;
import com.example.customview.wave.WaveItem;
import com.example.customview.wave_full.WaveFullItem;
import com.example.customview.youku.menu.MenuActivity;
import com.example.customview.youku.menu.YoukuMenu;

/**
 * 自定义控件 学习笔记
 * 需要掌握的重点：
 * 1. View 和 ViewGroup 的区别；
 * 2. Android 中的时间传递；
 * 3. View 的原理
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
    }

    private void findViews() {
        setContentView(R.layout.activity_main);

        TextView youkuMenu = findViewById(R.id.youku_menu);
        TextView viewpagerItem = findViewById(R.id.viewpager_item);
        TextView popupWindowsItem = findViewById(R.id.popup_windows_item);
        TextView toggleButtonItem = findViewById(R.id.toggle_button_item);
        TextView autoAttributeItem = findViewById(R.id.auto_attribute_item);
        TextView myViewPagerItem = findViewById(R.id.my_view_pager_item);
        TextView quickIndexItem = findViewById(R.id.quick_index_item);
        TextView slideMenuItem = findViewById(R.id.slide_menu_item);
        TextView waveItem = findViewById(R.id.wave_item);
        TextView propertyAnimationItem = findViewById(R.id.property_animation_item);
        TextView scrollByTestItem = findViewById(R.id.scrollby_test_item);
        TextView touchEventItem = findViewById(R.id.touch_event_item);
        TextView dispatchItem = findViewById(R.id.dispatch_item);
        TextView waveFullItem = findViewById(R.id.wave_full_item);
        TextView hKItem = findViewById(R.id.hk_item);
        TextView loadingItem = findViewById(R.id.loading_item);
        TextView btn_menu_item = findViewById(R.id.btn_menu_item);
        TextView btnDyLodingItem = findViewById(R.id.btn_dy_loding_item);

        youkuMenu.setOnClickListener(this);
        viewpagerItem.setOnClickListener(this);
        popupWindowsItem.setOnClickListener(this);
        toggleButtonItem.setOnClickListener(this);
        autoAttributeItem.setOnClickListener(this);
        myViewPagerItem.setOnClickListener(this);
        quickIndexItem.setOnClickListener(this);
        slideMenuItem.setOnClickListener(this);
        waveItem.setOnClickListener(this);
        propertyAnimationItem.setOnClickListener(this);
        scrollByTestItem.setOnClickListener(this);
        touchEventItem.setOnClickListener(this);
        dispatchItem.setOnClickListener(this);
        waveFullItem.setOnClickListener(this);
        hKItem.setOnClickListener(this);
        loadingItem.setOnClickListener(this);
        btn_menu_item.setOnClickListener(this);
        btnDyLodingItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.youku_menu:
                intent = new Intent(this, YoukuMenu.class);
                break;
            case R.id.viewpager_item:
                intent = new Intent(this, AdvanceItem.class);
                break;
            case R.id.popup_windows_item:
                intent= new Intent(this, PopupWindowsItem.class);
                break;
            case R.id.toggle_button_item:
                intent = new Intent(this, MyToggleButtonItem.class);
                break;
            case R.id.auto_attribute_item:
                intent = new Intent(this, AutoAttributeItem.class);
                break;
            case R.id.my_view_pager_item:
                intent = new Intent(this, MyViewPagerItem.class);
                break;
            case R.id.quick_index_item:
                intent= new Intent(this, QuickIndexItem.class);
                break;
            case R.id.slide_menu_item:
                intent = new Intent(this, SlideMenuItem.class);
                break;
            case R.id.wave_item:
                intent= new Intent(this, WaveItem.class);
                break;
            case R.id.property_animation_item:
                intent = new Intent(this, PropertyAnimation.class);
                break;
            case R.id.scrollby_test_item:
                intent= new Intent(this, TestScrollByAndScrollTo.class);
                break;
            case R.id.touch_event_item:
                intent= new Intent(this, TouchEventActivity.class);
                break;
            case R.id.dispatch_item:
                intent = new Intent(this, DispatchActivity.class);
                break;
            case R.id.wave_full_item:
                intent = new Intent(this, WaveFullItem.class);
                break;
           case R.id.hk_item:
               intent= new Intent(this, HK.class);
                break;
           case R.id.loading_item:
               intent= new Intent(this, LoadingActvity.class);
                break;
           case R.id.btn_menu_item:
               intent= new Intent(this, MenuActivity.class);
                break;
           case R.id.btn_dy_loding_item:
               intent= new Intent(this, DYLoadingActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
