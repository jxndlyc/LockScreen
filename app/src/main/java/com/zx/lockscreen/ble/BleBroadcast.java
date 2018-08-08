package com.zx.lockscreen.ble;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by liuyuchuan on 2018/5/10.
 */

public class BleBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context , "动态注册广播" , Toast.LENGTH_SHORT).show();
    }
}
