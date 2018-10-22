package com.ashlikun.supergridlayout;

import android.content.Context;
import android.view.View;

import com.ashlikun.adapter.ViewHolder;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/10/22　16:01
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class FlexViewHolder extends ViewHolder {
    int position;

    @Override
    public int getPositionInside() {
        return position;
    }

    public FlexViewHolder(Context context, View itemView, int position) {
        super(context, itemView, null);
        this.position = position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
