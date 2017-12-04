package utils.ashlikun.com.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.ashlikun.pathanim.PathAnimHelper;
import com.ashlikun.pathanim.utils.StringPathUtils;

import java.text.ParseException;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/19　16:31
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class PathView extends View implements PathAnimHelper.OnPathAnimCallback {
    Path path = new Path();
    Path animPath = new Path();
    PathAnimHelper pathHelper;
    Paint mPaint = new Paint();
    String pathStr = "M12,4l-1.41,1.41L16.17,11H4v2h12.17l-5.58,5.59L12,20l8,-8z";
    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            initView(context, attrs);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initView(Context context, AttributeSet attrs) throws ParseException {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        //path = new SvgPathParser().parsePath(pathStr);
        path = StringPathUtils.getPath("LIKUN");
        pathHelper = new PathAnimHelper(path, animPath);
        pathHelper.setCallback(this);
        pathHelper.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(animPath, mPaint);
    }

    @Override
    public boolean isDefaultHandler() {
        return true;
    }

    @Override
    public void callBack(PathMeasure pathMeasure, Path mAnimPath, float progress) {
        invalidate();
    }
}
