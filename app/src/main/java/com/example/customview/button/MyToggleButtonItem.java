package com.example.customview.button;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description: 自定义控件 状态开关按钮<见原版ToggleButton>
 * @date :2020/03/01 9:29
 */
public class MyToggleButtonItem extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_button_item);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("开关");
    }
}
