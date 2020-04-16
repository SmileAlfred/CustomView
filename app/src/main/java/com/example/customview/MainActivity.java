package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.customview.advance.AdvanceItem;
import com.example.customview.attribute.AutoAttributeItem;
import com.example.customview.button.MyToggleButtonItem;
import com.example.customview.dispatch.DispatchActivity;
import com.example.customview.hktech.HK;
import com.example.customview.my.viewpager.MyViewPagerItem;
import com.example.customview.popup.windows.PopupWindowsItem;
import com.example.customview.property.animation.PropertyAnimation;
import com.example.customview.quick.index.QuickIndexItem;
import com.example.customview.scrollbyandscrollto.TestScrollByAndScrollTo;
import com.example.customview.slide.SlideMenuItem;
import com.example.customview.touch_eventtest.TouchEventActivity;
import com.example.customview.wave.WaveItem;
import com.example.customview.wave_full.WaveFullItem;
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

        Button youkuMenu = findViewById(R.id.youku_menu);
        Button viewpagerItem = findViewById(R.id.viewpager_item);
        Button popupWindowsItem = findViewById(R.id.popup_windows_item);
        Button toggleButtonItem = findViewById(R.id.toggle_button_item);
        Button autoAttributeItem = findViewById(R.id.auto_attribute_item);
        Button myViewPagerItem = findViewById(R.id.my_view_pager_item);
        Button quickIndexItem = findViewById(R.id.quick_index_item);
        Button slideMenuItem = findViewById(R.id.slide_menu_item);
        Button waveItem = findViewById(R.id.wave_item);
        Button propertyAnimationItem = findViewById(R.id.property_animation_item);
        Button scrollByTestItem = findViewById(R.id.scrollby_test_item);
        Button TouchEventItem = findViewById(R.id.touch_event_item);
        Button DispatchItem = findViewById(R.id.dispatch_item);
        Button WaveFullItem = findViewById(R.id.wave_full_item);
        Button HKItem = findViewById(R.id.hk_item);

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
        TouchEventItem.setOnClickListener(this);
        DispatchItem.setOnClickListener(this);
        WaveFullItem.setOnClickListener(this);
        HKItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.youku_menu:
                Intent YoukuMenuIntent = new Intent(this, YoukuMenu.class);
                startActivity(YoukuMenuIntent);
                break;
            case R.id.viewpager_item:
                Intent AdvanceItemIntent = new Intent(this, AdvanceItem.class);
                startActivity(AdvanceItemIntent);
                break;
            case R.id.popup_windows_item:
                Intent PopupWindowsIntent = new Intent(this, PopupWindowsItem.class);
                startActivity(PopupWindowsIntent);
                break;
            case R.id.toggle_button_item:
                Intent MyToggleButtonIntent = new Intent(this, MyToggleButtonItem.class);
                startActivity(MyToggleButtonIntent);
                break;
            case R.id.auto_attribute_item:
                Intent PropertyItemIntent = new Intent(this, AutoAttributeItem.class);
                startActivity(PropertyItemIntent);
                break;
            case R.id.my_view_pager_item:
                Intent MyViewPagertemIntent = new Intent(this, MyViewPagerItem.class);
                startActivity(MyViewPagertemIntent);
                break;
            case R.id.quick_index_item:
                Intent quickIndexIntent = new Intent(this, QuickIndexItem.class);
                startActivity(quickIndexIntent);
                break;
            case R.id.slide_menu_item:
                Intent slideMenuIntent = new Intent(this, SlideMenuItem.class);
                startActivity(slideMenuIntent);
                break;
            case R.id.wave_item:
                Intent waveIntent = new Intent(this, WaveItem.class);
                startActivity(waveIntent);
                break;
            case R.id.property_animation_item:
                Intent propertyAnimationIntent = new Intent(this, PropertyAnimation.class);
                startActivity(propertyAnimationIntent);
            case R.id.scrollby_test_item:
                Intent scrollByTestIntent = new Intent(this, TestScrollByAndScrollTo.class);
                startActivity(scrollByTestIntent);
                break;
            case R.id.touch_event_item:
                Intent touchEventIntent = new Intent(this, TouchEventActivity.class);
                startActivity(touchEventIntent);
                break;
            case R.id.dispatch_item:
                Intent dispatchIntent = new Intent(this, DispatchActivity.class);
                startActivity(dispatchIntent);
                break;
            case R.id.wave_full_item:
                Intent waveFullIntent = new Intent(this, WaveFullItem.class);
                startActivity(waveFullIntent);
                break;
           case R.id.hk_item:
                Intent hkIntent = new Intent(this, HK.class);
                startActivity(hkIntent);
                break;
            default:
                break;
        }
    }
}
