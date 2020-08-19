package com.example.customview.property.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description: 测试 属性动画和视图动画；
 * 视图动画 旋转平移后，其原位置处 还 可以被点击；
 * 属性动画 旋转平移后，其原位置处 不 可以被点击；
 * @date :2020/03/11 10:49
 */
public class PropertyAnimation extends AppCompatActivity {

    private ImageView iv_animation;
    private TextView mNoteText;
    private Button mNoteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

        TextView tvTitle = findViewById(R.id.title_text_view);
        tvTitle.setText("动画测试");

        iv_animation = findViewById(R.id.iv_animation);
        iv_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了图片", Toast.LENGTH_SHORT).show();
            }
        });

        mNoteText = findViewById(R.id.property_note);
        mNoteButton = findViewById(R.id.title_edit_button);
        mNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNoteText.getVisibility() == View.GONE){
                    mNoteText.setVisibility(View.VISIBLE);
                }else {
                    mNoteText.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 补间(视图)动画
     *
     * @param v
     */
    public void testTweenAnimation(View v) {
        TranslateAnimation animation = new TranslateAnimation(0, iv_animation.getWidth()/2, 0, iv_animation.getHeight()/2);
        animation.setDuration(3000);
        animation.setFillAfter(true);
        iv_animation.startAnimation(animation);
    }

    /**
     * testPropertyAnimation 测试属性动画
     *
     * @param v
     */
    public void testPropertyAnimation(View v) {

//        v.setTranslationY();

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv_animation, "translationX", 0, iv_animation.getWidth() / 2);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(iv_animation, "translationY", 0, iv_animation.getHeight() / 2);

        AnimatorSet set = new AnimatorSet();    //把两个 animator 装在一起  一边一起运行
        set.playTogether(animator3, animator4);
        set.setDuration(2000);
        set.start();

//      //另外一种写法
//        iv_animation.animate()
//                 .translationXBy(iv_animation.getWidth())
//                 .translationYBy(iv_animation.getWidth())
//                 .setDuration(2000)
//                 .setInterpolator(new BounceInterpolator())   // 弹簧效果
//                 .start();
    }

    public void reset(View v) {
        iv_animation.clearAnimation();
    }

}