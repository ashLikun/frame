package utils.hbung.com.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.hbung.pathanim.PathAnimHelper;
import com.hbung.pathanim.PathAnimHelper.OnPathAnimCallback;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/19　14:57
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class MulitImageTagView extends View implements OnPathAnimCallback {
    private int selectColor = 0xff0385d8;
    private int defaultColor = 0xffA3A3A3;
    private int hookSelectColor = Color.BLACK;
    private int hookDefaultColor = Color.WHITE;
    Paint mPaint;
    boolean isSelect = true;
    Path path;
    Path animPath;
    Path pathBack;
    PathAnimHelper pathAnimHelper;

    public MulitImageTagView(Context context) {
        this(context, null);
    }

    public MulitImageTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MulitImageTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dp_3));
        path = new Path();
        pathBack = new Path();
        animPath = new Path();
        pathAnimHelper = new PathAnimHelper(path, animPath);
        pathAnimHelper.setCallback(this);
        pathAnimHelper.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int rectH = (int) (width / 7f);
        int rectW = (int) (rectH * Math.tan(Math.toRadians(63)));
        int rectCurrX = width / 4 + width / 2;
        int rectCurrY = (int) (height / 3.5);
        path.reset();
        path.moveTo(-rectW / 2, -rectH / 2);
        path.lineTo(-rectW / 2, rectH / 2);
        path.lineTo(rectW / 2, rectH / 2);
        Matrix matrix = new Matrix();
        matrix.setRotate(360 - 45, 0, 0);
        matrix.postTranslate(rectCurrX, rectCurrY);
        path.transform(matrix);
        pathBack.reset();
        pathBack.moveTo(0, 0);
        pathBack.lineTo(width, 0);
        pathBack.lineTo(width, height);
        pathBack.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelect) {
            mPaint.setColor(selectColor);
        } else {
            mPaint.setColor(defaultColor);
        }
        mPaint.setAlpha((int) (2.55 * animProgress));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(pathBack, mPaint);
        if (isSelect) {
            mPaint.setColor(hookSelectColor);
        } else {
            mPaint.setColor(hookDefaultColor);
        }

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(animPath, mPaint);
    }

    @Override
    public boolean isDefaultHandler() {
        return true;
    }

    float animProgress;

    @Override
    public void callBack(PathMeasure pathMeasure, Path mAnimPath, float progress) {
        animProgress = progress;
        invalidate();
    }
}
