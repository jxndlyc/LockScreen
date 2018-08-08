package com.zx.lockscreen.ble;

import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zx.lockscreen.R;


/**
 * Created by liuyuchuan on 2018/5/10.
 */

public class BleActivity extends AppCompatActivity/* implements View.OnClickListener*/ {
    private Button mButton1, mButton2, mButton3, mButton4, mButton5, mButton6;
    private BleBroadcast mBLEBroadcast;
    private BluetoothStateBroadcastReceiver mReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
    private static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        //mBLEBroadcast = new BleBroadcast();
        mReceiver = new BluetoothStateBroadcastReceiver();
        /*mButton1 = (Button) findViewById(R.id.testButton1);
        mButton2 = (Button) findViewById(R.id.testButton2);
        mButton3 = (Button) findViewById(R.id.testButton3);
        mButton4 = (Button) findViewById(R.id.testButton4);
        mButton5 = (Button) findViewById(R.id.testButton5);
        mButton6 = (Button) findViewById(R.id.testButton6);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);*/
        registerReceiver(mReceiver, makeFilters());
        //获取蓝牙适配器实例。如果设备不支持蓝牙则返回null
        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //获取状态码
        int test = mBluetoothAdapter.getState();
        //判断蓝牙状态
        switch (test) {
            case BluetoothAdapter.STATE_CONNECTED:
                Toast.makeText(this, "判断状态为连接中", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                Toast.makeText(this, "判断状态为连接", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                Toast.makeText(this, "判断状态为断开", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                Toast.makeText(this, "判断状态为断中", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_OFF:
                Toast.makeText(this, "关闭", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothAdapter.STATE_ON:
                Toast.makeText(this, "打开", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }*/
    }

    //蓝牙监听需要添加的Action
    private IntentFilter makeFilters() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        return intentFilter;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testButton1:
                //动态注册
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("weizhengzhou.top");
                registerReceiver(mBLEBroadcast, intentFilter);
                break;
            case R.id.testButton2:
                //发送动态注册的广播信息
                Intent intent = new Intent("weizhengzhou.top");
                sendBroadcast(intent);
                break;
            case R.id.testButton3:
                //发送静态注册的广播信息
                Intent intent2 = new Intent(ACTION_BOND_STATE_CHANGED);
                sendBroadcast(intent2);
                break;
            case R.id.testButton4:
                //解除动态绑定的广播接收者
                unregisterReceiver(mBLEBroadcast);
                break;
            case R.id.testButton5:
                //实现不响应静态注册的广播
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.zx.lockscreen.ble", BleBroadcast2.class.getName()),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                break;
            case R.id.testButton6:
                //重新激活静态广播接收者
                getPackageManager().setComponentEnabledSetting(
                        new ComponentName("com.zx.lockscreen.ble", BleBroadcast2.class.getName()),
                        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                        PackageManager.DONT_KILL_APP);
                break;
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
