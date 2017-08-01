package com.hbung.flatbutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/5/16 9:15
 * <p>
 * 方法功能：兼容水波纹button 和 shared
 * colorRipple 默认为colorPressed
 */

public class FlatButton extends TextView {
    //圆角
    private float cornerRadius;
    //边框宽度
    private float strokeWidth;
    //边框颜色
    private int strokeColor = Color.TRANSPARENT;

    //按下颜色
    private int colorPressed;
    private int colorPressedText;
    //默认的颜色
    private int colorNormal;
    //不可用的颜色
    private int colorEnable;
    private int colorEnableText;
    //水波纹颜色
    private int colorRipple;
    private long clickDelay = 0;


    private long lastClickTime = 0;

    private boolean isUseBackground;

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context) {
        this(context, null);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = getTypedArray(context, attrs, R.styleable.FlatButton);
        isUseBackground = attr.hasValue(R.styleable.FlatButton_android_background);
        cornerRadius = attr.getDimension(R.styleable.FlatButton_cornerRadius, dip2px(getContext(), 2));
        strokeColor = attr.getColor(R.styleable.FlatButton_strokeColor, strokeColor);
        strokeWidth = attr.getDimension(R.styleable.FlatButton_strokeWidth, strokeWidth);
        colorPressed = attr.getColor(R.styleable.FlatButton_colorPressed, 0xffeeeeee);
        colorNormal = attr.getColor(R.styleable.FlatButton_colorNormal, Color.TRANSPARENT);
        colorPressedText = attr.getColor(R.styleable.FlatButton_colorPressedText, getCurrentTextColor());
        colorRipple = attr.getColor(R.styleable.FlatButton_colorRipple, colorPressed);
        colorEnable = attr.getColor(R.styleable.FlatButton_colorEnable, colorPressed);
        colorEnableText = attr.getColor(R.styleable.FlatButton_colorEnableText, colorPressedText);
        clickDelay = attr.getInt(R.styleable.FlatButton_clickDelay, (int) clickDelay);
        setClickable(true);//默认可以点击
        if (!isUseBackground) {
            if (!isInEditMode()) {
                setBackgroundCompat(getStateListDrawable());
            }
        }
    }

    private Drawable getStateListDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable.addState(new int[]{android.R.attr.state_pressed},
                        createPressedDrawable());
                drawable.addState(new int[]{android.R.attr.state_focused},
                        createPressedDrawable());
                drawable.addState(new int[]{android.R.attr.state_selected},
                        createPressedDrawable());
            }
            drawable.addState(new int[]{-android.R.attr.state_enabled},
                    createEnableDrawable());
            drawable.addState(new int[]{}, createNormalDrawable());
            //文字的颜色
            setTextColor(getColorStateList(getCurrentTextColor(), colorEnableText, colorPressedText));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{colorRipple});
                return new RippleDrawable(colorList, drawable, createRippleDrawable());
            }
        } finally {

        }
        return drawable;
    }

    private Drawable createNormalDrawable() {
        GradientDrawable drawableBottom = new GradientDrawable();
        drawableBottom.setCornerRadius(getCornerRadius());
        drawableBottom.setColor(colorNormal);
        drawableBottom.setStroke((int) strokeWidth, strokeColor);
        return drawableBottom;
    }

    private Drawable createPressedDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(colorPressed);
        drawablePressed.setStroke((int) strokeWidth, strokeColor);
        return drawablePressed;
    }

    private Drawable createEnableDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(colorEnable);
        drawablePressed.setStroke((int) strokeWidth, strokeColor);
        return drawablePressed;
    }

    private Drawable createRippleDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(colorRipple);
        drawablePressed.setStroke((int) strokeWidth, strokeColor);
        return drawablePressed;
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }


    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    public void setBackgroundCompat(Drawable drawable) {
        int pL = getPaddingLeft();
        int pT = getPaddingTop();
        int pR = getPaddingRight();
        int pB = getPaddingBottom();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
        setPadding(pL, pT, pR, pB);
    }


    public void setColorPressed(int colorPressed) {
        this.colorPressed = colorPressed;
        if (!isUseBackground) {
            setBackgroundCompat(getStateListDrawable());
        }
    }

    public void setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;
        if (!isUseBackground) {
            setBackgroundCompat(getStateListDrawable());
        }
    }

    public void setColorPressedText(int colorPressedText) {
        this.colorPressedText = colorPressedText;
        if (!isUseBackground) {
            setBackgroundCompat(getStateListDrawable());
        }
    }


    public void setColorRipple(int colorRipple) {
        this.colorRipple = colorRipple;
        if (!isUseBackground) {
            setBackgroundCompat(getStateListDrawable());
        }
    }

    //设置是否使用外部设置的background
    public void setUseBackground(boolean useBackground) {
        isUseBackground = useBackground;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        if (!isUseBackground) {
            setBackgroundCompat(getStateListDrawable());
        }
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属�?�density�?
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private ColorStateList getColorStateList(int normal, int enable, int pressed) {
        int[] colors = new int[]{pressed, enable, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    public boolean performClick() {
        //暴力点击
        if (System.currentTimeMillis() - lastClickTime > clickDelay) {
            lastClickTime = System.currentTimeMillis();
            return super.performClick();
        } else {
            return false;
        }
    }

    @Override
    public boolean callOnClick() {
        //暴力点击
        if (System.currentTimeMillis() - lastClickTime > clickDelay) {
            lastClickTime = System.currentTimeMillis();
            return super.callOnClick();
        } else {
            return false;
        }
    }
}
