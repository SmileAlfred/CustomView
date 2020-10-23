package com.example.customview.mp_chart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.customview.R;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 实现蓝牙通信，传递数据，并绘制成动态数据
 */
public class BlueTooth2MPChartViewActivity extends AppCompatActivity implements OnClickListener {

    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合
    private EditText edit_text_popup_item_order;
    private TextView tv_power;
    private Button btn_send_order;
    private static final String TAG = BlueTooth2MPChartViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_chart_view);
        TextView title = findViewById(R.id.title_text_view);
        title.setText("绘制动态的折线图");
        tv_power = findViewById(R.id.tv_power);

        LineChart mChart1 = (LineChart) findViewById(R.id.dynamic_chart1);
        LineChart mChart2 = (LineChart) findViewById(R.id.dynamic_chart2);
        edit_text_popup_item_order = findViewById(R.id.et_popup_item_order);
        btn_send_order = findViewById(R.id.btn_send_order);
        btn_send_order.setOnClickListener(this);

        //折线名字
        names.add("温度");
        names.add("压强");
        names.add("其他");
        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);

        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names.get(0), colour.get(0));
        dynamicLineChartManager2 = new DynamicLineChartManager(mChart2, names, colour);

        dynamicLineChartManager1.setYAxis(100, 0, 10);
        dynamicLineChartManager2.setYAxis(100, 0, 10);

        //死循环添加数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list.add((int) (Math.random() * 50) + 10);
                            list.add((int) (Math.random() * 80) + 10);
                            list.add((int) (Math.random() * 100));
                            dynamicLineChartManager2.addEntry(list);
                            list.clear();
                        }
                    });
                }
            }
        }).start();

        initBlueTooth();
    }

    //按钮点击添加数据
    public void addEntry(View view) {
        dynamicLineChartManager1.addEntry((int) (Math.random() * 100));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_order:
                //TODO:发送命令
                String order = edit_text_popup_item_order.getText().toString();
                try {
                    if (null == _socket) return;
                    os.write(new String(order).getBytes());
                    os.flush();
                } catch (IOException e) {
                    Log.i(TAG, "onClick: 报错：" + e.getMessage());
                }
                Log.i(TAG, "onClick: _socket = "  + _socket);
                break;
            default:
                break;
        }
    }

    private BluetoothAdapter _bluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    static BluetoothDevice _device = null;     //蓝牙设备

    private String HC05SAddress = "14:A3:2F:63:EB:FA";
    BluetoothSocket _socket = null;      //蓝牙通信socket
    static int connectSuccessful;//判断蓝牙接口
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    private DownloadTask dTask;
    private boolean bRun = true;
    private InputStream is;    //输入流，用来接收蓝牙数据
    private static byte[] buffer;
    private static int streamSuccessful = 1;//判断输入流
    private String date, time;
    private boolean bThread = false;
    private OutputStream os = null;


    public void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
            finish();
            return;
        }
        _bluetoothAdapter = mBluetoothManager.getAdapter();

        //如果不能得到蓝牙，可以给APP定位权限并在代码中动态获取；
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 666);
            }
        }
        // 设置设备可以被搜索
        new Thread() {
            @Override
            public void run() {
                if (_bluetoothAdapter.isEnabled() == false) {
                    _bluetoothAdapter.enable();
                }
            }
        }.start();

        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        dTask = new DownloadTask();
        dTask.execute(100);
    }

    public void connectHC05() {
        if (_bluetoothAdapter.isDiscovering()) _bluetoothAdapter.cancelDiscovery();
        if (_device == null) {
            _device = _bluetoothAdapter.getRemoteDevice(HC05SAddress);
        }
        try {
            if (_socket == null)
                _socket = _device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (Exception e) {
            connectSuccessful = 2;
            Log.i(TAG, "connectHC05: _device.create 报错" + e.getMessage());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //_socket = (BluetoothSocket) _device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(_device, 1);
                    _socket.connect();
                    connectSuccessful = 1;
                    Log.i(TAG, "run: connect 结束");
                } catch (Exception e) {
                    try {
                        connectSuccessful = 3;
                        if (_socket != null) {
                            _socket.close();
                            _socket = null;
                        }
                    } catch (IOException ee) {
                        connectSuccessful = 4;
                        Log.i(TAG, "run: _socket.close()报错" + e.getMessage());
                    }
                }
            }
        }).start();
        try {
            if (connectSuccessful != 1) Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.i(TAG, "connectHC05: 睡过了" + e.getMessage());
        }
    }

    private boolean isStop = false;

    class DownloadTask extends AsyncTask<Integer, Integer, String> {

        public void mCancle() {
            this.cancel(true);
        }

        @Override
        protected String doInBackground(Integer... params) {
            while (connectSuccessful != 1) {
                connectHC05();
                if (isStop) break;
            }
            return "执行完毕";
        }

        @Override
        protected void onPostExecute(String result) {
            if (isStop) return;
            if (connectSuccessful == 1) {
                Log.i(TAG, "onPostExecute: connectSuccessful = " + connectSuccessful);
                doWork();
            } else {
                _socket = null;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectHC05();
                    }
                }).start();
            }
            super.onPostExecute(result);
        }

        private void doWork() {
            Log.i(TAG, "进入 doWork: ");
            if (_socket != null) {
                try {
                    os = _socket.getOutputStream();
                } catch (IOException e) {
                    Log.i(TAG, "doWork: _socket.getOutputStream() 报错：" + e.getMessage());
                }

                buffer = new byte[16];

                try {
                    is = _socket.getInputStream();
                } catch (IOException e) {
                    streamSuccessful = 0;
                    return;
                }

                if (!bThread) {
                    ReadThread.start();
                    bThread = true;
                } else {
                    bRun = true;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        //打包log.i(TAG, "onDestroy: ");
        if (_socket != null) { //关闭连接socket
            try {
                _socket.close();
//	    		is.close()
            } catch (IOException e) {
            }
        }
        if (dTask != null) {
            dTask.mCancle();
        }
        dTask = null;
        is = null;
        if (_device != null) _device = null;

        //	_bluetooth.disable();  //关闭蓝牙服务
    }

    //接收数据线程
    Thread ReadThread = new Thread() {
        @Override
        public void run() {
            Log.i(TAG, "run: 进入 ReadThread 线程");
            bRun = true;
            while (true) {
                try {
                    while (is != null && is.available() == 0) {
                        while (!bRun) {
                        }
                    }
                    while (is != null) {
                        int time = is.available();
                        if (time == 0) break;
                        buffer = new byte[time];
                        if (is != null) is.read(buffer);
                        String receivedMsg = new String(buffer);
                        if (receivedMsg.contains("TEMP")) {
                            receivedMsg = receivedMsg.replaceAll("TEMP:", "");
                            float f = Float.parseFloat(receivedMsg);
                            dynamicLineChartManager1.addEntry(f);
                        }
                        if (receivedMsg.contains("POWER")) {
                            tv_power.setText(receivedMsg.replaceAll("POWER:", ""));
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    };

    private BlueToothReceiver mReceiver = new BlueToothReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (connectSuccessful != 1) {
                return;
            }
            connectSuccessful = -1;
            if (dTask != null) {
                dTask.cancel(true);
            }
            dTask = null;
            dTask = new DownloadTask();
            dTask.execute(100);
        }
    };
}