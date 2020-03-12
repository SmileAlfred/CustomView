package com.example.customview.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class HkText extends View {

	private char[] counts = new char[]{'A','B','C','D','E','F','G','H','J','K','L','M','N','O'};

	private Paint paint;

	private Context ctx;

	public HkText(Context context, AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
		init();
	}

	/**
	 * 像素值
	 */
	private int textSize = 60;

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize);//PX值
		paint.setTextAlign(Align.LEFT);

		paint.setStyle(Style.FILL);
	}

//	private float sp2px(int sp){
//		TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_SP, sp, ctx.getResources().getDisplayMetrics());
//		
//		float px = sp *  ctx.getResources().getDisplayMetrics().scaledDensity;
//		return px;
//	}
//	
//	private float px2sp(int px){
//		float sp = px / ctx.getResources().getDisplayMetrics().scaledDensity;
//		return sp;
//	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Utils.logleo("getWidth()::"+getWidth());
		Utils.logleo("getHeight()::"+getHeight());

		textSize = getWidth()/10;
	}

	public float left = 150;
	public float left_bottom = 200;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			int flag = msg.what;// 0 -- 10 

//			paint.setColor(Color.BLUE);
			invalidate();
		};
	};

	private int seed = 0;

	private int stepCount = 11;


	@Override
	protected void onDraw(Canvas canvas) {

		left = 10;
		left_bottom = 400;
		for (int i = 0; i < 20; i++) {

			int seed_tem = seed;

			int alpha = 255 - (i + seed_tem) * 25;
			paint.setAlpha(alpha);// 0是没有

			canvas.drawText(counts, i % counts.length, 1, left, left_bottom,paint);

			left_bottom = (float) (left_bottom - textSize * 0.6);

		}
		seed = (seed + 1) % stepCount;
		handler.sendEmptyMessageDelayed(seed, 500);
	}


}
