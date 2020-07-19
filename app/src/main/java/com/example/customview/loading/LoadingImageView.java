package com.example.customview.loading;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.sip.SipSession;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @date :2020/07/15 18:01
 * @description:加载动画效果，图片上下移动一次，更改一次图片。
 * 详细介绍看 https://www.cnblogs.com/liusaisaiv1/p/13320627.html
 */
public class LoadingImageView extends androidx.appcompat.widget.AppCompatImageView {

    private int mTop;
    private ValueAnimator animator;

    private int curImgIndex = 0;
    private int mImageCount = 7;

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        animator = ValueAnimator.ofInt(0, 100, 0);
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        //TODO:对于自定义控件，如何在其所在 activity 销毁的时候，取消动画并释放内存呢？
        animator.setRepeatCount(3);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer dy = (Integer) animation.getAnimatedValue();
                setTop(mTop - dy);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setImageDrawable(getResources().getDrawable(R.drawable.channel2));
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //每重复一次就更换一次图片 放在 这个方法中写。
                curImgIndex++;
                int i = curImgIndex % mImageCount + 1 ;
                //通过文件名拿到对应的资源ID；当循环到第7张，7 % 7 = 0；没有匹配的图片。故统一向前加 1
                int id = getResources().getIdentifier("channel" + i, "drawable", "com.example.customview");
                //Log.i("TAG", "image id is : " + id);
                Drawable drawable = getResources().getDrawable(id);
                setImageDrawable(drawable);
            }
        });
        animator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTop = top;
    }

}
