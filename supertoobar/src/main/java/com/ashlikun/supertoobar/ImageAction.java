package com.ashlikun.supertoobar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.FrameLayout;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/7/26　11:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：图标action
 */
public class ImageAction extends Action {
    private Drawable drawable;
    @DrawableRes
    private int drawableId = 0;


    public ImageAction(SupperToolBar toolBar, Drawable drawable) {
        super(toolBar);
        this.drawable = drawable;
    }

    public ImageAction(SupperToolBar toolBar, @DrawableRes int drawable) {
        super(toolBar);
        this.drawableId = drawable;
    }

    @Override
    public CharSequence getText() {
        return null;
    }

    @Override
    public Drawable getDrawable() {
        if (drawableId != 0) {
            drawable = context.getResources().getDrawable(drawableId);
        }
        return drawable;
    }

    @Override
    protected void convert(FrameLayout view) {
    }
}
