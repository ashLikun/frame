package com.ashlikun.animcheckbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * N
 * <attr name="ab_strokeWidth" format="dimension" />
 * <attr name="ab_strokeColor" format="color" />
 * <attr name="ab_outColor" format="color" />
 * <attr name="ab_circleColor" format="color" />
 * <attr name="ab_isSelect" format="boolean" />
 * <attr name="ab_isCircle" format="boolean" />
 * <attr name="ab_autoSelect" format="boolean" />
 * <attr name="ab_text" format="string" />
 * <attr name="ab_textSize" format="dimension" />
 * <attr name="ab_textColor" format="color" />
 * <attr name="ab_textNoSelectColor" format="color" />
 */
public class AnimCheckBox extends View {
    private final String TAG = "AnimCheckBox";
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float radius;
    private RectF mRectF = new RectF();
    private RectF mInnerRectF = new RectF();
    private Path mPath = new Path();
    private float mSweepAngle = 0.0000001f;
    private final double mSin27 = Math.sin(Math.toRadians(27));
    private final double mSin63 = Math.sin(Math.toRadians(63));
    private float mHookStartY;
    private float mBaseLeftHookOffset;
    private float mBaseRightHookOffset;
    private float mEndLeftHookOffset;
    private float mEndRightHookOffset;
    private float size;
    private boolean mChecked = false;
    private float mHookOffset;
    private float mHookSize;
    private int mInnerCircleAlpha = 0XFF;
    private float mStrokeWidth = 2;
    private final int mDuration = 500;

    private Path mBroadPath;
    private Path mPathTemp;
    private boolean isCircle = true;//是否是圆形

    private String text;
    private float textSize = 16;
    private float textSpace = 5;
    private int textColor;
    private int textNoSelectColor;

    /**
     * 勾的颜色
     */
    private int mHookColor;
    /**
     * 选中的外边框颜色
     */
    private int mSelectStrokeColor;
    /**
     * 外边框颜色，没有选中的颜色
     */
    private int mNoSelectStrokeColor;
    private int mCircleColor = 0xffeeeeee;
    private final int defaultSize = 40;
    private List<OnCheckedChangeListener> mOnCheckedChangeListeners;
    private OnCheckedChangeListener onCheckedChangeListener;


    private boolean isAutoSelect = false;
    private boolean isOnLayout = false;

    public AnimCheckBox(Context context) {
        this(context, null);
    }

    public AnimCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //4.4的颜色
        int colorPrimary = 0xff333333;
        //5.0以上的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorPrimary = resolveColor(context,
                    android.R.attr.colorPrimary, colorPrimary);
        }
        textColor = colorPrimary;
        mSelectStrokeColor = colorPrimary;
        mNoSelectStrokeColor = colorPrimary;
        init(attrs);
    }

    @ColorInt
    public int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, fallback);
        } finally {
            a.recycle();
        }
    }

    private void init(AttributeSet attrs) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        boolean autoSelect = true;
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.AnimCheckBox);
            mStrokeWidth = (int) array.getDimension(R.styleable.AnimCheckBox_ab_strokeWidth, dip((int) mStrokeWidth));
            mSelectStrokeColor = array.getColor(R.styleable.AnimCheckBox_ab_selectStrokeColor, mSelectStrokeColor);
            mCircleColor = array.getColor(R.styleable.AnimCheckBox_ab_circleColor, mCircleColor);
            mNoSelectStrokeColor = array.getColor(R.styleable.AnimCheckBox_ab_noSelectStrokeColor, mNoSelectStrokeColor);
            mHookColor = array.getColor(R.styleable.AnimCheckBox_ab_hookColor, mSelectStrokeColor);
            mChecked = array.getBoolean(R.styleable.AnimCheckBox_ab_isSelect, mChecked);
            isCircle = array.getBoolean(R.styleable.AnimCheckBox_ab_isCircle, isCircle);
            autoSelect = array.getBoolean(R.styleable.AnimCheckBox_ab_autoSelect, autoSelect);
            text = array.getString(R.styleable.AnimCheckBox_ab_text);
            textSize = array.getDimension(R.styleable.AnimCheckBox_ab_textSize, dip((int) textSize));
            textColor = array.getColor(R.styleable.AnimCheckBox_ab_textColor, textColor);
            textNoSelectColor = array.getColor(R.styleable.AnimCheckBox_ab_textNoSelectColor, textColor);
            textSpace = array.getDimension(R.styleable.AnimCheckBox_ab_textSpace, dip((int) textSpace));
            array.recycle();
        } else {
            mStrokeWidth = dip((int) mStrokeWidth);
            textSize = dip((int) textSize);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mSelectStrokeColor);
        mBroadPath = new Path();
        mPathTemp = new Path();
        setAutoSelect(autoSelect);
        if (mChecked) {
            mInnerCircleAlpha = 0xFF;
            mSweepAngle = 0.000001f;
            mHookOffset = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        } else {
            mInnerCircleAlpha = 0x00;
            mSweepAngle = 360;
            mHookOffset = 0.000001f;
        }

    }

    public void setAutoSelect(boolean autoSelect) {
        if (isAutoSelect == autoSelect) {
            return;
        }
        isAutoSelect = autoSelect;
        if (isAutoSelect) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAutoSelect) {
                        boolean res = onCheckedChangeListener == null ?
                                false : onCheckedChangeListener.onChange(AnimCheckBox.this, !mChecked);
                        if (!res) {
                            setChecked(!mChecked);
                            if (mOnCheckedChangeListeners != null) {
                                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                                    mOnCheckedChangeListeners.get(i).onChange(AnimCheckBox.this, mChecked);
                                }
                            }
                        }
                    }
                }
            });
        } else {
            setOnClickListener(null);
        }
        setClickable(autoSelect);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = dip(defaultSize);
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width < height) {
            width = height;
        }
        if (text != null && text.length() != 0) {
            initDrawTextPaint();
            float textLength = mPaint.measureText(text);
            width = (int) (height + textSpace + textLength);
        }
        setMeasuredDimension(width, height);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        isOnLayout = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setParams();
    }

    /**
     * 设置一些必要的数据
     */
    private void setParams() {
        size = getHeight() - getPaddingTop() - getPaddingBottom();
        radius = (size - (2 * mStrokeWidth)) / 2;
        mRectF.set(mStrokeWidth + getPaddingLeft(), mStrokeWidth + getPaddingTop(), size - mStrokeWidth + getPaddingRight(), size - mStrokeWidth + getPaddingBottom());
        mInnerRectF.set(mRectF);
        mInnerRectF.inset(mStrokeWidth / 2 - 0.5f, mStrokeWidth / 2 - 0.5f);
        mHookStartY = (float) (size / 2 - (radius * mSin27 + (radius - radius * mSin63)));
        mBaseLeftHookOffset = (float) (radius * (1 - mSin63)) + mStrokeWidth / 2;
        mBaseRightHookOffset = 0f;
        mEndLeftHookOffset = mBaseLeftHookOffset + (2 * size / 3 - mHookStartY) * 0.33f;
        mEndRightHookOffset = mBaseRightHookOffset + (size / 3 + mHookStartY) * 0.38f;
        mHookSize = size - (mEndLeftHookOffset + mEndRightHookOffset);
        mHookOffset = mChecked ? mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset : 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
        drawHook(canvas);
        drwaText(canvas);
    }

    private void drwaText(Canvas canvas) {
        if (text != null && text.length() != 0) {
            initDrawTextPaint();
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();//文字的4根线，baseLine为0，top为负值， bottom为正数
            int baseline = (getHeight() - fontMetrics.top - fontMetrics.bottom) / 2;
            canvas.drawText(text, getPaddingLeft() + size + textSpace, baseline, mPaint);
        }
    }

    private void drawCircle(Canvas canvas) {
        initDrawStrokeCirclePaintAndPath();
        canvas.drawPath(mBroadPath, mPaint);
        initDrawAlphaStrokeCirclePaintAndPath();
        canvas.drawPath(mBroadPath, mPaint);
        initDrawInnerCirclePaintAndPath();
        canvas.drawPath(mBroadPath, mPaint);
    }

    private void drawHook(Canvas canvas) {
        if (mHookOffset == 0) {
            return;
        }
        initDrawHookPaint();

        mPath.reset();
        float offset;
        if (mHookOffset <= (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(mBaseLeftHookOffset + mHookOffset, mBaseLeftHookOffset + mHookStartY + mHookOffset);
        } else if (mHookOffset <= mHookSize) {
            mPath.moveTo(mBaseLeftHookOffset, mBaseLeftHookOffset + mHookStartY);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookOffset + mBaseLeftHookOffset,
                    2 * size / 3 - (mHookOffset - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset)));
        } else {
            offset = mHookOffset - mHookSize;
            mPath.moveTo(mBaseLeftHookOffset + offset, mBaseLeftHookOffset + mHookStartY + offset);
            mPath.lineTo(2 * size / 3 - mHookStartY, 2 * size / 3);
            mPath.lineTo(mHookSize + mBaseLeftHookOffset + offset,
                    2 * size / 3 - (mHookSize - (2 * size / 3 - mHookStartY - mBaseLeftHookOffset) + offset));
        }
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    /**
     * 初始化勾的颜色
     */
    private void initDrawHookPaint() {
        mPaint.setAlpha(0xFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mHookColor);
    }

    /**
     * 初始化外边框的patint与path
     */
    private void initDrawStrokeCirclePaintAndPath() {
        mPaint.setAlpha(0xFF);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mNoSelectStrokeColor);
        // canvas.drawArc(mRectF, 202, mSweepAngle, false, mPaint);
        mBroadPath.reset();
        if (isCircle) {
            mBroadPath.addArc(mRectF, 202, mSweepAngle);
        } else {
            mPathTemp.reset();
            float radius = getRadius(mRectF);
            mBroadPath.moveTo(mRectF.left, mRectF.top + mBaseLeftHookOffset + mHookStartY);
            mBroadPath.lineTo(mRectF.left, mRectF.top + radius);
            mBroadPath.quadTo(mRectF.left, mRectF.top, mRectF.left + radius, mRectF.top);
            mBroadPath.lineTo(mRectF.right - radius, mRectF.top);
            mBroadPath.quadTo(mRectF.right, mRectF.top, mRectF.right, mRectF.top + radius);
            mBroadPath.lineTo(mRectF.right, mRectF.bottom - radius);
            mBroadPath.quadTo(mRectF.right, mRectF.bottom, mRectF.right - radius, mRectF.bottom);
            mBroadPath.lineTo(mRectF.left + radius, mRectF.bottom);
            mBroadPath.quadTo(mRectF.left, mRectF.bottom, mRectF.left, mRectF.bottom - radius);
            mBroadPath.close();
            PathMeasure measure = new PathMeasure(mBroadPath, true);
            float startSweep = measure.getLength() / (360.0f / mSweepAngle);
            measure.getSegment(0, startSweep, mPathTemp, true);
            mBroadPath.reset();
            mBroadPath.addPath(mPathTemp);
        }
    }

    /**
     * 初始化外背景的paint与path
     */
    private void initDrawAlphaStrokeCirclePaintAndPath() {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mSelectStrokeColor);
        //canvas.drawArc(mRectF, 202, mSweepAngle - 360, false, mPaint);
        mBroadPath.reset();
        if (isCircle) {
            mBroadPath.addArc(mRectF, 202, mSweepAngle - 360);
        } else {
            mPathTemp.reset();
            float radius = getRadius(mRectF);
            mBroadPath.moveTo(mRectF.left, mRectF.top + mBaseLeftHookOffset + mHookStartY);
            mBroadPath.lineTo(mRectF.left, mRectF.top + radius);
            mBroadPath.quadTo(mRectF.left, mRectF.top, mRectF.left + radius, mRectF.top);
            mBroadPath.lineTo(mRectF.right - radius, mRectF.top);
            mBroadPath.quadTo(mRectF.right, mRectF.top, mRectF.right, mRectF.top + radius);
            mBroadPath.lineTo(mRectF.right, mRectF.bottom - radius);
            mBroadPath.quadTo(mRectF.right, mRectF.bottom, mRectF.right - radius, mRectF.bottom);
            mBroadPath.lineTo(mRectF.left + radius, mRectF.bottom);
            mBroadPath.quadTo(mRectF.left, mRectF.bottom, mRectF.left, mRectF.bottom - radius);
            mBroadPath.close();
            PathMeasure measure = new PathMeasure(mBroadPath, true);
            float startSweep = measure.getLength() / (360.0f / mSweepAngle);
            measure.getSegment(startSweep, measure.getLength(), mPathTemp, true);
            mBroadPath.reset();
            mBroadPath.addPath(mPathTemp);
        }
    }

    private float getRadius(RectF rectF) {
        float radius = size / 7f;
        if (radius > mBaseLeftHookOffset + mHookStartY) {
            radius = mBaseLeftHookOffset + mHookStartY - rectF.top;
        }
        return radius;
    }

    /**
     * 初始化圆形的背景
     */
    private void initDrawInnerCirclePaintAndPath() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        mPaint.setAlpha(mInnerCircleAlpha);
        mBroadPath.reset();
        if (isCircle) {
            mBroadPath.addArc(mInnerRectF, 0, 360);
        } else {
            float radius = getRadius(mInnerRectF);
            //顺时针
            mBroadPath.addRoundRect(mInnerRectF, radius, radius, Path.Direction.CW);
        }
    }

    private void initDrawTextPaint() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mChecked ? textColor : textNoSelectColor);
        mPaint.setAlpha(0xff);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(textSize);
    }

    private void startCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        final float circleMaxFraction = mHookSize / hookMaxValue;
        final float circleMaxValue = 360 / circleMaxFraction;
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                mHookOffset = fraction * hookMaxValue;
                if (fraction <= circleMaxFraction) {
                    mSweepAngle = (int) ((circleMaxFraction - fraction) * circleMaxValue);
                } else {
                    mSweepAngle = 0.00001f;
                }
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private void startUnCheckedAnim() {
        ValueAnimator animator = new ValueAnimator();
        final float hookMaxValue = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
        final float circleMinFraction = (mEndLeftHookOffset - mBaseLeftHookOffset) / hookMaxValue;
        final float circleMaxValue = 360 / (1 - circleMinFraction);
        animator.setFloatValues(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float circleFraction = animation.getAnimatedFraction();
                float fraction = 1 - circleFraction;
                mHookOffset = fraction * hookMaxValue;
                if (circleFraction >= circleMinFraction) {
                    mSweepAngle = (int) ((circleFraction - circleMinFraction) * circleMaxValue);
                } else {
                    mSweepAngle = 0.00001f;
                }
                mInnerCircleAlpha = (int) (fraction * 0xFF);
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mDuration).start();
    }

    private void startAnim() {
        clearAnimation();
        if (mChecked) {
            startCheckedAnim();
        } else {
            startUnCheckedAnim();
        }
    }


    private int getAlphaColor(int color, int alpha) {
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;
        return (color & 0x00FFFFFF) | alpha << 24;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }


    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();

    }

    public void setTextSpace(float textSpace) {
        this.textSpace = textSpace;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.textNoSelectColor = textColor;
        invalidate();
    }

    public void setTextNoSelectColor(int textNoSelectColor) {
        this.textNoSelectColor = textNoSelectColor;
        invalidate();
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }


    public void setChecked(boolean checked, boolean animation) {
        setChecked(checked, false, animation);
    }

    public void setCheckedNotifica(boolean checked) {
        setCheckedNotifica(checked, true);
    }

    public void setCheckedNotifica(boolean checked, boolean animation) {
        setChecked(checked, animation, true);
    }

    /**
     * @param checked
     * @param animation
     */
    public void setChecked(boolean checked, boolean isNotifica, boolean animation) {
        if (checked == this.mChecked) {
            return;
        }
        this.mChecked = checked;
        if (isNotifica) {
            boolean res = onCheckedChangeListener == null ?
                    false : onCheckedChangeListener.onChange(AnimCheckBox.this, mChecked);
            if (!res) {
                if (mOnCheckedChangeListeners != null) {
                    for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                        mOnCheckedChangeListeners.get(i).onChange(AnimCheckBox.this, mChecked);
                    }
                }
            }
        }
        if (animation) {
            if (!isOnLayout) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        startAnim();
                    }
                });
            } else {
                startAnim();
            }

        } else {
            if (mChecked) {
                mInnerCircleAlpha = 0xFF;
                mSweepAngle = 0.000001f;
                mHookOffset = mHookSize + mEndLeftHookOffset - mBaseLeftHookOffset;
            } else {
                mInnerCircleAlpha = 0x00;
                mSweepAngle = 360;
                mHookOffset = 0.000001f;
            }
            invalidate();
        }
    }

    public void setCircle(boolean circle) {
        isCircle = circle;
        invalidate();
    }

    private int dip(int dip) {
        return (int) getContext().getResources().getDisplayMetrics().density * dip;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }


    public void addOnCheckedChangeListener(OnCheckedChangeListener listener) {
        if (mOnCheckedChangeListeners == null) {
            mOnCheckedChangeListeners = new ArrayList<>();
        }
        mOnCheckedChangeListeners.add(listener);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        /**
         * 当调用{@link AnimCheckBox#setOnCheckedChangeListener}方法设置的监听的时候得有返回值
         *
         * @return true :回调已经处理完了，本view不做任何处理
         * false:没有处理，当前view处理
         */
        boolean onChange(AnimCheckBox checkBox, boolean checked);
    }
}
