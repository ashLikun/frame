/*
 * BadgeView.java
 * BadgeView
 * 
 * Copyright (c) 2012 Stefan Jauker.
 * https://github.com/kodex83/BadgeView
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hbung.badgeview;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabWidget;
import android.widget.TextView;


public class BadgeView extends TextView {

    private boolean mHideOnNull = true;

    private int radius = 9;
    private int bvColor = Color.RED;
    private int defaultHeight = 16;

    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getHeight() <= 0) {
            if (heightMode == MeasureSpec.EXACTLY) {
                // Parent has told us how big to be. So be it.
                height = heightSize;
            } else {
                height = defaultHeight;
                if (heightMode == MeasureSpec.AT_MOST) {
                    height = Math.min(defaultHeight, heightSize);
                }
            }
        } else {
            height = getHeight();
        }


        setBackground((int) (height / 2f), bvColor);
        if (ViewCompat.getMinimumWidth(this) <= 0 && height > 0) {
            setMinimumWidth(height);
        }
    }

    private void init(AttributeSet attrs) {
        defaultHeight = dip2px(getContext(), defaultHeight);
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.BadgeView);
        // set default background
        setBackground(dip2px(getContext(), radius), a.getColor(R.styleable.BadgeView_bvColor, bvColor));
        mHideOnNull = a.getBoolean(R.styleable.BadgeView_bvISHideNull, true);
        a.recycle();
        setGravity(Gravity.CENTER);
        setPadding((int) (radius / 3f), 0, (int) (radius / 3f), 0);
        ViewCompat.setElevation(this, dip2px(getContext(), 1));
        setTypeface(null, Typeface.BOLD);
    }

    public void setBackground(int badgeColor) {
        setBackground(9, badgeColor);
    }

    public void setBackground(int radius, int badgeColor) {

        if (radius <= 0 || (this.radius == radius && bvColor == badgeColor)) {
            return;
        }
        bvColor = badgeColor;
        this.radius = radius;
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackgroundDrawable(bgDrawable);
    }

    /**
     * @return Returns true if view is hidden on badge value 0 or null;
     */
    public boolean isHideOnNull() {
        return mHideOnNull;
    }

    /**
     * @param hideOnNull the hideOnNull to set
     */
    public void setHideOnNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.TextView#setText(java.lang.CharSequence, android.widget.TextView.BufferType)
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(View.GONE);
        } else {
            getShakeUp(1.2f, 10f).start();
            setVisibility(View.VISIBLE);
        }
        super.setText(text, type);
    }

    public void setBadgeCount(int count) {
        setText(String.valueOf(count));
    }

    public Integer getBadgeCount() {
        if (getText() == null) {
            return null;
        }

        String text = getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setBadgeGravity(int gravity) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    public int getBadgeGravity() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return params.gravity;
    }

    public void setBadgeMargin(int dipMargin) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
    }

    public void setBadgeMargin(int leftDipMargin, int topDipMargin, int rightDipMargin, int bottomDipMargin) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.leftMargin = dip2px(getContext(), leftDipMargin);
        params.topMargin = dip2px(getContext(), topDipMargin);
        params.rightMargin = dip2px(getContext(), rightDipMargin);
        params.bottomMargin = dip2px(getContext(), bottomDipMargin);
        setLayoutParams(params);
    }

    public int[] getBadgeMargin() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return new int[]{params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin};
    }

    public void incrementBadgeCount(int increment) {
        Integer count = getBadgeCount();
        if (count == null) {
            setBadgeCount(increment);
        } else {
            setBadgeCount(increment + count);
        }
    }

    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
    }

    /*
     * Attach the BadgeView to the TabWidget
     * 
     * @param target the TabWidget to attach the BadgeView
     * 
     * @param tabIndex index of the tab
     */
    public void setTargetView(TabWidget target, int tabIndex) {
        View tabView = target.getChildTabViewAt(tabIndex);
        setTargetView(tabView);
    }

    /*
     * Attach the BadgeView to the target view
     * 
     * @param target the view to attach the BadgeView
     */
    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);

        } else if (target.getParent() instanceof ViewGroup) {
            // use a new Framelayout container for adding badge
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

            badgeContainer.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(target);

            badgeContainer.addView(this);
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }

    }

    int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    ObjectAnimator getShakeUp(float scaleMax, float rotation) {
        // Keyframe是一个时间/值对，用于定义在某个时刻动画的状态
        // 在不同时间段的X轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                "scaleX", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));
        // 在不同时间段的Y轴0.8-1.1的缩放
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                "scaleY", Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(0.1f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.2f, scaleMax - 0.3f),
                Keyframe.ofFloat(0.3f, scaleMax),
                Keyframe.ofFloat(0.4f, scaleMax),
                Keyframe.ofFloat(0.5f, scaleMax),
                Keyframe.ofFloat(0.6f, scaleMax),
                Keyframe.ofFloat(0.7f, scaleMax),
                Keyframe.ofFloat(0.8f, scaleMax),
                Keyframe.ofFloat(0.9f, scaleMax), Keyframe.ofFloat(1f, 1f));

        // 在不同时间段的旋转 旋转角度 = 0.3*抖动系数
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
                "rotation", Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, rotation),
                Keyframe.ofFloat(0.2f, -rotation),
                Keyframe.ofFloat(0.3f, rotation),
                Keyframe.ofFloat(0.4f, -rotation),
                Keyframe.ofFloat(0.5f, rotation),
                Keyframe.ofFloat(0.6f, -rotation),
                Keyframe.ofFloat(0.7f, rotation),
                Keyframe.ofFloat(0.8f, -rotation),
                Keyframe.ofFloat(0.9f, rotation), Keyframe.ofFloat(1f, 0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhScaleX, pvhScaleY, pvhRotation).setDuration(1000);
        return objectAnimator;
    }
}
