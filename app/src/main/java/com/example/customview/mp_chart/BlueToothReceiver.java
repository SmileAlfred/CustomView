package com.example.customview.mp_chart;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author: LiuSaiSai
 * @date: 2020/09/13 10:28
 * @description:
 */
public class BlueToothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        //连接
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
        }
    }
}
