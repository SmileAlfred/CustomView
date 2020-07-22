package com.example.customview.wave_anim;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @date :2020/07/22 10:03
 * @description:
 */
public class WaveAnimActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wave_anim);
        super.onCreate(savedInstanceState);

        TextView tv = findViewById(R.id.title_text_view);
        tv.setText("水波纹动画");
    }
}
