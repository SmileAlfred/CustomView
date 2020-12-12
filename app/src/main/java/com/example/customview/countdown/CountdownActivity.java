package com.example.customview.countdown;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.customview.R;

/**
 * Description：输入数字，如100，【开始倒计时】每秒递减，【停止计时】即停止
 * 1. 定时器 TimerTask 的使用；
 * 2. Handler 的使用；
 */
public class CountdownActivity extends Activity implements OnClickListener{
	
	private EditText inputet;
	private Button getTime,startTime,stopTime;
	private TextView time;
	private int i = 0;
	private Timer timer = null;
	private TimerTask task = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        initView();
    }
    
    private void initView(){
    	inputet = (EditText) findViewById(R.id.inputtime);
        getTime = (Button) findViewById(R.id.gettime);
        TextView tvTitle = findViewById(R.id.title_text_view);
		tvTitle.setText("倒计时");
        startTime = (Button) findViewById(R.id.starttime);
        stopTime = (Button) findViewById(R.id.stoptime);
        time = (TextView) findViewById(R.id.time);
        getTime.setOnClickListener(this);
        startTime.setOnClickListener(this);
        stopTime.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gettime:
			time.setText(inputet.getText().toString());
			i = Integer.parseInt(TextUtils.isEmpty(inputet.getText().toString())?"10":inputet.getText().toString());
			break;

		case R.id.starttime:
			startTime();
			break;
		case R.id.stoptime:
			stopTime();
			break;
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			time.setText(msg.arg1+"");
			startTime();
		};
	};
	
	public void startTime(){
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				i--;
				Message  message = mHandler.obtainMessage();
				message.arg1 = i;
				mHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 1000);
	}
	public void stopTime(){
		timer.cancel();
	}
}
