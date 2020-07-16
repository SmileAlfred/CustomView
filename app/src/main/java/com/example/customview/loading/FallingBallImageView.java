package com.example.customview.loading;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * @author LiuSaiSai
 * @date :2020/07/16 11:36
 * @description:
 */
public class FallingBallImageView extends androidx.appcompat.widget.AppCompatImageView {
    public FallingBallImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFailPos(Point pos){
        layout(pos.x,pos.y,pos.x + getWidth() , pos.y + getHeight());
    }
}
