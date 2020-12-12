package com.example.customview.catchcrazycat;

import java.util.HashMap;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CrazyCat_Playground extends SurfaceView implements OnTouchListener{
		
	
	private static  int WIDTH = 40;
	private static final int ROW = 10;
	private static final int COL = 10;
	private static final int BLOCKS = 15;//默认添加的路障数量
	
	
	private CrazyCat_Dot matrix[][];
	private CrazyCat_Dot cat;

	public CrazyCat_Playground(Context context) {
		super(context);
		getHolder().addCallback(callback);
		matrix = new CrazyCat_Dot[ROW][COL];
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				matrix[i][j] = new CrazyCat_Dot(j, i);
			}
		}
		setOnTouchListener(this);
		initGame();
	}
	
	private CrazyCat_Dot getDot(int x, int y) {
		return matrix[y][x];
	}

	private boolean isAtEdge(CrazyCat_Dot d) {
		if (d.getX()*d.getY() == 0 || d.getX()+1 == COL || d.getY()+1 == ROW) {
			return true;
		}
		return false;
	}
	
	private CrazyCat_Dot getNeighbour(CrazyCat_Dot one, int dir) {
		switch (dir) {
		case 1:
			return getDot(one.getX()-1, one.getY());
		case 2:
			if (one.getY()%2 == 0) {
				return getDot(one.getX()-1, one.getY()-1);
			}else {
				return getDot(one.getX(), one.getY()-1);
			}
		case 3:
			if (one.getY()%2 == 0) {
				return getDot(one.getX(), one.getY()-1);
			}else {
				return getDot(one.getX()+1, one.getY()-1);
			}
		case 4:
			return getDot(one.getX()+1, one.getY());
		case 5:
			if (one.getY()%2 == 0) {
				return getDot(one.getX(), one.getY()+1);
			}else {
				return getDot(one.getX()+1, one.getY()+1);
			}
		case 6:
			if (one.getY()%2 == 0) {
				return getDot(one.getX()-1, one.getY()+1);
			}else {
				return getDot(one.getX(), one.getY()+1);
			}

		default:
			break;
		}
		return null;
	}
	
	private int getDistance(CrazyCat_Dot one, int dir) {
		int distance = 0;
		if (isAtEdge(one)) {
			return 1;
		}
		CrazyCat_Dot ori = one,next;
		while(true){
			next = getNeighbour(ori, dir);
			if (next.getStatus() == CrazyCat_Dot.STATUS_ON) {
				return distance*-1;
			}
			if (isAtEdge(next)) {
				distance++;
				return distance;
			}
			distance++;
			ori = next;
		}
	}
	
	private void MoveTo(CrazyCat_Dot one) {
		one.setStatus(CrazyCat_Dot.STATUS_IN);
		getDot(cat.getX(), cat.getY()).setStatus(CrazyCat_Dot.STATUS_OFF);;
		cat.setXY(one.getX(), one.getY());
	}
	
	private void move() {
		if (isAtEdge(cat)) {
			lose();return;
		}
		Vector<CrazyCat_Dot> avaliable = new Vector<>();
		Vector<CrazyCat_Dot> positive = new Vector<>();
		HashMap<CrazyCat_Dot, Integer> al = new HashMap<CrazyCat_Dot, Integer>();
		for (int i = 1; i < 7; i++) {
			CrazyCat_Dot n = getNeighbour(cat, i);
			if (n.getStatus() == CrazyCat_Dot.STATUS_OFF) {
				avaliable.add(n);
				al.put(n, i);
				if (getDistance(n, i) > 0) {
					positive.add(n);
					
				}
			}
		}
		if (avaliable.size() == 0) {
			win();
		}else if (avaliable.size() == 1) {
			MoveTo(avaliable.get(0));
		}else{
			CrazyCat_Dot best = null;
			if (positive.size() != 0 ) {//存在可以直接到达屏幕边缘的走向
				System.out.println("向前进");
				int min = 999;
				for (int i = 0; i < positive.size(); i++) {
					int a = getDistance(positive.get(i), al.get(positive.get(i)));
					if (a < min) {
						min = a;
						best = positive.get(i);
					}
				}
				MoveTo(best);
			}else {//所有方向都存在路障
				System.out.println("躲路障");
				int max = 0;
				for (int i = 0; i < avaliable.size(); i++) {
					int k = getDistance(avaliable.get(i), al.get(avaliable.get(i)));
					if (k <= max) {
						max = k;
						best = avaliable.get(i);
					}
				}
				MoveTo(best);
			}
		}
	}
	
	private void lose() {
		Toast.makeText(getContext(), "Lose", Toast.LENGTH_SHORT).show();
	}
	
	private void win() {
		Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT).show();
		
	}
	
	private void redraw() {
		Canvas c = getHolder().lockCanvas();
		c.drawColor(Color.LTGRAY);
		Paint paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		for (int i = 0; i < ROW; i++) {
			int offset = 0;
			if (i%2 != 0) {
				offset = WIDTH/2;
			}
			for (int j = 0; j < COL; j++) {
				CrazyCat_Dot one = getDot(j, i);
				switch (one.getStatus()) {
				case CrazyCat_Dot.STATUS_OFF:
					paint.setColor(0xFFEEEEEE);
					break;
				case CrazyCat_Dot.STATUS_ON:
					paint.setColor(0xFFFFAA00);
					break;
				case CrazyCat_Dot.STATUS_IN:
					paint.setColor(0xFFFF0000);
					break;

				default:
					break;
				}
				c.drawOval(new RectF(one.getX()*WIDTH+offset, 200 +one.getY()*WIDTH,
						(one.getX()+1)*WIDTH+offset, 200+(one.getY()+1)*WIDTH), paint);
			}
			
		}
		getHolder().unlockCanvasAndPost(c);
	}
	
	Callback callback = new Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			redraw();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			WIDTH = arg2/(COL+1);
			redraw();
		}
	};
	
	private void initGame() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				matrix[i][j].setStatus(CrazyCat_Dot.STATUS_OFF);
			}
		}
		cat = new CrazyCat_Dot(4, 5);
		getDot(4, 5).setStatus(CrazyCat_Dot.STATUS_IN);
		for (int i = 0; i < BLOCKS;) {
			int x = (int) ((Math.random()*1000)%COL);
			int y = (int) ((Math.random()*1000)%ROW);
			if (getDot(x, y).getStatus() == CrazyCat_Dot.STATUS_OFF) {
				getDot(x, y).setStatus(CrazyCat_Dot.STATUS_ON);
				i++;
				//System.out.println("Block:"+i);
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_UP) {
			int x,y;
			y = (int) (e.getY()/WIDTH);
			if (y%2 == 0) {
				x = (int) (e.getX()/WIDTH);
			}else {
				x = (int) ((e.getX()-WIDTH/2)/WIDTH);
			}
			if (x+1 > COL || y+1 > ROW) {
				initGame();
			}else if(getDot(x, y).getStatus() == CrazyCat_Dot.STATUS_OFF){
				getDot(x, y).setStatus(CrazyCat_Dot.STATUS_ON);
				move();
			}
			redraw();
		}
		return true;
	}
}
