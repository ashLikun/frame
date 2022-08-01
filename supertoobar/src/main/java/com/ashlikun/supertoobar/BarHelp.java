package com.ashlikun.supertoobar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/5/21 0021　上午 11:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：设置前景色效果的兼容
 */
public class BarHelp {

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/14 9:06
     * <p>
     * 方法功能：设置view的背景点击效果
     */

    public static void setForeground(int color, View view) {
        Drawable drawable;
        Drawable background = view.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            color = color & 0xEEFFFFFF;
            ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{color});
            drawable = new RippleDrawable(colorList, !isCanForeground(view) ? background : null,
                    background == null ? new ColorDrawable(Color.WHITE) : background);
        } else {
            if (isCanForeground(view)) {
                color = color & 0x33FFFFFF;
            }
            StateListDrawable bg = new StateListDrawable();
            Drawable colorDrawable;
            if (background instanceof GradientDrawable) {
                background.mutate();
                colorDrawable = background.getConstantState().newDrawable();
                ((GradientDrawable) colorDrawable).setColor(color);
            } else {
                colorDrawable = new ColorDrawable(color);
            }
            bg.addState(new int[]{android.R.attr.state_pressed}, colorDrawable);
            // View.EMPTY_STATE_SET
            bg.addState(new int[]{}, !isCanForeground(view) ? background : null);
            drawable = bg;
        }
        setForeground(view, drawable);
    }

    @SuppressLint("NewApi")
    public static void setForeground(View view, Drawable drawable) {
        if (isCanForeground(view)) {
            if (view instanceof FrameLayout) {
                ((FrameLayout) view).setForeground(drawable);
            } else {
                view.setForeground(drawable);
            }
        } else {
            //不可设置前景色，就只能设置背景色
            ViewCompat.setBackground(view, drawable);
        }
    }

    /**
     * 是否可以设置前景色
     *
     * @param view
     * @return
     */
    public static boolean isCanForeground(View view) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || view instanceof FrameLayout;
    }

    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static void setNotification(TextView v, int num, int max) {
        if (v != null) {
            if (num > 0) {
                v.setVisibility(View.VISIBLE);

                if (num > max) {
                    v.setText(String.format("%d+", max));
                } else {
                    v.setText(String.valueOf(num));
                }
            } else {
                v.setVisibility(View.GONE);
                v.setText("");
            }
        }
    }

    /**
     * 这个颜色是不是深色的
     *
     * @param color
     * @return
     */
    public static boolean isColorDrak(int color) {
        //int t = (color >> 24) & 0xFF;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return r * 0.299 + g * 0.578 + b * 0.114 <= 192;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取Bar默认大小
     */
    public static int getActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int barHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return barHeight;
    }

    /**
     * 获取Bar默认大小
     */
    public static int getActionBarSizeOrDefault(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int barHeight = (int) styledAttributes.getDimension(0, context.getResources().getDimensionPixelSize(R.dimen.stb_default_height));
        styledAttributes.recycle();
        return barHeight;
    }
}
