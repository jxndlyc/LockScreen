package com.zx.lockscreen.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

import com.zx.lockscreen.R;

/**
 * Created by liuyuchuan on 2018/5/2.
 */

public class LoadingAnimationActivity extends Activity {

    private LoadingView loadingView;
    private boolean wasSpinning = false;
    private SeekBar seekBarProgress;
    private Button  btnIncrement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_loading_activity);
        loadingView = (LoadingView) findViewById(R.id.loading_view_custom);
        seekBarProgress = (SeekBar) findViewById(R.id.progressAmount);
        btnIncrement = (Button) findViewById(R.id.btn_increment);

        seekBarProgress.setOnSeekBarChangeListener(new ProgressUpdater(loadingView));
        btnIncrement.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                loadingView.startAnimation(0, 75, 5000);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    private static class ProgressUpdater implements SeekBar.OnSeekBarChangeListener {

        private final LoadingView wheel;

        ProgressUpdater(LoadingView wheel) {
            this.wheel = wheel;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            //double progress = 360.0 * (seekBar.getProgress() / 100.0);
            wheel.setProgress(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
