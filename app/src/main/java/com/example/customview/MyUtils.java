package com.example.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

/**
 * @author LiuSaiSai
 * @date :2020/07/01 07:51
 * @description:
 */
public class MyUtils {


    /**
     * 重写 onTouchEvent 隐藏键盘
     *
     * @param activity 当前 活动
     * @param event    触摸手势；
     */
    public static void hideKeyBoard(Activity activity, MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null && manager != null) {
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null && manager != null) {
            manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 将状态栏设置为沉浸，并将颜色设置为透明；
     */
    @SuppressLint("ResourceType")
    public static void hideWindows(Activity activity) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.windowsColor)
                //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    /**
     * 设置底部 radio button 图片尺寸；
     */
    public static void setBottomPic(Activity activity, ArrayList<Drawable> drawables, ArrayList<RadioButton> radioButtons, int witdth, int height) {
        if (drawables.size() != radioButtons.size()) {
            Log.i(MyUtils.class.getSimpleName(), "setBottomPic: 传入的图片和radio button个数不等");
            return;
        }
        for (int i = 0; i < drawables.size(); i++) {
            //定义底部标签图片大小和位置F
            Drawable drawable = drawables.get(i);
            int witdthPx = dip2px(activity, witdth);
            int heightPx = dip2px(activity, height);
            //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
            drawable.setBounds(0, 0, witdthPx, heightPx);
            //设置图片在文字的哪个方向
            radioButtons.get(i).setCompoundDrawables(null, drawable, null, null);
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
