package com.zx.lockscreen.customview;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zx.lockscreen.R;

/**
 * Created by liuyuchuan on 2018/5/3.
 */

public class CircleProgressActivity extends AppCompatActivity {

    private int mPercent= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        final CircleProgressView circle = (CircleProgressView)findViewById(R.id.circle);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueAnimator valueAnimator = new ValueAnimator().ofFloat(0f,0.6f);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = (float)valueAnimator.getAnimatedValue();
                        circle.setmPercent(value);
                    }
                });
                valueAnimator.setDuration(1600);
                valueAnimator.start();

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("onConfigurationChanged","orientation:"+newConfig.orientation);
    }
}
