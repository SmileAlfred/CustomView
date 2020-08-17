package com.example.customview.gesture_track;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;

public class GestureTrackActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleText;
    private Button title_edit_button;
    //默认直线连接
    public static boolean isLineTo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_track);

        mTitleText = findViewById(R.id.title_text_view);
        mTitleText.setText("手写板");

        title_edit_button = findViewById(R.id.title_edit_button);
        title_edit_button.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_edit_button:
                isLineTo = !isLineTo;
                Toast.makeText(GestureTrackActivity.this, isLineTo ? "直线连接" : "二阶贝塞尔曲线连接", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        isLineTo = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}