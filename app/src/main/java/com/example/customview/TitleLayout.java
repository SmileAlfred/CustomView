package com.example.customview;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义控件——标题栏
 * 1. 继承自 LinearLayout 而非 AppcompatActivity;
 * 2. 创建的构造器是 两个 参数的这个
 * 3. 结束界面的方法：((Activity)getContext()).finish();
 * 4. 土司方法：makeText(getContext(), "",...
 * 5. × × × × × × 编辑标题栏，还没有实现
 * 6. × × × × × × 返回和编辑的 图标没有换，尺寸有瑕疵
 * 7. 引用标题栏，就像引用 <RecyclerView 一样
 */
public class TitleLayout extends LinearLayout implements View.OnClickListener {

    private TextView mTitleText;

    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);

        Button titleBackButton = findViewById(R.id.title_back_button);
        Button titleEditButton = findViewById(R.id.title_edit_button);
        mTitleText = findViewById(R.id.title_text_view);

        mTitleText.setText(getContext().getClass().getSimpleName());
        titleBackButton.setOnClickListener(this);
        titleEditButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_button:
                ((Activity) getContext()).finish();
                break;
            case R.id.title_edit_button:
                Toast.makeText(getContext(), "编辑标题还没有实现", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
