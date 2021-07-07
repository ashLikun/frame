package com.ashlikun.supertoobar;

import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/7/26　11:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：图标action
 */
public class ImageAction extends Action {
    private Drawable drawable;
    private int tintColor = -1;

    private boolean isSetTintColor = false;

    private ImageView imageView;

    public ImageAction(SuperToolBar toolBar, Drawable drawable) {
        super(toolBar);
        this.drawable = drawable;
    }

    public ImageAction(SuperToolBar toolBar, @DrawableRes int drawableId) {
        super(toolBar);
        drawable = context.getResources().getDrawable(drawableId);
    }


    public Drawable getDrawable() {
        return drawable;
    }

    public ImageAction setDrawable(Drawable drawable) {
        if (drawable != this.drawable) {
            this.drawable = drawable;
            updata();
        }
        return this;
    }

    public ImageAction setDrawableId(int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        if (drawable != this.drawable) {
            this.drawable = drawable;
            updata();
        }
        return this;
    }

    public ImageAction setWidth(int width) {
        this.width = width;
        updata();
        return this;
    }

    public ImageAction setHeight(int height) {
        this.height = height;
        updata();
        return this;
    }

    /**
     * 设置渲染的颜色
     *
     * @param tintColor
     */
    public ImageAction setTintColor(int tintColor) {
        if (!isSetTintColor || this.tintColor != tintColor) {
            this.tintColor = tintColor;
            isSetTintColor = true;
            if (imageView != null) {
                imageView.setColorFilter(tintColor);
            }
        }
        return this;
    }

    public ImageAction cleanTint() {
        this.tintColor = -1;
        isSetTintColor = false;
        if (imageView != null) {
            imageView.clearColorFilter();
        }
        return this;
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
            if (isSetTintColor) {
                imageView.setColorFilter(tintColor);
            }
            if (imageView.getLayoutParams() != null) {
                imageView.getLayoutParams().width = width;
                imageView.getLayoutParams().height = height;
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
        imageView = new ImageView(getContext());
        updata();
        return imageView;
    }
}
