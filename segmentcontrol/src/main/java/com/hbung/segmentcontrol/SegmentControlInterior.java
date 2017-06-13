package com.hbung.segmentcontrol;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/8　16:02
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：无边框的多段选择器
 */

public class SegmentControlInterior extends View {
    private Paint mPaint;//画笔
    private int mTextSize;//文字大小
    private String[] mTexts;//多段文本
    private int mCurrentIndex;//当前选中的位置
    private int mCornerRadius;//圆角大小
    private int mTextColorDefault;//文字默认颜色
    private int mTextColorSelect;//文字选中的颜色
    private int mDefaultColor;//默认的颜色
    private int[] mGradualColor;//渐变的颜色合集
    private float mPostion = 0;//选中的框已经走到哪里了   中心点
    private float mItemWidth = 0;//item的宽度
    private float mItemHeight = 0;//item的高度
    private int mHorizonGap;//水平的间隙
    private int mVerticalGap;//垂直的间隙

    //触摸相关
    private float mInitDownX;
    private float mDownMoveX;
    private float mInitDownY;
    private float mTouchSlop;
    private boolean mIsTouchMove = false;//这个触摸事件是否是移动
    private Rect mTextBounds = new Rect();

    //触摸事件
    private OnItemClickListener listener;

    //动画相关
    private boolean mIsAnimStart = false;//是否正在移动
    private boolean mIsPageing = false;//viewpager正在移动
    private boolean isOnLayout = false;

    public SegmentControlInterior(Context context) {
        this(context, null);
    }

    public SegmentControlInterior(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentControlInterior(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentControlInterior);
        String textArray = ta.getString(R.styleable.SegmentControlInterior_sci_texts);
        if (textArray != null) {
            mTexts = textArray.split("\\|");
        }
        mDefaultColor = ta.getColor(R.styleable.SegmentControlInterior_sci_default_color, 0xffffffff);
        int gradualColor1 = ta.getColor(R.styleable.SegmentControlInterior_sci_gradual_color1, 0xff4ce3f4);
        int gradualColor2 = ta.getColor(R.styleable.SegmentControlInterior_sci_gradual_color2, -1);
        int gradualColor3 = ta.getColor(R.styleable.SegmentControlInterior_sci_gradual_color3, -1);
        if (ta.hasValue(R.styleable.SegmentControlInterior_sci_gradual_color2)) {
            if (ta.hasValue(R.styleable.SegmentControlInterior_sci_gradual_color3)) {
                mGradualColor = new int[]{gradualColor1, gradualColor2, gradualColor3};
            } else {
                mGradualColor = new int[]{gradualColor1, gradualColor2};
            }
        }
//        mGradualColor = new int[]{0xff4ce3f4, 0xff579ff2};
        mTextColorDefault = ta.getColor(R.styleable.SegmentControlInterior_sci_default_text_color, 0xff000000);
        mTextColorSelect = ta.getColor(R.styleable.SegmentControlInterior_sci_select_text_color, 0xffffffff);
        int gaps = dip2px(10);
        if (ta.hasValue(R.styleable.SegmentControlInterior_sci_gaps)) {
            gaps = ta.getDimensionPixelSize(R.styleable.SegmentControlInterior_sci_gaps, -1);
            if (gaps > 0) {
                mHorizonGap = gaps;
                mVerticalGap = gaps;
            }
        }
        mHorizonGap = ta.getDimensionPixelSize(R.styleable.SegmentControlInterior_sci_horizon_gap, gaps);
        mVerticalGap = ta.getDimensionPixelSize(R.styleable.SegmentControlInterior_sci_vertical_gap, gaps);
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.SegmentControlInterior_sci_corner_radius, dip2px(15));
        mTextSize = ta.getDimensionPixelSize(R.styleable.SegmentControl_android_textSize, dip2px(18));
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = heightSize;
        if (getCount() > 0) {
            measureText();
            mItemWidth = mTextBounds.width() + mHorizonGap * 2;
            mItemHeight = mTextBounds.height() + mVerticalGap * 2;
            switch (widthMode) {
                case MeasureSpec.AT_MOST://内容包裹
                case MeasureSpec.UNSPECIFIED://未定义
                    width = (int) (mItemWidth * getCount());
                    break;
                case MeasureSpec.EXACTLY://最大或者是指定大小
                    width = widthSize;
                    mItemWidth = (1f * width) / getCount();
                    break;
            }
            switch (heightMode) {
                case MeasureSpec.AT_MOST://内容包裹
                case MeasureSpec.UNSPECIFIED://未定义
                    height = (int) mItemHeight;
                    break;
                case MeasureSpec.EXACTLY://最大或者是指定大小
                    height = heightSize;
                    mItemHeight = heightSize;
                    break;
            }
        } else {
            width = widthSize;
            height = widthSize;
        }
        mPostion = mCurrentIndex * mItemWidth + mItemWidth / 2;
        setMeasuredDimension(width, height);
        isOnLayout = true;
    }

    //测量字体最大值
    private void measureText() {
        //选取最大的控制器
        String maxText = mTexts[0];
        for (int i = 1; i < mTexts.length; i++) {
            if (mTexts[i].length() > maxText.length())
                maxText = mTexts[i];
        }
        //测量最大文本长度对应的大小
        mPaint.getTextBounds(maxText, 0, maxText.length(), mTextBounds);
    }

    //处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsAnimStart) return false;
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsTouchMove = false;
                mInitDownX = event.getX();
                mDownMoveX = mInitDownX;
                mInitDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                if (!mIsTouchMove) {
                    int dx = (int) (x - mInitDownX);
                    int dy = (int) (y - mInitDownY);
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance > mTouchSlop) {
                        mIsTouchMove = true;//是移动事件  取消抬起处理
                    }
                } else {
                    int index = (int) (mInitDownX / mItemWidth);//获取按下的位置
                    if (index == mCurrentIndex) {
                        touchMoveItem((int) (x - mDownMoveX));
                        mDownMoveX = x;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsTouchMove) {//不是移动事件
                    int index = (int) (mInitDownX / mItemWidth);//获取按下的位置
                    if (mCurrentIndex != index) {
                        if (listener != null)
                            listener.onItemClick(index);
                        //移动item
                        moveItem(index);
                    }
                } else {//是移动抬起了
                    //判断是否可以跳到下一个或者上一个
                    finishTouchMove();
                }
                break;
        }

        return true;
    }

    //结束触摸移动 回弹
    private void finishTouchMove() {
        final int currentIndex = (int) (mPostion / mItemWidth);
        float position = mItemWidth * currentIndex + mItemWidth / 2;
        float offset = mPostion - position;
        ValueAnimator animator = ValueAnimator.ofFloat(mPostion, position);
        animator.setDuration((long) (200 * Math.max(Math.abs(offset) / mItemWidth, 0.5)));
        animator.setInterpolator(new OvershootInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentIndex != currentIndex) {
                    mCurrentIndex = currentIndex;
                    if (listener != null)
                        listener.onItemClick(mCurrentIndex);
                }
                mIsAnimStart = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimStart = true;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPostion = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    private void touchMoveItem(int dx) {
        //float offX = dx / mTouchSlop;//去除摩檫力的实际移动
        mPostion = mPostion + dx;
        //越界判断
        if (mPostion < mItemWidth / 2) {
            mPostion = mItemWidth / 2;
        } else if (mPostion > getCount() * mItemWidth - mItemWidth / 2) {
            mPostion = getCount() * mItemWidth - mItemWidth / 2;
        }
        invalidate();
    }

    private void moveItem(final int index) {
        mPostion = mItemWidth * mCurrentIndex + mItemWidth / 2;
        if (isOnLayout) {
            ValueAnimator animator = ValueAnimator.ofFloat(mPostion, mItemWidth * index + mItemWidth / 2);
            animator.setDuration(200);
            animator.setInterpolator(new OvershootInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsAnimStart = false;
                    mCurrentIndex = index;
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    mIsAnimStart = true;
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    mPostion = value;
                    invalidate();
                }
            });
            animator.start();
        } else {
            mIsAnimStart = false;
            invalidate();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        setPaintDefault();
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mCornerRadius, mCornerRadius, mPaint);
        //绘制选中
        drawSelect(canvas);
        //绘制文本
        drawText(canvas);
    }

    private void drawSelect(Canvas canvas) {
        mPaint.setShader(changGradient());
        mPaint.setAlpha(0xff);
        //平移画布
        canvas.save();
        canvas.translate(mPostion - mItemWidth / 2, 0);
        canvas.drawRoundRect(new RectF(0, 0, mItemWidth, mItemHeight), mCornerRadius, mCornerRadius, mPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        if (getCount() <= 0) {
            return;
        }
        mPaint.setShader(null);
        mPaint.setAlpha(0xff);
        for (int i = 0; i < mTexts.length; i++) {
            String item = mTexts[i];//待绘制的文本
            if ((int) (mPostion / mItemWidth) == i) {//选中
                mPaint.setColor(mTextColorSelect);
            } else {//未选中
                mPaint.setColor(mTextColorDefault);
            }
            mPaint.setAlpha((int) ((mIsAnimStart ? 0.7 : 1) * 0xff));
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();//文字的4根线，baseLine为0，top为负值， bottom为正数
            int baseline = (getHeight() - fontMetrics.top - fontMetrics.bottom) / 2;
            canvas.drawText(item, mItemWidth * (i + 0.5f), baseline, mPaint);
        }
    }

    private LinearGradient changGradient() {
        int[] color = mGradualColor.clone();
        if (mGradualColor.length == 2 && mPostion >= (getCount() * mItemWidth) / 2) {
            color[0] = mGradualColor[1];
            color[1] = mGradualColor[0];
        }
        LinearGradient gradient = new LinearGradient(0, 0, mItemWidth, 0, color, null, Shader.TileMode.REPEAT);
        return gradient;
    }


    private void setPaintDefault() {
        mPaint.setColor(mDefaultColor);
        mPaint.setShader(null);
        mPaint.setAlpha(0xff);
    }


    //移动选中的item  viewpager
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!mIsAnimStart && mIsPageing) {
            mCurrentIndex = position;
            mPostion = mCurrentIndex * mItemWidth + mItemWidth / 2 + mItemWidth * positionOffset;
            invalidate();
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/13 10:49
     * <p>
     * 方法功能： viewpager
     * arg0 ==1的时辰默示正在滑动，
     * arg0==2的时辰默示滑动完毕了，
     * arg0==0的时辰默示什么都没做。
     */
    public void onPageScrollStateChanged(int state) {
        if (mIsPageing) {
            if (state == 0) {
                mIsPageing = false;
            }
        } else {
            mIsPageing = state == 1;
        }
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue DisplayMetrics类中属
     * @return
     */
    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    //对外提供的方法
    public void setCurrentIndex(int index) {
        if (mCurrentIndex == index || mIsAnimStart) {
            return;
        }
        moveItem(index);
        invalidate();
    }

    //设置渐变颜色合集
    public void setGradualColor(int[] mGradualColor) {
        this.mGradualColor = mGradualColor;
        invalidate();
    }

    public int getCount() {
        return mTexts == null ? 0 : mTexts.length;
    }

    public int[] getGradualColor() {
        return mGradualColor;
    }


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        public void onItemClick(int index);
    }
}
