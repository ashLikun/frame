package com.hbung.animcheckbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/9/18　17:49
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class CheckBoxGroup extends ViewGroup {
    private String[] title;
    private int mCheckPadding;
    private int mSpace;
    private int mSelectIndex;
    private int mOrientation = LinearLayout.HORIZONTAL;
    private float textSize = 16;
    private int textColor;
    private int textNoSelectColor;
    AnimCheckSingleHelp singleHelp;

    public CheckBoxGroup(Context context) {
        this(context, null);
    }

    public CheckBoxGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBoxGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        singleHelp = new AnimCheckSingleHelp();
        initView(context, attrs);
    }


    private void initView(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxGroup);
        int index = array.getInt(R.styleable.CheckBoxGroup_android_orientation, -1);
        if (index > 0) {
            setOrientation(index);
        }
        String text = array.getString(R.styleable.CheckBoxGroup_abg_texts);
        mCheckPadding = array.getDimensionPixelSize(R.styleable.CheckBoxGroup_abg_check_padding, dip(10));
        mSpace = array.getDimensionPixelSize(R.styleable.CheckBoxGroup_abg_space, 0);
        mSelectIndex = array.getInt(R.styleable.CheckBoxGroup_abg_select_index, -1);
        textSize = array.getDimension(R.styleable.CheckBoxGroup_abg_textSize, -1);
        textColor = array.getColor(R.styleable.CheckBoxGroup_abg_textColor, -1);
        textNoSelectColor = array.getColor(R.styleable.CheckBoxGroup_abg_textNoSelectColor, -1);
        array.recycle();
        setTexts(text);
    }

    private void addViews() {
        if (title == null || title.length == 0) return;
        removeAllViews();
        singleHelp.clean();
        for (int i = 0; i < title.length; i++) {
            String t = title[i];
            AnimCheckBox box = new AnimCheckBox(getContext());
            box.setPadding(mCheckPadding, mCheckPadding, mCheckPadding, mCheckPadding);
            box.setText(t);
            if (textColor != -1) {
                box.setTextColor(textColor);
            }
            if (textNoSelectColor != -1) {
                box.setTextNoSelectColor(textNoSelectColor);
            }
            if (textSize != -1) {
                box.setTextSize(textSize);
            }
            if (i == mSelectIndex) {
                if (title.length > 2) {
                    box.setAutoSelect(false);
                }
                box.setChecked(true, false);
            }
            addView(box);
            singleHelp.addAnimCheckBox(box);
        }
        singleHelp.single();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                v.measure(widthMeasureSpec, heightMeasureSpec);
                if (mOrientation == LinearLayout.VERTICAL) {
                    if (measureWidth < v.getMeasuredWidth()) {
                        measureWidth = v.getMeasuredWidth();
                    }
                    measureHeight += v.getMeasuredHeight();
                } else {
                    measureWidth += v.getMeasuredWidth();
                    if (measureHeight < v.getMeasuredHeight()) {
                        measureHeight = v.getMeasuredHeight();
                    }
                }
            }
            if (mOrientation == LinearLayout.VERTICAL) {
                measureHeight += (getChildCount() - 1) * mSpace;
            } else {
                measureWidth += (getChildCount() - 1) * mSpace;
            }
        }
        measureWidth += getPaddingLeft() + getPaddingRight();
        measureHeight += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == LinearLayout.VERTICAL) {
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int lastTop = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                top += lastTop;
                lastTop = v.getMeasuredHeight();
                if (i != 0) {
                    top += mSpace;
                }
                int right = left + v.getMeasuredWidth();
                int bottom = top + v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
            }

        } else if (mOrientation == LinearLayout.HORIZONTAL) {
            int top = getPaddingTop();
            int left = getPaddingLeft();
            int lastWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                left += lastWidth;
                lastWidth = v.getMeasuredWidth();
                if (i != 0) {
                    left += mSpace;
                }
                int right = left + v.getMeasuredWidth();
                int bottom = top + v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
            }
        }
    }


    public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public void setTexts(String str) {
        if (str != null) {
            title = str.split("\\|");
            addViews();
        }
    }

    public void setCheckPadding(int mCheckPadding) {
        this.mCheckPadding = mCheckPadding;
        requestLayout();
    }


    public void setSpace(int mSpace) {
        this.mSpace = mSpace;
        addViews();
        requestLayout();
    }

    public void setSelectIndex(int mSelectIndex) {
        this.mSelectIndex = mSelectIndex;
        addViews();
        requestLayout();
    }

    public int getSelectIndex() {
        return mSelectIndex = singleHelp.getSelectIndex();
    }

    public void addOnSingleSelectListener(AnimCheckSingleHelp.OnSingleSelectListener onSingleSelectListener) {
        singleHelp.addOnSingleSelectListener(onSingleSelectListener);
    }

    public AnimCheckBox getAnimCheckBox(int index) {
        return (AnimCheckBox) getChildAt(index);
    }

    private int dip(int dip) {
        return (int) getContext().getResources().getDisplayMetrics().density * dip;
    }
}
