package com.example.customview;

import android.animation.ObjectAnimator;
import android.provider.FontsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

/**
 * @author LiuSaiSai
 * @description: 优酷菜单中，控制其是否显示 圆环 的工具类
 * 隐藏和显示 都是通过旋转实现的
 * BUG:旋转存在一定的 bug：动画已经移动隐藏了；但是点击原来 menu 位置，依然会有都动画产生
 * 解决方法：1. 设置隐藏后不可以点击 → view.setEnabled(false);并且 将传入的控件 从View 改成ViewGroup；
 * 还不够；还要遍历其孩子，将每一个孩子都设置成 false
 * 2.利用属性动画特性解决 将原来的 视图动画 替换成 属性动画ObjectAnimator
 * @date :2020/02/27 8:44
 */
public class Tools {

    public static void hideView(ViewGroup view) {
        hideView(view, 0);
    }

    public static void showView(ViewGroup view) {
        showView(view, 0);
    }

    public static void hideView(ViewGroup view, long startOffSet) {

        /*
        //旋转工具类;此时传入四个参数：起始角度，结束角度，旋转中心的 X Y坐标
        //这个 X Y坐标有瑕疵；参考系是怎样定的？控件本身的宽没有充满屏幕——效果没有错，自己的理解有瑕疵
        RotateAnimation hideRotate = new RotateAnimation(0, 180, view.getWidth() / 2, view.getHeight());
        hideRotate.setDuration(500);    //设置动画的毫秒数    视图动画
        hideRotate.setFillAfter(true);  //设置动画播放完成后停留在最后的 状态；
        hideRotate.setStartOffset(startOffSet); //设置动画延迟多久后执行
        view.startAnimation(hideRotate);

        // View 和 ViewGroup 的区别，ViewGroup可以操作其孩子(控件中的子控件)；而 View 不行；导致隐藏后，孩子还可以被点击；
        // 故将其改为ViewGroup;并且遍历其孩子，将每一个孩子都设置成 false
        for (int count = 0; count < view.getChildCount(); count++) {
            View childrenView = view.getChildAt(count);
            childrenView.setEnabled(false);
        }
        //一、设置隐藏后不可以点击；这样不好使；没有操作其子控件
        //view.setEnabled(false);
        */

        //属性动画解决 BUG  ObjectAnimator
        //view.setRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 180);
        animator.setDuration(500);               //设置动画的毫秒数
        animator.setStartDelay(startOffSet);     //设置动画播放完成后停留在最后的 状态
        animator.start();

        view.setPivotX(view.getWidth() / 2);     //设置旋转中心；错误方法 .setRotationX()
        view.setPivotY(view.getHeight());
    }

    public static void showView(ViewGroup view, long startOffSet) {
        /*
        RotateAnimation hideRotate = new RotateAnimation(180, 360, view.getWidth() / 2, view.getHeight());
        hideRotate.setDuration(500);    //设置动画的毫秒数
        hideRotate.setFillAfter(true);  //设置动画播放完成后停留在最后的 状态；
        hideRotate.setStartOffset(startOffSet); //设置动画延迟多久后执行
        view.startAnimation(hideRotate);

        for (int count = 0; count < view.getChildCount(); count++) {
            View childrenView = view.getChildAt(count);
            childrenView.setEnabled(true);
        }
        //一、设置 显示 后还可以点击
        //view.setEnabled(true);
        */

        //属性动画解决 BUG  ObjectAnimator
        //view.setRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 180, 360);
        animator.setDuration(500);                   //设置动画的毫秒数
        animator.setStartDelay(startOffSet);         //设置动画播放完成后停留在最后的 状态
        animator.start();

        view.setPivotX(view.getWidth() / 2);         //设置旋转中心；错误方法 .setRotationX()
        view.setPivotY(view.getHeight());
    }
}
