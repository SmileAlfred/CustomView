package com.example.customview.float_windows;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author: LiuSaiSai
 * @date: 2020/08/17 07:28
 * @description: 悬浮窗，来自《启舰_自定义控件》S13，P487
 * BUG:重启该 activity时，取消悬浮窗报错;因为，重进来以后，虽然悬浮窗还在，但是并灭有添加，直接移除就报错
 */
public class FloatWinActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = FloatWinActivity.class.getClass().getSimpleName();
    private Button btn_add_float_win, btn_remove_float_win;
    private ImageView mImageView;
    private  static WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private static final int REQUESTCODE = 6;
    /**
     * 解决 activity 不可见之后，count 被置为0，无限添加 logo 问题；
     */
    private static int iconCount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_windows);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("悬浮窗");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //参考：https://blog.csdn.net/zxm317122667/article/details/52685492
            if (!Settings.canDrawOverlays(this)) {
                Intent mIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                mIntent.setData(Uri.parse("package:" + getPackageName()));
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(mIntent, REQUESTCODE);
            } else {
                initView();
            }
        } else {
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            initView();
        }
    }

    private void initView() {
        btn_add_float_win = findViewById(R.id.btn_add_float_win);
        btn_remove_float_win = findViewById(R.id.btn_remove_float_win);

        btn_add_float_win.setOnClickListener(this);
        btn_remove_float_win.setOnClickListener(this);

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_float_win:
                if (iconCount > 0) {
                    //只填加一个
                    break;
                }
                mImageView = new ImageView(this);
                mImageView.setBackgroundResource(R.drawable.icon52);

                //WindowManager.LayoutParams.WRAP_CONTENT,
                //WindowManager.LayoutParams.WRAP_CONTENT,
                mLayoutParams = new WindowManager.LayoutParams(
                        150, 150, 2003,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                        PixelFormat.TRANSPARENT);

                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mLayoutParams.x = 200;
                mLayoutParams.y = 800;

                mWindowManager.addView(mImageView, mLayoutParams);
                mImageView.setOnTouchListener(this);
                iconCount++;
                break;
            case R.id.btn_remove_float_win:
                if (iconCount < 1) {
                    break;
                }
                //待办事项：重启该 activity时，取消悬浮窗报错;因为，重进来以后，虽然悬浮窗还在，但是并灭有添加，直接移除就报错；
                mWindowManager.removeViewImmediate(mImageView);
                iconCount--;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mLayoutParams.x = rawX - mImageView.getWidth() / 2;
                mLayoutParams.y = rawY - mImageView.getHeight();

                mWindowManager.updateViewLayout(mImageView, mLayoutParams);
                break;
            default:
                break;
        }
        return false;
    }
}
