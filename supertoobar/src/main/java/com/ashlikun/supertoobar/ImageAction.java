package com.ashlikun.supertoobar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private int tintColor = -1;

    private ImageView imageView;

    public ImageAction(SupperToolBar toolBar, Drawable drawable) {
        super(toolBar);
        this.drawable = drawable;
    }

    public ImageAction(SupperToolBar toolBar, @DrawableRes int drawable) {
        super(toolBar);
        this.drawableId = drawable;
    }


    public Drawable getDrawable() {
        if (drawableId != 0) {
            drawable = context.getResources().getDrawable(drawableId);
        }
        return drawable;
    }

    /**
     * 设置渲染的颜色
     *
     * @param tintColor
     */
    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        if (imageView != null) {
            imageView.setColorFilter(tintColor);
        }
    }

    /**
     * 获取ImageView
     *
     * @return
     */
    public ImageView getImageView() {
        return imageView;
    }


    @Override
    protected void convert(FrameLayout view) {
    }

    @Override
    public void updata() {
        if (imageView != null) {
            imageView.setId(IMAGE_ID);
            imageView.setImageDrawable(getDrawable());
            imageView.setPadding(actionPadding, actionPadding, actionPadding, actionPadding);
            if (tintColor != -1) {
                imageView.setColorFilter(tintColor);
            }
        }
    }

    /**
     * 创建一个ImageView
     *
     * @return
     */
    @Override
    protected ImageView createView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView = new ImageView(getContext());
        updata();
        imageView.setLayoutParams(params);
        return imageView;
    }
}
