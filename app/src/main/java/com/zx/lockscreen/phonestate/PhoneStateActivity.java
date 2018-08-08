package com.zx.lockscreen.phonestate;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zx.lockscreen.R;
import com.zx.lockscreen.customview.CircleProgressView;

/**
 * Created by liuyuchuan on 2018/5/7.
 */

public class PhoneStateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_state);

        /* Update the listener, and start it */
        /*MyListener = new MyPhoneStateListener();

        Tel = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(MyListener , PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/

    }


    /* Start the PhoneState listener */
    private class MyPhoneStateListener extends PhoneStateListener {

        /* Get the Signal strength from the provider,
         * each tiome there is an update
         *从得到的信号强度,每个tiome供应商有更新
         */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            //信号强度换算公式
            int astSignal = -113 + 2 * signalStrength.getGsmSignalStrength();
            //gsm.setText("GSM 信号强度asu :" + signalStrength.getGsmSignalStrength() + "_dBm :" + astSignal);
        }

    };/* End of private Class */

    private void getWifiInfo() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getBSSID() != null) {
            //wifi名称
            String ssid = wifiInfo.getSSID();
            //wifi信号强度
            int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6);
            //wifi速度
            int speed = wifiInfo.getLinkSpeed();
            //wifi速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            //wifi.setText("wifi_level:"+signalLevel +"_speed:"+speed +"_units:"+units);
            //wifiDrawView.setType(signalLevel);
        }
    }

}
