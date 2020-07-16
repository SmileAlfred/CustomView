package com.example.customview.loading;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @date :2020/07/15 09:08
 * @description: 值动画的 ofObject
 */
public class Loading extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIv_loading;
    private ImageView iv_loading_cust;
    private ImageView iv_parabola;
    private ImageView iv_parabola_object;
    private TextView tv_loading;
    private TextView tv_char_change;
    private Button btn_parabola;

    private RotateAnimation rotateAnimation;
    private ValueAnimator mValueAnimator;
    private ValueAnimator mColorValueAnimator;
    private ValueAnimator charAnimator;
    private ValueAnimator parabolaAinmator;
    private ObjectAnimator objectAnimator;

    private int times = 1;
    private int windowsWidth;
    private int mediumX;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        windowsWidth = getWindowManager().getDefaultDisplay().getWidth();
        mediumX = windowsWidth / 2;

        mIv_loading = findViewById(R.id.iv_loading);
        iv_loading_cust = findViewById(R.id.iv_loading_cust);
        iv_parabola = findViewById(R.id.iv_parabola);
        iv_parabola_object = findViewById(R.id.iv_parabola_object);
        tv_loading = findViewById(R.id.tv_loading);
        tv_char_change = findViewById(R.id.tv_char_change);
        btn_parabola = findViewById(R.id.btn_parabola);
        //抛物线运行
        btn_parabola.setOnClickListener(this);
        //旋转 imageView
        mIv_loading.setOnClickListener(this);

        loadingTv();

        updateChar();
    }

    /**
     * ValueAnimator 进阶一一ofObject，实现 文字从 A - Z
     */
    private void updateChar() {
        charAnimator = ValueAnimator.ofObject(new CharEvaluator(), new Character('A'), new Character('Z'));
        charAnimator.setDuration(5000);
        charAnimator.setRepeatCount(ValueAnimator.INFINITE);
        charAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        charAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                char temp = (Character) charAnimator.getAnimatedValue();
                tv_char_change.setText(String.valueOf(temp));
            }
        });
        charAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_parabola:
                parabolaAinmator = ValueAnimator.ofObject(new ParabolaEvaluator(), new Point(100, 800), new Point(900, 800));
                parabolaAinmator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Point point = (Point) parabolaAinmator.getAnimatedValue();
                        Log.i("TAG", "point.x: " + point.x + "point.y: " + point.y);
                        iv_parabola.layout(point.x, point.y,
                                point.x + iv_parabola.getWidth(),
                                point.y + iv_parabola.getHeight());
                    }
                });
                parabolaAinmator.setDuration(2000);
                parabolaAinmator.setRepeatCount(2);
                parabolaAinmator.setRepeatMode(ValueAnimator.REVERSE);
                parabolaAinmator.start();

                objectAnimator = ObjectAnimator.ofObject(iv_parabola_object, "failPos", new ParabolaEvaluator(), new Point(200, 1000), new Point(800, 1000));
                objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
                objectAnimator.setDuration(2000);
                objectAnimator.setRepeatCount(2);
                objectAnimator.start();
                break;
            case R.id.iv_loading:
                rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setRepeatCount(Animation.INFINITE);
                rotateAnimation.setDuration(500);
                rotateAnimation.setInterpolator(new LinearInterpolator());

                Float flo = 20.1f;
                int i = flo.intValue();
                mIv_loading.startAnimation(rotateAnimation);
                Log.i("My Package Name ", "is : " + getPackageName());
                break;
            default:
                break;
        }
    }

    private class CharEvaluator implements TypeEvaluator<Character> {
        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            int startI = (int) startValue;
            int endI = (int) endValue;

            int characterInt = (int) ((endI - startI) * fraction + startI);
            return (char) characterInt;
        }
    }

    /**
     * 加载中动画效果；加 加载 加载中 加载中...
     * 详情看 https://www.cnblogs.com/liusaisaiv1/p/13320627.html
     */
    private void loadingTv() {
        mValueAnimator = ValueAnimator.ofInt(0, 3);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                times++;
                switch (times % 4) {
                    case 2:
                        tv_loading.setText("加");
                        break;
                    case 3:
                        tv_loading.append("载");
                        break;
                    case 0:
                        tv_loading.append("中");
                        break;
                    case 1:
                        tv_loading.append("…");
                        break;
                    default:
                        break;

                }
            }
        });

        mValueAnimator.start();

        mColorValueAnimator = ValueAnimator.ofInt(0xffffff00, 0xff0000ff);
        mColorValueAnimator.setDuration(3000);
        mColorValueAnimator.setEvaluator(new ArgbEvaluator());
        mColorValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mColorValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer curValue = (Integer) mColorValueAnimator.getAnimatedValue();
                tv_loading.setTextColor(curValue);
            }
        });
        mColorValueAnimator.start();
    }

    @Override
    protected void onStop() {
        //重复次数为时F INITE（无限循环）的动画，当 Activity 结束的时候，必须调用 cancel() 函数取消动画，
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
        mValueAnimator.cancel();
        mColorValueAnimator.cancel();
        charAnimator.cancel();
        parabolaAinmator.cancel();
        objectAnimator.cancel();

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private class ParabolaEvaluator implements TypeEvaluator<Point> {
        Point mPoint = new Point();

        @Override
        public Point evaluate(float fraction, Point pointStart, Point pointEnd) {
            mPoint.x = (int) (pointStart.x + fraction * (pointEnd.x - pointStart.x));
            //mPoint.y = (int) (pointStart.x + fraction * (pointEnd.x - pointStart.x));
            if (2 * fraction <= 1) {
                mPoint.y = (int) ((pointEnd.x - pointStart.x) * fraction * 2);
            } else {
                mPoint.y = (int) ((pointEnd.x - pointStart.x) * (1 - fraction) * 2);
            }

            return mPoint;
        }
    }

}
