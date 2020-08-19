package com.example.customview.hktech;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 *
 *  BUG 存在于 HkTextGroup.class 测量方法中，getWidth() 返回值得到的是 0 .
 *  解决方法：将getWidth()  换成 getMeasuredWidth()
 *
 */

public class HK extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hk);


        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("黑客");
    }
}
