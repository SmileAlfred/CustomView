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

        youkuMenu.setOnClickListener(this);
        viewpagerItem.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
