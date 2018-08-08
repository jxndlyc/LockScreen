package com.zx.lockscreen.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.zx.lockscreen.R;

import java.util.Random;

/**
 * Created by liuyuchuan on 2018/5/5.
 */

public class ColorArcProgressBar extends View {

    private int mWidth;
    private int mHeight;
    //直径
    private int diameter = 600;

    //圆心
    private float centerX;
    private float centerY;

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint titlePaint;
    private Paint desPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors;
    private int[] colors2;
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(50);
    private float hintSize = dipToPx(16);
    private float titleSize = dipToPx(28);
    private float desSize = dipToPx(13);
    private int aniSpeed = 5000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);
    private int hintColor;
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private int bgArcColor;
    private boolean isShowCurrentSpeed = true;
    private String hintString = "Km/h";
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private String titleString;
    private boolean isNeedDesText;
    private String desText;
    // sweepAngle / maxValues 的值
    private float k;

    private int startColor;
    private int endColor;
    private Random mRandom;
    private String endValue = "90";
    private int endColorShowType = 0;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        //colors = new int[]{color1, color2, color3, color3};
        startColor = a.getColor(R.styleable.ColorArcProgressBar_start_color, 0xfffad961);
        endColor = a.getColor(R.styleable.ColorArcProgressBar_end_color, 0xfff76b1c);
        bgArcColor = a.getColor(R.styleable.ColorArcProgressBar_bg_arc_color, 0xffdddddd);
        hintColor = a.getColor(R.styleable.ColorArcProgressBar_bg_arc_color, 0xffdddddd);
        colors = new int[]{startColor, endColor, endColor, startColor};
        colors2 = new int[]{0xffb92e2e, 0xffb92e2e, 0xffb92e2e, 0xfffb450f,0xffb92e2e};

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        isNeedDesText = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_des_text, false);
        desText = a.getString(R.styleable.ColorArcProgressBar_des_text);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();
        mRandom = new Random();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

        diameter = 3 * getScreenWidth() / 5;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(bgArcColor);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(hintColor);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(hintColor);
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        titlePaint = new Paint();
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(hintColor);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        //显示描述文字
        desPaint = new Paint();
        desPaint.setTextSize(desSize);
        desPaint.setColor(hintColor);
        desPaint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        //抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(270, centerX, centerY);
        sweepGradient.setLocalMatrix(matrix);
        //SweepGradient sweepGradient = new SweepGradient(centerX, centerY, startColor, endColor);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if (curValues == maxValues) {

            if(endColorShowType == 0){
                vTextPaint.setColor(endColor);
                hintPaint.setColor(endColor);
                titlePaint.setColor(endColor);
            }else if(endColorShowType == 1){
                vTextPaint.setColor(0xfffb450f);
                hintPaint.setColor(0xfffb450f);
                titlePaint.setColor(0xfffb450f);
            }
        }

        if (isNeedContent) {
            if (curValues == maxValues) {
                canvas.drawText(endValue, centerX, centerY + textSize / 4, vTextPaint);
                if(endColorShowType == 1) {
                    SweepGradient sweepGradient2 = new SweepGradient(centerX, centerY, colors2, null);
                    Matrix matrix2 = new Matrix();
                    matrix2.setRotate(270, centerX, centerY);
                    sweepGradient.setLocalMatrix(matrix2);
                    allArcPaint.setShader(sweepGradient2);
                    canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);
                }
            } else {
                canvas.drawText(String.format("%.0f", curValues / (mRandom.nextInt(2) + 2)), centerX, centerY + textSize / 4, vTextPaint);
            }
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + textSize, hintPaint);
        }
        if (isNeedTitle && curValues == maxValues) {
            canvas.drawText(titleString, centerX, centerY - textSize, titlePaint);
        }

        if (isNeedDesText) {
            canvas.drawText(desText, centerX + 2 * textSize / 3, centerY + textSize / 3, hintPaint);
        }

        //invalidate();

    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                curValues = currentAngle / k;
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setIsShowCurrentSpeed(boolean isShowCurrentSpeed) {
        this.isShowCurrentSpeed = isShowCurrentSpeed;
    }

    public void setEndValue(String end_Value){
        this.endValue = end_Value;
    }

    public void setEndColorShowType(int end_color_show_type){
        this.endColorShowType = end_color_show_type;
    }

}
