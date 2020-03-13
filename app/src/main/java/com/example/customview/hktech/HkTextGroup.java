package com.example.customview.hktech;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class HkTextGroup extends View {

	private char[] counts = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'J', 'K', 'L', 'M', 'N', 'O' };

	private Paint paint;

	private Context context;

	public HkTextGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private Cell[][] cells;
	/**
	 * 像素值
	 */
	private int textSize = 30;

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize);// PX值
		paint.setTextAlign(Align.LEFT);
		paint.setStyle(Style.FILL);

		cells = new Cell[list][row];
		for (int j = 0; j < list; j++) {
			for (int i = 0; i < row; i++) {
				cells[j][i] = new Cell(i, j);
				cells[j][i].alpha = 0;
				cells[j][i].msg = ""
						+ counts[(int) (Math.random() * counts.length)];
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Utils.logleo("getWidth()::" + getWidth());
		Utils.logleo("getHeight()::" + getHeight());

		textSize = getWidth() / 10;
		left_bottom = getHeight();
	}

	public float left;
	public float left_bottom;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			for (int j = 0; j < list; j++) {

				for (int i = row - 1; i >= 0; i--) {
					// 1、如果第一行透明度为0，则有一定机率变为255
					// 2、如果中间行透明度为0，不做处理
					// 3、中间行不为0，依次减少一个梯度
					// 4、我上面的一个是255，那么我也是255,而他亮度减1
					Cell cell = cells[j][i];

					if (i == 0) {
						if (cell.alpha == 0) {
							if (Math.random() * 10 > 9) {
								cell.alpha = 255;
							}
						} else {
							cell.alpha = cell.alpha - 25 > 0 ? cell.alpha - 25
									: 0;
						}
					} else if (i > 0 && i <= row - 1) {
						if (cells[j][i - 1].alpha == 255) {
							cell.alpha = 255;
						} else {
							cell.alpha = cell.alpha - 25 > 0 ? cell.alpha - 25
									: 0;
						}
					}
				}
			}
			invalidate();

		};
	};

	private int seed = 0;

	private int stepCount = 11;

	/** 行 */
	private int row = 100;

	/** 列 */
	private int list = 100;

	@Override
	protected void onDraw(Canvas canvas) {

		for (int j = 0; j < list; j++) {
			for (int i = 0; i < row; i++) {
				Cell cell = cells[j][i];
				// 小机率事件，改变内容
				if (Math.random() * 100 > 85) {
					cell.msg = ""
							+ counts[(int) (Math.random() * counts.length)];
				}
				// 根据透明度确定颜色
				if (cell.alpha == 255) {
					paint.setColor(Color.WHITE);
				} else {
					paint.setColor(Color.GREEN);
				}
				// 设置透明度
				paint.setAlpha(cell.alpha);

				// 绘制
				if (cell.alpha != 0) {
					canvas.drawText(cell.msg, cell.j * 15, (float) (cell.i
							* textSize * 0.6 + textSize), paint);
				}
			}
		}

		// seed = (seed + 1) % stepCount;
		handler.sendEmptyMessageDelayed(seed, 10);
	}

	private class Cell {
		public Cell(int i, int j) {
			super();
			this.i = i;
			this.j = j;
		}

		/** 行 */
		public int i;
		/** 列 */
		public int j;
		/** 种子 */
		public int seed;
		/** 透明度 */
		public int alpha;
		public String msg;

	}

}
