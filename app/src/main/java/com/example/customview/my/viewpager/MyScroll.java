package com.example.customview.my.viewpager;

import android.os.SystemClock;

/**
 * @author LiuSaiSai
 * @description: 用于 滑动 回弹的效果；解决瞬间复位的强硬
 * @date :2020/03/02 11:31
 */
public class MyScroll {

    /**
     * 起始坐标
     */
    private float startX;
    private float startY;

    /**
     * 移动距离
     */
    private float distanceX;
    private int distanceY;

    private long startTime;

    /**
     * 回弹的总共时间 500 毫秒
     */
    private long totalTime = 500;

    /**
     * 是否移动完成；true 表示移动完成
     */
    private boolean isFinish;

    public float getCurrentX() {
        return currentX;
    }

    /**
     * 现在的 坐标
     */
    private float currentX;

    public void startScroll(float startX, float startY, float distanceX, int distanceY, float abs) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;

        this.startTime = SystemClock.uptimeMillis();    //系统开机时间
        this.isFinish = false;
    }

    /**
     * 计算滚动的 偏移量
     * 速度
     * 求移动一小段的距离
     * 求移动一小段对应的坐标
     * 求移动一小段对应的时间
     * return true:正在移动
     * return false:移动结束
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }

        long endTime = SystemClock.uptimeMillis();    //滑动结束时间时间
        long passTime = endTime - startTime;          //这一小段 花的时间

        if (passTime < totalTime) {
            //正在移动;     计算平均 速度 和 一小段距离
//            float averageVelocity = distanceX / totalTime;
            float averageDistance = passTime * distanceX / totalTime;

            currentX = averageDistance + startX;
        } else {
            //移动结束
            isFinish = true;
            currentX = startX + distanceX;
        }
        return true;
    }
}