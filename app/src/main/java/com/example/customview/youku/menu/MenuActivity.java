package com.example.customview.youku.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * 撒花般 菜单效果
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_menu;
    private ImageView iv_item1;
    private ImageView iv_item2;
    private ImageView iv_item3;
    private ImageView iv_item4;
    private ImageView iv_item5;

    private boolean isClose = true;

    private double angle;
    private final int radius = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();

        angle = (double) Math.toRadians(90 / 4);
    }

    private void findViews() {
        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("撒花菜单");

        iv_menu = findViewById(R.id.iv_menu);
        iv_item1 = findViewById(R.id.iv_item1);
        iv_item2 = findViewById(R.id.iv_item2);
        iv_item3 = findViewById(R.id.iv_item3);
        iv_item4 = findViewById(R.id.iv_item4);
        iv_item5 = findViewById(R.id.iv_item5);

        iv_menu.setOnClickListener(this);
        iv_item1.setOnClickListener(this);
        iv_item2.setOnClickListener(this);
        iv_item3.setOnClickListener(this);
        iv_item4.setOnClickListener(this);
        iv_item5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                Toast.makeText(this, "点击了 menu", Toast.LENGTH_SHORT).show();
                ctrlMenu();
                break;
            case R.id.iv_item1:
            case R.id.iv_item2:
            case R.id.iv_item3:
            case R.id.iv_item4:
            case R.id.iv_item5:
                Toast.makeText(this, "点击了 item", Toast.LENGTH_SHORT).show();
                if (!isClose) {
                    Toast.makeText(MenuActivity.this, "You Clicked " + v, Toast.LENGTH_SHORT).show();
                    closeMenu();
                    isClose = !isClose;
                }
                break;
            default:
                break;
        }
    }

    private void ctrlMenu() {
        if (isClose) {
            openMenu();
        } else {
            closeMenu();
        }
        isClose = !isClose;
    }

    private void closeMenu() {
        doCloseMenu(iv_item1, 0, 5, 300);
        doCloseMenu(iv_item2, 1, 5, 300);
        doCloseMenu(iv_item3, 2, 5, 300);
        doCloseMenu(iv_item4, 3, 5, 300);
        doCloseMenu(iv_item5, 4, 5, 300);
    }

    private void doCloseMenu(final ImageView imageView, int index, int total, long radius) {

        double degree = angle * index;
        int translaX = (int) (-radius * Math.cos(degree));
        int translaY = (int) (-radius * Math.sin(degree));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(imageView, "translationX", translaX, 0),
                ObjectAnimator.ofFloat(imageView, "translationY", translaY, 0),
                ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
        );
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (imageView.getVisibility() == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void openMenu() {
        doOpenMenu(iv_item1, 0, 5, 300);
        doOpenMenu(iv_item2, 1, 5, 300);
        doOpenMenu(iv_item3, 2, 5, 300);
        doOpenMenu(iv_item4, 3, 5, 300);
        doOpenMenu(iv_item5, 4, 5, 300);
    }

    private void doOpenMenu(ImageView imageView, int index, int total, long radius) {
        double degree = angle * index;
        if (imageView.getVisibility() == View.GONE) {
            imageView.setVisibility(View.VISIBLE);
        }
        int translaX = -(int) (radius * Math.cos(degree));
        int translaY = -(int) (radius * Math.sin(degree));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(imageView, "translationX", 0, translaX),
                ObjectAnimator.ofFloat(imageView, "translationY", 0, translaY),
                ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(imageView, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
        );
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}