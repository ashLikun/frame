package com.ashlikun.flatbutton;

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

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/5/16 9:15
 * <p>
 * 方法功能：兼容水波纹button 和 shared
 * colorRipple 默认为colorPressed
 * <p>
 * 设置完属性 调用 {@link FlatButton#setBackground}
 */

public class FlatButton extends AppCompatTextView {
    //圆角
    public int cornerRadius;
    //边框宽度
    public int strokeWidth;
    //边框颜色
    public int strokeColor = Color.TRANSPARENT;
    public int strokeColorSelect = Color.TRANSPARENT;
    public int strokeColorEnable = Color.TRANSPARENT;
    public int strokeColorFocused = Color.TRANSPARENT;
    public boolean strokeColorSelectIsSet = false;
    public boolean strokeColorEnableIsSet = false;
    public boolean strokeColorFocusedIsSet = false;

    //按下颜色 2,3是渐变,默认没有
    public int colorPressed;
    public int colorPressed2;
    public int colorPressed3;
    //按下颜色 2,3是渐变,默认没有
    public boolean colorSelectedIsSet;
    public int colorSelected;
    public int colorSelected2;
    public int colorSelected3;
    //获取焦点颜色 2,3是渐变,默认没有
    public boolean colorFocusedIsSet;
    public int colorFocused;
    public int colorFocused2;
    public int colorFocused3;
    public int textColorPressed;
    //默认的颜色 2,3是渐变,默认没有
    public int colorNormal;
    private int colorNormal2;
    public int colorNormal3;
    public int textColor;
    //不可用的颜色 2,3是渐变,默认没有
    public boolean colorEnableIsSet;
    public int colorEnable;
    public int colorEnable2;
    public int colorEnable3;
    public int textColorEnable;
    //水波纹颜色
    public int colorRipple;
    public int clickDelay = 500;
    //textColor是否使用ColorStateList
    public boolean isUseTextColorList = true;
    //渐变的方向
    public int colorNormalOrientation = -1;
    public int colorPressedOrientation = -1;
    public int colorEnableOrientation = -1;
    public int colorFocusedOrientation = -1;
    public int colorSelectedOrientation = -1;

    public long lastClickTime = 0;

    public boolean isUseBackground;

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
        cornerRadius = attr.getDimensionPixelSize(R.styleable.FlatButton_cornerRadius, dip2px(getContext(), 2));
        strokeColor = attr.getColor(R.styleable.FlatButton_strokeColor, strokeColor);
        strokeColorSelect = attr.getColor(R.styleable.FlatButton_strokeColorSelect, strokeColorSelect);
        strokeColorEnable = attr.getColor(R.styleable.FlatButton_strokeColorEnable, strokeColorEnable);
        strokeColorFocused = attr.getColor(R.styleable.FlatButton_strokeColorFocused, strokeColorFocused);
        strokeColorSelectIsSet = attr.hasValue(R.styleable.FlatButton_strokeColorSelect);
        strokeColorEnableIsSet = attr.hasValue(R.styleable.FlatButton_strokeColorEnable);
        strokeColorFocusedIsSet = attr.hasValue(R.styleable.FlatButton_strokeColorFocused);
        strokeWidth = attr.getDimensionPixelSize(R.styleable.FlatButton_strokeWidth, 0);

        colorPressed = attr.getColor(R.styleable.FlatButton_colorPressed, 0xffeeeeee);
        colorPressed2 = attr.getColor(R.styleable.FlatButton_colorPressed2, 0);
        colorPressed3 = attr.getColor(R.styleable.FlatButton_colorPressed3, 0);

        colorNormal = attr.getColor(R.styleable.FlatButton_colorNormal, Color.TRANSPARENT);
        colorNormal2 = attr.getColor(R.styleable.FlatButton_colorNormal2, 0);
        colorNormal3 = attr.getColor(R.styleable.FlatButton_colorNormal3, 0);

        colorFocusedIsSet = attr.hasValue(R.styleable.FlatButton_colorFocused);
        colorFocused = attr.getColor(R.styleable.FlatButton_colorFocused, Color.TRANSPARENT);
        colorFocused2 = attr.getColor(R.styleable.FlatButton_colorFocused2, 0);
        colorFocused3 = attr.getColor(R.styleable.FlatButton_colorFocused3, 0);

        colorSelectedIsSet = attr.hasValue(R.styleable.FlatButton_colorSelected);
        colorSelected = attr.getColor(R.styleable.FlatButton_colorSelected, Color.TRANSPARENT);
        colorSelected2 = attr.getColor(R.styleable.FlatButton_colorSelected2, 0);
        colorSelected3 = attr.getColor(R.styleable.FlatButton_colorSelected3, 0);

        colorEnableIsSet = attr.hasValue(R.styleable.FlatButton_colorEnable);
        colorEnable = attr.getColor(R.styleable.FlatButton_colorEnable, colorPressed);
        colorEnable2 = attr.getColor(R.styleable.FlatButton_colorEnable2, 0);
        colorEnable3 = attr.getColor(R.styleable.FlatButton_colorEnable3, 0);

        textColor = attr.getColor(R.styleable.FlatButton_android_textColor, Color.GRAY);
        textColorPressed = attr.getColor(R.styleable.FlatButton_colorPressedText, getCurrentTextColor());
        colorRipple = attr.getColor(R.styleable.FlatButton_colorRipple, colorPressed);

        textColorEnable = attr.getColor(R.styleable.FlatButton_colorEnableText, textColorPressed);
        clickDelay = attr.getInt(R.styleable.FlatButton_clickDelay, clickDelay);
        colorNormalOrientation = attr.getInt(R.styleable.FlatButton_colorNormalOrientation, -1);
        colorPressedOrientation = attr.getInt(R.styleable.FlatButton_colorPressedOrientation, -1);
        colorEnableOrientation = attr.getInt(R.styleable.FlatButton_colorEnableOrientation, -1);
        colorFocusedOrientation = attr.getInt(R.styleable.FlatButton_colorFocusedOrientation, -1);
        colorSelectedOrientation = attr.getInt(R.styleable.FlatButton_colorSelectedOrientation, -1);
        isUseTextColorList = attr.getBoolean(R.styleable.FlatButton_isUseTextColorList, isUseTextColorList);
        setBackground();
        setClickable(true);
    }

    private Drawable getStateListDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        try {
            if (colorEnableIsSet || strokeColorEnableIsSet) {
                drawable.addState(new int[]{-android.R.attr.state_enabled},
                        createEnableDrawable());
            }
            if (colorFocusedIsSet || strokeColorFocusedIsSet) {
                drawable.addState(new int[]{android.R.attr.state_focused},
                        createFocusedDrawable());
            }
            if (colorSelectedIsSet || strokeColorSelectIsSet) {
                drawable.addState(new int[]{android.R.attr.state_selected},
                        createSelectedDrawable());
            }
            drawable.addState(new int[]{}, createNormalDrawable());
            //文字的颜色
            if (isUseTextColorList) {
                setTextColor(getColorStateList());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{colorRipple});
                return new RippleDrawable(colorList, drawable, createRippleDrawable());
            } else {
                drawable.addState(new int[]{android.R.attr.state_pressed},
                        createPressedDrawable());
            }

        } finally {

        }
        return drawable;
    }

    private Drawable createNormalDrawable() {
        GradientDrawable drawableBottom = new GradientDrawable();
        drawableBottom.setCornerRadius(getCornerRadius());
        drawableBottom.setStroke((int) strokeWidth, strokeColor);
        setGradientDrawableColor(drawableBottom, colorNormalOrientation, colorNormal, colorNormal2, colorNormal3);
        return drawableBottom;
    }

    private Drawable createPressedDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        setGradientDrawableColor(drawablePressed, colorPressedOrientation, colorPressed, colorPressed2, colorPressed3);
        drawablePressed.setStroke((int) strokeWidth, strokeColor);
        return drawablePressed;
    }

    private Drawable createEnableDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        setGradientDrawableColor(drawablePressed, colorEnableOrientation, colorEnable, colorEnable2, colorEnable3);
        drawablePressed.setStroke((int) strokeWidth, strokeColorEnableIsSet ? strokeColorEnable : strokeColor);
        return drawablePressed;
    }

    private Drawable createFocusedDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        setGradientDrawableColor(drawablePressed, colorFocusedOrientation, colorFocused, colorFocused2, colorFocused3);
        drawablePressed.setStroke((int) strokeWidth, strokeColorFocusedIsSet ? strokeColorFocused : strokeColor);
        return drawablePressed;
    }

    private Drawable createSelectedDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        setGradientDrawableColor(drawablePressed, colorSelectedOrientation, colorSelected, colorSelected2, colorSelected3);
        drawablePressed.setStroke((int) strokeWidth, strokeColorSelectIsSet ? strokeColorSelect : strokeColor);
        return drawablePressed;
    }

    private Drawable createRippleDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(colorRipple);
        drawablePressed.setStroke((int) strokeWidth, strokeColor);
        return drawablePressed;
    }

    private void setGradientDrawableColor(GradientDrawable drawable, int colorOrientation, int... color) {
        GradientDrawable.Orientation orientation = getGradientOrientation(colorOrientation);
        if (orientation != null) {
            drawable.setOrientation(orientation);
            int size = 0;
            for (int a : color) {
                if (a != 0) {
                    size++;
                }
            }
            int[] colorRes = new int[size];
            int aI = 0;
            for (int a : color) {
                if (a != 0) {
                    colorRes[aI++] = a;
                }
            }
            drawable.setColors(colorRes);
        } else {
            drawable.setColor(color[0]);
        }
    }

    private GradientDrawable.Orientation getGradientOrientation(int colorOrientation) {
        switch (colorOrientation) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.TR_BL;
            case 2:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 3:
                return GradientDrawable.Orientation.BR_TL;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.BL_TR;
            case 6:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 7:
                return GradientDrawable.Orientation.TL_BR;
            default:
                return null;
        }
    }

    public int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setBackground() {
        if (!isUseBackground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(getStateListDrawable());
            } else {
                setBackgroundDrawable(getStateListDrawable());
            }
        }
    }

    /**
     * 背景  设置默认的颜色
     *
     * @param colorNormal
     */
    public void setColorNormal(@ColorRes int colorNormal) {
        if (colorNormal == 0) {
            return;
        }
        setColorNormalInt(getColor(colorNormal));
    }

    public void setColorNormalInt(int colorNormal) {
        this.colorNormal = colorNormal;
        setBackground();
    }

    public void setColorNormal2(@ColorRes int colorNormal) {
        if (colorNormal == 0) {
            return;
        }
        setColorNormal2Int(getColor(colorNormal));
    }

    public void setColorNormal2Int(int colorNormal) {
        this.colorNormal2 = colorNormal;

    }

    public void setColorNormal3(@ColorRes int colorNormal) {
        if (colorNormal == 0) {
            return;
        }
        setColorNormal3Int(getColor(colorNormal));
    }

    public void setColorNormal3Int(int colorNormal) {
        this.colorNormal3 = colorNormal;
        setBackground();
    }

    /**
     * 背景   设置按下的颜色
     *
     * @param colorPressed
     */
    public void setColorPressed(@ColorRes int colorPressed) {
        if (colorPressed == 0) {
            return;
        }
        setColorPressedInt(getColor(colorPressed));
    }

    public void setColorPressedInt(int colorPressed) {
        this.colorPressed = colorPressed;
        setBackground();
    }

    public void setColorPressed2(@ColorRes int colorPressed) {
        if (colorPressed == 0) {
            return;
        }
        setColorPressed2Int(getColor(colorPressed));
    }

    public void setColorPressed2Int(int colorPressed) {
        this.colorPressed2 = colorPressed;
        setBackground();
    }

    public void setColorPressed3(@ColorRes int colorPressed) {
        if (colorPressed == 0) {
            return;
        }
        setColorPressed3Int(getColor(colorPressed));
    }

    public void setColorPressed3Int(int colorPressed) {
        this.colorPressed3 = colorPressed;
        setBackground();
    }

    /**
     * 背景，设置不可用的颜色
     *
     * @param colorEnable
     */
    public void setColorEnable(@ColorRes int colorEnable) {
        if (colorEnable == 0) {
            return;
        }
        setColorEnableInt(getColor(colorEnable));
    }

    public void setColorEnableInt(int colorEnable) {
        this.colorEnable = colorEnable;
        setBackground();
    }

    public void setColorEnable2(@ColorRes int colorEnable) {
        if (colorEnable == 0) {
            return;
        }
        setColorEnable2Int(getColor(colorEnable));
    }

    public void setColorEnable2Int(int colorEnable) {
        this.colorEnable2 = colorEnable;
        setBackground();
    }

    public void setColorEnable3(@ColorRes int colorEnable) {
        if (colorEnable == 0) {
            return;
        }
        setColorEnable3Int(getColor(colorEnable));
    }

    public void setColorEnable3Int(int colorEnable) {
        this.colorEnable3 = colorEnable;
        setBackground();
    }

    /**
     * 设置渐变方向
     *
     * @param colorOrientation
     */
    public void setColorNormalOrientation(int colorOrientation) {
        this.colorNormalOrientation = colorOrientation;
        setBackground();
    }

    /**
     * 设置渐变方向
     *
     * @param colorOrientation
     */
    public void setColorPressedOrientation(int colorOrientation) {
        this.colorPressedOrientation = colorOrientation;
        setBackground();
    }

    /**
     * 设置渐变方向
     *
     * @param colorOrientation
     */
    public void setColorEnableOrientation(int colorOrientation) {
        this.colorEnableOrientation = colorOrientation;
        setBackground();
    }

    /**
     * 设置水波纹颜色
     *
     * @param colorRipple
     */
    public void setColorRipple(@ColorRes int colorRipple) {
        if (colorRipple == 0) {
            return;
        }
        setColorRippleInt(getColor(colorRipple));
    }

    public void setColorRippleInt(int colorRipple) {
        this.colorRipple = colorRipple;
        setBackground();
    }

    /**
     * 设置圆角
     *
     * @param cornerRadius
     */
    public void setCornerRadiusSize(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setBackground();
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor
     */
    public void setStrokeColor(@ColorRes int strokeColor) {
        setStrokeColorInt(getColor(strokeColor));
    }

    public void setStrokeColorInt(int strokeColor) {
        this.strokeColor = strokeColor;
        setBackground();
    }

    /**
     * 设置边框大小
     *
     * @param strokeWidth
     */
    public void setStrokeWidthSize(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        setBackground();
    }

    /**
     * 默认文字颜色
     *
     * @param textColor
     */
    @Override
    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        super.setTextColor(this.textColor);
    }

    /**
     * 设置文字按下的颜色
     *
     * @param textColorPressed
     */
    public void setTextColorPressed(@ColorRes int textColorPressed) {
        if (textColorPressed == 0) {
            return;
        }
        setTextColorPressedInt(getColor(textColorPressed));
    }

    /**
     * 设置文字按下的颜色
     *
     * @param textColorPressed
     */
    public void setTextColorPressedInt(int textColorPressed) {
        this.textColorPressed = textColorPressed;
        if (isUseTextColorList) {
            setTextColor(getColorStateList());
        }
    }

    /**
     * 设置按钮不可用 时候  文字颜色
     *
     * @param textColorEnable
     */
    public void setTextColorEnable(@ColorRes int textColorEnable) {
        if (textColorEnable == 0) {
            return;
        }
        setTextColorEnableInt(getColor(textColorEnable));
    }

    public void setTextColorEnableInt(int textColorEnable) {
        this.textColorEnable = textColorEnable;
        if (isUseTextColorList) {
            setTextColor(getColorStateList());
        }
    }

    /**
     * textColor是否使用ColorStatusList
     *
     * @param useTextColorList
     */
    public void setUseTextColorList(boolean useTextColorList) {
        isUseTextColorList = useTextColorList;
        if (isUseTextColorList) {
            setTextColor(getColorStateList());
        }
    }

    /**
     * 设置是否使用外部设置的background
     *
     * @param useBackground
     */
    public void setUseBackground(boolean useBackground) {
        isUseBackground = useBackground;
        setBackground();
    }

    /**
     * 点击延迟
     *
     * @param clickDelay
     */
    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
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

    private ColorStateList getColorStateList() {
        int normal = textColor;
        int enable = textColorEnable;
        int pressed = textColorPressed;
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
