package com.zx.lockscreen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zx.lockscreen.MyService;
import com.zx.lockscreen.R;
import com.zx.lockscreen.bean.BleKeyInfo;
import com.zx.lockscreen.ble.BleActivity;
import com.zx.lockscreen.customview.CircleProgressActivity;
import com.zx.lockscreen.customview.ColorArcProgressBarActivity;
import com.zx.lockscreen.customview.LoadingAnimationActivity;
import com.zx.lockscreen.customview.WheelAnimationActivity;
import com.zx.lockscreen.phonestate.PhoneNetworkActivity;
import com.zx.lockscreen.simplecache.ACache;

/**
 * 仿qq，微信，支付宝锁屏消息
 * 使用步骤，点击这个按钮以后，按返回键退出APP，关闭手机屏幕，5s以后会受到锁屏消息，可以点击进入消息详情页面
 */
public class MainActivity extends AppCompatActivity {

    private ACache mACache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_service_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent); //启动后台服务
            }
        });

        findViewById(R.id.start_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WheelAnimationActivity.class);
                startActivity(intent); //启动动画
            }
        });

        findViewById(R.id.start_loading_animation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoadingAnimationActivity.class);
                startActivity(intent); //启动动画
            }
        });
        findViewById(R.id.start_loading_animation_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CircleProgressActivity.class);
                startActivity(intent); //启动动画
            }
        });

        findViewById(R.id.start_color_arc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ColorArcProgressBarActivity.class);
                startActivity(intent); //启动动画
            }
        });

        findViewById(R.id.start_phone_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhoneNetworkActivity.class);
                startActivity(intent); //启动动画
            }
        });

        findViewById(R.id.start_ble_listen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BleActivity.class);
                startActivity(intent); //启动动画
            }
        });

        mACache = ACache.get(this);
        findViewById(R.id.put_object_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleKeyInfo bleKeyInfo = new BleKeyInfo();
                bleKeyInfo.setId(100);
                bleKeyInfo.setBle_token("aldkfaflkadladlkfjlaadfad23rdffdf");
                bleKeyInfo.setSecret("1242343243aldkfaflkadladlkfjlaadfad23rdffdf453453");
                mACache.put("13683366102", bleKeyInfo, 20);
            }
        });
        findViewById(R.id.get_object_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleKeyInfo bleKeyInfo = (BleKeyInfo) mACache.getAsObject("13683366102");
                Log.e("liuyuchuan", "bleKeyInfo:" + bleKeyInfo);
                if(bleKeyInfo != null){
                    Log.e("liuyuchuan", "id:" + bleKeyInfo.getId() + ", token:" + bleKeyInfo.getBle_token() + ", secret:" + bleKeyInfo.getSecret());
                }

            }
        });
    }





}
