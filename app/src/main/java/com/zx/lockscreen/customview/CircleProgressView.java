package com.zx.lockscreen.customview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 49479 on 2018/5/2.
 */

public class CircleProgressView extends View {

    private int radius;

    private Point center;

    private Paint mPaint;

    private Line[] lineArr;

    private float mPercent;

    public CircleProgressView(Context context) {
        super(context);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private Point findPoint(float zi, float mu, int radius, Point center) {
        int x = (int) (Math.sin(Math.toRadians(360f * zi / mu)) * (radius));
        int y = (int) (Math.cos(Math.toRadians(360f * zi / mu)) * (radius));
        Log.i("findPoint", zi + "--" + "x:" + x + "y:" + y);
        return new Point(center.x - x, center.y - y);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        radius = getWidth() / 4;
        center = new Point();
        center.x = getWidth() / 2;
        center.y = getHeight() / 2;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);

        lineArr = new Line[50];

        ArgbEvaluator evaluator = new ArgbEvaluator();


        for (int i = 0; i < lineArr.length; i++) {
            int color = 0;
            if (i <= lineArr.length / 2) {
                color = (int) evaluator.evaluate(1.0f * i / (lineArr.length / 2), Color.YELLOW, Color.RED);
            } else {
                color = (int) evaluator.evaluate(1.0f * i / (lineArr.length / 2), Color.RED, Color.BLUE);
            }
            lineArr[i] = new Line(color, findPoint(i, lineArr.length, radius + 20, center));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(7.0f);

        int colorBall = 0;

        for (int i = 0; i < lineArr.length; i++) {
            Line l = lineArr[i];
            if (i <= mPercent * lineArr.length) {
                mPaint.setColor(l.color);
                colorBall = l.color;
            } else {
                mPaint.setColor(Color.GRAY);
            }
            canvas.drawLine(l.p.x, l.p.y, center.x, center.y, mPaint);
        }
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(center.x, center.y, radius, mPaint);


        int innerRadius = radius - 20;
        Shader shader = new LinearGradient(center.x, center.y - innerRadius, center.x, center.y + innerRadius, new int[]{Color.YELLOW, Color.RED}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(shader);
        //mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(center.x - innerRadius, center.y - innerRadius, center.x + innerRadius, center.y + innerRadius);
        canvas.drawArc(rectF, 270, 0, false, mPaint);

        Shader shader2 = new LinearGradient(center.x, center.y - innerRadius, center.x, center.y + innerRadius, new int[]{Color.BLUE, Color.RED}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(shader2);
        RectF rectF2 = new RectF(center.x - innerRadius, center.y - innerRadius, center.x + 1 + innerRadius, center.y + innerRadius);
        canvas.drawArc(rectF, 270, 0, false, mPaint);

        mPaint.setShader(null);

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(center.x, center.y, innerRadius - 5, mPaint);

        mPaint.setColor(Color.WHITE);
        int inerinerRadius = innerRadius + 5;
        RectF rectF3 = new RectF(center.x - inerinerRadius, center.y - inerinerRadius, center.x + inerinerRadius, center.y + inerinerRadius);
        canvas.drawArc(rectF3, 270, 360 * mPercent - 360, false, mPaint);

        mPaint.setColor(colorBall);
        Point ballPoint = findPoint((mPercent * 100), 100, radius - 20 - 2, center);
        canvas.drawCircle(ballPoint.x, ballPoint.y, 8, mPaint);
    }

    public float getmPercent() {
        return mPercent;
    }

    public void setmPercent(float mPercent) {
        if (mPercent > 1.0f)
            return;
        this.mPercent = mPercent;
        invalidate();
    }
}
