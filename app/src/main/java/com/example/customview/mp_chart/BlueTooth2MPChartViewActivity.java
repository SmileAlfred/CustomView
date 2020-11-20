package com.example.customview.mp_chart;

import androidx.annotation.NonNull;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.R;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 实现蓝牙通信，传递数据，并绘制成动态数据
 */
public class BlueTooth2MPChartViewActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = BlueTooth2MPChartViewActivity.class.getSimpleName();
    /**
     * View 和 表格 相关
     */
    private DynamicLineChartManager dynamicLineChartManager1;
    private DynamicLineChartManager dynamicLineChartManager2;
    private LineChart mChart1, mChart2;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合
    private EditText edit_text_popup_item_order;
    private AutoCompleteTextView et_bluetooth_ip;
    private TextView tv_power;

    /**
     * 蓝牙相关
     */
    private Button btn_send_order, btn_connect_bluetooth;
    private BluetoothAdapter _bluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    static BluetoothDevice _device = null;     //蓝牙设备

    //Dell: 34-23-87-40-73-04
    private String HC05SAddress = "14:A3:2F:63:EB:FA";
    private BluetoothSocket _socket = null;      //蓝牙通信socket
    private static int connectSuccessful;//判断蓝牙接口
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    private DownloadTask dTask;
    private boolean bRun = true;
    private InputStream is;    //输入流，用来接收蓝牙数据
    private static byte[] buffer;
    private static int streamSuccessful = 1;//判断输入流
    private String date, time;
    private boolean bThread = false;
    private OutputStream os = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        initChart();

        initBlueTooth();

        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        dTask = new DownloadTask();
        dTask.execute(100);
    }

    private void initChart() {
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
    }

    String[] autoStrings = new String[]{"联合国", "联合国安理会", "联合国五个常任理事国",
            "Google", "Google Map"};

    private void findViews() {
        setContentView(R.layout.activity_mp_chart_view);
        TextView title = findViewById(R.id.title_text_view);
        title.setText("绘制动态的折线图");
        tv_power = findViewById(R.id.tv_power);
        et_bluetooth_ip = (AutoCompleteTextView) findViewById(R.id.et_bluetooth_ip);

        String[] autoStrings = new String[]{"联合国", "联合国安理会", "联合国五个常任理事国",
                "Google", "Google Map"};
        // 第二个参数表示适配器的下拉风格
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BlueTooth2MPChartViewActivity.this,
                android.R.layout.simple_dropdown_item_1line, autoStrings);
        et_bluetooth_ip.setAdapter(adapter);

        btn_connect_bluetooth = findViewById(R.id.btn_connect_bluetooth);
        btn_connect_bluetooth.setOnClickListener(this);

        mChart1 = (LineChart) findViewById(R.id.dynamic_chart1);
        mChart2 = (LineChart) findViewById(R.id.dynamic_chart2);
        edit_text_popup_item_order = findViewById(R.id.et_popup_item_order);
        btn_send_order = findViewById(R.id.btn_send_order);
        btn_send_order.setOnClickListener(this);
    }

    //按钮点击添加数据
    public void addEntry(View view) {
        dynamicLineChartManager1.addEntry((int) (Math.random() * 100));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect_bluetooth:
                String temp = formatAddress(et_bluetooth_ip.getText().toString());
                if(null == temp)break;
                HC05SAddress = temp;
                isStop = true;
                if (dTask != null) {
                    dTask.mCancle();
                }

                if (_socket != null) { //关闭连接socket
                    try {
                        _socket.close();
                    } catch (IOException e) {
                    }
                }

                if (_device != null) _device = null;
                is = null;
                isStop = false;
                dTask = new DownloadTask();
                dTask.execute(100);
                break;
            case R.id.btn_send_order:
                if (os == null) {
                    Toast.makeText(this, "蓝牙没连接", 1000).show();
                    return;
                }
                //TODO:发送命令
                String order = edit_text_popup_item_order.getText().toString();
                try {
                    if (null == _socket) return;
                    os.write(new String(order).getBytes());
                    os.flush();
                } catch (IOException e) {
                    Log.i(TAG, "onClick: 报错：" + e.getMessage());
                }
                Log.i(TAG, "onClick: _socket = " + _socket);
                break;
            default:
                break;
        }
    }

    public void initBlueTooth() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
            Toast.makeText(this, "设备不支持蓝牙，别玩了", Toast.LENGTH_SHORT).show();
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
    }

    public void connectHC05() {
        while (!_bluetoothAdapter.isEnabled()) {
        }

        if (_bluetoothAdapter.isDiscovering()) _bluetoothAdapter.cancelDiscovery();
        //打包log.i(TAG, "connectHC05: 搜索？" + (_bluetoothAdapter.isDiscovering()));
        if (_device == null) {
            _device = _bluetoothAdapter.getRemoteDevice(HC05SAddress);
        }
        try {
            if (_socket == null)
                _socket = _device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            //_socket = (BluetoothSocket) _device.getClass().getDeclaredMethod("createRfcommSocket", new Class[]{int.class}).invoke(_device, 1);
        } catch (Exception e) {
            connectSuccessful = 2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            //打包log.i(TAG, "connectHC05: connectSuccessful = " + connectSuccessful + e.getMessage());
        }

        try {
            _socket.connect();
            connectSuccessful = 1;
            Log.i(TAG, "1 连接成功");
        } catch (IOException connectException) {
            connectSuccessful = 3;
            try {
                Method m = _device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                _socket = (BluetoothSocket) m.invoke(_device, 1);
                _socket.connect();
                connectSuccessful = 1;
                Log.i(TAG, "2 连接成功");
            } catch (Exception e) {
                connectSuccessful = 4;
                try {
                    _socket.close();
                    _socket = null;
                } catch (IOException ie) {
                }
            }
            return;
        }

        //TODO：源代码
        /*//if (Thread.State.NEW == connectThread.getState()) connectThread.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _socket = (BluetoothSocket) _device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(_device, 1);
                    //打包log.i(TAG, "run: _socket = " + _socket);
                    _socket.connect();
                    connectSuccessful = 1;
                    //打包log.i(TAG, "connectHC05: connectSuccessful = " + connectSuccessful);
                } catch (Exception e) {
                    writePadLog("connectHC05() --> _device.getClass()... 报错：\t" + e.getMessage() );
                    try {
                        connectSuccessful = 3;
                        //打包log.i(TAG, "connectHC05: connectSuccessful = " + connectSuccessful + e.getMessage());
                        if (_socket != null) {
                            _socket.close();
                            _socket = null;
                        }
                    } catch (IOException ee) {
                        connectSuccessful = 4;
                        writePadLog("connectHC05() --> _socket.close() 报错：\t" + ee.getMessage());
                        //打包log.i(TAG, "connectHC05: connectSuccessful = " + connectSuccessful + ee.getMessage());
                    }
                    try {
                        Looper.prepare();
                        Toast.makeText(InsertInformation.this, getResources().getString(R.string.connectFailHint), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start();*/


        try {
            if (connectSuccessful != 1) Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    /*public void connectHC05() {
        if (_bluetoothAdapter.isDiscovering()) _bluetoothAdapter.cancelDiscovery();
        if (_device == null) _device = _bluetoothAdapter.getRemoteDevice(HC05SAddress);

        try {
            if (_socket == null)
                _socket = _device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (Exception e) {
            connectSuccessful = 2;
            Log.i(TAG, "connectHC05: _device.create 报错" + e.getMessage());
        }

        try {
            _socket.connect();
            connectSuccessful = 1;
            Looper.prepare();
            Toast.makeText(this, "蓝牙连接成功，可以通信", Toast.LENGTH_SHORT).show();
            Looper.loop();
            Log.i(TAG, "connectHC05: 第一次连接成功");
        } catch (IOException connectException) {
            connectSuccessful = 3;
            try {
                Method m = _device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                _socket = (BluetoothSocket) m.invoke(_device, 1);
                _socket.connect();
                connectSuccessful = 1;
                Log.i(TAG, "connectHC05: 第二次连接成功");
            } catch (Exception e) {
                Log.i(TAG, "connectHC05: 第二次连接报错："+e.getMessage());
                connectSuccessful = 4;
                try {
                    _socket.close();
                    _socket = null;
                } catch (IOException ie) {
                    Log.i(TAG, "connectHC05: 第二次关闭 socket 报错:" + ie.getMessage());
                }
            }
            return;
        }

        try {
            if (connectSuccessful != 1) Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.i(TAG, "connectHC05: 睡过了" + e.getMessage());
        }
    }*/

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
                            Message msg = new Message();
                            msg.what = POWER;
                            msg.obj = receivedMsg.replaceAll("POWER:", "");
                            handler.sendMessage(msg);
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case POWER:
                    tv_power.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

    private static final int POWER = 4;

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

    public String formatAddress(String newAddress) {
        if (TextUtils.isEmpty(newAddress)) return null;
        String result = "";
        for (int i = 0; i < newAddress.length(); i++) {
            result += newAddress.charAt(i);
            if (i % 2 != 0 && i < newAddress.length() - 1) {//偶！数！位！最后一个位置不补 :
                result += ":";
            }
        }
        Log.i(TAG, "格式化后的地址格式：" + result);
        return result;
    }
}