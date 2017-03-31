package com.hbung.wheelview3d.adapter;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/31　11:08
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public interface BaseLoopAdapter<T> {
    public int getItemCount();

    public T getItem(int position);

    public String getShowText(int position);
}
