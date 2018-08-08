package com.zx.lockscreen.activity;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zx.lockscreen.R;

/**
 * 锁屏消息内容的activity
 */
public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "liuyuchuan";
    private Context mContext;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate:启动了消息内容的activity ");
        //四个标志位顾名思义，分别是锁屏状态下显示，解锁，保持屏幕长亮，打开屏幕。这样当Activity启动的时候，它会解锁并亮屏显示。
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                | WindowManager.LayoutParams.FLAG_FULLSCREEN //全屏
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
        //使用手机的背景
        Drawable wallPaper = WallpaperManager.getInstance(this).getDrawable();
        win.setBackgroundDrawable(wallPaper);
        setContentView(R.layout.activity_message);

        wakeScreen();

        mContext = this;
        initView();
        setVibrator();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        findViewById(R.id.iv_cat_camera_answer_telephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //先解锁系统自带锁屏服务，放在锁屏界面里面
                KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
                keyguardManager.newKeyguardLock("").disableKeyguard(); //解锁
                //点击进入消息对应的页面
                mContext.startActivity(new Intent(mContext, DetailsActivity.class));
                finish();
            }
        });

        findViewById(R.id.iv_cat_camera_refuse_telephone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setVibrator() {
        mVibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 2000, 50};
        mVibrator.vibrate(patter, 0);
    }

    private void cancalVibrator(){
        if (mVibrator != null)mVibrator.cancel();
    }

    private void wakeScreen(){
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();  //点亮屏幕
            //wl.release();  //任务结束后释放
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancalVibrator();
    }
}
