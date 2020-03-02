package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 这是 自定义控件 学习笔记
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button youkuMenu = findViewById(R.id.youku_menu);
        Button viewpagerItem = findViewById(R.id.viewpager_item);
        Button popupWindowsItem = findViewById(R.id.popup_windows_item);
        Button toggleButtonItem = findViewById(R.id.toggle_button_item);
        Button autoAttributeItem = findViewById(R.id.auto_attribute_item);
        Button myViewPagerItem = findViewById(R.id.my_view_pager_item);

        youkuMenu.setOnClickListener(this);
        viewpagerItem.setOnClickListener(this);
        popupWindowsItem.setOnClickListener(this);
        toggleButtonItem.setOnClickListener(this);
        autoAttributeItem.setOnClickListener(this);
        myViewPagerItem.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
