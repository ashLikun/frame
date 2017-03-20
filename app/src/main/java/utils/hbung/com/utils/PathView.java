package utils.hbung.com.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.hbung.pathanim.PathAnimHelper;
import com.hbung.pathanim.utils.PathParserUtils;

import java.text.ParseException;
import java.util.ArrayList;

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
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

//        path = new SvgPathParser().parsePath(context.getString(R.string.tieta));
//        path = PathParserUtils.getPathFromArrayFloatList(StringPathUtils.getPath("LIKUN A B C"));
        path.moveTo(0, 0);
        path.addCircle(100, 100, 60, Path.Direction.CW);
//        path = new SvgPathParser().parsePath("M950.144 248.384l-71.744-71.68c-13.184-13.248-34.624-13.248-47.872 0L400.576 606.656 196.096 400.704c-13.248-13.248-34.624-13.248-47.872 0L76.48 472.448c-13.184 13.248-13.184 34.56 0 47.744l299.968 301.952c13.184 13.184 34.56 13.184 47.808 0l525.888-525.888C963.328 283.072 963.328 261.632 950.144 248.384z");
        //path.addCircle(100, 100, 40, Path.Direction.CW);
        ArrayList<float[]> aaa = new ArrayList<>();
        aaa.add(new float[]{0,30,30,60});
        aaa.add(new float[]{30,60,60,0});
        path = PathParserUtils.getPathFromArrayFloatList(aaa);
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
//        float end = pathMeasure.getLength() / 100f * progress;
//        float start = end - pathMeasure.getLength() / 100 * (50 - Math.abs(progress - 50));
//        animPath.reset();
//        Log.e("aaaaaaa", "progress = " + progress + "   end = " + end + "   " + "start" + start);
//        if (start <= 0) {
//            pathMeasure.getSegment(pathMeasure.getLength() + start, pathMeasure.getLength(), mAnimPath, true);
//        }
//        pathMeasure.getSegment(start, end, mAnimPath, true);
        invalidate();
    }
}
