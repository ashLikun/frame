package com.hbung.flatbutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public class FlatButton extends TextView {
    //圆角
    private float cornerRadius;
    //边框宽度
    private float strokeWidth;
    //边框颜色
    private int strokeColor;

    //按下颜色
    private int colorPressed;
    private int colorNormal;
    private int colorPressedText;
    private int colorNormalText;

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
        cornerRadius = attr.getDimension(R.styleable.FlatButton_cornerRadius, dip2px(getContext(), 2));
        strokeColor = attr.getColor(R.styleable.FlatButton_strokeColor, strokeColor);
        strokeWidth = attr.getDimension(R.styleable.FlatButton_strokeWidth, strokeWidth);
        colorPressed = attr.getColor(R.styleable.FlatButton_colorPressed, 0xffeeeeee);
        colorNormal = attr.getColor(R.styleable.FlatButton_colorNormal, Color.TRANSPARENT);
        colorPressedText = attr.getColor(R.styleable.FlatButton_colorPressedText, 0xff676767);
        colorNormalText = attr.getColor(R.styleable.FlatButton_colorNormalText, Color.TRANSPARENT);
        attr.recycle();


        setBackgroundCompat(getStateListDrawable());
    }

    private Drawable getStateListDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable.addState(new int[]{android.R.attr.state_pressed},
                        createPressedDrawable());
                drawable.addState(new int[]{android.R.attr.state_focused},
                        createPressedDrawable());
                drawable.addState(new int[]{-android.R.attr.state_enabled},
                        createPressedDrawable());
                drawable.addState(new int[]{android.R.attr.state_selected},
                        createPressedDrawable());
            }
            drawable.addState(new int[]{}, createNormalDrawable());
            //文字的颜色
            setTextColor(getColorStateList(Color.TRANSPARENT != colorNormalText ?
                    colorNormalText : getCurrentTextColor(), colorPressedText));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{colorPressed});
                return new RippleDrawable(colorList, drawable.getAlpha() == 0 ? null : drawable, drawable.getAlpha() == 0 ? new ColorDrawable(Color.WHITE) : null);
            }
        } finally {

        }
        return drawable;
    }

    private LayerDrawable createNormalDrawable() {


        int deffault = Color.TRANSPARENT;
        //默认的颜色为透明，那么第一层的颜色也为透明
        if (colorNormal == deffault) {
            colorPressed = 0;
        }
        //第一层
        GradientDrawable drawableTop = new GradientDrawable();
        drawableTop.setCornerRadius(getCornerRadius());
        drawableTop.setShape(GradientDrawable.RECTANGLE);
        drawableTop.setColor(colorPressed);
        drawableTop.setStroke((int) strokeWidth, strokeColor);

        //第二层
        GradientDrawable drawableBottom = new GradientDrawable();
        drawableBottom.setCornerRadius(getCornerRadius());
        drawableBottom.setShape(GradientDrawable.RECTANGLE);

        drawableBottom.setColor(colorNormal);

        LayerDrawable layerDrawable = new LayerDrawable(new GradientDrawable[]{drawableTop, drawableBottom});

        layerDrawable.setLayerInset(1, 0, 0, dip2px(getContext(), 1f), dip2px(getContext(), 1.8f));
        return layerDrawable;
    }

    private Drawable createPressedDrawable() {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(colorPressed);
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
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
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

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBackgroundCompat(getStateListDrawable());
    }

    public void setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;
        setBackgroundCompat(getStateListDrawable());
    }

    public void setColorNormalText(int colorNormalText) {
        this.colorNormalText = colorNormalText;
        setBackgroundCompat(getStateListDrawable());
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

    private ColorStateList getColorStateList(int normal, int pressed) {
        int[] colors = new int[]{pressed, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
}
