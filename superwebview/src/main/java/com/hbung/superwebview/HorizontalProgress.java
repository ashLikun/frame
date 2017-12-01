package com.ashlikun.superwebview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/13　16:17
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class HorizontalProgress extends View {
    private int mDefaultColor;
    private int mProgressColor;
    private Paint mPaint;
    private int mProgress;
    private int mProgressMax;

    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mDefaultColor = context.getResources().getColor(R.color.webview_progress_default_color);
        mProgressColor = context.getResources().getColor(R.color.webview_progress_color);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgress);
        mDefaultColor = ta.getColor(R.styleable.HorizontalProgress_hp_default_color, mDefaultColor);
        mProgressColor = ta.getColor(R.styleable.HorizontalProgress_hp_progress_color, mProgressColor);
        mProgress = ta.getInt(R.styleable.HorizontalProgress_hp_progress, 0);
        mProgressMax = ta.getInt(R.styleable.HorizontalProgress_hp_progress_max, 100);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float bili = mProgress / 1f / mProgressMax;
        int progressX = (int) (bili * getWidth());
        //绘制默认颜色
        mPaint.setColor(mDefaultColor);
        canvas.drawRect(progressX, 0, getWidth(), getHeight(), mPaint);
        //绘制进度
        mPaint.setColor(mProgressColor);
        canvas.drawRect(0, 0, progressX, getHeight(), mPaint);
    }

    public void setProgressMax(int mProgressMax) {
        this.mProgressMax = mProgressMax;
        invalidate();
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }
}
