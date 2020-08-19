package com.example.customview.attribute;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description:    自定义属性主类
 * 在 xml 文件中声明，
 * 在 values 文件夹下 新建 attrs.xml
 * @date :2020/03/01 18:22
 */
public class AutoAttributeItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_attribute_item);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("自定义属性");
    }
}
