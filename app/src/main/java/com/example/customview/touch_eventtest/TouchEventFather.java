package com.example.customview.touch_eventtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TouchEventFather extends LinearLayout {

	public TouchEventFather(Context context) {
		super(context);
	}

	public TouchEventFather(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e("eventTest", "Father | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.dispatchTouchEvent(ev);
//		return true;
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.i("eventTest", "Father | onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onInterceptTouchEvent(ev);
		//return false;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("eventTest", "Father | onTouchEvent --> " + TouchEventUtil.getTouchAction(ev.getAction()));
		return super.onTouchEvent(ev);
//		return true;
	}

}
