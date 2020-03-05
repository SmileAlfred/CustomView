package com.example.customview;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author LiuSaiSai
 * @description:优酷菜单实现. 1. 关于其布局，要先添加 外面的 大的半圆，这样小圆也可以点击，
 * @date :2020/02/26 20:50
 */
public class YoukuMenu extends AppCompatActivity implements View.OnClickListener {
    private ImageView iconHome;
    private ImageView iconMenu;
    private RelativeLayout level1Small;
    private RelativeLayout level2Medium;
    private RelativeLayout level3Large;

    /**
     * 设置是否显示第三个圆环；
     * true  为显示：
     * false 为隐藏
     */
    private boolean isShowLevel3 = true;

    /**
     * 设置是否显示第二个圆环；
     * true  为显示：
     * false 为隐藏
     */
    private boolean isShowLevel2 = true;

    /**
     * 设置是否显示第一个圆环；
     * true  为显示：
     * false 为隐藏
     */
    private boolean isShowLevel1 = true;
    private Button youkuMenuButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youku_menu);

        iconHome = findViewById(R.id.home_youku_menu);
        iconMenu = findViewById(R.id.menu_youku_menu);
        level1Small = findViewById(R.id.level1_small);
        level2Medium = findViewById(R.id.level2_medium);
        level3Large = findViewById(R.id.level3_large);
        youkuMenuButton = findViewById(R.id.title_edit_button);
        youkuMenuButton.setText("菜单");

        // 设置单击事件
        iconHome.setOnClickListener(this);
        iconMenu.setOnClickListener(this);
        youkuMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_youku_menu:
                //如果大菜单和中菜单都是显示的，让其隐藏
                //如果大菜单和中菜单都是隐藏的，二级菜单显示
                if (isShowLevel3) {
                    isShowLevel2 = false;
                    isShowLevel3 = false;
                    YouKuMenuTools.hideView(level2Medium, 200);
                    YouKuMenuTools.hideView(level3Large);
                } else if (isShowLevel2) {
                    isShowLevel2 = false;
                    YouKuMenuTools.hideView(level2Medium);
                } else {
                    isShowLevel2 = true;
                    YouKuMenuTools.showView(level2Medium);
                }

                break;
            case R.id.menu_youku_menu:
                //如果显示，让其隐藏；如果隐藏让其显示
                if (isShowLevel3) {
                    isShowLevel3 = false;
                    YouKuMenuTools.hideView(level3Large);
                } else {
                    isShowLevel3 = true;
                    YouKuMenuTools.showView(level3Large);
                }
                break;
            case R.id.title_edit_button:
                //设置右上角的点击事件；如果菜单存在；则让其隐藏；
                if (isShowLevel1 || isShowLevel2 || isShowLevel3) {
                    if (isShowLevel3) {
                        isShowLevel3 = false;
                        YouKuMenuTools.hideView(level3Large);
                    }
                    if (isShowLevel2) {
                        isShowLevel2 = false;
                        YouKuMenuTools.hideView(level2Medium, 200);
                    }
                    if (isShowLevel1) {
                        isShowLevel1 = false;
                        YouKuMenuTools.hideView(level1Small, 400);
                    }
                }
                //如果隐藏，就让其显示
                else {
                    isShowLevel1 = true;
                    isShowLevel2 = true;

                    YouKuMenuTools.showView(level1Small);
                    YouKuMenuTools.showView(level2Medium, 200);
                }
            default:
                break;
        }
    }

    /**
     * 监听物理键；但是不好使，menu菜单启动多任务；查网上说需要设置广播 × × × × 没有尝试继续做
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //如果 一、二、三 级菜单都显示就都让其隐藏；
            if (isShowLevel3) {
                isShowLevel1 = false;
                isShowLevel2 = false;
                isShowLevel3 = false;

                YouKuMenuTools.hideView(level1Small, 400);
                YouKuMenuTools.hideView(level2Medium, 200);
                YouKuMenuTools.hideView(level3Large);
            }
            //如果 一、二、三 级菜单都隐藏就显示 一、二 级菜单
            if (!(isShowLevel1 && isShowLevel2 && isShowLevel3)) {
                YouKuMenuTools.showView(level1Small);
                YouKuMenuTools.showView(level2Medium, 200);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
