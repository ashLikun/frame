package utils.hbung.com.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/7/7　13:21
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：加载对话框的箭头
 */

public class LoaddingArrorView extends View {
    Path mPath;
    Paint mPaint;


    public LoaddingArrorView(Context context) {
        this(context, null);
    }

    public LoaddingArrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoaddingArrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPath = new Path();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int radio = (int) (width * 0.13);

        mPath.moveTo(width / 2, height * 7 / 8f);
        float oneX = width * 7 / 8f;
        float oneY = height * 1 / 4f;
        mPath.quadTo(width * 7 / 8f, height * 3 / 4f, oneX, oneY);
        mPath.lineTo(oneX + 30, oneY);
        mPath.quadTo(width * 15 / 16f, height * 3 / 4f, width / 2, height * 7 / 8f);
        mPath.close();
        mPath.addCircle(10,10,radio, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);
    }
}
