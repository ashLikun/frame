package com.ashlikun.stickyrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间: 2020/6/10　14:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：吸顶的跟布局
 */
public class StickyRootView {
    Canvas canvas;
    View view;

    public StickyRootView(Context context) {
    }


    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


    public void setView(View itemView) {
        view = itemView;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public View getView() {
        return view;
    }


    public void invalidate() {
        draw();
    }

    public void draw() {
        view.draw(canvas);
    }
}
