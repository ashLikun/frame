package com.hbung.pathanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import static android.content.ContentValues.TAG;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/19　15:15
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：一个操作Path的工具类
 */

public class PathAnimHelper {

    protected Path mSourcePath;//源Path
    protected Path mAnimPath;//用于绘制动画的Path
    protected long mAnimTime;//动画一共的时间
    protected boolean mIsInfinite;//是否无限循环


    protected ValueAnimator mAnimator;//动画对象
    protected OnPathAnimCallback callback;//动画监听者
    float valueOld = 0;

    public PathAnimHelper(Path sourcePath, Path animPath) {
        this(sourcePath, animPath, 1500, true);
    }

    public PathAnimHelper(Path sourcePath, Path animPath, long animTime, boolean isInfinite) {
        if (sourcePath == null || animPath == null) {
            Log.e(TAG, "PathAnimHelper初始化错误");
            return;
        }
        mSourcePath = sourcePath;
        mAnimPath = animPath;
        mAnimTime = animTime;
        mIsInfinite = isInfinite;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/1/19 15:29
     * <p>
     * 方法功能：停止动画
     */

    public void stop() {
        if (null != mAnimator && mAnimator.isRunning()) {
            mAnimator.end();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/1/19 15:24
     * <p>
     * 方法功能：一个SourcePath 内含多段Path，循环取出每段Path，并做一个动画
     * 自定义动画的总时间
     * 和是否循环
     */
    public void start() {
        if (mSourcePath == null || mAnimPath == null) {
            return;
        }
        PathMeasure pathMeasure = new PathMeasure();
        mAnimPath.reset();
        mAnimPath.lineTo(0, 0);
        valueOld = 0;
        pathMeasure.setPath(mSourcePath, false);
        loopAnim(pathMeasure);
    }

    /**
     * 循环取出每一段path ，并执行动画
     */
    protected void loopAnim(final PathMeasure pathMeasure) {
        //动画正在运行的话，先停止动画
        stop();
        mAnimator = ValueAnimator.ofFloat(0, 100);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(mAnimTime);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.i("TAG", "onAnimationUpdate");
                float value = (float) animation.getAnimatedValue();
                if (value < valueOld) {
                    return;
                }
                valueOld = value;
                if (callback != null) {
                    if (callback.isDefaultHandler()) {
                        //获取一个段落
                        pathMeasure.getSegment(0, pathMeasure.getLength() / 100.0f * value, mAnimPath, true);
                    }
                    callback.callBack(pathMeasure, mAnimPath, value);
                } else {
                    //获取一个段落
                    pathMeasure.getSegment(0, pathMeasure.getLength() / 100.0f * value, mAnimPath, true);
                }
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                //每段path走完后，要补一下 某些情况会出现 animPath不满的情况
                pathMeasure.getSegment(0, pathMeasure.getLength(), mAnimPath, true);
                //绘制完一条Path之后，再绘制下一条
                pathMeasure.nextContour();
                //长度为0 说明一次循环结束

                if (pathMeasure.getLength() == 0) {
                    if (mIsInfinite) {//如果需要循环动画
                        mAnimPath.reset();
                        mAnimPath.lineTo(0, 0);
                        valueOld = 0;
                        pathMeasure.setPath(mSourcePath, false);
                    } else {//不需要就停止（因为repeat是无限 需要手动停止）
                        stop();
                    }
                } else {
                    valueOld = 0;
                }
            }

        });
        mAnimator.start();
    }


    public void setCallback(OnPathAnimCallback callback) {
        this.callback = callback;
    }

    public interface OnPathAnimCallback {
        //是否处理默认
        boolean isDefaultHandler();

        void callBack(PathMeasure pathMeasure, Path mAnimPath, float progress);
    }
}
