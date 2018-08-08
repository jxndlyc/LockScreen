package com.zx.lockscreen.ble;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by liuyuchuan on 2018/5/10.
 */

public class BleBroadcast2 extends BroadcastReceiver {

    private static final String ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED";
    private static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context , "静态注册广播"  , Toast.LENGTH_SHORT).show();

        String action = intent.getAction();
        Log.e("liuyuchuan", "BleBroadcast2-----action="+action);
        if(TextUtils.equals(action, ACTION_BOND_STATE_CHANGED)){

        }else if(TextUtils.equals(action, ACTION_CONNECTION_STATE_CHANGED)){

        }
    }
}
