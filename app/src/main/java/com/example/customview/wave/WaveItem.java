package com.example.customview.wave;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description:    自定义 水波纹
 * @date :2020/03/04 23:07
 */
public class WaveItem extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_item);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("水波纹");
    }
}
