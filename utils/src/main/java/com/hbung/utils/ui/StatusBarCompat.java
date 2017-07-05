package com.hbung.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/7/5　13:27
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：状态栏的兼容
 */
public class StatusBarCompat {

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:42
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：兼容设置状态栏颜色,要在setContentView之后,而且没有设置fitsSystemWindows
     *
     * @param statusColor 颜色，如果<=0 那么就会是透明的
     */
    public static void compat(Activity activity, @ColorRes int statusColor) {
        //4.4以下不设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        int color = activity.getResources().getColor(statusColor);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
        //4.4版本
        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/5 13:29
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
