package com.example.customview.attribute;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;

/**
 * @author LiuSaiSai
 * @description: 自定义属性类
 * @date :2020/03/01 18:22
 */
public class MyAttributeView extends View {
    private static final String TAG = "MyAttributeView";
    private int mAge;
    private String mName;
    private Drawable mPicture;
    private Bitmap mBitmap;
    private Paint mPaint;

    public MyAttributeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获取属性三种方式
         *        1.用命名空间取获取        //attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "name");
         *        2.遍历属性集合           //attrs.getAttributeValue(i))
         * √ √ √  3.使用系统工具，获取属性  //typedArray
         */
        String name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "name");
        String age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "age");
        String picture = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "picture");
        Log.i(TAG, "MyAttributeView:获取属性方式一： " + name + "\t" + age + "岁\t" + picture);

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.i(TAG, "MyAttributeView:获取属性方式二： " + attrs.getAttributeName(i) + "：" + attrs.getAttributeValue(i));
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAttributeView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.MyAttributeView_age:
                    mAge = typedArray.getInt(index, 0);
                    break;
                case R.styleable.MyAttributeView_name:
                    mName = typedArray.getString(index);
                    break;
                case R.styleable.MyAttributeView_picture:
                    mPicture = typedArray.getDrawable(index);
                    BitmapDrawable drawable = (BitmapDrawable) mPicture;
                    mBitmap = drawable.getBitmap();
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();   //记得回收
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        canvas.drawText(mName + mAge, 500, 500, mPaint);
        canvas.drawBitmap(mBitmap, 200, 500, mPaint);
    }
}