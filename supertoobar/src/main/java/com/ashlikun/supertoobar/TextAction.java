package com.ashlikun.supertoobar;

import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/7/26　11:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：图标action
 */
public class TextAction extends Action {
    private CharSequence text = null;

    public TextAction(SupperToolBar toolBar, CharSequence text) {
        super(toolBar);
        this.text = text;
    }

    @Override
    public CharSequence getText() {
        return text;
    }

    @Override
    public Drawable getDrawable() {
        return null;
    }

    @Override
    protected void convert(FrameLayout view) {
    }

}
