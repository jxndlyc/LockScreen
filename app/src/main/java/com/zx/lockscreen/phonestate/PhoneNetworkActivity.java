package com.zx.lockscreen.phonestate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.zx.lockscreen.R;

/**
 * Created by liuyuchuan on 2018/5/7.
 */

public class PhoneNetworkActivity extends AppCompatActivity {

    private static final int NETWORKTYPE_WIFI = 0;
    private static final int NETWORKTYPE_4G = 1;
    private static final int NETWORKTYPE_2G = 2;
    private static final int NETWORKTYPE_NONE = 3;
    public TextView mTextView;
    public TelephonyManager mTelephonyManager;
    public PhoneStatListener mListener;
    public int mGsmSignalStrength;
    private NetWorkBroadCastReciver mNetWorkBroadCastReciver;

    /**
     * 网络信号强度监听
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_network);

        mTextView = (TextView) findViewById(R.id.phone_state_strength);
        //获取telephonyManager
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //开始监听
        mListener = new PhoneStatListener();
        /**由于信号值变化不大时，监听反应不灵敏，所以通过广播的方式同时监听wifi和信号改变更灵敏*/
        mNetWorkBroadCastReciver = new NetWorkBroadCastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(mNetWorkBroadCastReciver, intentFilter);
        //getCurrentNetDBM(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //用户不在当前页面时，停止监听
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_NONE);
    }

    private class PhoneStatListener extends PhoneStateListener {
        //获取信号强度

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            //获取网络信号强度
            //获取0-4的5种信号级别，越大信号越好,但是api23开始才能用
            int level = signalStrength.getLevel();
            Log.e("liuyuchuan", "level:" + level + ", signalStrength.getGsmSignalStrength()==" + signalStrength.getGsmSignalStrength());
            mGsmSignalStrength = signalStrength.getGsmSignalStrength();
            //网络信号改变时，获取网络信息
            getNetWorkInfo();
        }
    }

    /**
     * 暂时不用这个方法
     */
    public int getNetWorkType(Context context) {
        int mNetWorkType = -1;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return isFastMobileNetwork() ? NETWORKTYPE_4G : NETWORKTYPE_2G;
            }
        } else {
            mNetWorkType = NETWORKTYPE_NONE;//没有网络
        }
        return mNetWorkType;
    }

    /**
     * 判断网络速度
     */
    private boolean isFastMobileNetwork() {
        if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            //这里只简单区分两种类型网络，认为4G网络为快速，但最终还需要参考信号值
            return true;
        }
        return false;
    }

    //接收网络状态改变的广播
    class NetWorkBroadCastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNetWorkInfo();
        }
    }

    /**
     * 获取网络的信息
     */
    private void getNetWorkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    //wifi
                    WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo connectionInfo = manager.getConnectionInfo();
                    int rssi = connectionInfo.getRssi();
                    mTextView.setText("当前为wifi网络，信号强度=" + rssi);
                    //-50~0好
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    //移动网络,可以通过TelephonyManager来获取具体细化的网络类型
                    String netWorkStatus = isFastMobileNetwork() ? "4G网络" : "2G网络";
                    mTextView.setText("当前为" + netWorkStatus + "，信号强度=" + mGsmSignalStrength);
                    break;
            }
        } else {
            mTextView.setText("没有可用网络");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetWorkBroadCastReciver);
    }

    /**
     * 得到当前的手机蜂窝网络信号强度
     * 获取LTE网络和3G/2G网络的信号强度的方式有一点不同，
     * LTE网络强度是通过解析字符串获取的，
     * 3G/2G网络信号强度是通过API接口函数完成的。
     * asu 与 dbm 之间的换算关系是 dbm=-113 + 2*asu
     */
    public void getCurrentNetDBM(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener mylistener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                String signalInfo = signalStrength.toString();
                String[] params = signalInfo.split(" ");


                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络 最佳范围   >-90dBm 越大越好
                    int Itedbm = Integer.parseInt(params[9]);
                    Log.e("liuyuchuan", "Itedbm:" + Itedbm + "");

                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    //3G网络最佳范围  >-90dBm  越大越好  ps:中国移动3G获取不到  返回的无效dbm值是正数（85dbm）
                    //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方
                    /*String yys = IntenetUtil.getYYS(getApplication());//获取当前运营商
                    if (yys=="中国移动") {
                        setDBM(0+"");//中国移动3G不可获取，故在此返回0
                    }else if (yys=="中国联通") {
                        int cdmaDbm = signalStrength.getCdmaDbm();
                        setDBM(cdmaDbm+"");
                    }else if (yys=="中国电信") {
                        int evdoDbm = signalStrength.getEvdoDbm();
                        setDBM(evdoDbm+"");
                    }*/

                    Log.e("liuyuchuan","signalStrength.getCdmaDbm():" + signalStrength.getCdmaDbm() + ", signalStrength.getEvdoDbm():" +signalStrength.getEvdoDbm());

                } else {
                    //2G网络最佳范围>-90dBm 越大越好
                    int asu = signalStrength.getGsmSignalStrength();
                    int dbm = -113 + 2 * asu;
                    Log.e("liuyuchuan","dbm:" + dbm + "");
                }

            }
        };
        //开始监听
        tm.listen(mylistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }
}
